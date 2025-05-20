
package org.wso2.financial.services.fdx.identity.authorize.impl;

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
import org.wso2.financial.services.fdx.identity.authorize.commons.FDXIdentityCommonConstants;
import org.wso2.financial.services.fdx.identity.authorize.commons.ScopeDataClusterMappings;
import org.wso2.financial.services.fdx.identity.authorize.utils.AuthorizationDetailProcessorUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
            if (clientId != null) {
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
            throw new RuntimeException(ex);
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

        // Get details map
        Map<String, Object> details = authorizationDetails.getDetails();
        if (details == null || !details.containsKey(FDXIdentityCommonConstants.CONSENT_REQUEST)) {
            return dataClusters; // Return empty list if no consentRequest
        }

        // Get consentRequest
        Object consentRequestObj = details.get(FDXIdentityCommonConstants.CONSENT_REQUEST);
        if (!(consentRequestObj instanceof Map)) {
            return dataClusters; // Return empty list if not a Map
        }

        Map<String, Object> consentRequest = (Map<String, Object>) consentRequestObj;
        if (!consentRequest.containsKey(FDXIdentityCommonConstants.RESOURCES)) {
            return dataClusters;  // Return empty list if no resources
        }

        // Get resources list
        Object resourcesObj = consentRequest.get(FDXIdentityCommonConstants.RESOURCES);
        if (!(resourcesObj instanceof List)) {
            return dataClusters; // Return empty list if not a List
        }

        List<?> resources = (List<?>) resourcesObj;
        for (Object resourceObj : resources) {
            if (resourceObj instanceof Map) {
                Map<String, Object> resource = (Map<String, Object>) resourceObj;
                if (!resource.containsKey(FDXIdentityCommonConstants.DATA_CLUSTERS)) {
                    continue; // Skip if no dataClusters
                }

                Object dataClustersObj = resource.get(FDXIdentityCommonConstants.DATA_CLUSTERS);
                if (dataClustersObj instanceof List) {
                    List<?> clusterList = (List<?>) dataClustersObj;
                    List<String> extractedClusters = new ArrayList<>();

                    for (Object cluster : clusterList) {
                        if (cluster instanceof String) {
                            extractedClusters.add((String) cluster);
                        }
                    }

                    if (!extractedClusters.isEmpty()) {
                        dataClusters.add(extractedClusters);
                    }
                }
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
