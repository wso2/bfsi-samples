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

package org.wso2.financial.services.fdx.extensions.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.json.JSONObject;
import org.wso2.financial.services.accelerator.consent.mgt.extensions.common.ConsentException;
import org.wso2.financial.services.fdx.extensions.model.DetailedConsentResourceDataWithAmendments;
import org.wso2.financial.services.fdx.extensions.model.ErrorResponse;
import org.wso2.financial.services.fdx.extensions.model.FailedResponseInConsent;
import org.wso2.financial.services.fdx.extensions.model.PersistAuthorizedConsentRequestBody;
import org.wso2.financial.services.fdx.extensions.model.Response200ForPersistAuthorizedConsent;
import org.wso2.financial.services.fdx.extensions.model.SuccessResponsePersistAuthorizedConsent;
import org.wso2.financial.services.fdx.extensions.model.SuccessResponsePersistAuthorizedConsentData;
import org.wso2.financial.services.fdx.extensions.utils.FDXCommonConstants;
import org.wso2.financial.services.fdx.extensions.utils.FDXConsentPersistUtils;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Represents a collection of functions to interact with the API endpoints.
 */
@Path("/persist-authorized-consent")
@Api(description = "the persist-authorized-consent API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PersistAuthorizedConsentApi {

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(
            value = "handle consent persistence logic and enrich response with user authorization and account mapping" +
                    " data",
            notes = "", response = Response200ForPersistAuthorizedConsent.class, authorizations = {
            @Authorization(value = "OAuth2", scopes = {
            }),

            @Authorization(value = "BasicAuth")
    }, tags = {"Consent"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response",
                    response = Response200ForPersistAuthorizedConsent.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Server Error", response = ErrorResponse.class)
    })
    public Response persistAuthorizedConsentPost(
            @Valid @NotNull PersistAuthorizedConsentRequestBody persistAuthorizedConsentRequestBody) {

        try {
            // Moved inside try block
            Map<String, Object> persistResponse =
                    FDXConsentPersistUtils.persistConsent(persistAuthorizedConsentRequestBody);

            if (persistResponse.get(FDXCommonConstants.STATUS) == FailedResponseInConsent.StatusEnum.ERROR) {
                FailedResponseInConsent failedResponseInConsent = new FailedResponseInConsent();
                failedResponseInConsent.setStatus(FailedResponseInConsent.StatusEnum.ERROR);
                failedResponseInConsent.setErrorCode(
                        (Integer) persistResponse.get(FDXCommonConstants.RESPONSE_STATUS));
                failedResponseInConsent.setData(persistResponse.get(FDXCommonConstants.DATA));
                return Response.status(Response.Status.OK)
                        .entity(new JSONObject(failedResponseInConsent).toString())
                        .build();
            } else {
                SuccessResponsePersistAuthorizedConsent response = new SuccessResponsePersistAuthorizedConsent();
                response.setResponseId(persistAuthorizedConsentRequestBody.getRequestId());
                response.setStatus((SuccessResponsePersistAuthorizedConsent.StatusEnum)
                        persistResponse.get(FDXCommonConstants.STATUS));

                response.setData(new SuccessResponsePersistAuthorizedConsentData()
                        .consentResource(new DetailedConsentResourceDataWithAmendments()
                                .type((String) persistResponse.get(FDXCommonConstants.TYPE))
                                .status((String) persistResponse.get(FDXCommonConstants.FDX_CONSENT_STATUS))
                                .validityTime(
                                        (Long) persistResponse.getOrDefault(FDXCommonConstants.DURATION_PERIOD, 0L))
                                .recurringIndicator(
                                        (Boolean) persistResponse.getOrDefault(FDXCommonConstants.IS_RECURRING, false))
                                .frequency(
                                        (Integer) persistResponse.getOrDefault(FDXCommonConstants.FREQUENCY_SIMPLE, 0))
                                .receipt(persistResponse.get(FDXCommonConstants.RECEIPT))
                                .attributes(persistResponse.get(FDXCommonConstants.ATTRIBUTES))
                                .authorizations(
                                        (List<org.wso2.financial.services.fdx.extensions.model.Authorization>)
                                                persistResponse.get(FDXCommonConstants.AUTHORIZATION_RESOURCES_KEY))
                        )
                );
                return Response.status(Response.Status.OK)
                        .entity(new JSONObject(response).toString())
                        .build();
            }
        } catch (ConsentException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(ErrorResponse.StatusEnum.ERROR);

            JSONObject errorData = new JSONObject();
            errorData.put("errorMessage", e.getStatus());
            errorData.put("errorDescription", e.getMessage());
            errorResponse.setData(errorData);

            int statusCode = e.getStatus().getStatusCode();
            return Response.status(statusCode)
                    .entity(new JSONObject(errorResponse).toString())
                    .build();

        }
    }
}
