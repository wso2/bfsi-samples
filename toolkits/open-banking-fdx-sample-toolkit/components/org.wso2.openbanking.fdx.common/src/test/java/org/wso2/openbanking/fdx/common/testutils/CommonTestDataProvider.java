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

package org.wso2.openbanking.fdx.common.testutils;

import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Provider FDX Common Tests.
 */
public class CommonTestDataProvider {

    @DataProvider(name = "dcrConfigs")
    public Object[][] dcrConfigsTestDataProvider() {

        return new Object[][] {
                {"DCR.MaximumDurationPeriod", "200"},
                {"DCR.MaximumLookbackPeriod", "356"},
                {"DCR.TokenEndpointAuthMethod", "tls_client_auth"}
        };
    }

    @DataProvider(name = "sampleArrayConfig")
    public Object[][] sampleArrayConfigTestDataProvider() {

        List<String> expectedList = new ArrayList<>();
        expectedList.add("DummyValue1");
        expectedList.add("DummyValue2");
        expectedList.add("DummyValue3");

        return new Object[][] {
                {"SampleArray.RepeatableTag", expectedList }
        };
    }

    @DataProvider(name = "conversionData")
    public Object[][] conversionData() {

        return new Object[][] {

                {"key1", 10.5, 10},
                {"key2", 7.8, 7},
                {"key3", "value", "value"},
                {"key4", null, null},
                {"key5", 20, 20}
        };
    }
}

