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

package org.wso2.openbanking.fdx.gateway.executor.dcr;

import com.google.gson.Gson;
import com.wso2.openbanking.accelerator.gateway.executor.dcr.DCRExecutor;
import com.wso2.openbanking.accelerator.gateway.executor.model.OBAPIRequestContext;
import com.wso2.openbanking.accelerator.gateway.executor.model.OBAPIResponseContext;
import org.apache.commons.lang3.StringUtils;
import org.wso2.openbanking.fdx.common.utils.CommonConstants;
import org.wso2.openbanking.fdx.common.utils.FDXCommonUtils;
import org.wso2.openbanking.fdx.gateway.util.FDXGatewayConstants;
import org.wso2.openbanking.fdx.gateway.util.FDXGatewayUtils;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.HttpMethod;

/**
 * Executor for adding X-FAPI-INTERACTION-ID to DCR response headers.
 */
public class FDXDCRExecutor extends DCRExecutor {

    private static final Gson gson = new Gson();

    @Override
    public void preProcessRequest(OBAPIRequestContext obapiRequestContext) {

        Map<String, String> requestHeaders = obapiRequestContext.getMsgInfo().getHeaders();
        String xFapiInteractionId = requestHeaders.get(FDXGatewayConstants.INTERACTION_ID_HEADER);
        //validate x-fapi-interaction-id header
        if (StringUtils.isEmpty(xFapiInteractionId)) {
            FDXGatewayUtils.handleInvalidHeaderFieldsError(obapiRequestContext,
                    "Mandatory header x-fapi-interaction-id is not provided");
            return;
        }
        if (!FDXGatewayUtils.isValidUUID(xFapiInteractionId)) {
            FDXGatewayUtils.handleInvalidHeaderFieldsError(obapiRequestContext, "Invalid interaction ID provided.");
            return;
        }
        obapiRequestContext.addContextProperty(FDXGatewayConstants.INTERACTION_ID_HEADER, xFapiInteractionId);

        if (HttpMethod.POST.equalsIgnoreCase(obapiRequestContext.getMsgInfo().getHttpMethod()) ||
                HttpMethod.PUT.equalsIgnoreCase(obapiRequestContext.getMsgInfo().getHttpMethod())) {

            // set client name as the software id in DCR request payload
            Map<String, Object>  requestParameters  = gson.fromJson(obapiRequestContext
                    .getRequestPayload(), Map.class);
            requestParameters.put(FDXGatewayConstants.SOFTWARE_ID,
                    requestParameters.get(FDXGatewayConstants.CLIENT_NAME));
            String requestPayload = gson.toJson(requestParameters);
            obapiRequestContext.setModifiedPayload(requestPayload);
        }

        super.preProcessRequest(obapiRequestContext);
    }

    @Override
    public void postProcessResponse(OBAPIResponseContext obapiResponseContext) {

        super.postProcessResponse(obapiResponseContext);

        // add interaction id to response headers
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put(FDXGatewayConstants.INTERACTION_ID_HEADER,
                obapiResponseContext.getContextProperty(FDXGatewayConstants.INTERACTION_ID_HEADER));
        obapiResponseContext.setAddedHeaders(responseHeaders);

        if (HttpMethod.POST.equalsIgnoreCase(obapiResponseContext.getMsgInfo().getHttpMethod()) ||
                HttpMethod.PUT.equalsIgnoreCase(obapiResponseContext.getMsgInfo().getHttpMethod())) {

            // remove software id from dcr response payload
            Map<String, Object> responseParameters = gson.fromJson(obapiResponseContext
                    .getResponsePayload(), Map.class);
            responseParameters.remove(FDXGatewayConstants.SOFTWARE_ID);
            FDXCommonUtils.convertDoubleValueToInt(responseParameters, CommonConstants.DURATION_PERIOD);
            FDXCommonUtils.convertDoubleValueToInt(responseParameters, CommonConstants.LOOKBACK_PERIOD);
            String responsePayload = gson.toJson(responseParameters);
            obapiResponseContext.setModifiedPayload(responsePayload);
        }
    }
}

