/**
 * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 * <p>
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.financial.services.fdx.extensions.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains common constants used in the FDX extension.
 */
public class FDXCommonConstants {
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String RECEIPT = "receipt";
    public static final String CONSENT_DATA = "consentData";
    public static final String CONSUMER_DATA = "consumerData";
    public static final String DATA = "data";
    public static final String CONSENT_REQUEST = "consentRequest";
    public static final String RESPONSE_STATUS = "responseStatus";
    public static final String INVALID_REQUEST_MSG = "invalid_request";
    public static final String USER_ID_KEY_NAME = "userId";
    public static final String ACCOUNT_ID = "account_id";
    public static final String ONE_TIME_CONSENT = "ONE_TIME";
    public static final String ACCOUNT_TYPE = "type";
    public static final String ACCOUNT_ID_DISPLAYABLE = "accountIdToDisplay";
    public static final String AUTHORIZATION_DETAILS = "authorization_details";
    public static final String DURATION_PERIOD = "durationPeriod";
    public static final String EXPIRATION_DATE_TIME = "expiryDate";
    public static final String RESOURCES = "resources";
    public static final String RESOURCE_TYPE = "resourceType";
    public static final Map<String, Map<String, List<String>>> DATA_CLUSTERS;
    public static final String REDIRECT_URL = "redirect_uri";
    public static final String DATA_REQUESTED = "data_requested";
    public static final String ACCOUNT_IDS = "accountIds";
    public static final String COMMON_AUTH_ID = "commonAuthId";
    public static final String IS_RECURRING = "isRecurring";
    public static final String AUTHORIZATION_RESOURCES_KEY = "authorizationResources";
    public static final String SERVER_ERROR_MSG = "server_error";
    public static final String DURATION_TYPE = "durationType";
    public static final String FDX_TYPE = "FDX_ACCOUNTS";
    public static final String FREQUENCY_SIMPLE = "frequency";
    public static final String COOKIES = "cookies";
    public static final String ATTRIBUTES = "commonAuthId";
    public static final String DATA_CLUSTERS_TITLE = "dataClusters";
    public static final String FDX_CONSENT_AUTHORISED = "Authorised";
    public static final String FDX_CONSENT_STATUS = "consentStatus";
    public static final String ERROR = "error";
    public static final Integer BAD_REQUEST = 400;
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String ERROR_DESCRIPTION = "errorDescription";
    public static final Integer INTERNAL_SERVER_ERROR = 500;

    static {
        Map<String, Map<String, List<String>>> dataCluster = new HashMap<>();
        dataCluster.put("ACCOUNT_BASIC", createPermissionLanguage(
                "Account Information - Basic",
                "Account display name", "Masked account number", "Account type and Description"));
        dataCluster.put("ACCOUNT_DETAILED", createPermissionLanguage(
                "Account Information - Details",
                "Account display name", "Masked account number", "Account type and Description",
                "Account balances", "Credit limits", "Due dates and Interest rates"));
        dataCluster.put("ACCOUNT_PAYMENTS", createPermissionLanguage(
                "Account Information - Payments",
                "Full account and routing number", "SWIFT or IBAN numbers"));
        dataCluster.put("TRANSACTIONS", createPermissionLanguage(
                "Transactions",
                "Historical and current transactions", "Transaction types", "Amounts",
                "Dates and descriptions"));
        dataCluster.put("INVESTMENTS", createPermissionLanguage(
                "Investments",
                "Investment contributions", "Investment loans", "Pension data",
                "Vesting and account holding details"));
        dataCluster.put("PAYMENT_SUPPORT", createPermissionLanguage(
                "Payments IDs",
                "Full account number and bank routing number"));
        dataCluster.put("CUSTOMER_CONTACT", createPermissionLanguage(
                "Customer and Account Contact Information",
                "Your Name, Email, Address and Phone on file with this institution.",
                "Name, Email, Address and Phone of any other account holders."));
        dataCluster.put("CUSTOMER_PERSONAL", createPermissionLanguage(
                "Sensitive personal Information",
                "Your Name, Email, Address and Phone on file with this institution.",
                "Name, Email, Address and Phone of any other account holders.", "Your Date of Birth", "Tax ID",
                "SSN (Social Security Number)"));
        dataCluster.put("STATEMENTS", createPermissionLanguage(
                "Statements",
                "Periodic PDF statement showing personal information",
                "Account and transaction details. May contain PII such as name, address."));
        dataCluster.put("BILLS", createPermissionLanguage(
                "Bills",
                ""));
        dataCluster.put("TAX", createPermissionLanguage(
                "Tax",
                "All tax form entities (both JSON and PDF)"));
        dataCluster.put("REWARDS", createPermissionLanguage(
                "Rewards",
                ""));
        dataCluster.put("IMAGES", createPermissionLanguage(
                "Images",
                "Images of checks and receipts, which may include PII such as name, " +
                        "full account and routing number."));

        DATA_CLUSTERS = Collections.unmodifiableMap(dataCluster);
    }

    private static Map<String, List<String>> createPermissionLanguage(String uxName, String... uxDescription) {
        Map<String, List<String>> permissionLanguage = new LinkedHashMap<>();
        permissionLanguage.put(uxName, Arrays.asList(uxDescription));
        return permissionLanguage;
    }
}
