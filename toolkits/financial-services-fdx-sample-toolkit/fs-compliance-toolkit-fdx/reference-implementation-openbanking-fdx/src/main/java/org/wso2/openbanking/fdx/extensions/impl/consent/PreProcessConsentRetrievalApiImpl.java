/*
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 LLC. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein in any form is strictly forbidden, unless permitted by WSO2 expressly.
 * You may not alter or remove any copyright or other notice from copies of this content.
 */

package org.wso2.openbanking.fdx.extensions.impl.consent;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.openbanking.fdx.extensions.model.PreProcessConsentRequestBody;
import org.wso2.openbanking.fdx.extensions.model.StoredBasicConsentResourceData;
import org.wso2.openbanking.fdx.extensions.model.SuccessResponseForResponseAlternation;
import org.wso2.openbanking.fdx.extensions.model.SuccessResponseForResponseAlternationData;
import org.wso2.openbanking.fdx.extensions.utils.FDXCommonConstants;
import org.wso2.openbanking.fdx.extensions.utils.FDXCommonUtils;

import java.time.Instant;

import javax.ws.rs.core.Response;

/**
 * PreProcessConsentRetrievalApiImpl
 */
public class PreProcessConsentRetrievalApiImpl {

    public static Response handlePreProcessConsentRetrieval(PreProcessConsentRequestBody requestBody) {
        SuccessResponseForResponseAlternation response = new SuccessResponseForResponseAlternation();
        response.setResponseId(requestBody.getRequestId());
        response.setStatus(SuccessResponseForResponseAlternation.StatusEnum.SUCCESS);

        SuccessResponseForResponseAlternationData data = new SuccessResponseForResponseAlternationData();
        try {
            data.setModifiedResponse(constructModifiedPayload(requestBody));
        } catch (JsonProcessingException e) {
            data.setModifiedResponse(requestBody.getData().getConsentResource());
        }
        response.setData(data);
        return Response.status(Response.Status.OK).entity(new JSONObject(response).toString()).build();
    }

    private static JSONObject constructModifiedPayload(PreProcessConsentRequestBody requestBody)
            throws JsonProcessingException {

        StoredBasicConsentResourceData consentResource = requestBody.getData().getConsentResource();
        JSONObject receipt = FDXCommonUtils.convertObjectToJson(consentResource.getReceipt());
        JSONObject consentRequest = receipt.getJSONObject(FDXCommonConstants.CONSENT_REQUEST);

        JSONObject payload = new JSONObject();
        payload.put("id", consentResource.getId());
        payload.put("status", getFDXStatus(consentResource.getStatus()));

        if (consentRequest.has("parties")) {
            payload.put("parties", consentRequest.getJSONArray("parties"));
        }

        payload.put("createdTime", epochSecondsToIso8601(consentResource.getCreatedTime()));
        payload.put("updatedTime", epochSecondsToIso8601(consentResource.getUpdatedTime()));
        payload.put("durationType", consentRequest.optString(FDXCommonConstants.DURATION_TYPE));
        payload.put("lookbackPeriod", consentRequest.optInt(FDXCommonConstants.LOOKBACK_PERIOD));

        if (consentRequest.has(FDXCommonConstants.RESOURCES)) {
            payload.put("resources", consentRequest.getJSONArray(FDXCommonConstants.RESOURCES));
        }

        JSONObject links = new JSONObject();
        links.put("href", "/consents/" + consentResource.getId());
        links.put("action", "GET");
        JSONArray linksArray = new JSONArray();
        linksArray.put(links);
        payload.put("links", linksArray);

        return payload;
    }

    private static String epochSecondsToIso8601(Integer epochSeconds) {
        if (epochSeconds == null) {
            return null;
        }
        return Instant.ofEpochSecond(epochSeconds).toString();
    }

    private static String getFDXStatus(String status) {
        switch (status) {
            case "Revoked":
                return "REVOKED";
            case "Expired":
                return "EXPIRED";
            default:
                return "ACTIVE";
        }
    }
}
