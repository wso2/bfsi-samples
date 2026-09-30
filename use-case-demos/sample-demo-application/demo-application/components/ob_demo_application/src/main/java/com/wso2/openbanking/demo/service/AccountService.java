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
import com.wso2.openbanking.demo.exceptions.BankInfoLoadException;
import com.wso2.openbanking.demo.exceptions.SSLContextCreationException;
import com.wso2.openbanking.demo.models.Account;
import com.wso2.openbanking.demo.models.Transaction;
import com.wso2.openbanking.demo.utils.ConfigLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;


/** Handles account consent, data fetching, and consent revocation via the Open Banking API. */
public final class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private final HttpTlsClient client;
    private final OAuthTokenService oauthService;
    private String accessToken;
    private String currentConsentId;

    private static final DateTimeFormatter ISO_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Creates an AccountService with the given HTTP client and OAuth service.
     *
     * @param client       TLS HTTP client for making API calls
     * @param oauthService service for obtaining and managing OAuth tokens
     */
    private AccountService(HttpTlsClient client, OAuthTokenService oauthService) {
        this.client = client;
        this.oauthService = oauthService;
    }

    /**
     * Creates an AccountService instance using the given TLS client.
     *
     * @param client TLS HTTP client used for API calls
     * @return new AccountService instance
     * @throws BankInfoLoadException if OAuth or TLS service setup fails
     */
    public static AccountService create(HttpTlsClient client) throws BankInfoLoadException {
        try {
            OAuthTokenService oauthService = new OAuthTokenService(client);
            return new AccountService(client, oauthService);
        } catch (GeneralSecurityException | IOException e) {
            throw new BankInfoLoadException("OAuth token service initialization failed.", e);
        } catch (SSLContextCreationException e) {
            throw new BankInfoLoadException("TLS client initialization failed during OAuth service setup.", e);
        }
    }

    /**
     * Sets the OAuth access token for authenticated API requests.
     *
     * @param accessToken valid OAuth access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Sets the consent identifier that authorized the current access token, so fetched accounts
     * can be stamped with the consent they belong to.
     *
     * @param consentId consent identifier resolved from the token exchange, may be null
     */
    public void setConsentId(String consentId) {
        this.currentConsentId = consentId;
    }

    /**
     * Fetches all accounts and their transactions for the current consent.
     *
     * @return list of accounts with transaction data
     * @throws IOException if any API call fails
     */
    public List<Account> createBankInContext() throws IOException {
        List<String> fetchedAccountIds = fetchAccountIds();
        return fetchAccountsWithTransactions(fetchedAccountIds);
    }

    /**
     * Starts an account access authorization and returns the OAuth authorization URL.
     *
     * @param session browser session the authorization belongs to
     * @return authorization redirect URL for the account access flow
     * @throws Exception if the authorization request fails
     */
    public String processAddAccount(HttpSession session) throws Exception {
        return oauthService.authorize(createAccountAuthorizationDetails(), session);
    }

    /**
     * Builds the URL of a single account resource. No path may end in a slash: the Identity
     * Server redirects such a request, and the gateway rejects the redirect as a response its
     * schema does not define.
     *
     * @param accountId account ID to address
     * @return the account resource URL
     */
    private String accountUrl(String accountId) {
        return ConfigLoader.getAccountBaseUrl() + OpenBankingConstants.PATH_ACCOUNTS + "/" + accountId;
    }

    /**
     * Fetches the list of account IDs available under the current access token.
     *
     * @return list of account ID strings
     * @throws IOException if the API call fails
     */
    private List<String> fetchAccountIds() throws IOException {
        String response = client.getWithAuth(
                ConfigLoader.getAccountBaseUrl() + OpenBankingConstants.PATH_ACCOUNTS,
                this.accessToken);
        JSONArray accountsArray = new JSONObject(response)
                .getJSONObject(OpenBankingConstants.FIELD_DATA)
                .getJSONArray(OpenBankingConstants.FIELD_ACCOUNT);
        List<String> accountIds = new ArrayList<>();
        for (int i = 0; i < accountsArray.length(); i++) {
            accountIds.add(accountsArray.getJSONObject(i).getString(OpenBankingConstants.FIELD_ACCOUNT_ID));
        }
        return accountIds;
    }

    /**
     * Fetches the display name of an account by its ID.
     *
     * <p>The nickname is what labels the account, so it is what the customer should see. The Name
     * inside the nested Account array is the account *holder*, which is the same across every
     * account someone owns, so it is only a fallback.
     *
     * @param accountId account ID to look up
     * @return account name or a default value if not found
     * @throws IOException if the API call fails
     */
    private String fetchAccountName(String accountId) throws IOException {
        String url = accountUrl(accountId);
        String response = client.getWithAuth(url, this.accessToken);
        JSONObject accountDataNode = new JSONObject(response)
                .getJSONObject(OpenBankingConstants.FIELD_DATA)
                .getJSONArray(OpenBankingConstants.FIELD_ACCOUNT)
                .getJSONObject(0);

        String nickname = accountDataNode.optString(OpenBankingConstants.FIELD_NICKNAME, "");
        if (!nickname.isEmpty()) {
            return nickname;
        }
        if (accountDataNode.has(OpenBankingConstants.FIELD_ACCOUNT)) {
            return accountDataNode.getJSONArray(OpenBankingConstants.FIELD_ACCOUNT)
                    .getJSONObject(0)
                    .optString(OpenBankingConstants.FIELD_NAME, OpenBankingConstants.DEFAULT_ACCOUNT_NAME);
        }
        return OpenBankingConstants.DEFAULT_STANDARD_ACCOUNT;
    }

    /**
     * Fetches full account details and transactions for each account ID.
     *
     * @param accountIds list of account IDs to fetch
     * @return list of Account objects with transactions
     * @throws IOException if any API call fails
     */
    private List<Account> fetchAccountsWithTransactions(List<String> accountIds) throws IOException {
        List<Account> accounts = new ArrayList<>();
        for (String accountId : accountIds) {
            String accountName = fetchAccountName(accountId);
            double balance = fetchAccountBalance(accountId);
            List<Transaction> transactions = fetchAccountTransactions(accountId);
            Account account = new Account(accountId, accountName, balance, transactions);
            account.setConsentId(currentConsentId);
            accounts.add(account);
        }
        currentConsentId = null;
        return accounts;
    }


    /**
     * Fetches the current balance of an account by its ID.
     *
     * @param accountId account ID to look up
     * @return account balance as a double
     * @throws IOException if the API call fails
     */
    private double fetchAccountBalance(String accountId) throws IOException {
        String url = accountUrl(accountId) + OpenBankingConstants.PATH_BALANCES;
        String response = client.getWithAuth(url, this.accessToken);
        String amount = new JSONObject(response)
                .getJSONObject(OpenBankingConstants.FIELD_DATA)
                .getJSONArray(OpenBankingConstants.FIELD_BALANCE)
                .getJSONObject(0)
                .getJSONObject(OpenBankingConstants.FIELD_AMOUNT)
                .getString(OpenBankingConstants.FIELD_AMOUNT);
        return Double.parseDouble(amount);
    }

    /**
     * Fetches the transaction list for an account by its ID.
     *
     * @param accountId account ID to fetch transactions for
     * @return list of Transaction objects, or empty list if none found
     * @throws IOException if the API call fails
     */
    private List<Transaction> fetchAccountTransactions(String accountId) throws IOException {
        String url = accountUrl(accountId) + OpenBankingConstants.PATH_TRANSACTIONS;
        String response = client.getWithAuth(url, this.accessToken);
        JSONObject root = new JSONObject(response);
        if (!root.has(OpenBankingConstants.FIELD_DATA)
                || !root.getJSONObject(OpenBankingConstants.FIELD_DATA)
                .has(OpenBankingConstants.FIELD_TRANSACTION)) {
            return new ArrayList<>();
        }
        JSONArray transactionsArray = root.getJSONObject(OpenBankingConstants.FIELD_DATA)
                .getJSONArray(OpenBankingConstants.FIELD_TRANSACTION);
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < transactionsArray.length(); i++) {
            transactions.add(parseTransaction(transactionsArray.getJSONObject(i), accountId));
        }
        return transactions;
    }

    /**
     * Parses a JSON transaction object into a Transaction model.
     *
     * @param txn       JSON object representing a single transaction
     * @param accountId account ID the transaction belongs to
     * @return populated Transaction object
     */
    private Transaction parseTransaction(JSONObject txn, String accountId) {
        Transaction transaction = new Transaction();
        transaction.setId(txn.getString(OpenBankingConstants.FIELD_TRANSACTION_ID));
        transaction.setDate(convertIsoDateTimeToDate(txn.getString(OpenBankingConstants.FIELD_BOOKING_DATE_TIME)));
        transaction.setReference(txn.getString(OpenBankingConstants.FIELD_TRANSACTION_INFORMATION));
        JSONObject amountObj = txn.getJSONObject(OpenBankingConstants.FIELD_AMOUNT);
        transaction.setAmount(amountObj.getString(OpenBankingConstants.FIELD_AMOUNT));
        transaction.setCurrency(amountObj.getString(OpenBankingConstants.FIELD_CURRENCY));
        transaction.setCreditDebitStatus(txn.getString(OpenBankingConstants.FIELD_CREDIT_DEBIT_INDICATOR));
        transaction.setAccount(accountId);
        return transaction;
    }

    /**
     * Converts an ISO datetime string to a plain date string (yyyy-MM-dd).
     *
     * @param isoDateTime ISO 8601 datetime string to convert
     * @return date string in yyyy-MM-dd format, or the original value on failure
     */
    private String convertIsoDateTimeToDate(String isoDateTime) {
        try {
            return ZonedDateTime.parse(isoDateTime, ISO_DATETIME_FORMATTER).format(DATE_FORMATTER);
        } catch (Exception e1) {
            try {
                return LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        .format(DATE_FORMATTER);
            } catch (Exception e2) {
                if (isoDateTime.length() >= 10) {
                    return isoDateTime.substring(0, 10);
                }
                return isoDateTime;
            }
        }
    }

    /**
     * Builds the authorization details describing the account access this demo needs: the
     * permissions behind the accounts, balances and transactions screens, over a 90 day
     * consent covering the last 30 days of transactions.
     *
     * @return an {@code account_information_v1.0} authorization details array
     */
    private JSONArray createAccountAuthorizationDetails() {
        ZonedDateTime now = ZonedDateTime.now(java.time.ZoneOffset.of(OpenBankingConstants.TIMEZONE_OFFSET));
        JSONObject accountInformation = new JSONObject()
                .put(OpenBankingConstants.FIELD_TYPE, OpenBankingConstants.TYPE_ACCOUNT_INFORMATION)
                .put(OpenBankingConstants.FIELD_PERMISSIONS, new JSONArray()
                        .put(OpenBankingConstants.PERM_READ_ACCOUNTS_BASIC)
                        .put(OpenBankingConstants.PERM_READ_ACCOUNTS_DETAIL)
                        .put(OpenBankingConstants.PERM_READ_BALANCES)
                        .put(OpenBankingConstants.PERM_READ_TRANSACTIONS_BASIC)
                        .put(OpenBankingConstants.PERM_READ_TRANSACTIONS_DETAIL)
                        .put(OpenBankingConstants.PERM_READ_TRANSACTIONS_CREDITS)
                        .put(OpenBankingConstants.PERM_READ_TRANSACTIONS_DEBITS))
                .put(OpenBankingConstants.FIELD_EXPIRATION_DATE_TIME,
                        now.plusDays(90).format(ISO_DATETIME_FORMATTER))
                .put(OpenBankingConstants.FIELD_TRANSACTION_FROM_DATE_TIME,
                        now.minusDays(30).format(ISO_DATETIME_FORMATTER))
                .put(OpenBankingConstants.FIELD_TRANSACTION_TO_DATE_TIME,
                        now.format(ISO_DATETIME_FORMATTER));
        return new JSONArray().put(accountInformation);
    }

    /**
     * Revokes the consent for a given account and returns the revocation result.
     *
     * @param accountId account ID whose consent is being revoked
     * @param bankName  bank name associated with the account
     * @param consentId consent ID to revoke
     * @return true if revocation succeeded, false otherwise
     * @throws Exception if the revocation request fails
     */
    public boolean revokeAccountConsent(String accountId, String bankName, String consentId) {
        if (consentId == null || consentId.isEmpty()) {
            LOG.warn("No consent id given for accountId: {}, bankName: {}; nothing to revoke.",
                    accountId, bankName);
            return false;
        }
        String revokeUrl = ConfigLoader.getAccountBaseUrl()
                + OpenBankingConstants.PATH_CONSENTS + "/" + consentId;
        LOG.info("Revoking consent {} for accountId: {}, bankName: {}", consentId, accountId, bankName);
        try {
            // This call is made by the application rather than on behalf of the customer, so it
            // carries a client credentials token, not the customer's access token.
            String token = oauthService.getClientCredentialsToken();
            return client.deleteWithAuth(revokeUrl, token);
        } catch (GeneralSecurityException | IOException e) {
            LOG.error("Failed to revoke consent {}: {}", consentId, e.getMessage(), e);
            return false;
        }
    }
}
