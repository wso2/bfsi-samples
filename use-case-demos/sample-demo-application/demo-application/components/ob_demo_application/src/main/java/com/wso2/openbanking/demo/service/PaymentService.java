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

import com.wso2.openbanking.demo.constants.OpenBankingConstants;
import com.wso2.openbanking.demo.exceptions.AuthorizationException;
import com.wso2.openbanking.demo.exceptions.PaymentException;
import com.wso2.openbanking.demo.exceptions.SSLContextCreationException;
import com.wso2.openbanking.demo.models.Payment;
import com.wso2.openbanking.demo.utils.ConfigLoader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpSession;

/** Handles payment authorization via a Rich Authorization Request, and payment submission. */
public final class PaymentService {

    private final OAuthTokenService oauthService;
    private final HttpTlsClient client;

    /**
     * Creates a PaymentService with the given HTTP client and OAuth service.
     *
     * @param client       TLS HTTP client for making API calls
     * @param oauthService service for obtaining OAuth tokens
     */
    private PaymentService(HttpTlsClient client, OAuthTokenService oauthService) {
        this.client = client;
        this.oauthService = oauthService;
    }

    /**
     * Creates a PaymentService instance using the given TLS client.
     *
     * @param client TLS HTTP client for API calls
     * @return new PaymentService instance
     * @throws GeneralSecurityException    if JWT service setup fails
     * @throws IOException                 if the signing key cannot be read
     * @throws SSLContextCreationException if TLS setup fails
     */
    public static PaymentService create(HttpTlsClient client)
            throws GeneralSecurityException, IOException, SSLContextCreationException {
        OAuthTokenService oauthService = new OAuthTokenService(client);
        return new PaymentService(client, oauthService);
    }

    /**
     * Creates a payment consent and returns the OAuth authorization redirect URL.
     *
     * @param payment payment details to create a consent for
     * @param session browser session the authorization belongs to
     * @return authorization redirect URL for the payment consent
     * @throws AuthorizationException if the authorization request fails
     */
    public String processPaymentRequest(Payment payment, HttpSession session) throws AuthorizationException {
        JSONObject initiation = buildInitiation(payment);
        AuthFlowState.storePaymentInitiation(session, initiation.toString());
        try {
            return oauthService.authorize(createPaymentAuthorizationDetails(initiation), session);
        } catch (IOException e) {
            throw new AuthorizationException("Failed to push the payment authorization request", e);
        } catch (GeneralSecurityException e) {
            throw new AuthorizationException("Failed to sign the payment authorization request", e);
        }
    }

    /**
     * Submits the current payment using the given access token.
     *
     * @param accessToken valid OAuth access token from the authorization callback
     * @param consentId   consent identifier that authorized the payment
     * @param session     browser session the authorization belongs to
     * @return true if payment was submitted successfully, false if no pending payment exists
     * @throws PaymentException if the payment submission request fails
     */
    public boolean processPaymentAuthorization(String accessToken, String consentId, HttpSession session)
            throws PaymentException {
        String initiation = AuthFlowState.consumePaymentInitiation(session);
        if (initiation == null || consentId == null) {
            return false;
        }
        try {
            String paymentUrl = ConfigLoader.getPaymentBaseUrl() + OpenBankingConstants.PATH_DOMESTIC_PAYMENTS;
            String paymentBody = createPaymentSubmissionBody(new JSONObject(initiation), consentId);
            client.postPayments(paymentUrl, paymentBody, accessToken);
            return true;
        } catch (IOException e) {
            throw new PaymentException("Failed to submit payment to bank endpoint", e);
        }
    }

    /**
     * Wraps a payment initiation in the authorization details that request consent for it.
     *
     * @param initiation the payment initiation the customer is asked to authorize
     * @return a {@code domestic_payment_v1.0} authorization details array
     */
    private JSONArray createPaymentAuthorizationDetails(JSONObject initiation) {
        JSONObject domesticPayment = new JSONObject()
                .put(OpenBankingConstants.FIELD_TYPE, OpenBankingConstants.TYPE_DOMESTIC_PAYMENT)
                .put(OpenBankingConstants.FIELD_INITIATION, initiation)
                .put(OpenBankingConstants.FIELD_RISK, new JSONObject());
        return new JSONArray().put(domesticPayment);
    }

    /**
     * Builds the JSON request body for submitting a payment. The initiation is the very object
     * that was authorized, so the submission cannot drift from the consent.
     *
     * @param initiation the authorized payment initiation
     * @param consentId  consent ID that authorized this payment
     * @return payment submission request body as a JSON string
     */
    private String createPaymentSubmissionBody(JSONObject initiation, String consentId) {
        return new JSONObject()
                .put(OpenBankingConstants.FIELD_DATA, new JSONObject()
                        .put(OpenBankingConstants.FIELD_CONSENT_ID, consentId)
                        .put(OpenBankingConstants.FIELD_INITIATION, initiation))
                .put(OpenBankingConstants.FIELD_RISK, new JSONObject())
                .toString(4);
    }

