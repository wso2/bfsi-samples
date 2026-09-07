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

import com.wso2.openbanking.demo.utils.ConfigLoader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory ring buffer of Open Banking exchanges, captured for the developer console.
 * Holds at most {@value #CAPACITY} entries; the oldest are discarded first.
 */
public final class FlowRecorder {

    /** Maximum number of exchanges retained. */
    public static final int CAPACITY = 100;

    /** Body/response fields whose values are shortened before being exposed. */
    private static final String[] SENSITIVE_FIELDS = {
            "access_token", "id_token", "refresh_token", "client_assertion", "request"
    };

    private static final Deque<FlowEntry> ENTRIES = new ArrayDeque<>();
    private static final AtomicLong SEQUENCE = new AtomicLong();

    private FlowRecorder() {
    }

    /**
     * Begins capturing an exchange. Returns null when the console is disabled.
     *
     * @param method  HTTP method
     * @param url     full request URL
     * @param headers request headers
     * @param body    request body, may be null
     * @return the entry to complete once the response is known, or null when disabled
     */
    public static FlowEntry start(String method, String url, Map<String, String> headers, String body) {
        if (!ConfigLoader.isDevConsoleEnabled()) {
            return null;
        }
        FlowEntry entry = new FlowEntry(
                SEQUENCE.incrementAndGet(),
                Instant.now().toString(),
                labelFor(method, url),
                method,
                url,
                redactHeaders(headers),
                redactBody(body));
        decodeInto(entry, body, "request");
        synchronized (ENTRIES) {
            if (ENTRIES.size() >= CAPACITY) {
                ENTRIES.removeFirst();
            }
            ENTRIES.addLast(entry);
        }
        return entry;
    }

    /**
     * Completes a previously started exchange.
     *
     * @param entry      the entry returned by start, may be null
     * @param status     HTTP status code, or 0 when no response was received
     * @param body       response body
     * @param durationMs wall clock duration in milliseconds
     * @param error      true when the call failed or returned a non-2xx status
     */
    public static void finish(FlowEntry entry, int status, String body, long durationMs, boolean error) {
        if (entry == null) {
            return;
        }
        entry.complete(status, redactBody(body), durationMs, error);
        decodeInto(entry, body, "response");
    }

    /**
     * Records a step that involves no outbound call from this backend, such as the browser
     * redirect to the authorization endpoint.
     *
     * @param label human readable step name
     * @param url   the URL the browser is sent to
     */
    public static void recordRedirect(String label, String url) {
        if (!ConfigLoader.isDevConsoleEnabled()) {
            return;
        }
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("(no request from backend)", "browser follows this URL");
        FlowEntry entry = new FlowEntry(SEQUENCE.incrementAndGet(), Instant.now().toString(),
                label, "REDIRECT", redactUrl(url), headers, null);
        entry.complete(302, "User agent is redirected to the authorization server.", 0, false);
        decodeQueryParams(entry, url);
        synchronized (ENTRIES) {
            if (ENTRIES.size() >= CAPACITY) {
                ENTRIES.removeFirst();
            }
            ENTRIES.addLast(entry);
        }
    }

    /**
     * Returns every retained exchange, oldest first.
     *
     * @return the captured exchanges as JSON
     */
    public static JSONArray snapshot() {
        List<FlowEntry> copy;
        synchronized (ENTRIES) {
            copy = new ArrayList<>(ENTRIES);
        }
        JSONArray array = new JSONArray();
        for (FlowEntry entry : copy) {
            array.put(entry.toJson());
        }
        return array;
    }

    /** Discards every retained exchange. */
    public static void clear() {
        synchronized (ENTRIES) {
            ENTRIES.clear();
        }
    }

    private static String labelFor(String method, String url) {
        if (url == null) {
            return method;
        }
        if (url.contains("/oauth2/par")) {
            return "Pushed authorization request";
        }
        if (url.contains("/oauth2/token")) {
            return "Token request";
        }
        if (url.contains("account-access-consents")) {
            return "DELETE".equals(method) ? "Revoke account access consent" : "Create account access consent";
        }
        if (url.contains("payment-consents")) {
            return "Create payment consent";
        }
        if (url.contains("/balances")) {
            return "Get account balances";
        }
        if (url.contains("/transactions")) {
            return "Get account transactions";
        }
        if (url.contains("/payments")) {
            return "Submit payment";
        }
        if (url.contains("/accounts")) {
            return "Get accounts";
        }
        return method + " " + url;
    }

    private static Map<String, String> redactHeaders(Map<String, String> headers) {
        Map<String, String> out = new LinkedHashMap<>();
        if (headers == null) {
            return out;
        }
        for (Map.Entry<String, String> header : headers.entrySet()) {
            String value = header.getValue();
            if (redactionOn() && "Authorization".equalsIgnoreCase(header.getKey()) && value != null) {
                int space = value.indexOf(' ');
                value = space > 0
                        ? value.substring(0, space + 1) + JwtDecoder.mask(value.substring(space + 1))
                        : JwtDecoder.mask(value);
            }
            out.put(header.getKey(), value);
        }
        return out;
    }

    private static String redactBody(String body) {
        if (body == null || !redactionOn()) {
            return body;
        }
        String out = body;
        for (String field : SENSITIVE_FIELDS) {
            out = maskFormField(out, field);
            out = maskJsonField(out, field);
        }
        return out;
    }

    private static String redactUrl(String url) {
        if (url == null || !redactionOn()) {
            return url;
        }
        return maskFormField(url, "request");
    }

    private static String maskFormField(String text, String field) {
        int at = text.indexOf(field + "=");
        if (at < 0) {
            return text;
        }
        int start = at + field.length() + 1;
        int end = text.indexOf('&', start);
        if (end < 0) {
            end = text.length();
        }
        String value = text.substring(start, end);
        if (value.isEmpty()) {
            return text;
        }
        return text.substring(0, start) + JwtDecoder.mask(value) + text.substring(end);
    }

    private static String maskJsonField(String text, String field) {
        String needle = "\"" + field + "\"";
        int at = text.indexOf(needle);
        if (at < 0) {
            return text;
        }
        int open = text.indexOf('"', text.indexOf(':', at + needle.length()) + 1);
        if (open < 0) {
            return text;
        }
        int close = text.indexOf('"', open + 1);
        if (close < 0) {
            return text;
        }
        String value = text.substring(open + 1, close);
        return text.substring(0, open + 1) + JwtDecoder.mask(value) + text.substring(close);
    }

    private static void decodeInto(FlowEntry entry, String raw, String origin) {
        if (raw == null) {
            return;
        }
        for (String field : SENSITIVE_FIELDS) {
            String token = extractRaw(raw, field);
            if (token != null) {
                String decoded = JwtDecoder.decode(token);
                if (decoded != null) {
                    entry.addDecoded(origin + "." + field, decoded);
                }
            }
        }
    }

    private static void decodeQueryParams(FlowEntry entry, String url) {
        if (url == null) {
            return;
        }
        String token = extractRaw(url, "request");
        if (token != null) {
            String decoded = JwtDecoder.decode(urlDecode(token));
            if (decoded != null) {
                entry.addDecoded("request_object", decoded);
            }
        }
    }

    private static String extractRaw(String text, String field) {
        int at = text.indexOf(field + "=");
        if (at >= 0) {
            int start = at + field.length() + 1;
            int end = text.indexOf('&', start);
            return text.substring(start, end < 0 ? text.length() : end);
        }
        String needle = "\"" + field + "\"";
        at = text.indexOf(needle);
        if (at < 0) {
            return null;
        }
        try {
            JSONObject json = new JSONObject(text);
            return json.has(field) ? json.getString(field) : null;
        } catch (RuntimeException e) {
            return null;
        }
    }

    private static String urlDecode(String value) {
        try {
            return java.net.URLDecoder.decode(value, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return value;
        }
    }

    private static boolean redactionOn() {
        return ConfigLoader.isDevConsoleRedacted();
    }
}
