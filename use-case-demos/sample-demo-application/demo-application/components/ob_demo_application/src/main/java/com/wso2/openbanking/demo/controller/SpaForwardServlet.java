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

package com.wso2.openbanking.demo.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Serves the single page application shell for the context root and for every client side
 * route, so deep links such as {@code /callback} render the application.
 *
 * <p>Mapping this servlet to {@code /*} also keeps the container from redirecting
 * {@code /<context>} to {@code /<context>/}. The Carbon request normalization valve
 * redirects any URL ending in a slash straight back to the slash-less form, so relying on
 * the welcome file alone leaves the browser bouncing between the two.
 */
public class SpaForwardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String SPA_SHELL = "/index.html";

    /**
     * Forwards the request to the application shell.
     *
     * @param request  the incoming servlet request
     * @param response the outgoing servlet response
     * @throws ServletException if the forward fails
     * @throws IOException      if an I/O error occurs while forwarding
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(SPA_SHELL).forward(request, response);
    }
}
