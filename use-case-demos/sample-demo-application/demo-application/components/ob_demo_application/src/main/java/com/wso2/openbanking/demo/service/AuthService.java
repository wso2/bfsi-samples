/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.wso2.openbanking.demo.service;

import com.wso2.openbanking.demo.devconsole.FlowRecorder;
import com.wso2.openbanking.demo.exceptions.AuthorizationException;
import com.wso2.openbanking.demo.exceptions.PaymentException;
import com.wso2.openbanking.demo.exceptions.SSLContextCreationException;
import com.wso2.openbanking.demo.models.Account;
import com.wso2.openbanking.demo.utils.ConfigLoader;
import com.wso2.openbanking.demo.utils.JwtDecoder;
import com.wso2.openbanking.demo.utils.JwtUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

/** Handles OAuth authorization callbacks for account and payment flows. */
public final class AuthService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    private static final String FIELD_CODE              = "code";
    private static final String FIELD_ERROR             = "error";
    private static final String FIELD_ERROR_DESCRIPTION = "error_description";
    private static final String FIELD_EXP               = "exp";
    private static final String FIELD_ISS               = "iss";
    private static final long   MILLIS_PER_SECOND       = 1000L;

    private static final String DEFAULT_REQUEST_STATUS = "accounts";

    private static final String CLIENT_ASSERTION_TYPE =
            "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";

    private static final String STEP_CALLBACK = "Authorization Callback";
    private static final String STEP_TOKEN    = "Token Exchange";

    private final AccountService accountService;
    private final PaymentService paymentService;
    private final HttpTlsClient client;
    private String requestStatus = DEFAULT_REQUEST_STATUS;
    private List<Account> lastFetchedAccounts = new ArrayList<>();
    private boolean lastPaymentSuccess = false;

    /**
     * Creates an AuthService with the given account, payment, and HTTP client dependencies.
     *
     * @param accountService service for fetching account data
     * @param paymentService service for processing payments
     * @param client         TLS HTTP client for making API calls
     * @throws SSLContextCreationException if the TLS client copy fails
     */
    private AuthService(AccountService accountService,
                        PaymentService paymentService,
                        HttpTlsClient client) throws SSLContextCreationException {
        this.accountService = accountService;
        this.paymentService = paymentService;
        this.client = client.deepCopy();
        LOG.debug("AuthService instance created successfully.");
    }

    /**
     * Creates a new AuthService instance with the given dependencies.
     *
     * @param accountService service for fetching account data
     * @param paymentService service for processing payments
     * @param client         TLS HTTP client for making API calls
     * @return new AuthService instance
     * @throws SSLContextCreationException if TLS client setup fails
     */
    public static AuthService create(AccountService accountService,
                                     PaymentService paymentService, HttpTlsClient client)
            throws SSLContextCreationException {
        LOG.debug("Creating new AuthService instance.");
        return new AuthService(accountService, paymentService, client);
    }

    /**
     * Sets the current request status to track the active flow type.
     *
     * @param status  flow type identifier (e.g. "accounts" or "payments")
     * @param session browser session the flow belongs to
     */
    public void setRequestStatus(String status, HttpSession session) {
        LOG.debug("Request status updated: {}", status);
        this.requestStatus = status;
        AuthFlowState.storeRequestStatus(session, status);
    }

    /**
     * Processes the OAuth callback code and triggers account or payment handling.
     *
     * @param code         authorization code received from the OAuth callback, if any
     * @param jarmResponse signed authorization response JWT received from the callback, if any
     * @throws AuthorizationException if token exchange or handling fails
     * @throws IOException            if an API call fails during handling
     */
    public void processAuthorizationCallback(String code, String jarmResponse, HttpSession session)
            throws AuthorizationException, IOException {
        this.requestStatus = AuthFlowState.getRequestStatus(session, DEFAULT_REQUEST_STATUS);
        LOG.debug("Processing authorization callback. Request status: {}", requestStatus);
        String authorizationCode = resolveAuthorizationCode(code, jarmResponse);
        String tokenResponse = exchangeCodeForToken(authorizationCode, session);
        String accessToken = parseAccessToken(tokenResponse);
        // Shown beside the token call, because the claim the rest of the flow needs - the consent
        // id - is only visible once the access token is decoded.
        FlowRecorder.attachClaims(STEP_TOKEN, accessToken);
        String consentId = resolveConsentId(accessToken);
        LOG.debug("Access token obtained successfully. Proceeding to handle authorization.");
        handleAuthorizationSuccess(accessToken, consentId, session);
    }

    /**
     * Resolves the authorization code from the callback parameters. When the authorization
     * response is returned as a JWT (JARM, requested with {@code response_mode=jwt}) the code is
     * carried inside that JWT rather than as a query parameter.
     *
     * @param code         authorization code query parameter, if the response was not a JARM one
     * @param jarmResponse signed authorization response JWT, if the response was a JARM one
     * @return the authorization code to exchange
     * @throws AuthorizationException if neither is usable, or the response reports an error
     */
    private String resolveAuthorizationCode(String code, String jarmResponse)
            throws AuthorizationException {
        if (jarmResponse == null || jarmResponse.isEmpty()) {
            if (code == null || code.isEmpty()) {
                throw new AuthorizationException("Authorization response carried neither a code nor a response JWT");
            }
            return code;
        }

        // The browser brings the authorization response back, so this hop is recorded here for
        // the developer console rather than in the HTTP layer.
        FlowRecorder.recordHop(STEP_CALLBACK, "GET", ConfigLoader.getRedirectUri(),
                jarmResponse, jarmResponse);

        JSONObject claims;
        try {
            claims = JwtDecoder.decodeClaims(jarmResponse);
        } catch (IllegalArgumentException | JSONException e) {
            throw new AuthorizationException("Authorization response JWT could not be read", e);
        }
        if (claims.has(FIELD_ERROR)) {
            throw new AuthorizationException("Authorization was rejected: "
                    + claims.optString(FIELD_ERROR_DESCRIPTION, claims.getString(FIELD_ERROR)));
        }
        if (claims.has(FIELD_EXP)
                && claims.getLong(FIELD_EXP) < System.currentTimeMillis() / MILLIS_PER_SECOND) {
            throw new AuthorizationException("Authorization response JWT has expired");
        }
        if (!claims.has(FIELD_CODE)) {
            throw new AuthorizationException("Authorization response JWT carried no code claim");
        }
        LOG.debug("Authorization code extracted from the JARM response issued by {}",
                claims.optString(FIELD_ISS, "unknown issuer"));
        return claims.getString(FIELD_CODE);
    }

    /**
     * Reads the consent identifier that the authorization created out of the access token, which
     * the Identity Server issues as a JWT carrying it as a claim. The payment submission has to
     * echo that identifier back to the bank.
     *
     * @param accessToken access token issued for this authorization
     * @return the consent identifier, or null when the token carries no such claim
     */
    private String resolveConsentId(String accessToken) {
        String claimName = ConfigLoader.getConsentIdClaim();
        try {
            String consentId = JwtDecoder.optClaim(accessToken, claimName);
            if (consentId == null) {
                LOG.warn("The access token carries no {} claim. A payment submission needs it, so check "
                        + "that the Identity Server is configured to add the consent id to access tokens.",
                        claimName);
            }
            return consentId;
        } catch (IllegalArgumentException | JSONException e) {
            LOG.warn("Could not read {} from the access token: {}", claimName, e.getMessage());
            return null;
        }
    }

    /**
     * Exchanges an authorization code for an OAuth token response.
     *
     * @param code    authorization code from the OAuth callback
     * @param session browser session holding the PKCE code verifier
     * @return raw JSON response from the token endpoint
     * @throws AuthorizationException if the token request or parsing fails
     */
    private String exchangeCodeForToken(String code, HttpSession session) throws AuthorizationException {
        LOG.debug("Exchanging authorization code for access token.");
        try {
            String clientAssertion = JwtTokenService.getInstance().createClientAssertion(JwtUtils.generateJti());
            LOG.debug("Client assertion created successfully.");
            String body = buildTokenRequestBody(code, clientAssertion, session);
            String response = client.postAccessToken(ConfigLoader.getTokenUrl(), body);
            LOG.debug("Received response from token endpoint.");
            return response;
        } catch (IOException e) {
            LOG.error("Failed to contact token endpoint: {}", e.getMessage(), e);
            throw new AuthorizationException("Token endpoint call failed: " + e.getMessage(), e);
        } catch (JSONException e) {
            LOG.error("Token response did not contain a valid access_token: {}", e.getMessage(), e);
            throw new AuthorizationException(
                    "Token response did not contain a valid access_token: " + e.getMessage(), e);
        } catch (Exception e) {
            LOG.error("Failed to create client assertion due to a security error: {}", e.getMessage(), e);
            throw new AuthorizationException("Failed to create client assertion due to a security error", e);
        }
    }

    /**
     * Builds the URL-encoded token request body for the authorization code grant.
     *
     * @param code            authorization code from the OAuth callback
     * @param clientAssertion signed JWT used as the client credential
     * @param session         browser session holding the PKCE code verifier
     * @return URL-encoded token request body string
     */
    private String buildTokenRequestBody(String code, String clientAssertion, HttpSession session) {
        LOG.debug("Building token request body for client ID: {}", ConfigLoader.getClientId());
        // Parameters and order follow the reference implementation's Postman collection. It sends
        // no scope: the access was described by authorization_details when the request was pushed,
        // so asking again here is meaningless - and the value this used to send, "accounts", was a
        // scope from the pre-initiated consent flow that the move to RAR removed.
        String body = "grant_type=authorization_code"
                + "&client_assertion_type=" + encode(CLIENT_ASSERTION_TYPE)
                + "&client_assertion=" + encode(clientAssertion)
                + "&redirect_uri=" + encode(ConfigLoader.getRedirectUri())
                + "&client_id=" + encode(ConfigLoader.getClientId())
                + "&code=" + encode(code);

        String codeVerifier = AuthFlowState.consumeCodeVerifier(session);
        if (codeVerifier == null) {
            LOG.warn("No PKCE code verifier is held for this session. The authorization request that "
                    + "started this flow was made by a different session, so the token request will "
                    + "be rejected.");
        } else {
            LOG.debug("Attaching PKCE code verifier to the token request.");
            body = body + "&code_verifier=" + encode(codeVerifier);
        }

        return body;
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * Parses the access token from the token endpoint JSON response.
     *
     * @param response raw JSON response string from the token endpoint
     * @return access token string
     */
    private String parseAccessToken(String response) {
        LOG.debug("Parsing access token from token endpoint response.");
        return new JSONObject(response).getString("access_token");
    }

    /**
     * Handles post-authorization logic by fetching accounts or processing a payment.
     *
     * @param accessToken valid OAuth access token from the token exchange
     * @param consentId   consent identifier the authorization created, may be null
     * @param session     browser session the flow belongs to
     * @throws AuthorizationException if payment processing fails
     * @throws IOException            if an API call fails
     */
    private void handleAuthorizationSuccess(String accessToken, String consentId, HttpSession session)
            throws AuthorizationException, IOException {
        LOG.debug("Handling authorization success. Request status: {}", requestStatus);
        accountService.setAccessToken(accessToken);
        accountService.setConsentId(consentId);
        try {
            if ("accounts".equals(requestStatus)) {
                LOG.debug("Fetching accounts from bank context.");
                lastFetchedAccounts = accountService.createBankInContext();
                LOG.debug("Accounts fetched successfully. Count: {}", lastFetchedAccounts.size());
            } else if ("payments".equals(requestStatus)) {
                LOG.debug("Processing payment authorization.");
                lastPaymentSuccess = paymentService.processPaymentAuthorization(accessToken, consentId, session);
                LOG.debug("Payment authorization completed. Success: {}", lastPaymentSuccess);
            } else {
                LOG.warn("Unrecognized request status during authorization handling: {}", requestStatus);
            }
        } catch (PaymentException e) {
            LOG.error("Failed to process payment after successful authorization: {}", e.getMessage(), e);
            throw new AuthorizationException("Failed to add payment after successful authorization", e);
        } catch (IOException e) {
            LOG.error("IO error during authorization handling: {}", e.getMessage(), e);
            throw new IOException(e);
        }
    }

    /**
     * Returns the current request status.
     *
     * @return current flow type identifier
     */
    public String getRequestStatus() {
        return this.requestStatus;
    }

    /**
     * Returns a copy of the last fetched accounts list.
     *
     * @return list of accounts fetched in the last authorization flow
     */
    public List<Account> getLastFetchedAccounts() {
        return new ArrayList<>(lastFetchedAccounts);
    }

    /**
     * Returns whether the last payment was processed successfully.
     *
     * @return true if the last payment succeeded, false otherwise
     */
    public boolean isLastPaymentSuccess() {
        return lastPaymentSuccess;
    }
}
