/**
 * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 * <p>
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.financial.services.fdx.identity.authorize.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.AuthorizedScopes;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.oauth.rar.exception.AuthorizationDetailsProcessingException;
import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetail;
import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetails;
import org.wso2.carbon.identity.oauth.rar.model.ValidationResult;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.rar.core.AuthorizationDetailsProcessor;
import org.wso2.carbon.identity.oauth2.rar.model.AuthorizationDetailsContext;
import org.wso2.financial.services.fdx.identity.authorize.commons.ScopeDataClusterMappings;
import org.wso2.financial.services.fdx.identity.authorize.model.FDXAuthorizationDetails;
import org.wso2.financial.services.fdx.identity.authorize.utils.AuthorizationDetailProcessorUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The {@code FDXAuthorizationDetailProcessorImpl} class is an implementation of the
 * {@code AuthorizationDetailsProcessor}
 * inteface that provides validation for authorization details specific to the FDX authorization flow.
 * <p>
 * This class validates the authorization details provided in a rich authorization request against the scopes
 * defined for the client application. The validation logic ensures that the requested data clusters are within
 * the scope of the client application.
 * </p>
 */
@Component(service = AuthorizationDetailsProcessor.class)
public class FDXAuthorizationDetailProcessorImpl implements AuthorizationDetailsProcessor {
    private static final Log log = LogFactory.getLog(FDXAuthorizationDetailProcessorImpl.class);

    /**
     * Validates the authorization details provided in the authorization request.
     *
     * @param authorizationDetailsContext The context containing the authorization details and client information.
     * @return A {@code ValidationResult} indicating whether the validation was successful or not.
     * @throws AuthorizationDetailsProcessingException If an error occurs during validation.
     */
    @Override
    public ValidationResult validate(AuthorizationDetailsContext authorizationDetailsContext)
            throws AuthorizationDetailsProcessingException {
        try {
            AuthorizationDetail authorizationDetail = authorizationDetailsContext.getAuthorizationDetail();
            List<List<String>> dataClusters = getDataClusters(authorizationDetail);
            String clientId = authorizationDetailsContext.getOAuthAppDO().getOauthConsumerKey();
            if (StringUtils.isNotEmpty(clientId)) {
                String tenantDomain = IdentityTenantUtil.getTenantDomainFromContext();
                String appId =
                        AuthorizationDetailProcessorUtils.getApplicationResourceIdByClientId(clientId, tenantDomain);

                List<AuthorizedScopes> clientScopes =
                        AuthorizationDetailProcessorUtils.getAuthorizedScopesByAppId(appId, tenantDomain);

                Set<String> scopeList = new HashSet<>();
                for (AuthorizedScopes scope : clientScopes) {
                    if (scope != null && scope.getScopes() != null) {
                        scope.getScopes().stream()
                                .filter(s -> s != null && s.startsWith("fdx:"))
                                .forEach(scopeList::add);
                    }
                }

                for (List<String> dataCluster : dataClusters) {
                    for (String cluster : dataCluster) {
                        Set<String> scopeMapping = ScopeDataClusterMappings.getScopeByDataCluster(cluster);
                        if (scopeMapping.isEmpty()) {
                            log.debug("Requested data cluster not found");
                            throw new AuthorizationDetailsProcessingException("invalid_scope");
                        }
                        for (String scope : scopeMapping) {
                            if (!scopeList.contains(scope)) {
                                log.debug("Requested scope not found");
                                throw new AuthorizationDetailsProcessingException("invalid_scope");
                            }
                        }
                    }
                }
                log.debug("Validation successful");
                return ValidationResult.valid();
            } else {
                log.debug("Client not found");
                return ValidationResult.invalid("Client not found");
            }

        } catch (IdentityOAuth2Exception | IdentityApplicationManagementException ex) {
            log.debug(ex.getMessage());
            throw new AuthorizationDetailsProcessingException(ex.getMessage());
        }
    }

    /**
     * Extracts data clusters from the authorization details.
     *
     * @param authorizationDetails The authorization details object.
     * @return A list of data clusters extracted from the authorization details.
     */
    public List<List<String>> getDataClusters(AuthorizationDetail authorizationDetails) {
        List<List<String>> dataClusters = new ArrayList<>();
        FDXAuthorizationDetails fdxDetails =
                FDXAuthorizationDetails.parseFDXAuthorizationDetails(authorizationDetails);

        if (fdxDetails == null || fdxDetails.getConsentRequest() == null) {
            return dataClusters;
        }

        for (FDXAuthorizationDetails.Resource resource :
                fdxDetails.getConsentRequest().getResources()) {
            if (resource.getDataClusters() != null && !resource.getDataClusters().isEmpty()) {
                dataClusters.add(resource.getDataClusters());
            }
        }
        return dataClusters;
    }

    /**
     * Returns the type of the authorization detail processor.
     *
     * @return The type of the authorization detail processor.
     */
    @Override
    public String getType() {
        return "fdx_v1.0";
    }

    /**
     * Returns the supported authorization detail types.
     *
     * @return An array of supported authorization detail types.
     */
    @Override
    public boolean isEqualOrSubset(AuthorizationDetail authorizationDetail, AuthorizationDetails authorizationDetails) {
        return false;
    }

    /**
     * Enriches the authorization details.
     *
     * @param authorizationDetailsContext The context containing the authorization details and client information.
     * @return The enriched authorization detail.
     */
    @Override
    public AuthorizationDetail enrich(AuthorizationDetailsContext authorizationDetailsContext) {
        return null;
    }
}
