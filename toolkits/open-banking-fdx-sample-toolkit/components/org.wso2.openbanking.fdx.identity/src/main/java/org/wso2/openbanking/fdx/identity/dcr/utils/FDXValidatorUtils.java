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

import com.wso2.openbanking.accelerator.identity.dcr.model.RegistrationRequest;
import org.apache.commons.lang.StringUtils;
import org.wso2.openbanking.fdx.common.config.OpenBankingFDXConfigParser;
import org.wso2.openbanking.fdx.identity.dcr.constants.FDXValidationConstants;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Util class for validation logic implementation.
 */
public class FDXValidatorUtils {

    /**
     * Adds allowed grant types to the registration request based on the requested grant types.
     * If data recipient has not requested any grant types or none of the requested  grant types are allowed,
     * sets the allowed grant types.
     *
     * @param registrationRequest The registration request object.
     */
    public static void addAllowedGrantTypes(RegistrationRequest registrationRequest) {

        List<String> requestedGrantTypes = registrationRequest.getGrantTypes();
        List<String> allowedGrantTypes = AllowedGrantTypesEnum.getAllowedGrantTypes();

        // If grant types are provided in the request, filter and collect only those that are allowed;
        // Otherwise, use the default allowed grant types.
        List<String> grantTypesToAdd = Optional.ofNullable(requestedGrantTypes)
                .map(types -> types.stream().filter(allowedGrantTypes::contains).collect(Collectors.toList()))
                .orElse(allowedGrantTypes);

        // If none of the requested grant types are valid, set allowedGrantTypes as grant types to be added
        if (grantTypesToAdd.isEmpty()) {
            grantTypesToAdd = allowedGrantTypes;
        }

        registrationRequest.setGrantTypes(grantTypesToAdd);
        registrationRequest.getRequestParameters().put(FDXValidationConstants.GRANT_TYPES, grantTypesToAdd);
    }

    /**
     * Adds an allowed token endpoint authentication method based on the requested authentication method.
     * If the requested authentication method is blank or not allowed, sets the configured default method.
     *
     * @param registrationRequest The registration request object.
     */
    public static void addAllowedTokenEndpointAuthMethod(RegistrationRequest registrationRequest) {

        String requestedAuthMethod = registrationRequest.getTokenEndPointAuthentication();
        List<String> allowedAuthMethods = AllowedTokenEndPointAuthMethodsEnum.getAllowedAuthMethods();

        if (StringUtils.isBlank(requestedAuthMethod) || !allowedAuthMethods.contains(requestedAuthMethod)) {

            // Retrieve the default token endpoint authentication method from the configuration
            String defaultTokenEndpointAuthMethod = (String) OpenBankingFDXConfigParser.getInstance()
                    .getConfiguration(FDXValidationConstants.DCR_DEFAULT_TOKEN_ENDPOINT_AUTH_METHOD);

            // Determine the authentication method to add, based on whether the default auth method is allowed
            String authMethodToAdd = (allowedAuthMethods.contains(defaultTokenEndpointAuthMethod)) ?
                    defaultTokenEndpointAuthMethod : AllowedTokenEndPointAuthMethodsEnum
                                                        .PRIVATE_KEY_JWT.getAuthMethod();

            registrationRequest.setTokenEndPointAuthentication(authMethodToAdd);
            registrationRequest.getRequestParameters()
                    .put(FDXValidationConstants.TOKEN_ENDPOINT_AUTH_METHOD, authMethodToAdd);
        }
    }
}

