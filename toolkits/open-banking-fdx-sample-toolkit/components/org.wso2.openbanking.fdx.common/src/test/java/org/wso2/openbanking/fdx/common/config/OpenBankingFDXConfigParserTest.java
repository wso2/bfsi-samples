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

package org.wso2.openbanking.fdx.common.config;

import com.wso2.openbanking.accelerator.common.exception.OpenBankingRuntimeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.openbanking.fdx.common.testutils.CommonTestDataProvider;

import java.io.File;
import java.util.List;

/**
 * Test Class for FDX Config Parser.
 */
public class OpenBankingFDXConfigParserTest {

    private String absolutePathForTestResources;

    @BeforeClass
    public void beforeClass() throws ReflectiveOperationException {

        //set carbon.home to current directory
        System.setProperty("carbon.home", ".");

        //get absolute path of resources directory
        String path = "src/test/resources";
        File file = new File(path);
        absolutePathForTestResources = file.getAbsolutePath();
    }

    //Runtime exception is thrown here because carbon home is not defined properly for an actual carbon product
    @Test(expectedExceptions = OpenBankingRuntimeException.class, priority = 1)
    public void testConfigParserInitiationWithoutPath() {

        OpenBankingFDXConfigParser.getInstance();
    }

    //Runtime exception is thrown here because the xml file is empty
    @Test(expectedExceptions = OpenBankingRuntimeException.class, priority = 2)
    public void testRuntimeExceptionInvalidConfigFile() {

        System.setProperty("carbon.config.dir.path", absolutePathForTestResources + "/test-data");
        OpenBankingFDXConfigParser.getInstance();
    }

    @Test(expectedExceptions = OpenBankingRuntimeException.class, priority = 3)
    public void testRuntimeExceptionNonExistentFile() {

        System.setProperty("carbon.config.dir.path", absolutePathForTestResources + "/value");
        OpenBankingFDXConfigParser.getInstance();
    }

    @Test(priority = 4, dataProvider = "dcrConfigs", dataProviderClass = CommonTestDataProvider.class)
    public void testConfigParserInit(String configName, String expectedConfigValue) {

        System.setProperty("carbon.config.dir.path", absolutePathForTestResources);
        OpenBankingFDXConfigParser openBankingFDXConfigParser = OpenBankingFDXConfigParser.getInstance();
        Object configValue = openBankingFDXConfigParser.getConfiguration(configName);

        Assert.assertEquals(configValue , expectedConfigValue);
    }

    @Test(priority = 5, dataProvider = "sampleArrayConfig", dataProviderClass = CommonTestDataProvider.class)
    public void testReadChildElementArrays(String configName, List<String> expectedConfigValue) {

        System.setProperty("carbon.config.dir.path", absolutePathForTestResources);
        OpenBankingFDXConfigParser openBankingFDXConfigParser = OpenBankingFDXConfigParser.getInstance();
        Object configValue = openBankingFDXConfigParser.getConfiguration(configName);

        Assert.assertEquals(configValue, expectedConfigValue);
    }

    @Test(priority = 6)
    public void testReplaceSystemPropertyMethod() {

        System.setProperty("carbon.config.dir.path", absolutePathForTestResources);
        OpenBankingFDXConfigParser openBankingFDXConfigParser = OpenBankingFDXConfigParser.getInstance();

        String carbonHome = (String) openBankingFDXConfigParser.getConfiguration("CarbonHome");
        if (carbonHome.endsWith("\\.\\.")) {
            carbonHome = carbonHome.substring(0, carbonHome.length() - 2);
        }
        File file = new File(".");

        Assert.assertEquals(carbonHome, file.getAbsolutePath());
    }

    @Test(priority = 7)
    public void testSingleton() {

        System.setProperty("carbon.config.dir.path", absolutePathForTestResources);

        OpenBankingFDXConfigParser instance1 = OpenBankingFDXConfigParser.getInstance();
        OpenBankingFDXConfigParser instance2 = OpenBankingFDXConfigParser.getInstance();
        Assert.assertEquals(instance2, instance1);
    }
}

