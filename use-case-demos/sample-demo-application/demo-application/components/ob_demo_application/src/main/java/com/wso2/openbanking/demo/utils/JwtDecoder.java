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

package com.wso2.openbanking.demo.utils;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Reads the claims out of a compact serialized JWT.
 *
 * <p>The signature is NOT verified. Both JWTs this demo reads back are minted by the paired
 * Identity Server, and this keeps the sample free of a JOSE dependency. A production client
 * must verify the JARM response against the authorization server's JWKS before trusting it,
 * because that JWT arrives through the browser rather than over the client's own TLS
 * connection to the server.
 */
public final class JwtDecoder {

    private static final int JWT_PART_COUNT = 3;
    private static final int PAYLOAD_INDEX = 1;

    private JwtDecoder() {
        /* This utility class should not be instantiated */
    }

    /**
     * Decodes the claim set of a compact serialized JWT.
     *
     * @param jwt compact serialized JWT
     * @return the decoded claims
     * @throws IllegalArgumentException if the value is not a three part JWT or the payload is not JSON
     */
    public static JSONObject decodeClaims(String jwt) {
        if (jwt == null || jwt.isEmpty()) {
            throw new IllegalArgumentException("JWT is missing");
        }
        String[] parts = jwt.split("\\.");
        if (parts.length != JWT_PART_COUNT) {
            throw new IllegalArgumentException(
                    "Expected a three part JWT but found " + parts.length + " parts");
        }
        byte[] payload = Base64.getUrlDecoder().decode(parts[PAYLOAD_INDEX]);
        return new JSONObject(new String(payload, StandardCharsets.UTF_8));
    }

    /**
     * Returns the value of a claim, or null when the JWT does not carry it.
     *
     * @param jwt   compact serialized JWT
     * @param claim name of the claim to read
     * @return the claim value, or null if absent
     */
    public static String optClaim(String jwt, String claim) {
        JSONObject claims = decodeClaims(jwt);
        return claims.has(claim) ? claims.getString(claim) : null;
    }
}
