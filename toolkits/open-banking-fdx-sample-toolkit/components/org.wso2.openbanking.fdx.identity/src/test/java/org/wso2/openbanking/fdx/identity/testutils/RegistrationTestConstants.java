/**
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
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

package org.wso2.openbanking.fdx.identity.testutils;

/**
 * FDX Registration Test Constants.
 */
public class RegistrationTestConstants {

    public static String registrationRequestJson = "{\n" +
            "  \"exp\": 2147483646,\n" +
            "  \"jti\": \"37747cd1c10545699f754adf28b73e32\",\n" +
            "  \"aud\": \"https://secure.api.dataholder.com/issuer\",\n" +
            "  \"redirect_uris\": [\n" +
            "    \"https://www.mockcompany.com/redirects/redirect1\"\n" +
            "  ],\n" +
            " \"token_endpoint_auth_signing_alg\": \"PS256\",\n" +
            " \"token_endpoint_auth_method\": \"private_key_jwt\",\n" +
            "  \"grant_types\": [\n" +
            "    \"client_credentials\",\n" +
            "    \"authorization_code\",\n" +
            "    \"refresh_token\",\n" +
            "    \"urn:ietf:params:oauth:grant-type:jwt-bearer\"\n" +
            "  ],\n" +
            "  \"response_types\": [\n" +
            "    \"code id_token\"\n" +
            "  ],\n" +
            "  \"application_type\": \"web\",\n" +
            "  \"id_token_signed_response_alg\": \"PS256\",\n" +
            "  \"id_token_encrypted_response_alg\": \"RSA-OAEP\",\n" +
            "  \"id_token_encrypted_response_enc\": \"A256GCM\",\n" +
            "  \"request_object_signing_alg\": \"PS256\",\n" +
            "  \"scope\": \"ACCOUNT_DETAILED  ACCOUNT_PAYMENTS TRANSACTIONS openid\",\n" +
            "  \"client_name\": \"My Example Client\", \n" +
            "  \"description\": \"Recipient application for specified financial use case\", \n" +
            "  \"logo_uri\": \"https://client.example.org/logo.png\", \n" +
            "  \"client_uri\": \"https://example.net/\", \n" +
            "  \"contacts\": [\"support@example.net\"], \n" +
            "  \"duration_type\": [\"time_bound\",\"one_time\"], \n" +
            "  \"duration_period\": 65, \n" +
            "  \"lookback_period\": 265, \n" +
            "  \"registry_references\": [  \n" +
            "  { \n" +
            "    \"registered_entity_name\": \"Data recipient company legal name\", \n" +
            "    \"registered_entity_id\": \"4HCHXIURY78NNH6JH\", \n" +
            "    \"registry\": \"GLIEF\" \n" +
            "  },\n" +
            "  { \n" +
            "    \"registered_entity_name\": \"Sample company name\", \n" +
            "    \"registered_entity_id\": \"4HCHXYTU78NNH6JH\", \n" +
            "    \"registry\": \"GLIEF\" \n" +
            "  }]\n" +
            "}";

    public static String registryReference = "{ \n" +
            "    \"registered_entity_name\": \"Data recipient company legal name\", \n" +
            "    \"registered_entity_id\": \"4HCHXIURY78NNH6JH\", \n" +
            "    \"registry\": \"GLIEF\" \n" +
            "  }";
}

