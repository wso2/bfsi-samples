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

package com.wso2.openbanking.demo.http;

import com.wso2.openbanking.demo.utils.ConfigLoader;

import static org.apache.cxf.common.util.UrlUtils.urlEncode;

/** Builds the OAuth authorization URL for a pushed authorization request. */
public class AuthUrlBuilder {

    /**
     * Builds an authorization URL that references a request already pushed to the PAR endpoint.
     * All other authorization parameters were sent with the pushed request, so only the client
     * identifier and the request URI are carried on the redirect (RFC 9126 section 4).
     *
     * @param requestUri request URI returned by the PAR endpoint
     * @param clientId   OAuth client ID
     * @return fully constructed authorization URL
     */
    public static String buildWithRequestUri(String requestUri, String clientId) {
        return ConfigLoader.getAuthorizeUrl()
                + "?client_id=" + urlEncode(clientId)
                + "&request_uri=" + urlEncode(requestUri);
    }
}
