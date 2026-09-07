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

import com.wso2.openbanking.demo.devconsole.FlowRecorder;
import com.wso2.openbanking.demo.exceptions.SSLContextCreationException;
import com.wso2.openbanking.demo.http.AuthUrlBuilder;
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
    private static final String HEADER_FAPI_ID       = "x-fapi-financial-id";
    private static final String HEADER_IDEMPOTENCY   = "x-idempotency-key";

    private static final String MEDIA_JSON            = "application/json";
    private static final String MEDIA_JSON_UTF8       = "application/json; charset=UTF-8";
    private static final String MEDIA_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private static final String BEARER_PREFIX = "Bearer ";

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

    public String postJwt(String url, String body) throws IOException {
        return HttpConnection.post(url, sslContext)
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_FORM_URLENCODED)
                .addHeader(HEADER_ACCEPT, MEDIA_JSON)
                .withBody(body)
                .execute();
    }

    public String postAccessToken(String url, String body) throws IOException {
        return HttpConnection.post(url, sslContext)
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_FORM_URLENCODED)
                .addHeader("Cache-Control", "no-cache")
                .withBody(body)
                .execute();
    }

    public String postConsentInit(String url, String body, String token) throws IOException {
        String fapiId = ConfigLoader.getFapiFinancialId();
        if (logger.isInfoEnabled()) {
            logger.info("Consent initiation request send {}", url);
        }
        String response = HttpConnection.post(url, sslContext)
                .addHeader(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .addHeader(HEADER_FAPI_ID, fapiId)
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_JSON)
                .withBody(body)
                .execute();
        if (logger.isInfoEnabled()) {
            logger.info("Consent initiation response received {}", url);
        }
        return response;
    }

    public String postConsentAuthRequest(String requestObjectJwt, String clientId, String scope) {
        String authorizeUrl = AuthUrlBuilder.build(requestObjectJwt, clientId, scope);
        FlowRecorder.recordRedirect("Browser redirect to authorization endpoint", authorizeUrl);
        return authorizeUrl;
    }

    public String getWithAuth(String url, String token) throws IOException {
        String fapiId = ConfigLoader.getFapiFinancialId();
        if (logger.isInfoEnabled()) {
            logger.info("Request send, {}", url);
        }
        String response = HttpConnection.get(url, sslContext)
                .addHeader(HEADER_FAPI_ID, fapiId)
                .addHeader(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .addHeader(HEADER_ACCEPT, MEDIA_JSON)
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_JSON_UTF8)
                .execute();
        if (logger.isInfoEnabled()) {
            logger.info("Response received from bank, {}", url);
        }
        return response;
    }

    public String postPaymentConsentInit(String url, String body, String token) throws IOException {
        String idempotencyKey = UUID.randomUUID().toString();
        String fapiId = ConfigLoader.getFapiFinancialId();
        if (logger.isInfoEnabled()) {
            logger.info("Payment consent request send, {}", url);
        }
        String response = HttpConnection.post(url, sslContext)
                .addHeader(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .addHeader(HEADER_FAPI_ID, fapiId)
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_JSON)
                .addHeader(HEADER_IDEMPOTENCY, idempotencyKey)
                .withBody(body)
                .execute();
        if (logger.isInfoEnabled()) {
            logger.info("Payment consent response received, {}", url);
        }
        return response;
    }

    public String postPayments(String url, String body, String token) throws IOException {
        String idempotencyKey = UUID.randomUUID().toString();
        String fapiId = ConfigLoader.getFapiFinancialId();
        if (logger.isInfoEnabled()) {
            logger.info("Payment request send, {}", url);
        }
        String response = HttpConnection.post(url, sslContext)
                .addHeader(HEADER_FAPI_ID, fapiId)
                .addHeader(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .addHeader(HEADER_ACCEPT, MEDIA_JSON)
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_JSON_UTF8)
                .addHeader(HEADER_IDEMPOTENCY, idempotencyKey)
                .withBody(body)
                .execute();
        if (logger.isInfoEnabled()) {
            logger.info("Payment submission response received, {}", url);
        }
        return response;
    }

    public boolean deleteWithAuth(String url, String token) throws IOException {
        String fapiId = ConfigLoader.getFapiFinancialId();
        if (logger.isInfoEnabled()) {
            logger.info("Consent revocation request send, {}", url);
        }
        int statusCode = HttpConnection.delete(url, sslContext)
                .addHeader(HEADER_FAPI_ID, fapiId)
                .addHeader(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .addHeader(HEADER_ACCEPT, MEDIA_JSON)
                .executeAndGetStatus();
        if (logger.isInfoEnabled()) {
            logger.info("Consent revocation response received, {}", url);
        }
        return statusCode >= 200 && statusCode < 300;
    }
}
