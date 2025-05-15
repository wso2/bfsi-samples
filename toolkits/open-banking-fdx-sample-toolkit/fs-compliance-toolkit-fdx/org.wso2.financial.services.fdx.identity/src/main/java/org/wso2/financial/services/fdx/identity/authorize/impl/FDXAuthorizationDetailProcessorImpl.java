
package org.wso2.financial.services.fdx.identity.authorize.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.AuthorizedScopes;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementServiceImpl;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.oauth.rar.exception.AuthorizationDetailsProcessingException;
import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetail;
import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetails;
import org.wso2.carbon.identity.oauth.rar.model.ValidationResult;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2ServerException;
import org.wso2.carbon.identity.oauth2.rar.core.AuthorizationDetailsProcessor;
import org.wso2.carbon.identity.oauth2.rar.model.AuthorizationDetailsContext;
import org.wso2.financial.services.fdx.identity.authorize.commons.ScopeDataClusterMappings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.wso2.carbon.identity.oauth2.util.OAuth2Util.getServiceProvider;

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

    @Override
    public ValidationResult validate(AuthorizationDetailsContext authorizationDetailsContext) throws
        AuthorizationDetailsProcessingException, IdentityOAuth2ServerException {
        try {
            AuthorizationDetail authorizationDetail = authorizationDetailsContext.getAuthorizationDetail();
            List<List<String>> dataClusters = getDataClusters(authorizationDetail);
            String clientId = authorizationDetailsContext.getOAuthAppDO().getOauthConsumerKey();

            ServiceProvider serviceProvider = getServiceProvider(clientId);
            String appId;
            String tenantDomain = IdentityTenantUtil.getTenantDomainFromContext();
            if (serviceProvider != null) {
                appId = serviceProvider.getApplicationResourceId();
                AuthorizedAPIManagementServiceImpl service = new AuthorizedAPIManagementServiceImpl();
                List<AuthorizedScopes> clientScopes = service.getAuthorizedScopes(appId, tenantDomain);

                Set<String> scopeList = new HashSet<>();
                for (AuthorizedScopes scope : clientScopes) {
                    scopeList.addAll(scope.getScopes());
                }
                for (List<String> dataCluster : dataClusters) {
                    for (String cluster : dataCluster) {
                        Set<String> scopeMapping = ScopeDataClusterMappings.getScopeByDataCluster(cluster);
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

    public List<List<String>> getDataClusters(AuthorizationDetail authorizationDetails) {
        List<List<String>> dataClusters = new ArrayList<>();

        // Get details map
        Map<String, Object> details = authorizationDetails.getDetails();
        if (details == null || !details.containsKey("consentRequest")) {
            return dataClusters; // Return empty list if no consentRequest
        }

        // Get consentRequest
        Object consentRequestObj = details.get("consentRequest");
        if (!(consentRequestObj instanceof Map)) {
            return dataClusters; // Return empty list if not a Map
        }

        Map<String, Object> consentRequest = (Map<String, Object>) consentRequestObj;
        if (!consentRequest.containsKey("resources")) {
            return dataClusters;  // Return empty list if no resources
        }

        // Get resources list
        Object resourcesObj = consentRequest.get("resources");
        if (!(resourcesObj instanceof List)) {
            return dataClusters; // Return empty list if not a List
        }

        List<?> resources = (List<?>) resourcesObj;
        for (Object resourceObj : resources) {
            if (resourceObj instanceof Map) {
                Map<String, Object> resource = (Map<String, Object>) resourceObj;
                if (!resource.containsKey("dataClusters")) {
                    continue; // Skip if no dataClusters
                }

                Object dataClustersObj = resource.get("dataClusters");
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

    @Override
    public String getType() {
        return "fdx_v1.0";
    }

    @Override
    public boolean isEqualOrSubset(AuthorizationDetail authorizationDetail, AuthorizationDetails authorizationDetails) {
        return false;
    }

    @Override
    public AuthorizationDetail enrich(AuthorizationDetailsContext authorizationDetailsContext) {
        return null;
    }
}
