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
import javax.validation.Valid;

/**
 * Utility class for FDX consent retrieval.
 */
public class FDXConsentRetrievalUtils {
    private static final Log log = LogFactory.getLog(FDXConsentRetrievalUtils.class);

    public static void retrieveConsentData(
            @Valid PopulateConsentAuthorizeScreenRequestBody populateConsentAuthorizeScreenRequestBody,
            JSONObject validationResponse) {

        validationResponse.put(FDXCommonConstants.STATUS,
                SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);

        PopulateConsentAuthorizeScreenData data = populateConsentAuthorizeScreenRequestBody.getData();
        Object requestParam = data.getRequestParameters();

        JSONObject requestParameters = new JSONObject((Map<?, ?>) requestParam);

        // Append consent data to response
        appendConsentDataToResponse(requestParameters, validationResponse);
    }

    private static void appendConsentDataToResponse(JSONObject requestParameters,
                                                    JSONObject validationResponse) {

        long sharingDuration = 0;
        JSONObject consentDataObject = new JSONObject();
        JSONArray consentRequests = new JSONArray();
        if (requestParameters.has(FDXCommonConstants.AUTHORIZATION_DETAILS)) {
            JSONArray authorizationDetails = requestParameters.getJSONArray
                    (FDXCommonConstants.AUTHORIZATION_DETAILS);
            for (int i = 0; i < authorizationDetails.length(); i++) {
                JSONObject authorizationDetail = authorizationDetails.getJSONObject(i);
                if (authorizationDetail.has("consentRequest")) {
                    JSONObject authorizationDetailJSON = authorizationDetail.getJSONObject("consentRequest");
                    Map<String, Object> requestMap = new HashMap<>();
                    if (authorizationDetailJSON.has(FDXCommonConstants.DURATION_TYPE) &&
                            !authorizationDetailJSON.optString(FDXCommonConstants.DURATION_TYPE).equals("ONE_TIME")) {
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
                            String resourceType = resource.getString("resourceType");
                            JSONArray dataClusters = resource.getJSONArray("dataClusters");

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

            validationResponse.put(FDXCommonConstants.CONSENT_DATA, consentDataArray);
        } else {
            handleBadRequests(validationResponse);
        }
    }

    private static void handleBadRequests(JSONObject response) {
        response.clear();
        response.put(FDXCommonConstants.STATUS, FailedResponse.StatusEnum.ERROR);
        response.put(FDXCommonConstants.RESPONSE_STATUS, FDXCommonConstants.BAD_REQUEST);
        response.put(FDXCommonConstants.DATA, new JSONObject().put(FDXCommonConstants.ERROR,
                "No debtor account found in consent"));
    }

    public static Object getConsentExpiryDateTime(long sharingDuration) {
        OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
        return currentTime.plusDays(sharingDuration);
    }

    public static void retrieveAccountData(
            @Valid PopulateConsentAuthorizeScreenRequestBody populateConsentAuthorizeScreenRequestBody,
            JSONObject validationResponse) throws Exception {
        // If previous validation failed
        if (validationResponse.has(FDXCommonConstants.STATUS)) {
            if (validationResponse.get(FDXCommonConstants.STATUS) == FailedResponse.StatusEnum.ERROR) {
                return;
            }
        } else {
            validationResponse.put(FDXCommonConstants.STATUS,
                    SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
        }
        String accountsURL = ConfigurableProperties.SHARABLE_ENDPOINT;
        String userId = populateConsentAuthorizeScreenRequestBody.getData().getUserId();

        if (StringUtils.isNotBlank(accountsURL)) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(FDXCommonConstants.USER_ID_KEY_NAME, userId);
            String accountData = getAccountsFromEndpoint(accountsURL, parameters, new HashMap<>());

            if (accountData == null) {
                throw new Exception("Exception occurred while getting accounts data");
            } else if (accountData.isEmpty() && !validationResponse.has(FDXCommonConstants.CONSENT_DATA)) {
                throw new Exception("Exception occurred while getting accounts data");
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
                validationResponse.put(FDXCommonConstants.CONSUMER_DATA, consumerDataObject);
            } catch (JSONException e) {
                log.error("Error occurred while parsing account data", e);
                throw new JSONException(e.getMessage());
            }
        } else {
            log.error("Sharable accounts endpoint is not configured properly");
            throw new Exception();
        }
    }

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

    public static void retrieveDataClusterData(JSONObject validationResponse) {

        // If previous validation failed
        if (validationResponse.has(FDXCommonConstants.STATUS)) {
            if (validationResponse.get(FDXCommonConstants.STATUS) == FailedResponse.StatusEnum.ERROR) {
                return;
            }
        } else {
            validationResponse.put(FDXCommonConstants.STATUS,
                    SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
        }
        for (Object item : validationResponse.getJSONArray(FDXCommonConstants.CONSENT_DATA)) {
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
