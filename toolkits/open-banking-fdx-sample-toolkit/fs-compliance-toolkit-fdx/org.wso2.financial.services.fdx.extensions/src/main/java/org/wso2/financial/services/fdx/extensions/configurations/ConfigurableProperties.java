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

package org.wso2.financial.services.fdx.extensions.configurations;

/**
 * This class contains configurable properties for the FDX extension.
 */
public class ConfigurableProperties {
    // TODO: Add the properties to a config.properties file. https://github.com/wso2/bfsi-samples/issues/12
    public static final String SHARABLE_ENDPOINT =
            "http://localhost:9766/api/openbanking/uk/backend/services/bankaccounts/bankaccountservice/sharable" +
                    "-accounts";
    public static final boolean MASKING_ENABLED = true;

}
