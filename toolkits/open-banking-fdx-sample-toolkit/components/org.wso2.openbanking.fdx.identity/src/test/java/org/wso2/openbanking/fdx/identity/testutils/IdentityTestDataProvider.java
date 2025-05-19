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

import org.testng.annotations.DataProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Data Provider for FDX Identity Tests.
 */
public class IdentityTestDataProvider {

    @DataProvider(name = "nullAndEmpty")
    public Object[][] nullAndEmptyTestDataProvider() {

        return new Object[][] {
                { null },
                { "" }
        };
    }

    @DataProvider(name = "zeroAndNegative")
    public Object[][] zeroAndNegativeTestDataProvider() {

        return new Object[][] {
                { 0 },
                { -100 }
        };
    }

    @DataProvider(name = "nullAndEmptyArray")
    public Object[][] nullAndEmptyArrayTestDataProvider() {

        return new Object[][] {
                { null },
                { Collections.emptyList() }
        };
    }

    @DataProvider(name = "grantTypes")
    public Object[][] grantTypesTestDataProvider() {

        //TODO: this needs to be removed when /register/token endpoint is introduced to obtain DCR access tokens
        List<String> defaultGrantTypesTemp = Arrays.asList("authorization_code", "refresh_token", "client_credentials");
        List<String> defaultGrantTypes = Arrays.asList("authorization_code", "refresh_token");
        List<String> authCodeGrantType = Collections.singletonList("authorization_code");
        List<String> refreshTokenGrantType = Collections.singletonList("refresh_token");

        return new Object[][] {
                { null, defaultGrantTypesTemp},
                {Collections.emptyList(), defaultGrantTypesTemp},
                {Collections.singletonList("auth_code"), defaultGrantTypesTemp},
                { Arrays.asList("authorization_code", "refresh_token"), defaultGrantTypes },
                {Collections.singletonList("authorization_code"), authCodeGrantType},
                {Collections.singletonList("refresh_token"), refreshTokenGrantType}
        };
    }

    @DataProvider(name = "tokenEndpointAuthMethods")
    public Object[][] tokenEndpointAuthMethodsTestDataProvider() {

        String privateKeyJwt = "private_key_jwt";
        String tlsClientAUth = "tls_client_auth";

        return new Object[][] {
                { null, tlsClientAUth},
                {"", tlsClientAUth},
                {"private_key_jwt", privateKeyJwt},
                {"tls_client_auth", tlsClientAUth},
                {"sample_auth_method", tlsClientAUth}
        };
    }

    @DataProvider(name = "jsonStrings")
    public Object[][] jsonData() {

        return new Object[][] {
                {"{\"key\": \"value\"}", true},
                {"Invalid JSON string", false},
                {123, false},
                {null, false},
                {"", false}
        };
    }
}

