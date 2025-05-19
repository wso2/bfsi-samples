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

package org.wso2.openbanking.fdx.identity.dcr;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wso2.openbanking.accelerator.identity.dcr.exception.DCRValidationException;
import com.wso2.openbanking.accelerator.identity.dcr.model.RegistrationRequest;
import com.wso2.openbanking.accelerator.identity.dcr.utils.ValidatorUtils;
import com.wso2.openbanking.accelerator.identity.dcr.validation.RegistrationValidator;
import com.wso2.openbanking.accelerator.identity.util.IdentityCommonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.openbanking.fdx.common.config.OpenBankingFDXConfigParser;
import org.wso2.openbanking.fdx.common.utils.CommonConstants;
import org.wso2.openbanking.fdx.common.utils.FDXCommonUtils;
import org.wso2.openbanking.fdx.identity.dcr.utils.FDXValidatorUtils;
import org.wso2.openbanking.fdx.identity.dcr.validation.FDXRegistrationValidatorImpl;
import org.wso2.openbanking.fdx.identity.testutils.RegistrationTestConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test Class for FDX DCR Validation class.
 */
@PrepareForTest({OpenBankingFDXConfigParser.class, ValidatorUtils.class, FDXValidatorUtils.class,
        IdentityCommonUtil.class, FDXCommonUtils.class})
@PowerMockIgnore({"javax.net.ssl.*", "jdk.internal.reflect.*"})
public class FDXDCRValidationTest {

    private static final Log log = LogFactory.getLog(FDXDCRValidationTest.class);

    private RegistrationValidator registrationValidator;
    private static final Gson gson = new Gson();

    @BeforeClass
    public void beforeClass() {

        String dcrValidator = "org.wso2.openbanking.fdx.identity.dcr.validation.FDXRegistrationValidatorImpl";
        registrationValidator = getDCRValidator(dcrValidator);
    }

    @Test
    public void testGetResponseWithAddedAccessTokenAndClientUri() {

        String clientId = "AAAAAA";
        String tlsCert = "BBBBBB";
        String accessToken = "CCCCC";
        String clientUri = "https://localhost:8243/fdxv6.0.0recipientapi/6.0.0/register/";

        Map<String, Object> spMetadata = new HashMap<>();
        spMetadata.put("client_id", clientId);
        spMetadata.put("tls_cert", tlsCert);

        mockStatic(IdentityCommonUtil.class);
        when(IdentityCommonUtil.getDCRModifyResponseConfig()).thenReturn(true);
        mockStatic(ValidatorUtils.class);
        when(ValidatorUtils.generateAccessToken(anyString(), anyString()))
                .thenReturn(accessToken);
        when(ValidatorUtils.getRegistrationClientURI()).thenReturn(clientUri);

        String fdxRegistrationResponse = registrationValidator.getRegistrationResponse(spMetadata);

        // Assert that all expected values are present in the response
        List<String> expectedValues = Arrays.asList(
                String.format("\"registration_client_uri\":\"%s%s\"", clientUri, clientId),
                String.format("\"registration_access_token\":\"%s\"", accessToken)
        );
        Assert.assertTrue(expectedValues.stream().allMatch(fdxRegistrationResponse::contains));
    }

    @Test
    public void testGetRegistrationResponseWithJsonStrings() {

        List<Object> metaDataList = spy(new ArrayList<>());
        metaDataList.add(RegistrationTestConstants.registryReference);

        Map<String, Object> spMetaData = new HashMap<>();
        spMetaData.put("registry_references", metaDataList);
        JsonObject jsonObject =  new JsonParser().parse(RegistrationTestConstants.registryReference).getAsJsonObject();

        mockStatic(IdentityCommonUtil.class);
        when(IdentityCommonUtil.getDCRModifyResponseConfig()).thenReturn(false);

        registrationValidator.getRegistrationResponse(spMetaData);
        verify(metaDataList).set(0, jsonObject);
    }

    @Test
    public void testValidatePost() throws DCRValidationException {
        mockStatic(OpenBankingFDXConfigParser.class);
        mockStatic(FDXValidatorUtils.class);
        mockStatic(ValidatorUtils.class);
        mockStatic(FDXCommonUtils.class);

        RegistrationRequest registrationRequest = getRegistrationRequestObject(RegistrationTestConstants
                                                                .registrationRequestJson);
        // set request parameters
        Type type = new com.google.gson.reflect.TypeToken<Map<String, Object>>() { }.getType();
        Map<String, Object> requestParameters = gson.fromJson(RegistrationTestConstants.registrationRequestJson, type);
        registrationRequest.setRequestParameters(requestParameters);

        registrationValidator.validatePost(registrationRequest);

        //Verify that addAllowedGrantTypes() method is invoked exactly once
        verifyStatic(FDXValidatorUtils.class);
        FDXValidatorUtils.addAllowedGrantTypes(registrationRequest);
        //Verify that addAllowedTokenEndpointAuthMethod() method is invoked exactly once
        verifyStatic(FDXValidatorUtils.class);
        FDXValidatorUtils.addAllowedTokenEndpointAuthMethod(registrationRequest);

        verifyStatic(FDXCommonUtils.class);
        FDXCommonUtils.convertDoubleValueToInt(requestParameters, CommonConstants.DURATION_PERIOD);
        verifyStatic(FDXCommonUtils.class);
        FDXCommonUtils.convertDoubleValueToInt(requestParameters, CommonConstants.LOOKBACK_PERIOD);
    }

    private static RegistrationRequest getRegistrationRequestObject(String request) {

        Gson gson = new Gson();
        return gson.fromJson(request, RegistrationRequest.class);
    }

    public static RegistrationValidator getDCRValidator(String dcrValidator)  {

        if (StringUtils.isEmpty(dcrValidator)) {
            return new FDXRegistrationValidatorImpl();
        }

        try {
            return (RegistrationValidator) Class.forName(dcrValidator).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Error instantiating " + dcrValidator, e);
            return new FDXRegistrationValidatorImpl();
        } catch (ClassNotFoundException e) {
            log.error("Cannot find class: " + dcrValidator, e);
            return new FDXRegistrationValidatorImpl();
        }
    }

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
}
