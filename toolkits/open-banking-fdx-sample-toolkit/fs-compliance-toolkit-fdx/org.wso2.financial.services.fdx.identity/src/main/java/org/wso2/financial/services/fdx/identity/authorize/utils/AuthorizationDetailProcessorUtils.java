package org.wso2.financial.services.fdx.identity.authorize.utils;

import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.AuthorizedScopes;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementServiceImpl;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;

import java.util.List;

/**
 * Utility class for processing authorization details.
 * <p>
 * This class provides utility methods for handling authorization details in the context of OAuth2
 * and Rich Authorization Requests (RAR). It includes methods for retrieving application resource IDs
 * and other related functionalities.
 * </p>
 */
public class AuthorizationDetailProcessorUtils {

    public static String getApplicationResourceIdByClientId(String clientId, String tenantDomain)
            throws IdentityOAuth2Exception {
        ServiceProvider serviceProvider = OAuth2Util.getServiceProvider(clientId, tenantDomain);
        return serviceProvider.getApplicationResourceId();
    }

    public static List<AuthorizedScopes> getAuthorizedScopesByAppId(String appId, String tenantDomain)
            throws IdentityApplicationManagementException {
        AuthorizedAPIManagementServiceImpl authorizedAPIManagementService =
                new AuthorizedAPIManagementServiceImpl();
        return authorizedAPIManagementService.getAuthorizedScopes(appId, tenantDomain);
    }
}
