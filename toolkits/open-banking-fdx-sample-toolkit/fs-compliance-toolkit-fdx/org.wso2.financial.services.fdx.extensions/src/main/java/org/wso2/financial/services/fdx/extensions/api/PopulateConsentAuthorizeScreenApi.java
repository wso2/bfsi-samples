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
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.financial.services.fdx.extensions.model.ErrorResponse;
import org.wso2.financial.services.fdx.extensions.model.FailedResponse;
import org.wso2.financial.services.fdx.extensions.model.PopulateConsentAuthorizeScreenRequestBody;
import org.wso2.financial.services.fdx.extensions.model.Response200ForPopulateConsentAuthorizeScreen;
import org.wso2.financial.services.fdx.extensions.model.SuccessResponsePopulateConsentAuthorizeScreen;
import org.wso2.financial.services.fdx.extensions.model.SuccessResponsePopulateConsentAuthorizeScreenData;
import org.wso2.financial.services.fdx.extensions.utils.FDXCommonConstants;
import org.wso2.financial.services.fdx.extensions.utils.FDXConsentRetrievalUtils;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Represents a collection of functions to interact with the API endpoints.
 */
@Path("/populate-consent-authorize-screen")
@Api(description = "the populate-consent-authorize-screen API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PopulateConsentAuthorizeScreenApi {

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(
            value = "handle validations before consent  authorization and consent data to load in consent " +
                    "authorization UI",
            notes = "", response = Response200ForPopulateConsentAuthorizeScreen.class, authorizations = {
            @Authorization(value = "OAuth2", scopes = {
            }),

            @Authorization(value = "BasicAuth")
    }, tags = {"Consent"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = Response200ForPopulateConsentAuthorizeScreen.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Server Error", response = ErrorResponse.class)
    })
    public Response populateConsentAuthorizeScreenPost(
            @Valid PopulateConsentAuthorizeScreenRequestBody populateConsentAuthorizeScreenRequestBody) {
        try {
            JSONObject consentRetrievalResponse =
                    FDXConsentRetrievalUtils.retrieveConsentData(populateConsentAuthorizeScreenRequestBody);

            if (consentRetrievalResponse.get(FDXCommonConstants.STATUS) == FailedResponse.StatusEnum.ERROR) {
                FailedResponse failedResponse = new FailedResponse();
                failedResponse.setStatus(FailedResponse.StatusEnum.ERROR);
                failedResponse.setErrorCode(consentRetrievalResponse.getInt(FDXCommonConstants.RESPONSE_STATUS));
                failedResponse.setData(consentRetrievalResponse.get(FDXCommonConstants.DATA));
                return Response.status(Response.Status.OK).entity(new JSONObject(failedResponse).toString()).build();
            } else {
                SuccessResponsePopulateConsentAuthorizeScreen response =
                        new SuccessResponsePopulateConsentAuthorizeScreen();
                response.setResponseId(populateConsentAuthorizeScreenRequestBody.getRequestId());
                response.setStatus((SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum)
                        consentRetrievalResponse.get(FDXCommonConstants.STATUS));
                response.setData(new SuccessResponsePopulateConsentAuthorizeScreenData()
                        .consentData(consentRetrievalResponse.get(FDXCommonConstants.CONSENT_DATA))
                        .consumerData(consentRetrievalResponse.get(FDXCommonConstants.CONSUMER_DATA)));
                return Response.status(Response.Status.OK).entity(new JSONObject(response).toString()).build();
            }
        } catch (JSONException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(ErrorResponse.StatusEnum.ERROR);

            JSONObject errorData = new JSONObject();
            errorData.put(FDXCommonConstants.ERROR_MESSAGE, FDXCommonConstants.INVALID_REQUEST_MSG);
            errorData.put(FDXCommonConstants.ERROR_DESCRIPTION, e.getMessage());

            errorResponse.setData(errorData);
            return Response.status(Response.Status.BAD_REQUEST).entity(new JSONObject(errorResponse).toString())
                    .build();
        }
    }
}
