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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** ConfigLoader implementation. */
public class ConfigLoader {

    private static final Properties prop = new Properties();

    static {
        try (InputStream inputStream = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading application.properties", e);
        }
    }

    public static String getProperty(String key) {
        String value = prop.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found: " + key);
        }
        return value;
    }

    public static String getClientId() {
        return getProperty("oauth.client.id");
    }

    public static String getClientKid() {
        return getProperty("oauth.client.kid");
    }

    public static String getOAuthAlgorithm() {
        return getProperty("oauth.algorithm");
    }

    public static String getTokenType() {
        return getProperty("oauth.token.type");
    }

    public static String getTokenUrl() {
        return getProperty("oauth.token.url");
    }

    public static String getAuthorizeUrl() {
        return getProperty("oauth.authorize.url");
    }

    public static String getRedirectUri() {
        return getProperty("oauth.redirect.uri");
    }

    public static String getOAuthState() {
        return getProperty("oauth.state");
    }

    public static String getOAuthNonce() {
        return getProperty("oauth.nonce");
    }

    public static String getOAuthPrompt() {
        return getProperty("oauth.prompt");
    }

    public static String getResponseType() {
        return getProperty("oauth.response.type");
    }

    public static String getAccountBaseUrl() {
        return getProperty("openbanking.account.base.url");
    }

    public static String getPaymentBaseUrl() {
        return getProperty("openbanking.payment.base.url");
    }

    public static String getFapiFinancialId() {
        return getProperty("openbanking.fapi.financial.id");
    }

    public static String getCertificatePath() {
        return getProperty("ssl.certificate.path");
    }

    public static String getKeyPath() {
        return getProperty("ssl.key.path");
    }

    public static String getTruststorePath() {
        return getProperty("ssl.truststore.path");
    }

    public static String getTruststorePassword() {
        return getProperty("ssl.truststore.password");
    }

    public static String getFrontendHomeUrl() {
        return getProperty("frontend.home.url");
    }

    public static String getBackendBaseUrl() {
        return getProperty("backend.base.url");
    }

    public static String getCorsAllowedOrigin() {

        return getProperty("cors.allowed.origin");
    }

    public static String getIsBaseUrl() {
        return getProperty("is.base.url");
    }

    /**
     * Whether the developer console captures Open Banking exchanges. Enabled unless explicitly disabled.
     *
     * @return true when capture is enabled
     */
    public static boolean isDevConsoleEnabled() {
        return !"false".equalsIgnoreCase(getProperty("devconsole.enabled"));
    }

    /**
     * Whether tokens, assertions and signatures are shortened before being exposed to the console.
     *
     * @return true when redaction is enabled
     */
    public static boolean isDevConsoleRedacted() {
        return !"false".equalsIgnoreCase(getProperty("devconsole.redact"));
    }
}
