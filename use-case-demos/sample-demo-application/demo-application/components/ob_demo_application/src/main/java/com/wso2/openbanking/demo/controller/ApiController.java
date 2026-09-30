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

package com.wso2.openbanking.demo.controller;

import com.wso2.openbanking.demo.constants.ApiConstants;
import com.wso2.openbanking.demo.devconsole.FlowEntry;
import com.wso2.openbanking.demo.devconsole.FlowLog;
import com.wso2.openbanking.demo.exceptions.AuthorizationException;
import com.wso2.openbanking.demo.exceptions.BankInfoLoadException;
import com.wso2.openbanking.demo.exceptions.SSLContextCreationException;
import com.wso2.openbanking.demo.models.Account;
import com.wso2.openbanking.demo.models.Payment;
import com.wso2.openbanking.demo.models.Transaction;
import com.wso2.openbanking.demo.service.AccountService;
import com.wso2.openbanking.demo.service.AuthService;
import com.wso2.openbanking.demo.service.HttpTlsClient;
import com.wso2.openbanking.demo.service.PaymentService;
import com.wso2.openbanking.demo.utils.ConfigLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/** REST controller that handles account, payment, and authorization API requests. */
@Path("")
public final class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    @Context
    private HttpServletRequest httpRequest;

    private AccountService accountService;
    private AuthService authService;
    private PaymentService paymentService;
    private boolean initialized;

    /** Initializes services needed for accounts, payments, and authorization. */
    public ApiController() {

        try {
            HttpTlsClient httpClient = new HttpTlsClient(
                    ConfigLoader.getCertificatePath(),
                    ConfigLoader.getKeyPath()
            );

            this.accountService = AccountService.create(httpClient);
            this.paymentService = PaymentService.create(httpClient);
            this.authService = AuthService.create(accountService, paymentService, httpClient);
            initialized = true;

        } catch (SSLContextCreationException | GeneralSecurityException | IOException | BankInfoLoadException e) {
            log.error("Failed to initialize ApiController: {}", e.getMessage(), e);
        }
    }

    /**
     * Returns a 503 Service Unavailable response when the controller failed to initialize.
     *
     * @return 503 response indicating the service is unavailable
     */
    private Response serviceUnavailable() {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("{\"" + ApiConstants.FIELD_ERROR + "\":\"Service not initialized\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * Initiates the account addition flow and returns a redirect URL.
     *
     * @param requestBody map containing request parameters for adding an account
     * @return 200 response with the redirect URL for the account addition flow
     */
    @POST
    @Path("/add-accounts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectAccountToAdd(Map<String, String> requestBody) throws Exception {
        if (!initialized) {
            return serviceUnavailable();
        }
        HttpSession session = httpRequest.getSession(true);
        String redirectUrl = accountService.processAddAccount(session);
        authService.setRequestStatus(ApiConstants.STATUS_ACCOUNTS, session);
        return Response.ok(createRedirectResponse(redirectUrl)).build();
    }

    /**
     * Processes a payment request and returns a redirect URL.
     *
     * @param payment payment object containing the payment details
     * @return 200 response with the redirect URL for the payment flow
     */
    @POST
    @Path("/payment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response makePayment(Payment payment) throws Exception {
        if (!initialized) {
            return serviceUnavailable();
        }
        HttpSession session = httpRequest.getSession(true);
        String redirectUrl = paymentService.processPaymentRequest(payment, session);
        authService.setRequestStatus(ApiConstants.STATUS_PAYMENTS, session);
        return Response.ok(createRedirectResponse(redirectUrl)).build();
    }

    /**
     * Handles the OAuth callback and returns account or payment status. The authorization
     * response arrives either as a plain code or, when {@code response_mode=jwt} was requested,
     * as a signed response JWT carrying the code.
     *
     * @param code        authorization code received from the OAuth callback, if any
     * @param responseJwt signed authorization response JWT (JARM), if any
     * @return 200 response with account or payment status, or 500 on authorization failure
     */
    @GET
    @Path("/processAuth")
    @Produces(MediaType.APPLICATION_JSON)
    public Response processAuth(@QueryParam("code") String code,
                                @QueryParam("response") String responseJwt) throws IOException {
        if (!initialized) {
            return serviceUnavailable();
        }
        try {
            authService.processAuthorizationCallback(code, responseJwt, httpRequest.getSession(true));

            String status = authService.getRequestStatus();

            if (ApiConstants.STATUS_ACCOUNTS.equals(status)) {
                Map<String, Object> response = getStringObjectMap();
                return Response.ok(new JSONObject(response).toString()).build();

            } else if (ApiConstants.STATUS_PAYMENTS.equals(status)) {
                boolean success = authService.isLastPaymentSuccess();

                Map<String, Object> response = new LinkedHashMap<>();
                response.put(ApiConstants.FIELD_TYPE,    ApiConstants.STATUS_PAYMENTS);
                response.put(ApiConstants.FIELD_STATUS,  ApiConstants.VALUE_SUCCESS);
                response.put(ApiConstants.FIELD_SUCCESS, success);

                return Response.ok(new JSONObject(response).toString()).build();
            }

            JSONObject response = new JSONObject()
                    .put(ApiConstants.FIELD_STATUS, ApiConstants.VALUE_SUCCESS)
                    .put(ApiConstants.FIELD_TYPE, status);
            return Response.ok(response).build();

        } catch (AuthorizationException e) {
            return Response.serverError()
                    .entity(new JSONObject()
                            .put(ApiConstants.FIELD_STATUS, ApiConstants.FIELD_ERROR)
                            .put("message", String.valueOf(e.getMessage()))
                            .toString())
                    .build();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Revokes the consent for a linked bank account.
     *
     * @param accountId unique identifier of the account to revoke consent for
     * @param bankName  name of the bank associated with the account
     * @param consentId unique identifier of the consent to revoke
     * @return 200 if revoked, 400 if params missing, 404 if not found, 500 on error
     */
    @DELETE
    @Path("/revoke-consent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response revokeConsent(@QueryParam("accountId") String accountId,
                                  @QueryParam("bankName") String bankName,
                                  @QueryParam("consentId") String consentId) {
        if (!initialized) {
            return serviceUnavailable();
        }
        try {
            if (accountId == null || accountId.isEmpty() || bankName == null || bankName.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"" + ApiConstants.FIELD_ERROR + "\":\"accountId and bankName are required\"}")
                        .build();
            }
            boolean success = accountService.revokeAccountConsent(accountId, bankName, consentId);
            if (success) {
                return Response.ok("{\"" + ApiConstants.FIELD_STATUS + "\":\"" + ApiConstants.VALUE_REVOKED + "\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"" + ApiConstants.FIELD_ERROR + "\":\"Account not found or revocation failed\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"" + ApiConstants.FIELD_ERROR + "\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Returns the steps of the flow this browser has driven, for the developer console panel.
     * It is deliberately not gated on initialization: when the application failed to start, the
     * console is where one looks to find out how far a call got.
     *
     * @return 200 response with the recorded flow steps, oldest first
     */
    @GET
    @Path("/dev-console")
    @Produces(MediaType.APPLICATION_JSON)
    public Response developerConsole() {
        JSONArray entries = new JSONArray();
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            for (FlowEntry entry : FlowLog.of(session).snapshot()) {
                entries.put(entry.toJson());
            }
        }
        return Response.ok(new JSONObject().put("entries", entries).toString())
                .header("Cache-Control", "no-store")
                .build();
    }

    /**
     * Clears the recorded flow steps, so the next flow can be demonstrated on its own.
     *
     * @return 200 response confirming the log was cleared
     */
    @DELETE
    @Path("/dev-console")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearDeveloperConsole() {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            FlowLog.of(session).clear();
        }
        return Response.ok("{\"" + ApiConstants.FIELD_STATUS + "\":\"cleared\"}").build();
    }

    /**
     * Wraps a redirect URL into a response map.
     *
     * @param url the redirect URL to wrap
     * @return map with the redirect URL keyed as "redirect"
     */
    private Map<String, String> createRedirectResponse(String url) {
        Map<String, String> response = new HashMap<>();
        response.put("redirect", url);
        return response;
    }

    /**
     * Builds a response map of accounts with their transactions.
     *
     * @return map containing account list with nested transaction details
     */
    private Map<String, Object> getStringObjectMap() {
        List<Account> accounts = authService.getLastFetchedAccounts();

        List<Map<String, Object>> accountsList = new ArrayList<>();
        for (Account acc : accounts) {
            List<Map<String, Object>> txnList = new ArrayList<>();
            if (acc.getTransactions() != null) {
                for (Transaction txn : acc.getTransactions()) {
                    // These are the names this application's own API uses, which are the ones the
                    // frontend reads. The OpenBankingConstants.FIELD_* values are the *bank's*
                    // PascalCase spellings, for parsing its payloads in the service layer - using
                    // them here sent "TransactionId" where the caller looks for "id", so every
                    // field but the date arrived empty.
                    Map<String, Object> t = new LinkedHashMap<>();
                    t.put("id",                 txn.getId());
                    t.put("date",               txn.getDate());
                    t.put("reference",          txn.getReference());
                    t.put("account",            txn.getAccount());
                    t.put("amount",             txn.getAmount());
                    t.put("currency",           txn.getCurrency());
                    t.put("creditDebitStatus",  txn.getCreditDebitStatus());

                    txnList.add(t);
                }
            }
            Map<String, Object> a = new LinkedHashMap<>();
            a.put("id",           acc.getId());
            a.put("name",         acc.getName());
            a.put("balance",      acc.getBalance());
            a.put("consentId",    acc.getConsentId());
            a.put("transactions", txnList);
            accountsList.add(a);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put(ApiConstants.FIELD_TYPE,     ApiConstants.STATUS_ACCOUNTS);
        response.put(ApiConstants.FIELD_STATUS,   ApiConstants.VALUE_SUCCESS);
        response.put("accounts",                  accountsList);
        return response;
    }
}
