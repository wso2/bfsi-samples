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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.financial.services.accelerator.consent.mgt.extensions.common.AuthErrorCode;
import org.wso2.financial.services.accelerator.consent.mgt.extensions.common.ConsentException;
import org.wso2.financial.services.accelerator.consent.mgt.extensions.common.ResponseStatus;
import org.wso2.financial.services.fdx.extensions.model.Authorization;
import org.wso2.financial.services.fdx.extensions.model.PersistAuthorizedConsent;
import org.wso2.financial.services.fdx.extensions.model.PersistAuthorizedConsentRequestBody;
import org.wso2.financial.services.fdx.extensions.model.Resource;
import org.wso2.financial.services.fdx.extensions.model.SuccessResponsePersistAuthorizedConsent;
import org.wso2.financial.services.fdx.extensions.model.UserGrantedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for persisting FDX consent.
 */
public class FDXConsentPersistUtils {

    private static final Log log = LogFactory.getLog(FDXConsentPersistUtils.class);

    /**
     * Persists the FDX consent data.
     *
     * @param persistAuthorizedConsentRequestBody The request body containing the consent data to be persisted.
     * @return A map containing the response of the persistence operation.
     */
    public static Map<String, Object> persistConsent(
            PersistAuthorizedConsentRequestBody persistAuthorizedConsentRequestBody) {

        Map<String, Object> persistResponse = new HashMap<>();

        persistResponse.put(FDXCommonConstants.STATUS,
                SuccessResponsePersistAuthorizedConsent.StatusEnum.SUCCESS);

        // Get the consent data from the request body
        PersistAuthorizedConsent consentData = persistAuthorizedConsentRequestBody.getData();
        if (consentData == null) {
            log.error("Consent data is not available");
            throw new ConsentException(ResponseStatus.BAD_REQUEST, AuthErrorCode.SERVER_ERROR.name(),
                    "Consent data is not available");
        }

        UserGrantedData userGrantedData = consentData.getUserGrantedData();

        // Get details of consented accounts
        JSONObject authorizedResources = new JSONObject((Map<?, ?>) userGrantedData.getAuthorizedResources());

        ArrayList<String> accountIdList = getAccountIdList(authorizedResources);

        JSONObject jsonObject = new JSONObject((Map<?, ?>) userGrantedData.getRequestParameters());

        StringBuilder dataClustersString = new StringBuilder();
        long validityPeriod = 0;
        JSONObject authorizationDetailsJSON = new JSONObject();
        if (jsonObject.has(FDXCommonConstants.AUTHORIZATION_DETAILS)
                && jsonObject.get(FDXCommonConstants.AUTHORIZATION_DETAILS) instanceof JSONArray) {
            JSONArray authorizationDetails = jsonObject.getJSONArray(FDXCommonConstants.AUTHORIZATION_DETAILS);

            authorizationDetailsJSON.put(FDXCommonConstants.AUTHORIZATION_DETAILS, authorizationDetails);

            // We are only considering the first authorization detail for now. Might need to handle multiple
            // authroization details
            if (!(authorizationDetails.isEmpty())) {
                JSONObject authorizationDetail = authorizationDetails.getJSONObject(0);

                if (authorizationDetail.has(FDXCommonConstants.CONSENT_REQUEST)) {
                    JSONObject consentRequest = authorizationDetail.getJSONObject(FDXCommonConstants.CONSENT_REQUEST);
                    if (consentRequest.has(FDXCommonConstants.DURATION_TYPE) && !
                            consentRequest.getString(FDXCommonConstants.DURATION_TYPE)
                                    .equals(FDXCommonConstants.ONE_TIME_CONSENT)) {
                        if (consentRequest.has(FDXCommonConstants.DURATION_PERIOD)) {
                            validityPeriod = consentRequest.getLong(FDXCommonConstants.DURATION_PERIOD);
                        }
                    }
                    if (consentRequest.has(FDXCommonConstants.RESOURCES)) {
                        JSONArray resources = consentRequest.getJSONArray(FDXCommonConstants.RESOURCES);
                        JSONObject resource = resources.getJSONObject(0);
                        if (resource.has(FDXCommonConstants.DATA_CLUSTERS_TITLE)) {
                            JSONArray dataClusters = resource.getJSONArray(FDXCommonConstants.DATA_CLUSTERS_TITLE);
                            for (int i = 0; i < dataClusters.length(); i++) {
                                dataClustersString.append(dataClusters.getString(i));
                                dataClustersString.append("&");
                            }
                        }
                    }
                }
            }
        } else {
            log.error("Authorization details are missing or not in the correct format");
            throw new ConsentException(ResponseStatus.BAD_REQUEST, "Invalid or missing authorization details");
        }

        String commonAuthId = authorizedResources.getJSONObject(FDXCommonConstants.COOKIES)
                .getString(FDXCommonConstants.COMMON_AUTH_ID);
        Map<String, String> attributes = new HashMap<>();
        attributes.put(FDXCommonConstants.COMMON_AUTH_ID, commonAuthId);

        List<Authorization> authorizations = buildAuthorizationResource(
                userGrantedData.getUserId(),
                accountIdList,
                dataClustersString.toString()
        );

        persistResponse.put(FDXCommonConstants.FDX_CONSENT_STATUS, FDXCommonConstants.FDX_CONSENT_AUTHORISED);
        persistResponse.put(FDXCommonConstants.TYPE, FDXCommonConstants.FDX_TYPE);
        persistResponse.put(FDXCommonConstants.RECEIPT, authorizationDetailsJSON);
        persistResponse.put(FDXCommonConstants.IS_RECURRING, false);
        persistResponse.put(FDXCommonConstants.DURATION_PERIOD, validityPeriod);
        persistResponse.put(FDXCommonConstants.ATTRIBUTES, attributes);
        persistResponse.put(FDXCommonConstants.AUTHORIZATION_RESOURCES_KEY, authorizations);

        return persistResponse;
    }

