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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.financial.services.accelerator.common.util.HTTPClientUtils;
import org.wso2.financial.services.fdx.extensions.configurations.ConfigurableProperties;
import org.wso2.financial.services.fdx.extensions.model.FailedResponse;
import org.wso2.financial.services.fdx.extensions.model.PopulateConsentAuthorizeScreenData;
import org.wso2.financial.services.fdx.extensions.model.PopulateConsentAuthorizeScreenRequestBody;
import org.wso2.financial.services.fdx.extensions.model.SuccessResponsePopulateConsentAuthorizeScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for FDX consent retrieval.
 */
public class FDXConsentRetrievalUtils {
    private static final Log log = LogFactory.getLog(FDXConsentRetrievalUtils.class);

    /**
     * Retrieves consent data and populates the response.
     *
     * @param populateConsentAuthorizeScreenRequestBody The request body containing the consent data to be retrieved.
     * @throws Exception If an error occurs during the retrieval process.
     */
    public static JSONObject retrieveConsentData(
            PopulateConsentAuthorizeScreenRequestBody populateConsentAuthorizeScreenRequestBody) {

        JSONObject consentRetrievalResponse = new JSONObject();

        consentRetrievalResponse.put(FDXCommonConstants.STATUS,
                SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);

        PopulateConsentAuthorizeScreenData data = populateConsentAuthorizeScreenRequestBody.getData();
        Object requestParam = data.getRequestParameters();

        JSONObject requestParameters = new JSONObject((Map<?, ?>) requestParam);

        // Append consent data to response
        appendConsentDataToResponse(requestParameters, consentRetrievalResponse);
        retrieveDataClusterData(consentRetrievalResponse);
        retrieveAccountData(populateConsentAuthorizeScreenRequestBody, consentRetrievalResponse);

        return consentRetrievalResponse;
    }

    /**
     * Appends consent data to the response object.
     *
     * @param requestParameters        The request parameters containing consent details.
     * @param consentRetrievalResponse The response object to be populated with consent data.
     */
    private static void appendConsentDataToResponse(JSONObject requestParameters,
                                                    JSONObject consentRetrievalResponse) {

        long sharingDuration = 0;
        JSONObject consentDataObject = new JSONObject();
        JSONArray consentRequests = new JSONArray();
        if (requestParameters.has(FDXCommonConstants.AUTHORIZATION_DETAILS)) {
            JSONArray authorizationDetails = requestParameters.getJSONArray
                    (FDXCommonConstants.AUTHORIZATION_DETAILS);
            for (int i = 0; i < authorizationDetails.length(); i++) {
                JSONObject authorizationDetail = authorizationDetails.getJSONObject(i);
                if (authorizationDetail.has(FDXCommonConstants.CONSENT_REQUEST)) {
                    JSONObject authorizationDetailJSON =
                            authorizationDetail.getJSONObject(FDXCommonConstants.CONSENT_REQUEST);
                    Map<String, Object> requestMap = new HashMap<>();
                    if (authorizationDetailJSON.has(FDXCommonConstants.DURATION_TYPE) &&
                            !authorizationDetailJSON.optString(FDXCommonConstants.DURATION_TYPE)
                                    .equals(FDXCommonConstants.ONE_TIME_CONSENT)) {
                        String sharingDurationStr = authorizationDetailJSON.optString
                                (FDXCommonConstants.DURATION_PERIOD, "");
                        sharingDuration = sharingDurationStr.isEmpty() ? 0 : Long.parseLong(sharingDurationStr);
                    }

                    requestMap.put(FDXCommonConstants.EXPIRATION_DATE_TIME,
                            getConsentExpiryDateTime(sharingDuration));
                    requestMap.put(FDXCommonConstants.DURATION_PERIOD, sharingDuration);

                    if (authorizationDetailJSON.has(FDXCommonConstants.RESOURCES)) {
                        JSONArray resources = authorizationDetailJSON.getJSONArray
                                (FDXCommonConstants.RESOURCES);
                        Map<String, List<String>> resourcesMap = new HashMap<>();

                        for (int j = 0; j < resources.length(); j++) {
                            JSONObject resource = resources.getJSONObject(j);
                            String resourceType = resource.getString(FDXCommonConstants.RESOURCE_TYPE);
                            JSONArray dataClusters = resource.getJSONArray(FDXCommonConstants.DATA_CLUSTERS_TITLE);

                            List<String> dataClusterList = new ArrayList<>();
                            for (int k = 0; k < dataClusters.length(); k++) {
                                dataClusterList.add(dataClusters.getString(k));
                            }
                            resourcesMap.put(resourceType, dataClusterList);
                        }
                        requestMap.put(FDXCommonConstants.RESOURCES, resourcesMap);
                    }
                    consentRequests.put(requestMap);
                }
            }
            consentDataObject.put(FDXCommonConstants.AUTHORIZATION_DETAILS, consentRequests);
            JSONArray consentDataArray = new JSONArray();
            consentDataObject.put(FDXCommonConstants.TYPE, FDXCommonConstants.FDX_TYPE);

            // appending redirect URL
            consentDataObject.put(FDXCommonConstants.REDIRECT_URL,
                    requestParameters.getString(FDXCommonConstants.REDIRECT_URL));
            consentDataArray.put(consentDataObject);

            consentRetrievalResponse.put(FDXCommonConstants.CONSENT_DATA, consentDataArray);
        } else {
            handleBadRequests(consentRetrievalResponse, FDXCommonConstants.BAD_REQUEST,
                    "\"Authorization details are not available\"");
        }
    }

