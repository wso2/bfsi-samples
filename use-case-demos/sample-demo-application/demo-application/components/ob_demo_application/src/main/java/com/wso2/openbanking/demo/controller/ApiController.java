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
import com.wso2.openbanking.demo.devconsole.FlowRecorder;
import com.wso2.openbanking.demo.constants.OpenBankingConstants;
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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/** REST controller that handles account, payment, and authorization API requests. */
@Path("")
public final class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

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
        String redirectUrl = accountService.processAddAccount();
        authService.setRequestStatus(ApiConstants.STATUS_ACCOUNTS);
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
        String redirectUrl = paymentService.processPaymentRequest(payment);
        authService.setRequestStatus(ApiConstants.STATUS_PAYMENTS);
        return Response.ok(createRedirectResponse(redirectUrl)).build();
    }

    /**
     * Handles the OAuth callback and returns account or payment status.
     *
     * @param code authorization code received from the OAuth callback
     * @return 200 response with account or payment status, or 500 on authorization failure
     */
    @GET
    @Path("/processAuth")
    @Produces(MediaType.APPLICATION_JSON)
    public Response processAuth(@QueryParam("code") String code) throws IOException {
        if (!initialized) {
            return serviceUnavailable();
        }
        try {
            authService.processAuthorizationCallback(code);

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
                    .entity("{\"" + ApiConstants.FIELD_STATUS + "\":\"" + ApiConstants.FIELD_ERROR
                            + "\",\"message\":\"" + e.getMessage() + "\"}")
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
     * Returns the Open Banking exchanges captured for the developer console, oldest first.
     *
     * @return 200 response with the captured exchanges as a JSON array
     */
    @GET
    @Path("/dev-console")
    @Produces(MediaType.APPLICATION_JSON)
    public Response devConsoleEntries() {
        return Response.ok(FlowRecorder.snapshot().toString()).build();
    }

    /**
     * Clears every captured exchange.
     *
     * @return 200 response confirming the console was cleared
     */
    @DELETE
    @Path("/dev-console")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearDevConsole() {
        FlowRecorder.clear();
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
                    Map<String, Object> t = new LinkedHashMap<>();
                    t.put(OpenBankingConstants.FIELD_TRANSACTION_ID,         txn.getId());
                    t.put("date",                                             txn.getDate());
                    t.put(OpenBankingConstants.FIELD_REFERENCE,              txn.getReference());
                    t.put(OpenBankingConstants.FIELD_ACCOUNT,                txn.getAccount());
                    t.put(OpenBankingConstants.FIELD_AMOUNT,                 txn.getAmount());
                    t.put(OpenBankingConstants.FIELD_CURRENCY,               txn.getCurrency());
                    t.put(OpenBankingConstants.FIELD_CREDIT_DEBIT_INDICATOR, txn.getCreditDebitStatus());

                    txnList.add(t);
                }
            }
            Map<String, Object> a = new LinkedHashMap<>();
            a.put("id",                                      acc.getId());
            a.put(OpenBankingConstants.FIELD_NAME,           acc.getName());
            a.put("balance",                                 acc.getBalance());
            a.put(OpenBankingConstants.FIELD_CONSENT_ID,     acc.getConsentId());
            a.put("transactions",                            txnList);
            a.put("consentId", acc.getConsentId());
            accountsList.add(a);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put(ApiConstants.FIELD_TYPE,     ApiConstants.STATUS_ACCOUNTS);
        response.put(ApiConstants.FIELD_STATUS,   ApiConstants.VALUE_SUCCESS);
        response.put("accounts",                  accountsList);
        return response;
    }
}
