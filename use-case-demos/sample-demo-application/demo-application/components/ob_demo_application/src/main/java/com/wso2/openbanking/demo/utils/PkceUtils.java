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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/** Utility class for generating PKCE code verifiers and code challenges (RFC 7636). */
public final class PkceUtils {

    /** Code challenge transformation method advertised to the authorization server. */
    public static final String CODE_CHALLENGE_METHOD = "S256";

    private static final String DIGEST_ALGORITHM = "SHA-256";
    private static final int VERIFIER_BYTE_LENGTH = 32;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder BASE64_URL = Base64.getUrlEncoder().withoutPadding();

    private PkceUtils() {
        /* This utility class should not be instantiated */
    }

    /**
     * Generates a high entropy code verifier as defined in RFC 7636 section 4.1.
     *
     * @return base64url encoded code verifier without padding
     */
    public static String generateCodeVerifier() {
        byte[] verifierBytes = new byte[VERIFIER_BYTE_LENGTH];
        RANDOM.nextBytes(verifierBytes);
        return BASE64_URL.encodeToString(verifierBytes);
    }

    /**
     * Derives the S256 code challenge for the given code verifier.
     *
     * @param codeVerifier code verifier returned by {@link #generateCodeVerifier()}
     * @return base64url encoded SHA-256 digest of the code verifier
     * @throws NoSuchAlgorithmException if the SHA-256 digest is unavailable
     */
    public static String deriveCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
        byte[] digest = MessageDigest.getInstance(DIGEST_ALGORITHM)
                .digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
        return BASE64_URL.encodeToString(digest);
    }
}