    /**
     * Handles bad requests by populating the response object with error details.
     *
     * @param response    The response object to be populated with error details.
     * @param statusCode  The HTTP status code for the error.
     * @param errorMessage The error message to be included in the response.
     */
    public static void handleBadRequests(JSONObject response, Integer statusCode, String errorMessage) {
        response.clear();
        response.put(FDXCommonConstants.STATUS, FailedResponse.StatusEnum.ERROR);
        response.put(FDXCommonConstants.RESPONSE_STATUS, statusCode);
        response.put(FDXCommonConstants.DATA, new JSONObject().put(FDXCommonConstants.DATA, errorMessage));
    }

    /**
     * Calculates the consent expiry date and time based on the sharing duration.
     *
     * @param sharingDuration The duration in days for which the consent is valid.
     * @return The calculated expiry date and time.
     */
    public static Object getConsentExpiryDateTime(long sharingDuration) {
        OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
        return currentTime.plusDays(sharingDuration);
    }

    /**
     * Retrieves account data from the specified endpoint and populates the response.
     *
     * @param populateConsentAuthorizeScreenRequestBody The request body containing the user ID for account retrieval.
     * @param consentRetrievalResponse                  The response object to be populated with account data.
     * @throws Exception If an error occurs during the retrieval process.
     */
    public static void retrieveAccountData(
            PopulateConsentAuthorizeScreenRequestBody populateConsentAuthorizeScreenRequestBody,
            JSONObject consentRetrievalResponse) throws JSONException {
        // If previous validation failed
        if (consentRetrievalResponse.has(FDXCommonConstants.STATUS)) {
            if (consentRetrievalResponse.get(FDXCommonConstants.STATUS) == FailedResponse.StatusEnum.ERROR) {
                return;
            }
        } else {
            consentRetrievalResponse.put(FDXCommonConstants.STATUS,
                    SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
        }
        String accountsURL = ConfigurableProperties.SHARABLE_ENDPOINT;
        String userId = populateConsentAuthorizeScreenRequestBody.getData().getUserId();

        if (StringUtils.isNotBlank(accountsURL)) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(FDXCommonConstants.USER_ID_KEY_NAME, userId);
            String accountData = getAccountsFromEndpoint(accountsURL, parameters, new HashMap<>());

            if (accountData == null || accountData.isEmpty()) {
                handleBadRequests(consentRetrievalResponse,
                        FDXCommonConstants.INTERNAL_SERVER_ERROR,
                        "Unable to load accounts data for the user: " + userId);
            }

            try {
                JSONArray consumerDataObject = new JSONArray();
                JSONObject jsonAccountData = new JSONObject(accountData);
                JSONArray accountsArray = jsonAccountData.getJSONArray(FDXCommonConstants.DATA);
                for (int accountIndex = 0; accountIndex < accountsArray.length(); accountIndex++) {
                    JSONObject object = accountsArray.getJSONObject(accountIndex);
                    String accountId = object.getString(FDXCommonConstants.ACCOUNT_ID);
                    String accountType = object.getString(FDXCommonConstants.ACCOUNT_TYPE);

                    // Masking account ID
                    boolean isAccountMaskingEnabled = ConfigurableProperties.MASKING_ENABLED;

                    String accountNumberDisplay = accountId;
                    if (isAccountMaskingEnabled) {
                        accountNumberDisplay = getMaskedAccountNumber(accountId);
                    }

                    JSONObject accountObject = new JSONObject()
                            .put(FDXCommonConstants.ACCOUNT_ID, accountId)
                            .put(FDXCommonConstants.ACCOUNT_TYPE, accountType)
                            .put(FDXCommonConstants.ACCOUNT_ID_DISPLAYABLE, accountNumberDisplay);

                    consumerDataObject.put(accountObject);
                }
                consentRetrievalResponse.put(FDXCommonConstants.CONSUMER_DATA, consumerDataObject);
            } catch (JSONException e) {
                log.error("Error occurred while parsing account data", e);
                throw new JSONException(e.getMessage());
            }
        } else {
            log.error("Sharable accounts endpoint is not configured properly");
            handleBadRequests(consentRetrievalResponse, FDXCommonConstants.INTERNAL_SERVER_ERROR,
                    "Accounts endpoint is not configured properly");
        }
    }

