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

package org.wso2.financial.services.fdx.identity.authorize.commons;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for mapping scopes to data clusters.
 */

public class ScopeDataClusterMappings {
    // Scope-to-DataCluster Mapping
    private static final Map<String, String> FDX_DATA_CLUSTER;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("fdx:accountbasic:read", "ACCOUNT_BASIC");
        map.put("fdx:accountdetailed:read", "ACCOUNT_DETAILED");
        map.put("fdx:bills:read", "BILLS");
        map.put("fdx:customercontact:read", "CUSTOMER_CONTACT");
        map.put("fdx:customerpersonal:read", "CUSTOMER_PERSONAL");
        map.put("fdx:images:read", "IMAGES");
        map.put("fdx:investments:read", "INVESTMENTS");
        map.put("fdx:notifications:subscribe", "NOTIFICATIONS");
        map.put("fdx:notifications:publish", "NOTIFICATIONS");
        map.put("fdx:paymentsupport:read", "PAYMENT_SUPPORT");
        map.put("fdx:rewards:read", "REWARDS");
        map.put("fdx:statements:read", "STATEMENTS");
        map.put("fdx:tax:read", "TAX");
        map.put("fdx:transactions:read", "TRANSACTIONS");

        FDX_DATA_CLUSTER = Collections.unmodifiableMap(map);
    }

    /**
     * Get the data cluster for a given scope.
     *
     * @param value The scope to look up.
     * @return The data cluster associated with the scope, or null if not found.
     */
    public static Set<String> getScopeByDataCluster(String value) {
        Set<String> keys = new HashSet<>();
        for (Map.Entry<String, String> entry : FDX_DATA_CLUSTER.entrySet()) {
            if (entry.getValue().equals(value)) {
                keys.add(entry.getKey());
            }
        }
            return keys;
    }
}
