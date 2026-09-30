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
import com.wso2.openbanking.demo.exceptions.SSLContextCreationException;
import com.wso2.openbanking.demo.http.HttpConnection;
import com.wso2.openbanking.demo.http.SSLContextFactory;
import com.wso2.openbanking.demo.utils.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import javax.net.ssl.SSLContext;

/** HttpTlsClient implementation. */
public final class HttpTlsClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpTlsClient.class);

    private static final String HEADER_CONTENT_TYPE  = "Content-Type";
    private static final String HEADER_ACCEPT        = "Accept";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_IDEMPOTENCY   = "x-idempotency-key";
    private static final String HEADER_JWS_SIGNATURE = "x-jws-signature";

    private static final String MEDIA_JSON            = "application/json";
    private static final String MEDIA_JSON_UTF8       = "application/json; charset=UTF-8";
    private static final String MEDIA_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private static final String BEARER_PREFIX = "Bearer ";

    // Names the developer console shows against each call.
    private static final String STEP_TOKEN     = "Token Exchange";
    private static final String STEP_PAR       = "Pushed Authorization Request";
    private static final String STEP_PAYMENT   = "Submit Payment";
    private static final String STEP_REVOKE    = "Revoke Consent";

    private final String certPath;
    private final String keyPath;
    private final SSLContext sslContext;

    /**
     * Creates an {@link HttpTlsClient} using the provided certificate and private key paths.
     *
     * @param certPath path to the PEM-encoded certificate file
     * @param keyPath  path to the PEM-encoded private key file
     * @throws SSLContextCreationException if the TLS context fails to initialize
     */
    public HttpTlsClient(String certPath, String keyPath)
            throws SSLContextCreationException {
        this.certPath = certPath;
        this.keyPath = keyPath;
        this.sslContext = SSLContextFactory.create(certPath, keyPath);
    }

    /**
     * Creates a new {@link HttpTlsClient} instance using the same TLS credentials as this instance.
     * Since {@link SSLContext} is not cloneable, a fresh context is initialized from the
     * stored credential paths, ensuring the caller retains no shared mutable reference.
     *
     * @return a new {@link HttpTlsClient} backed by a fresh {@link SSLContext}
     * @throws SSLContextCreationException if the TLS context fails to initialize
     */
    public HttpTlsClient deepCopy() throws SSLContextCreationException {
        return new HttpTlsClient(this.certPath, this.keyPath);
    }

    public String postAccessToken(String url, String body) throws IOException {
        return postAccessToken(url, body, STEP_TOKEN);
    }

    /**
     * Posts to the token endpoint, naming the step it represents in the developer console.
     *
     * <p>The endpoint serves more than one grant, and they are different steps to anyone reading
     * the flow: an authorization code exchange completes a customer authorization, while a client
     * credentials request does not involve a customer at all. Labelling both "Token Exchange"
     * would hide that.
     *
     * @param url   token endpoint URL
     * @param body  URL-encoded request body
     * @param label step name shown in the developer console
     * @return raw JSON response from the token endpoint
     * @throws IOException if the request fails
     */
    public String postAccessToken(String url, String body, String label) throws IOException {
        return HttpConnection.post(url, sslContext)
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_FORM_URLENCODED)
                .addHeader("Cache-Control", "no-cache")
                .withBody(body)
                .withLabel(label)
                .execute();
    }

    public String postPushedAuthorizationRequest(String url, String body) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("Pushed authorization request send, {}", url);
        }
        String response = HttpConnection.post(url, sslContext)
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_FORM_URLENCODED)
                .addHeader(HEADER_ACCEPT, MEDIA_JSON)
                .withBody(body)
                .withLabel(STEP_PAR)
                .execute();
        if (logger.isInfoEnabled()) {
            logger.info("Pushed authorization response received, {}", url);
        }
        return response;
    }

    public String getWithAuth(String url, String token) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("Request send, {}", url);
        }
        String response = HttpConnection.get(url, sslContext)
                .addHeader(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .addHeader(HEADER_ACCEPT, MEDIA_JSON)
                .withLabel(resourceStep(url))
                .execute();
        if (logger.isInfoEnabled()) {
            logger.info("Response received from bank, {}", url);
        }
        return response;
    }

    public String postPayments(String url, String body, String token) throws IOException {
        String idempotencyKey = UUID.randomUUID().toString();
        if (logger.isInfoEnabled()) {
            logger.info("Payment request send, {}", url);
        }
        String response = HttpConnection.post(url, sslContext)
                .addHeader(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .addHeader(HEADER_ACCEPT, MEDIA_JSON)
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_JSON_UTF8)
                .addHeader(HEADER_IDEMPOTENCY, idempotencyKey)
                .addHeader(HEADER_JWS_SIGNATURE, ConfigLoader.getJwsSignature())
                .withBody(body)
                .withLabel(STEP_PAYMENT)
                .execute();
        if (logger.isInfoEnabled()) {
            logger.info("Payment submission response received, {}", url);
        }
        return response;
    }

    /**
     * Names a resource read for the developer console, from the resource the URL addresses,
     * so that the several reads a single account fan out into stay distinguishable.
     *
     * @param url resource URL being read
     * @return the step name, e.g. "Fetch Transactions"
     */
    /**
     * Deletes a consent, authenticating with a client credentials token.
     *
     * @param url   consent resource URL
     * @param token client credentials access token
     * @return true when the bank accepted the revocation
     * @throws IOException if the request fails
     */
    public boolean deleteWithAuth(String url, String token) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("Consent revocation request send, {}", url);
        }
        int status = HttpConnection.delete(url, sslContext)
                .addHeader(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .addHeader(HEADER_ACCEPT, MEDIA_JSON)
                .withLabel(STEP_REVOKE)
                .executeAndGetStatus();
        if (logger.isInfoEnabled()) {
            logger.info("Consent revocation response received, status {}", status);
        }
        return status >= 200 && status < 300;
    }

    private static String resourceStep(String url) {
        if (url.endsWith(OpenBankingConstants.PATH_TRANSACTIONS)) {
            return "Fetch Transactions";
        }
        if (url.endsWith(OpenBankingConstants.PATH_BALANCES)) {
            return "Fetch Balances";
        }
        if (url.endsWith(OpenBankingConstants.PATH_ACCOUNTS)) {
            return "Fetch Accounts";
        }
        return "Fetch Account";
    }
}
