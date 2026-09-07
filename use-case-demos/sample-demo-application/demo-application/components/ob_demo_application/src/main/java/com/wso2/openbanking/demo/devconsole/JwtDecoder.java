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

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Decodes JWTs into readable JSON for the developer console. Signatures are never revealed. */
public final class JwtDecoder {

    private static final int MASK_KEEP = 6;

    private JwtDecoder() {
    }

    /**
     * Checks whether a value has the three-part shape of a JWT.
     *
     * @param value candidate string
     * @return true when the value looks like a JWT
     */
    public static boolean looksLikeJwt(String value) {
        if (value == null || value.length() < 20) {
            return false;
        }
        String[] parts = value.split("\\.");
        return parts.length == 3 && parts[0].length() > 4 && parts[1].length() > 4;
    }

    /**
     * Decodes the header and payload of a JWT. The signature is replaced with its length only.
     *
     * @param jwt the compact serialized JWT
     * @return pretty printed JSON containing header and payload, or null when it cannot be decoded
     */
    public static String decode(String jwt) {
        if (!looksLikeJwt(jwt)) {
            return null;
        }
        try {
            String[] parts = jwt.split("\\.");
            JSONObject out = new JSONObject()
                    .put("header", new JSONObject(decodeSegment(parts[0])))
                    .put("payload", new JSONObject(decodeSegment(parts[1])))
                    .put("signature", "<" + parts[2].length() + " chars, not shown>");
            return out.toString(2);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Shortens a sensitive value so it can be correlated without being usable.
     *
     * @param value the value to mask
     * @return the masked value, or the original when it is too short to be worth masking
     */
    public static String mask(String value) {
        if (value == null || value.length() <= MASK_KEEP * 2) {
            return value;
        }
        return value.substring(0, MASK_KEEP) + "…[" + (value.length() - MASK_KEEP * 2) + " chars]…"
                + value.substring(value.length() - MASK_KEEP);
    }

    private static String decodeSegment(String segment) {
        return new String(Base64.getUrlDecoder().decode(segment), StandardCharsets.UTF_8);
    }
}
