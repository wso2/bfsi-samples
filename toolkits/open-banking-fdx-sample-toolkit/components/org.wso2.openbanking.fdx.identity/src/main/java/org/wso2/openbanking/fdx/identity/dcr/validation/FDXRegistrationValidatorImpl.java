/**
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
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

package org.wso2.openbanking.fdx.identity.dcr.validation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.wso2.openbanking.accelerator.identity.dcr.exception.DCRValidationException;
import com.wso2.openbanking.accelerator.identity.dcr.model.RegistrationRequest;
import com.wso2.openbanking.accelerator.identity.dcr.utils.ValidatorUtils;
import com.wso2.openbanking.accelerator.identity.dcr.validation.RegistrationValidator;
import com.wso2.openbanking.accelerator.identity.util.IdentityCommonConstants;
import com.wso2.openbanking.accelerator.identity.util.IdentityCommonUtil;
import org.wso2.openbanking.fdx.common.utils.CommonConstants;
import org.wso2.openbanking.fdx.common.utils.FDXCommonUtils;
import org.wso2.openbanking.fdx.identity.dcr.model.FDXRegistrationRequest;
import org.wso2.openbanking.fdx.identity.dcr.model.FDXRegistrationResponse;
import org.wso2.openbanking.fdx.identity.dcr.utils.FDXValidatorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.wso2.openbanking.fdx.identity.dcr.utils.FDXRegistrationUtils.getJsonObject;
import static org.wso2.openbanking.fdx.identity.dcr.utils.FDXRegistrationUtils.isJsonString;

/**
 * FDX specific registration validator class..
 */
public class FDXRegistrationValidatorImpl extends RegistrationValidator {

    private static final Gson gson = new Gson();

    @Override
    public void validatePost(RegistrationRequest registrationRequest) throws DCRValidationException {

        validateRequest(registrationRequest);
    }

    @Override
    public void validateGet(String clientId) throws DCRValidationException {

    }

    @Override
    public void validateDelete(String clientId) throws DCRValidationException {

    }

    @Override
    public void validateUpdate(RegistrationRequest registrationRequest) throws DCRValidationException {

        validateRequest(registrationRequest);
    }

    @Override
    public void setSoftwareStatementPayload(RegistrationRequest registrationRequest, String decodedSSA) {

    }

    @Override
    public String getRegistrationResponse(Map<String, Object> spMetaData) {

        spMetaData.forEach((key, value) -> {
            if (value instanceof ArrayList) {
                List<Object> metaDataList = (ArrayList<Object>) value;
                // Convert JSON strings  in metadata list to JSON objects
                for (Object element : metaDataList) {
                    if (isJsonString(element)) {
                        metaDataList.set(metaDataList.indexOf(element), getJsonObject(element.toString()));
                    }
                }
            }
        });

        // Append registration access token and registration client URI to the DCR response if the config is enabled
        if (IdentityCommonUtil.getDCRModifyResponseConfig()) {
            String tlsCert = spMetaData.get(IdentityCommonConstants.TLS_CERT).toString();
            String clientId = spMetaData.get(IdentityCommonConstants.CLIENT_ID).toString();

            if (!spMetaData.containsKey(IdentityCommonConstants.REGISTRATION_ACCESS_TOKEN)) {
                // add the access token to the response
                spMetaData.put(IdentityCommonConstants.REGISTRATION_ACCESS_TOKEN,
                        ValidatorUtils.generateAccessToken(clientId, tlsCert));
            }

            // add the dcr url to the response with the client id appended at the end
            spMetaData.put(IdentityCommonConstants.REGISTRATION_CLIENT_URI,
                    ValidatorUtils.getRegistrationClientURI() + clientId);
        }

        JsonElement jsonElement = gson.toJsonTree(spMetaData);
        FDXRegistrationResponse fdxRegistrationResponse =
                gson.fromJson(jsonElement, FDXRegistrationResponse.class);
        return gson.toJson(fdxRegistrationResponse);
    }

    /**
     * validate the request parameters when creating or updating a registration.
     *
     * @param registrationRequest request
     * @throws DCRValidationException if any validation failure occurs
     */
    private void validateRequest(RegistrationRequest registrationRequest) throws DCRValidationException {

        // convert requestParameters in the registrationRequest to a fdxRegistrationRequest
        Map<String, Object> requestParameters = registrationRequest.getRequestParameters();
        JsonElement jsonElement = gson.toJsonTree(requestParameters);
        FDXRegistrationRequest fdxRegistrationRequest = gson.fromJson(jsonElement, FDXRegistrationRequest.class);

        // do validations related to registration request
        ValidatorUtils.getValidationViolations(fdxRegistrationRequest);

        // add grant types and an authentication method to the registration request
        FDXValidatorUtils.addAllowedGrantTypes(registrationRequest);
        FDXValidatorUtils.addAllowedTokenEndpointAuthMethod(registrationRequest);

        //convert duration_period and lookback_period values to integers
        FDXCommonUtils.convertDoubleValueToInt(registrationRequest.getRequestParameters(),
                CommonConstants.DURATION_PERIOD);
        FDXCommonUtils.convertDoubleValueToInt(registrationRequest.getRequestParameters(),
                CommonConstants.LOOKBACK_PERIOD);
    }
}

