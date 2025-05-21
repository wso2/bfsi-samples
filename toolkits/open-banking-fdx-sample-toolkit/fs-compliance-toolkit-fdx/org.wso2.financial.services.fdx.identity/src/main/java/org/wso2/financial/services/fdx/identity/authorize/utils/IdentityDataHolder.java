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

package org.wso2.financial.services.fdx.identity.authorize.utils;

import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementService;
import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementServiceImpl;

/**
 * Holder class for AuthorizedAPIManagementServiceImpl.
 * This class is used to create a singleton instance of AuthorizedAPIManagementServiceImpl.
 */
public class IdentityDataHolder {
    private static volatile IdentityDataHolder instance;
    private final AuthorizedAPIManagementService authorizedAPIManagementService;

    private IdentityDataHolder() {
        this.authorizedAPIManagementService = new AuthorizedAPIManagementServiceImpl();
    }

    public static IdentityDataHolder getInstance() {
        if (instance == null) {
            synchronized (IdentityDataHolder.class) {
                if (instance == null) {
                    instance = new IdentityDataHolder();
                }
            }
        }
        return instance;
    }

    public AuthorizedAPIManagementService getAuthorizedAPIManagementService() {
        return this.authorizedAPIManagementService;
    }
}
