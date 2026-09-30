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

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * The steps of the flow one browser has driven, newest last.
 *
 * <p>It lives in the HTTP session for the same reason the rest of the flow state does: the
 * authorization request and the callback are separate requests, the JAX-RS resource serving them
 * is instantiated per request, and the log has to survive the customer's trip to the
 * authorization server. Keeping it per session also means one customer's console never shows
 * another's tokens.
 */
public final class FlowLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SESSION_ATTRIBUTE = "ob.demo.devconsole.flowLog";

    /** Older steps are dropped past this, so a long lived session cannot grow without bound. */
    private static final int MAX_ENTRIES = 60;

    private final List<FlowEntry> entries = new ArrayList<>();
    private int nextId = 1;

    private FlowLog() {
        /* Obtained through of(HttpSession) so one log is shared by the whole session */
    }

    /**
     * Returns the log of the given session, creating it on first use.
     *
     * @param session browser session driving the flow
     * @return the session's flow log
     */
    public static synchronized FlowLog of(HttpSession session) {
        FlowLog log = (FlowLog) session.getAttribute(SESSION_ATTRIBUTE);
        if (log == null) {
            log = new FlowLog();
            session.setAttribute(SESSION_ATTRIBUTE, log);
        }
        return log;
    }

    /**
     * Appends a step to the log.
     *
     * @param label          short name of the step, e.g. "Token Exchange"
     * @param method         HTTP method of the call
     * @param url            target URL
     * @param requestHeaders headers that were sent, rendered for display, may be null
     * @param requestBody    request body, rendered for display, may be null
     * @param status         HTTP status of the response, or 0 when the step is not an HTTP call
     * @param responseBody   response body, rendered for display, may be null
     * @param error          true when the step failed
     */
    synchronized void add(String label, String method, String url, String requestHeaders,
                          String requestBody, int status, String responseBody, boolean error) {
        entries.add(new FlowEntry(nextId, label, Instant.now().toString(), method, url,
                requestHeaders, requestBody, status, responseBody, error));
        nextId++;
        while (entries.size() > MAX_ENTRIES) {
            entries.remove(0);
        }
    }

    /**
     * Attaches decoded JWT claims to the step just recorded.
     *
     * <p>The claims are read after the call that produced the JWT returns, so they belong to the
     * last entry. The expected label is checked rather than assumed: if anything else was
     * recorded in between the claims are dropped instead of being shown against the wrong step.
     *
     * @param expectedLabel label the last entry must carry for the claims to belong to it
     * @param claims        decoded claims, rendered for display
     */
    synchronized void attachDecoded(String expectedLabel, String claims) {
        if (entries.isEmpty()) {
            return;
        }
        FlowEntry last = entries.get(entries.size() - 1);
        if (expectedLabel.equals(last.getLabel())) {
            last.setDecodedPayload(claims);
        }
    }

    /**
     * Returns the steps recorded so far.
     *
     * @return an unmodifiable snapshot, oldest first
     */
    public synchronized List<FlowEntry> snapshot() {
        return Collections.unmodifiableList(new ArrayList<>(entries));
    }

    /** Empties the log, so the next flow can be demonstrated on its own. */
    public synchronized void clear() {
        entries.clear();
        nextId = 1;
    }
}
