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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Util class which includes helper methods required for FDX DCR.
 */
public class FDXRegistrationUtils {

    /**
     * Checks if the given object is a JSON string.
     *
     * @param obj The object to be checked.
     * @return true if the object is a JSON string, false otherwise.
     */
    public static boolean isJsonString(Object obj) {

        return obj instanceof String && obj.toString().contains("{");
    }

    /**
     * Convert the given string to a JSON object.
     *
     * @param string The string to be parsed as JSON.
     * @return The JSON object parsed from the string.
     */
    public static JsonObject getJsonObject(String string) {

        if (isJsonString(string)) {
            return new JsonParser().parse(string).getAsJsonObject();
        }
        return null;
    }
}

