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

import com.wso2.openbanking.demo.constants.OpenBankingConstants;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents the JWT request object payload for a Rich Authorization Request (RFC 9396).
 * The access being requested is carried by {@code authorization_details} rather than by a
 * scope or a pre-initiated consent identifier.
 */
public class RequestObjectPayload {

    private final String iss;
    private final String responseType;
    private final String responseMode;
    private final String redirectUri;
    private final String nonce;
    private final String clientId;
    private final String aud;
    private final long nbf;
    private final long exp;
    private final String scope;
    private final String codeChallenge;
    private final String codeChallengeMethod;
    private final JSONArray authorizationDetails;

    /**
     * Creates a RequestObjectPayload from the given builder.
     *
     * @param builder builder instance with all payload fields set
     */
    private RequestObjectPayload(Builder builder) {
        this.iss = builder.iss;
        this.responseType = builder.responseType;
        this.responseMode = builder.responseMode;
        this.redirectUri = builder.redirectUri;
        this.nonce = builder.nonce;
        this.clientId = builder.iss;
        this.aud = builder.aud;
        this.nbf = builder.nbf;
        this.exp = builder.exp;
        this.scope = builder.scope;
        this.codeChallenge = builder.codeChallenge;
        this.codeChallengeMethod = builder.codeChallengeMethod;
        this.authorizationDetails = builder.authorizationDetails;
    }

    /** Builder for constructing a RequestObjectPayload with individual field setters. */
    public static class Builder {

        String iss;
        private String responseType;
        private String responseMode;
        private String redirectUri;
        private String nonce;
        private String aud;
        private long nbf;
        private long exp;
        private String scope;
        private String codeChallenge;
        private String codeChallengeMethod;
        private JSONArray authorizationDetails;

        /**
         * Sets the issuer claim.
         *
         * @param iss OAuth client ID used as the issuer
         * @return this builder
         */
        public Builder iss(String iss) {
            this.iss = iss;
            return this;
        }

        /**
         * Sets the expected OAuth response type.
         *
         * @param responseType expected OAuth response type
         * @return this builder
         */
        public Builder responseType(String responseType) {
            this.responseType = responseType;
            return this;
        }

        /**
         * Sets the response mode, for example {@code jwt} to request a JARM response.
         *
         * @param responseMode OAuth response mode
         * @return this builder
         */
        public Builder responseMode(String responseMode) {
            this.responseMode = responseMode;
            return this;
        }

        /**
         * Sets the redirect URI the authorization response is returned to.
         *
         * @param redirectUri OAuth callback redirect URI
         * @return this builder
         */
        public Builder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        /**
         * Sets the nonce claim.
         *
         * @param nonce unique value to prevent replay attacks
         * @return this builder
         */
        public Builder nonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        /**
         * Sets the audience claim.
         *
         * @param aud intended audience, typically the token endpoint URL
         * @return this builder
         */
        public Builder aud(String aud) {
            this.aud = aud;
            return this;
        }

        /**
         * Sets the not-before claim.
         *
         * @param nbf Unix timestamp before which the token is not valid
         * @return this builder
         */
        public Builder nbf(long nbf) {
            this.nbf = nbf;
            return this;
        }

        /**
         * Sets the expiry claim.
         *
         * @param exp Unix timestamp at which the token expires
         * @return this builder
         */
        public Builder exp(long exp) {
            this.exp = exp;
            return this;
        }

        /**
         * Sets the requested scopes.
         *
         * @param scope space-separated OAuth scopes to request
         * @return this builder
         */
        public Builder scope(String scope) {
            this.scope = scope;
            return this;
        }

        /**
         * Sets the PKCE code challenge and its transformation method.
         *
         * @param codeChallenge       PKCE code challenge
         * @param codeChallengeMethod code challenge transformation method
         * @return this builder
         */
        public Builder codeChallenge(String codeChallenge, String codeChallengeMethod) {
            this.codeChallenge = codeChallenge;
            this.codeChallengeMethod = codeChallengeMethod;
            return this;
        }

        /**
         * Sets the authorization details describing the access being requested.
         *
         * @param authorizationDetails RFC 9396 authorization details array
         * @return this builder
         */
        public Builder authorizationDetails(JSONArray authorizationDetails) {
            // Copied so a later edit by the caller cannot change what gets signed.
            this.authorizationDetails = new JSONArray(authorizationDetails.toString());
            return this;
        }

        /**
         * Builds the request object payload.
         *
         * @return a new RequestObjectPayload instance
         */
        public RequestObjectPayload build() {
            return new RequestObjectPayload(this);
        }
    }

    /**
     * Serializes the payload to a JSON string including all OAuth and authorization detail fields.
     *
     * @return JSON string representation of the request object payload
     */
    public String toJson() {
        JSONObject payload = new JSONObject()
                .put("iss", iss)
                .put("response_type", responseType)
                .put("redirect_uri", redirectUri)
                .put("nonce", nonce)
                .put("client_id", clientId)
                .put("aud", aud)
                .put("nbf", nbf)
                .put("exp", exp)
                .put("iat", nbf)
                .put("scope", scope)
                .put(OpenBankingConstants.FIELD_AUTHORIZATION_DETAILS, authorizationDetails);

        if (responseMode != null) {
            payload.put("response_mode", responseMode);
        }
        if (codeChallenge != null) {
            payload.put("code_challenge", codeChallenge);
            payload.put("code_challenge_method", codeChallengeMethod);
        }

        return payload.toString();
    }
}