    /**
     * Builds the authorization resource for the consent.
     *
     * @param userId        The user ID.
     * @param accountIdList The list of account IDs.
     * @param permissions   The permissions granted by the user.
     * @return A list of Authorization objects representing the authorization resources.
     */
    private static List<Authorization> buildAuthorizationResource(String userId, ArrayList<String> accountIdList,
                                                                  String permissions) {
        List<Resource> resources = new ArrayList<>();
        for (String accountId : accountIdList) {
            Resource resource = new Resource();
            resource.setAccountId(accountId);
            resource.setPermission(permissions);
            resource.setStatus(FDXCommonConstants.FDX_CONSENT_AUTHORISED);
            resources.add(resource);
        }
        List<Authorization> authorizations = new ArrayList<>();
        Authorization authorization = new Authorization();
        authorization.setResources(resources);
        authorization.setType(FDXCommonConstants.FDX_TYPE);
        authorization.setStatus(FDXCommonConstants.FDX_CONSENT_AUTHORISED);
        authorization.setUserId(userId);
        authorizations.add(authorization);
        return authorizations;
    }

    /**
     * Retrieves the account ID list from the payload.
     *
     * @param payload The payload containing the account IDs.
     * @return A list of account IDs.
     */
    private static ArrayList<String> getAccountIdList(JSONObject payload) {
        if (payload.get(FDXCommonConstants.ACCOUNT_IDS) == null
                || !(payload.get(FDXCommonConstants.ACCOUNT_IDS) instanceof JSONArray)) {
            log.error("Account IDs not available in persist request");
            throw new ConsentException(ResponseStatus.BAD_REQUEST, "Account IDs not available in persist request");
        }

        JSONArray accountIds = (JSONArray) payload.get(FDXCommonConstants.ACCOUNT_IDS);
        ArrayList<String> accountIdsList = new ArrayList<>();
        for (Object account : accountIds) {
            if (!(account instanceof String)) {
                log.error("Account IDs format error in persist request");
                throw new ConsentException(ResponseStatus.BAD_REQUEST, "Account IDs format error in persist request");
            }
            accountIdsList.add((String) account);
        }
        return accountIdsList;
    }
}
