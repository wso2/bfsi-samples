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

package com.wso2.openbanking.demo.service;

import javax.servlet.http.HttpSession;

/**
 * Holds the state of the authorization flow that is in flight for one browser.
 *
 * <p>It lives in the HTTP session because that is what spans the redirect to the authorization
 * server and back: the browser leaves the application at the authorization request and returns
 * to the callback as a separate request, and the JAX-RS resource serving them is instantiated
 * per request. Keeping this per session also means two customers can authorize at once.
 */
final class AuthFlowState {

    private static final String ATTR_CODE_VERIFIER = "ob.demo.pkce.codeVerifier";
    private static final String ATTR_REQUEST_STATUS = "ob.demo.flow.requestStatus";
    private static final String ATTR_PAYMENT_INITIATION = "ob.demo.flow.paymentInitiation";

    private AuthFlowState() {
        /* This holder class should not be instantiated */
    }

    /**
     * Stores the code verifier of the authorization request about to be sent.
     *
     * @param session browser session driving the flow
     * @param codeVerifier PKCE code verifier
     */
    static void storeCodeVerifier(HttpSession session, String codeVerifier) {
        session.setAttribute(ATTR_CODE_VERIFIER, codeVerifier);
    }

    /**
     * Returns the stored code verifier and clears it, so it is used only once.
     *
     * @param session browser session driving the flow
     * @return the code verifier of the in-flight authorization request, or null if there is none
     */
    static String consumeCodeVerifier(HttpSession session) {
        String codeVerifier = (String) session.getAttribute(ATTR_CODE_VERIFIER);
        session.removeAttribute(ATTR_CODE_VERIFIER);
        return codeVerifier;
    }

    /**
     * Records which flow the pending authorization belongs to.
     *
     * @param session browser session driving the flow
     * @param status  flow type identifier
     */
    static void storeRequestStatus(HttpSession session, String status) {
        session.setAttribute(ATTR_REQUEST_STATUS, status);
    }

    /**
     * Returns the flow the pending authorization belongs to.
     *
     * @param session      browser session driving the flow
     * @param defaultValue value to use when no flow was recorded
     * @return the recorded flow type identifier
     */
    static String getRequestStatus(HttpSession session, String defaultValue) {
        String status = (String) session.getAttribute(ATTR_REQUEST_STATUS);
        return status == null ? defaultValue : status;
    }

    /**
     * Stores the payment initiation the customer is authorizing, so the submission after the
     * callback carries exactly what was consented to.
     *
     * @param session    browser session driving the flow
     * @param initiation serialized payment initiation
     */
    static void storePaymentInitiation(HttpSession session, String initiation) {
        session.setAttribute(ATTR_PAYMENT_INITIATION, initiation);
    }

    /**
     * Returns the stored payment initiation and clears it.
     *
     * @param session browser session driving the flow
     * @return the serialized payment initiation, or null when no payment is pending
     */
    static String consumePaymentInitiation(HttpSession session) {
        String initiation = (String) session.getAttribute(ATTR_PAYMENT_INITIATION);
        session.removeAttribute(ATTR_PAYMENT_INITIATION);
        return initiation;
    }
}
