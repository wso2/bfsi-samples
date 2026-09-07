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

import java.util.LinkedHashMap;
import java.util.Map;

/** A single captured Open Banking request/response exchange shown in the developer console. */
public class FlowEntry {

    private final long id;
    private final String timestamp;
    private final String label;
    private final String method;
    private final String url;
    private final Map<String, String> requestHeaders;
    private final String requestBody;
    private final Map<String, String> decoded = new LinkedHashMap<>();

    private int status;
    private String responseBody;
    private long durationMs;
    private boolean error;

    /**
     * Creates an entry describing an outbound exchange.
     *
     * @param id          monotonically increasing identifier
     * @param timestamp   ISO-8601 capture time
     * @param label       human readable step name
     * @param method      HTTP method
     * @param url         full request URL
     * @param headers     request headers, already redacted
     * @param requestBody request body, already redacted, may be null
     */
    public FlowEntry(long id, String timestamp, String label, String method, String url,
                     Map<String, String> headers, String requestBody) {
        this.id = id;
        this.timestamp = timestamp;
        this.label = label;
        this.method = method;
        this.url = url;
        this.requestHeaders = headers == null ? new LinkedHashMap<>() : new LinkedHashMap<>(headers);
        this.requestBody = requestBody;
    }

    /**
     * Records the outcome of the exchange.
     *
     * @param status       HTTP status code, or 0 when the call failed before a response
     * @param responseBody response body, already redacted
     * @param durationMs   wall clock duration in milliseconds
     * @param error        true when the call failed or returned a non-2xx status
     */
    public void complete(int status, String responseBody, long durationMs, boolean error) {
        this.status = status;
        this.responseBody = responseBody;
        this.durationMs = durationMs;
        this.error = error;
    }

    /**
     * Attaches a decoded JWT to this entry.
     *
     * @param name  where the token came from, e.g. "client_assertion"
     * @param value decoded JWT as readable JSON
     */
    public void addDecoded(String name, String value) {
        if (name != null && value != null) {
            decoded.put(name, value);
        }
    }

    /**
     * Serializes this entry for the developer console UI.
     *
     * @return JSON representation of the entry
     */
    public JSONObject toJson() {
        JSONObject request = new JSONObject()
                .put("method", method)
                .put("url", url)
                .put("headers", new JSONObject(requestHeaders));
        if (requestBody != null) {
            request.put("body", requestBody);
        }

        JSONObject response = new JSONObject()
                .put("status", status)
                .put("isError", error);
        if (responseBody != null) {
            response.put("body", responseBody);
        }

        JSONObject json = new JSONObject()
                .put("id", id)
                .put("timestamp", timestamp)
                .put("label", label)
                .put("durationMs", durationMs)
                .put("request", request)
                .put("response", response);

        if (!decoded.isEmpty()) {
            json.put("decoded", new JSONObject(decoded));
        }
        return json;
    }
}
