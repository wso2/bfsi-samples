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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

/** Utility class for generating JWT helper values. */
public class JwtUtils {

    private static final int NONCE_BYTE_LENGTH = 16;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder BASE64_URL = Base64.getUrlEncoder().withoutPadding();

    /**
     * Generates a unique JWT ID from a random UUID.
     *
     * @return unique numeric JWT ID string
     */
    public static String generateJti() {
        return new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16).toString();
    }

    /**
     * Generates a fresh nonce for an authorization request. It has to be unique per request:
     * a replayed nonce lets the authorization server reject the authorization as a replay.
     *
     * @return base64url encoded random nonce
     */
    public static String generateNonce() {
        byte[] nonceBytes = new byte[NONCE_BYTE_LENGTH];
        RANDOM.nextBytes(nonceBytes);
        return BASE64_URL.encodeToString(nonceBytes);
    }
}