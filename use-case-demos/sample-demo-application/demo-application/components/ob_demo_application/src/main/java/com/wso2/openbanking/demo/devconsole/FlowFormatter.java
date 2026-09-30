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

import com.wso2.openbanking.demo.utils.JwtDecoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Renders what went over the wire into something readable in the developer console.
 *
 * <p>The interesting parts of an Open Banking flow are hidden twice over: the authorization
 * request and the client credential are JWTs, and they travel URL-encoded inside a form body. A
 * raw dump of that body shows neither the requested {@code authorization_details} nor the claims
 * the client authenticated with, which is exactly what the console exists to show. So form
 * bodies are split into their parameters and any parameter that is a JWT is expanded into its
 * claims underneath.
 */
final class FlowFormatter {

    private static final int INDENT = 2;
    private static final int JWT_PART_COUNT = 3;
    private static final String NESTED_INDENT = "    ";

    private FlowFormatter() {
        /* This utility class should not be instantiated */
    }

    /**
     * Renders a request or response body for display.
     *
     * @param body raw body as it was sent or received, may be null
     * @return the body pretty printed as JSON, unpacked as a form body, or as-is when it is
     *         neither; null when there was no body
     */
    static String body(String body) {
        if (body == null) {
            return null;
        }
        String trimmed = body.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        String asJson = prettyJson(trimmed);
        if (asJson != null) {
            return asJson;
        }
        if (isFormEncoded(trimmed)) {
            return formBody(trimmed);
        }
        return trimmed;
    }

    /**
     * Renders request headers for display, one per line.
     *
     * @param headers headers that were sent
     * @return the rendered headers, or null when there were none
     */
    static String headers(Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        StringBuilder rendered = new StringBuilder();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            if (rendered.length() > 0) {
                rendered.append('\n');
            }
            rendered.append(header.getKey()).append(": ").append(header.getValue());
        }
        return rendered.toString();
    }

    /**
     * Renders the claim set of a JWT for display.
     *
     * @param jwt compact serialized JWT
     * @return the claims pretty printed, or null when the value is not a readable JWT
     */
    static String jwtClaims(String jwt) {
        if (jwt == null || jwt.split("\\.").length != JWT_PART_COUNT) {
            return null;
        }
        try {
            return JwtDecoder.decodeClaims(jwt).toString(INDENT);
        } catch (IllegalArgumentException | JSONException e) {
            return null;
        }
    }

    /**
     * Renders a JSON structure for display.
     *
     * @param value candidate JSON text
     * @return the value pretty printed, or null when it is not JSON
     */
    static String prettyJson(String value) {
        String trimmed = value == null ? "" : value.trim();
        try {
            if (trimmed.startsWith("{")) {
                return new JSONObject(trimmed).toString(INDENT);
            }
            if (trimmed.startsWith("[")) {
                return new JSONArray(trimmed).toString(INDENT);
            }
        } catch (JSONException e) {
            return null;
        }
        return null;
    }

    /**
     * Splits a form encoded body into its parameters, expanding any JWT parameter into its
     * claims. Parameter order is preserved, so the body reads as it was built.
     *
     * @param body URL-encoded form body
     * @return the rendered parameters
     */
    private static String formBody(String body) {
        StringBuilder rendered = new StringBuilder();
        for (String pair : body.split("&")) {
            if (pair.isEmpty()) {
                continue;
            }
            int separator = pair.indexOf('=');
            String name = separator < 0 ? pair : pair.substring(0, separator);
            String value = separator < 0 ? "" : decode(pair.substring(separator + 1));

            if (rendered.length() > 0) {
                rendered.append('\n');
            }
            String json = prettyJson(value);
            String claims = jwtClaims(value);
            if (json != null) {
                rendered.append(name).append(":\n").append(indent(json));
            } else if (claims != null) {
                rendered.append(name).append(": ").append(value)
                        .append('\n').append(indent(claims));
            } else {
                rendered.append(name).append(": ").append(value);
            }
        }
        return rendered.toString();
    }

    /**
     * Decides whether a body is a URL-encoded form. Anything with a parameter name followed by
     * "=" before the first line break is treated as one; the bodies this application sends are
     * either JSON, already ruled out by the caller, or forms.
     *
     * @param body body to inspect
     * @return true when the body looks like a form
     */
    private static boolean isFormEncoded(String body) {
        int separator = body.indexOf('=');
        if (separator <= 0) {
            return false;
        }
        String name = body.substring(0, separator);
        return name.indexOf('\n') < 0 && name.indexOf(' ') < 0;
    }

    private static String indent(String text) {
        return NESTED_INDENT + text.replace("\n", "\n" + NESTED_INDENT);
    }

    private static String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException | IllegalArgumentException e) {
            return value;
        }
    }
}
