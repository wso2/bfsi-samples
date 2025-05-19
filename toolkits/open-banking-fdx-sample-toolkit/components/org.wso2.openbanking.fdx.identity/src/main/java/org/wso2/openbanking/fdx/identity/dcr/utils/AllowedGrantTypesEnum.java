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

package org.wso2.openbanking.fdx.identity.dcr.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enum for FDX supported grant types.
 */
public enum AllowedGrantTypesEnum {

    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token"),
    //TODO: This needs to be removed when /register/token endpoint is introduced to obtain DCR access tokens
    CLIENT_CREDENTIALS("client_credentials");

    private final String grantType;

    AllowedGrantTypesEnum(String grantType) {
        this.grantType = grantType;
    }

    public String getGrantType() {
        return grantType;
    }

    /**
     * Retrieves a list of all FDX supported grant types.
     * Each grant type is represented  by its corresponding string value.
     *
     * @return A list of string representations of all supported grant types.
     */
    public static List<String> getAllowedGrantTypes() {

        return Collections.unmodifiableList(Arrays.stream(AllowedGrantTypesEnum.values())
                .map(AllowedGrantTypesEnum::getGrantType)
                .collect(Collectors.toList()));
    }
}

