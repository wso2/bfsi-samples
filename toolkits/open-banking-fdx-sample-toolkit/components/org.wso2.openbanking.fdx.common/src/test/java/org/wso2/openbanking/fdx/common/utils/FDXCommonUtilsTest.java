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

import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.openbanking.fdx.common.testutils.CommonTestDataProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Test Class for FDX Common Utils class.
 */
public class FDXCommonUtilsTest {

    @Test(dataProvider = "conversionData", dataProviderClass = CommonTestDataProvider.class)
    public void testConvertDoubleValueToInt(String key, Object value , Object expectedValue) {

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        FDXCommonUtils.convertDoubleValueToInt(map, key);

        Assert.assertEquals(map.get(key), expectedValue);
    }
}
