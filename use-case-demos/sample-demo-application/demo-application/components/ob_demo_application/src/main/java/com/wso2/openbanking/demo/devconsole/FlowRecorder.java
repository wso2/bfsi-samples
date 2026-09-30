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

import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * Where the code that talks to the bank writes what it did, for the developer console to read.
 *
 * <p>The calls worth showing are made several layers below the request handler and, for the API
 * calls, by services that never see the session. Rather than thread a log through every one of
 * those signatures, the log of the session being served is bound to the request thread for the
 * length of the request by {@link FlowRecorderFilter}; each of these calls is a blocking HTTP
 * call on that same thread, so it finds the right log. Nothing bound means nothing recorded, so
 * this is safe to call from anywhere.
 */
public final class FlowRecorder {

    private static final ThreadLocal<FlowLog> CURRENT = new ThreadLocal<>();

    private FlowRecorder() {
        /* This utility class should not be instantiated */
    }

    /**
     * Binds the given session's flow log to this thread for the length of a request.
     *
     * @param session browser session being served
     */
    public static void bind(HttpSession session) {
        if (session != null) {
            CURRENT.set(FlowLog.of(session));
        }
    }

    /** Releases the log bound to this thread, so a pooled thread carries nothing over. */
    public static void unbind() {
        CURRENT.remove();
    }

    /**
     * Records a completed HTTP call.
     *
     * @param label        short name of the step, e.g. "Token Exchange"
     * @param method       HTTP method
     * @param url          target URL
     * @param headers      headers that were sent
     * @param requestBody  request body as it was sent, may be null
     * @param status       HTTP status of the response
     * @param responseBody response body as it was received, may be null
     * @param error        true when the status was not a success
     */
    public static void record(String label, String method, String url, Map<String, String> headers,
                              String requestBody, int status, String responseBody, boolean error) {
        FlowLog log = CURRENT.get();
        if (log == null) {
            return;
        }
        log.add(label, method, url, FlowFormatter.headers(headers), FlowFormatter.body(requestBody),
                status, FlowFormatter.body(responseBody), error);
    }

    /**
     * Records a step that is not an HTTP call this application makes itself, such as the browser
     * being redirected to the authorization server and coming back.
     *
     * @param label   short name of the step
     * @param method  method of the browser hop
     * @param url     URL involved
     * @param detail  what the step carried, rendered by the formatter, may be null
     * @param jwt     JWT the step carried, shown decoded beside it, may be null
     */
    public static void recordHop(String label, String method, String url, String detail, String jwt) {
        FlowLog log = CURRENT.get();
        if (log == null) {
            return;
        }
        log.add(label, method, url, null, FlowFormatter.body(detail), 0, null, false);
        String claims = FlowFormatter.jwtClaims(jwt);
        if (claims != null) {
            log.attachDecoded(label, claims);
        }
    }

    /**
     * Attaches the claim set of a JWT that the step just recorded returned, so the console shows
     * the exchange and what it actually yielded side by side.
     *
     * @param expectedLabel label the step must carry for the claims to belong to it
     * @param jwt           compact serialized JWT that was received
     */
    public static void attachClaims(String expectedLabel, String jwt) {
        FlowLog log = CURRENT.get();
        if (log == null) {
            return;
        }
        String claims = FlowFormatter.jwtClaims(jwt);
        if (claims != null) {
            log.attachDecoded(expectedLabel, claims);
        }
    }
}