    /**
     * Retrieves account data from the specified endpoint.
     *
     * @param accountsURL The URL of the accounts endpoint.
     * @param parameters  The parameters to be sent in the request.
     * @param headers     The headers to be sent in the request.
     * @return The response from the endpoint as a string.
     */
    public static String getAccountsFromEndpoint(String accountsURL, Map<String, String> parameters,
                                                 Map<String, String> headers) {

        String retrieveUrl = accountsURL;
        if (retrieveUrl.endsWith("/")) {
            retrieveUrl = retrieveUrl.substring(0, retrieveUrl.length() - 1);
        }
        if (!parameters.isEmpty()) {
            retrieveUrl = buildRequestURL(retrieveUrl, parameters);
        }
        try {
            CloseableHttpClient client = HTTPClientUtils.getHttpsClient();
            HttpGet request = new HttpGet(retrieveUrl);
            request.addHeader("Accept", "application/json");

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        request.addHeader(entry.getKey(), entry.getValue());
                    }
                }
            }

            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Build the complete URL with query parameters sent in the map.
     *
     * @param baseURL    the base URL
     * @param parameters map of parameters
     * @return the output URL
     */
    private static String buildRequestURL(String baseURL, Map<String, String> parameters) {
        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        String query = URLEncodedUtils.format(pairs, StandardCharsets.UTF_8);
        return baseURL.contains("?") ? baseURL + "&" + query : baseURL + "?" + query;
    }

    /**
     * Retrieves data cluster data from the consent retrieval response.
     *
     * @param consentRetrievalResponse The consent retrieval response object.
     */
    public static void retrieveDataClusterData(JSONObject consentRetrievalResponse) {

        // If previous validation failed
        if (consentRetrievalResponse.has(FDXCommonConstants.STATUS)) {
            if (consentRetrievalResponse.get(FDXCommonConstants.STATUS) == FailedResponse.StatusEnum.ERROR) {
                return;
            }
        } else {
            consentRetrievalResponse.put(FDXCommonConstants.STATUS,
                    SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
        }
        for (Object item : consentRetrievalResponse.getJSONArray(FDXCommonConstants.CONSENT_DATA)) {
            Map<String, Object> consentDataItem = ((JSONObject) item).toMap();
            Map<String, List<String>> dataClusterMapping = new HashMap<>();

            List<Map<String, Object>> authDetailArray =
                    (List<Map<String, Object>>) consentDataItem.get(FDXCommonConstants.AUTHORIZATION_DETAILS);

            for (Map<String, Object> authorizationDetail : authDetailArray) {
                if (authorizationDetail.containsKey(FDXCommonConstants.RESOURCES)) {
                    Map<String, List<String>> resources =
                            (Map<String, List<String>>) authorizationDetail.get(FDXCommonConstants.RESOURCES);

                    for (List<String> dataClusterList : resources.values()) {
                        for (String dataCluster : dataClusterList) {
                            Map<String, List<String>> permissionData =
                                    FDXCommonConstants.DATA_CLUSTERS.get(dataCluster);

                            if (permissionData != null && !permissionData.isEmpty()) {
                                Map.Entry<String, List<String>> firstEntry =
                                        permissionData.entrySet().iterator().next();
                                dataClusterMapping.put(firstEntry.getKey(), firstEntry.getValue());
                            }
                        }
                    }
                }
            }

            // Add the final dataClusterMapping back into the consentDataItem
            ((JSONObject) item).put(FDXCommonConstants.DATA_REQUESTED, dataClusterMapping);
        }
    }

    /**
     * Masks the account number based on its length.
     *
     * @param accountId The account ID to be masked.
     * @return The masked account number.
     */
    public static String getMaskedAccountNumber(String accountId) {
        int accountIdLength = accountId.length();
        if (accountIdLength > 1) {
            if (accountIdLength < 4) {
                // If the length is less than 4, mask all but the last character
                String maskedPart = StringUtils.repeat('*', accountIdLength - 1);
                String visiblePart = StringUtils.right(accountId, 1);
                return maskedPart + visiblePart;
            } else if (accountIdLength == 4) {
                // If the length is exactly 4, mask all but the last two characters
                return "**" + StringUtils.right(accountId, 2);
            } else {
                // If the length is greater than 4, mask all but the last 4 characters
                String maskedPart = StringUtils.repeat('*', accountIdLength - 4);
                String visiblePart = StringUtils.right(accountId, 4);
                return maskedPart + visiblePart;
            }
        }
        return accountId;
    }
}
