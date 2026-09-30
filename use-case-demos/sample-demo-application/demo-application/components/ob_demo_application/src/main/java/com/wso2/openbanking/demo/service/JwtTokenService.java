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
import com.wso2.openbanking.demo.utils.ConfigLoader;
import com.wso2.openbanking.demo.utils.PkceUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/** Singleton service for creating and signing JWTs for OAuth client assertions and request objects. */
public final class JwtTokenService {

    private static final String SIGNING_KEY_PATH      = "/obsigning.key";
    private static final String SIGNATURE_ALGORITHM   = "RSASSA-PSS";
    private static final String HASH_ALGORITHM        = "SHA-256";
    private static final String MGF_ALGORITHM         = "MGF1";
    private static final int    SALT_LENGTH           = 32;
    private static final int    TRAILER_FIELD         = 1;
    private static final long   TOKEN_VALIDITY_MINUTES = 5;

    private static JwtTokenService instance;
    private final PrivateKey privateKey;

    /**
     * Initializes the service by loading the private signing key from the classpath.
     *
     * @throws GeneralSecurityException if the key format is invalid
     * @throws IOException              if the key file cannot be read
     */
    private JwtTokenService() throws GeneralSecurityException, IOException {
        this.privateKey = loadPrivateKey();
    }

    /**
     * Returns the singleton instance, creating it if it does not exist.
     *
     * @return shared JwtTokenService instance
     * @throws GeneralSecurityException if key loading fails
     * @throws IOException              if the key file cannot be read
     */
    public static synchronized JwtTokenService getInstance()
            throws GeneralSecurityException, IOException {
        if (instance == null) {
            instance = new JwtTokenService();
        }
        return instance;
    }

    /**
     * Creates a signed client assertion JWT for the given JTI value.
     *
     * @param jti unique JWT ID for the assertion
     * @return signed client assertion JWT string
     * @throws GeneralSecurityException if JWT signing fails
     * @throws IOException              if key loading fails
     */
    public String createClientAssertion(String jti)
            throws GeneralSecurityException, IOException {
        long issuedAt = getCurrentTimeSeconds();
        long expiration = issuedAt + TimeUnit.MINUTES.toSeconds(TOKEN_VALIDITY_MINUTES);

        JwtHeader header = new JwtHeader(
                ConfigLoader.getOAuthAlgorithm(),
                ConfigLoader.getClientKid(),
                ConfigLoader.getTokenType()
        );

        ClientAssertionPayload payload = new ClientAssertionPayload(
                ConfigLoader.getClientId(),
                ConfigLoader.getClientId(),
                expiration,
                issuedAt,
                jti,
                ConfigLoader.getTokenUrl()
        );

        return buildJwt(header.toJson(), payload.toJson());
    }

    /**
     * Creates a signed JWT request object for a Rich Authorization Request.
     *
     * @param authorizationDetails RFC 9396 authorization details describing the requested access
     * @param codeChallenge        PKCE code challenge for the authorization request
     * @param nonce                fresh nonce for this authorization request
     * @return signed JWT string representing the request object
     * @throws GeneralSecurityException if JWT signing fails
     */
    public String createRequestObject(JSONArray authorizationDetails, String codeChallenge, String nonce)
            throws GeneralSecurityException {
        long currentTime = getCurrentTimeSeconds();
        long expiration = currentTime + TimeUnit.MINUTES.toSeconds(TOKEN_VALIDITY_MINUTES);

        JwtHeader header = new JwtHeader(
                ConfigLoader.getOAuthAlgorithm(),
                ConfigLoader.getClientKid(),
                ConfigLoader.getTokenType()
        );

        RequestObjectPayload payload = new RequestObjectPayload.Builder()
                .iss(ConfigLoader.getClientId())
                .responseType(ConfigLoader.getResponseType())
                .responseMode(ConfigLoader.getResponseMode())
                .redirectUri(ConfigLoader.getRedirectUri())
                .nonce(nonce)
                .aud(ConfigLoader.getTokenUrl())
                .nbf(currentTime)
                .exp(expiration)
                .scope(OpenBankingConstants.SCOPE_OPENID)
                .codeChallenge(codeChallenge, PkceUtils.CODE_CHALLENGE_METHOD)
                .authorizationDetails(authorizationDetails)
                .build();

        return buildJwt(header.toJson(), payload.toJson());
    }

    /**
     * Builds a signed JWT string from the given header and payload JSON.
     *
     * @param headerJson  JSON string representing the JWT header
     * @param payloadJson JSON string representing the JWT payload
     * @return signed JWT in the format {@code header.payload.signature}
     * @throws GeneralSecurityException if signing the JWT fails
     */
    private String buildJwt(String headerJson, String payloadJson)
            throws GeneralSecurityException {
        String encodedHeader  = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
        String signingInput   = encodedHeader + "." + encodedPayload;
        return signingInput + "." + signData(signingInput);
    }

    /**
     * Signs the given data using RSA-PSS and returns the Base64URL-encoded signature.
     *
     * @param data string to be signed
     * @return Base64URL-encoded RSA-PSS signature
     * @throws GeneralSecurityException if signing fails or the algorithm is unavailable
     */
    private String signData(String data) throws GeneralSecurityException {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.setParameter(new PSSParameterSpec(
                HASH_ALGORITHM,
                MGF_ALGORITHM,
                new MGF1ParameterSpec(HASH_ALGORITHM),
                SALT_LENGTH,
                TRAILER_FIELD
        ));
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.US_ASCII));
        return base64UrlEncode(signature.sign());
    }

    /**
     * Loads the RSA private key from the classpath signing key file.
     *
     * @return loaded PrivateKey instance
     * @throws NoSuchAlgorithmException if the key algorithm is unsupported
     * @throws InvalidKeySpecException  if the key format is invalid
     * @throws IOException              if the key file is missing or unreadable
     */
    private PrivateKey loadPrivateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        InputStream keyStream = JwtTokenService.class.getResourceAsStream(SIGNING_KEY_PATH);
        if (keyStream == null) {
            throw new IOException("Signing key not found on classpath: " + SIGNING_KEY_PATH);
        }
        return KeyReader.loadPrivateKeyFromStream(keyStream);
    }

    /**
     * Encodes the given bytes to a Base64URL string without padding.
     *
     * @param bytes byte array to encode
     * @return Base64URL-encoded string without padding
     */
    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Returns the current time in seconds since the Unix epoch.
     *
     * @return current Unix timestamp in seconds
     */
    private long getCurrentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }
}