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

package org.wso2.openbanking.fdx.identity.dcr.util;

import com.google.gson.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.openbanking.fdx.identity.dcr.utils.FDXRegistrationUtils;
import org.wso2.openbanking.fdx.identity.testutils.IdentityTestDataProvider;

/**
 * Test Class for FDX Registration Utils class.
 */
public class FDXRegistrationUtilsTest {

    @Test(dataProvider = "jsonStrings", dataProviderClass = IdentityTestDataProvider.class)
    public void testIsJsonString(Object input, boolean expectedResult) {

        Assert.assertEquals(FDXRegistrationUtils.isJsonString(input), expectedResult);
    }

    @Test
    public void testGetJsonObjectValidJson() {

        String validJsonString = "{\"key\": \"value\"}";
        JsonObject jsonObject = FDXRegistrationUtils.getJsonObject(validJsonString);

        Assert.assertNotNull(jsonObject);
        Assert.assertTrue(jsonObject.has("key"));
        Assert.assertEquals("value", jsonObject.get("key").getAsString());
    }

    @Test
    public void testGetJsonObjectInvalidJson() {

        String invalidJsonString = "Invalid JSON string";
        JsonObject jsonObject = FDXRegistrationUtils.getJsonObject(invalidJsonString);

        Assert.assertNull(jsonObject);
    }

    @Test
    public void testGetJsonObjectNullInput() {

        String nullInput = null;
        JsonObject jsonObject = FDXRegistrationUtils.getJsonObject(nullInput);

        Assert.assertNull(jsonObject);
    }
}