    /**
     * Builds the payment initiation JSON object from the payment the customer entered.
     *
     * @param payment payment details captured on the payment screen
     * @return payment initiation JSON object
     */
    private JSONObject buildInitiation(Payment payment) {
        String[] userAccount = parseAccountIdentifier(payment.getUserAccount());
        String[] payeeAccount = parseAccountIdentifier(payment.getPayeeAccount());

        JSONObject initiation = new JSONObject();
        initiation.put(OpenBankingConstants.FIELD_INSTRUCTION_IDENTIFICATION, generateInstructionId());
        initiation.put(OpenBankingConstants.FIELD_END_TO_END_IDENTIFICATION, generateEndToEndId());
        initiation.put(OpenBankingConstants.FIELD_LOCAL_INSTRUMENT,
                OpenBankingConstants.LOCAL_INSTRUMENT_DOMESTIC_CREDIT_TRANSFER);
        initiation.put(OpenBankingConstants.FIELD_INSTRUCTED_AMOUNT,
                buildAmount(payment.getAmount(), payment.getCurrency()));
        initiation.put(OpenBankingConstants.FIELD_CREDITOR_ACCOUNT, buildCreditorAccount(payeeAccount));
        initiation.put(OpenBankingConstants.FIELD_DEBTOR_ACCOUNT, buildDebtorAccount(userAccount));

        String reference = payment.getReference();
        if (reference != null && !reference.trim().isEmpty()) {
            initiation.put(OpenBankingConstants.FIELD_REMITTANCE_INFORMATION, new JSONObject()
                    .put(OpenBankingConstants.FIELD_UNSTRUCTURED, new JSONArray().put(reference)));
        }
        return initiation;
    }

    /**
     * Builds a JSON object representing the instructed payment amount.
     *
     * @param amount   payment amount as a string
     * @param currency payment currency code
     * @return JSON object with amount and currency fields
     */
    private JSONObject buildAmount(String amount, String currency) {
        return new JSONObject()
                .put(OpenBankingConstants.FIELD_AMOUNT, formatAmount(amount))
                .put(OpenBankingConstants.FIELD_CURRENCY, currency);
    }

    /**
     * Builds the creditor (payee) account JSON object.
     *
     * @param payeeAccount parsed payee account parts (name and ID)
     * @return JSON object representing the creditor account
     */
    private JSONObject buildCreditorAccount(String[] payeeAccount) {
        return new JSONObject()
                .put(OpenBankingConstants.FIELD_SCHEME_NAME, OpenBankingConstants.SCHEME_BBAN)
                .put(OpenBankingConstants.FIELD_IDENTIFICATION, payeeAccount[1])
                .put(OpenBankingConstants.FIELD_NAME, payeeAccount[0])
                .put(OpenBankingConstants.FIELD_SECONDARY_IDENTIFICATION, OpenBankingConstants.PAYMENT_SECONDARY_ID_FIXED);
    }

    /**
     * Builds the debtor (user) account JSON object.
     *
     * @param userAccount parsed user account parts (name and ID)
     * @return JSON object representing the debtor account
     */
    private JSONObject buildDebtorAccount(String[] userAccount) {
        return new JSONObject()
                .put(OpenBankingConstants.FIELD_SCHEME_NAME, OpenBankingConstants.SCHEME_BBAN)
                .put(OpenBankingConstants.FIELD_IDENTIFICATION, userAccount[1])
                .put(OpenBankingConstants.FIELD_NAME, userAccount[0])
                .put(OpenBankingConstants.FIELD_SECONDARY_IDENTIFICATION,
                        userAccount[1] + OpenBankingConstants.PAYMENT_SECONDARY_ID_SUFFIX);
    }

    /**
     * Splits an account identifier string into name and ID parts.
     *
     * @param accountIdentifier account string in "name-id" format
     * @return two-element array of name and ID
     */
    private String[] parseAccountIdentifier(String accountIdentifier) {
        String[] parts = accountIdentifier.split("-", 2);
        return new String[]{parts[0], parts.length > 1 ? parts[1] : ""};
    }

    /**
     * Formats an amount string to two decimal places.
     *
     * @param amount raw amount string to format
     * @return amount formatted to two decimal places, or original value if parsing fails
     */
    private String formatAmount(String amount) {
        try {
            return String.format("%.2f", Double.parseDouble(amount));
        } catch (NumberFormatException e) {
            return amount;
        }
    }

    /**
     * Generates a unique instruction identification string.
     *
     * @return instruction ID with a constant prefix and random suffix
     */
    private String generateInstructionId() {
        return OpenBankingConstants.PAYMENT_INSTRUCTION_PREFIX
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }

    /**
     * Generates a unique end-to-end identification string.
     *
     * @return end-to-end ID with a constant prefix and random suffix
     */
    private String generateEndToEndId() {
        return OpenBankingConstants.PAYMENT_END_TO_END_PREFIX
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }

}
