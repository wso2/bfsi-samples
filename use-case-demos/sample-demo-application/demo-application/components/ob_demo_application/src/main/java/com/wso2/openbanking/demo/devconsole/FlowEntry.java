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

package com.wso2.openbanking.demo.devconsole;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * One step of the Open Banking flow as the developer console shows it: what this application
 * sent, what came back, and - where a JWT was involved - what that JWT actually said.
 */
public final class FlowEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String label;
    private final String timestamp;
    private final String method;
    private final String url;
    private final String requestHeaders;
    private final String requestBody;
    private final int status;
    private final String responseBody;
    private final boolean error;
    private String decodedPayload;

    /**
     * Creates a flow entry.
     *
     * @param id             position of this step in the session's flow log, starting at 1
     * @param label          short name of the step, e.g. "Token Exchange"
     * @param timestamp      ISO-8601 instant the step was recorded
     * @param method         HTTP method, or the browser hop's method for a redirect
     * @param url            target URL
     * @param requestHeaders headers that were sent, already rendered for display, may be null
     * @param requestBody    request body, already rendered for display, may be null
     * @param status         HTTP status of the response, or 0 when the step is not an HTTP call
     * @param responseBody   response body, already rendered for display, may be null
     * @param error          true when the step failed
     */
    FlowEntry(int id, String label, String timestamp, String method, String url,
              String requestHeaders, String requestBody, int status, String responseBody,
              boolean error) {
        this.id = id;
        this.label = label;
        this.timestamp = timestamp;
        this.method = method;
        this.url = url;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
        this.status = status;
        this.responseBody = responseBody;
        this.error = error;
    }

    /**
     * Returns the step's label.
     *
     * @return short name of the step
     */
    String getLabel() {
        return label;
    }

    /**
     * Attaches the claim set of the JWT this step produced, so the console can show it beside the
     * raw exchange.
     *
     * @param claims decoded claims, rendered for display
     */
    void setDecodedPayload(String claims) {
        this.decodedPayload = claims;
    }

    /**
     * Renders the entry in the shape the developer console expects.
     *
     * @return JSON object with the request, the response, and any decoded JWT payload
     */
    public JSONObject toJson() {
        JSONObject request = new JSONObject()
                .put("method", method)
                .put("url", url);
        if (requestHeaders != null) {
            request.put("headers", requestHeaders);
        }
        if (requestBody != null) {
            request.put("body", requestBody);
        }

        JSONObject json = new JSONObject()
                .put("id", id)
                .put("label", label)
                .put("timestamp", timestamp)
                .put("request", request);

        if (status != 0 || responseBody != null || error) {
            JSONObject response = new JSONObject()
                    .put("body", responseBody == null ? "" : responseBody)
                    .put("isError", error);
            if (status != 0) {
                response.put("status", status);
            }
            json.put("response", response);
        }
        if (decodedPayload != null) {
            json.put("decodedPayload", decodedPayload);
        }
        return json;
    }
}
