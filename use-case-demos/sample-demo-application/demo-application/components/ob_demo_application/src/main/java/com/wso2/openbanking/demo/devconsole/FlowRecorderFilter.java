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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Binds the session's flow log to the request thread for the length of every backend request, so
 * that the OAuth and Open Banking calls made while serving it are captured for the developer
 * console without every service having to know about the session.
 */
public final class FlowRecorderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        /* Nothing to configure */
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            FlowRecorder.bind(((HttpServletRequest) request).getSession(true));
        }
        try {
            chain.doFilter(request, response);
        } finally {
            FlowRecorder.unbind();
        }
    }

    @Override
    public void destroy() {
        /* Nothing to release */
    }
}
