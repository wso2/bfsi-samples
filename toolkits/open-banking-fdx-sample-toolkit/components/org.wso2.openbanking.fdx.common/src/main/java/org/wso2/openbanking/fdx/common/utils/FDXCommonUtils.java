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

package org.wso2.openbanking.fdx.common.utils;

import java.util.Map;

/**
 * FDX Common Utils.
 */
public class FDXCommonUtils {

    /**
     * Converts Double value to integer for the specified key in the given map.
     *
     * @param map The map containing key-value pairs.
     * @param key The list of keys for which Double values need to be converted to integers.
     */
    public static void convertDoubleValueToInt(Map<String, Object> map, String key) {

        if (map.get(key) instanceof Double) {
            Double doubleValue = (Double) map.get(key);
            map.put(key, doubleValue.intValue());
        }
    }
}
