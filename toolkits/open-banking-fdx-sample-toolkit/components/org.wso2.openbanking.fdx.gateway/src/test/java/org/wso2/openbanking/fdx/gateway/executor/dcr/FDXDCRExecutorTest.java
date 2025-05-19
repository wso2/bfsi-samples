/**
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 * <p>
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.openbanking.fdx.gateway.executor.dcr;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wso2.openbanking.accelerator.common.config.OpenBankingConfigurationService;
import com.wso2.openbanking.accelerator.gateway.executor.dcr.DCRExecutor;
import com.wso2.openbanking.accelerator.gateway.executor.model.OBAPIRequestContext;
import com.wso2.openbanking.accelerator.gateway.executor.model.OBAPIResponseContext;
import com.wso2.openbanking.accelerator.gateway.internal.GatewayDataHolder;
import com.wso2.openbanking.accelerator.gateway.util.GatewayConstants;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.apimgt.common.gateway.dto.MsgInfoDTO;
import org.wso2.openbanking.fdx.gateway.testutils.GatewayTestDataProvider;
import org.wso2.openbanking.fdx.gateway.util.FDXGatewayConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.HttpMethod;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Test for FDX DCR executor.
 */
@PowerMockIgnore("jdk.internal.reflect.*")
@PrepareForTest({OBAPIRequestContext.class, GatewayDataHolder.class})
public class FDXDCRExecutorTest {

    Map<String, Object> configMap = new HashMap<>();
    Map<String, List<String>> configuredAPIList = new HashMap<>();

    @Mock
    Map<String, Object> urlMap = new HashMap<>();

    @Mock
    private GatewayDataHolder gatewayDataHolder;

    @Mock
    OpenBankingConfigurationService openBankingConfigurationService;

    @BeforeTest
    public void init() {

        MockitoAnnotations.initMocks(this);

        urlMap = new HashMap<>();
        urlMap.put("userName", "admin");
        urlMap.put("password", "admin".toCharArray());
        urlMap.put(GatewayConstants.IAM_HOSTNAME, "localhost");

        configMap.put("DCR.RequestJWTValidation", "false");

        List<String> roles = new ArrayList<>();
        configuredAPIList.put("DynamicClientRegistration", roles);
    }

    @Test
    public void testInvalidInteractionIdHeader() {

        OBAPIRequestContext obapiRequestContext = mock(OBAPIRequestContext.class);
        MsgInfoDTO msgInfoDTO = new MsgInfoDTO();
        FDXDCRExecutor dcrExecutor = spy(FDXDCRExecutor.class);

        msgInfoDTO.setHttpMethod(HttpMethod.GET);
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put(FDXGatewayConstants.INTERACTION_ID_HEADER, "765489876548650");
        msgInfoDTO.setHeaders(requestHeaders);
        when(obapiRequestContext.getMsgInfo()).thenReturn(msgInfoDTO);

        dcrExecutor.preProcessRequest(obapiRequestContext);

        verify(obapiRequestContext).setError(true);
    }

    @Test
    public void testInteractionIdHeaderNotProvided() {

        OBAPIRequestContext obapiRequestContext = mock(OBAPIRequestContext.class);
        MsgInfoDTO msgInfoDTO = new MsgInfoDTO();
        FDXDCRExecutor dcrExecutor = spy(FDXDCRExecutor.class);

        msgInfoDTO.setHttpMethod(HttpMethod.GET);
        Map<String, String> requestHeaders = new HashMap<>();
        msgInfoDTO.setHeaders(requestHeaders);
        when(obapiRequestContext.getMsgInfo()).thenReturn(msgInfoDTO);

        dcrExecutor.preProcessRequest(obapiRequestContext);

        verify(obapiRequestContext).setError(true);
    }

    @Test
    public void testSetInteractionIdHeader() {

        when(openBankingConfigurationService.getConfigurations()).thenReturn(configMap);
        when(gatewayDataHolder.getOpenBankingConfigurationService())
                                        .thenReturn(openBankingConfigurationService);
        mockStatic(GatewayDataHolder.class);
        when(GatewayDataHolder.getInstance()).thenReturn(gatewayDataHolder);

        OBAPIRequestContext obapiRequestContext = mock(OBAPIRequestContext.class);
        FDXDCRExecutor fdxdcrExecutor = spy(new FDXDCRExecutor());

        String interactionId = "770aef3-6784-41f7-8e0e-ff5f97bddb3";

        MsgInfoDTO msgInfoDTO = new MsgInfoDTO();
        msgInfoDTO.setHttpMethod(HttpMethod.GET);
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put(FDXGatewayConstants.INTERACTION_ID_HEADER, interactionId);
        msgInfoDTO.setHeaders(requestHeaders);
        when(obapiRequestContext.getMsgInfo()).thenReturn(msgInfoDTO);

        fdxdcrExecutor.preProcessRequest(obapiRequestContext);

        verify(obapiRequestContext)
                .addContextProperty(FDXGatewayConstants.INTERACTION_ID_HEADER, interactionId);
    }

    @Test(dataProvider = "httpMethod", dataProviderClass = GatewayTestDataProvider.class)
    public void testSetSoftwareId(String httpMethod) {

        when(openBankingConfigurationService.getConfigurations()).thenReturn(configMap);
        when(gatewayDataHolder.getOpenBankingConfigurationService())
                .thenReturn(openBankingConfigurationService);
        mockStatic(GatewayDataHolder.class);
        when(GatewayDataHolder.getInstance()).thenReturn(gatewayDataHolder);

        OBAPIRequestContext obapiRequestContext = mock(OBAPIRequestContext.class);
        FDXDCRExecutor fdxdcrExecutor = spy(new FDXDCRExecutor());

        String interactionId = "770aef3-6784-41f7-8e0e-ff5f97bddb3";

        MsgInfoDTO msgInfoDTO = new MsgInfoDTO();
        msgInfoDTO.setHttpMethod(httpMethod);
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put(FDXGatewayConstants.INTERACTION_ID_HEADER, interactionId);
        msgInfoDTO.setHeaders(requestHeaders);

        Gson gson = new Gson();
        JsonObject emptyJsonObject = new JsonObject();
        String emptyJsonString = gson.toJson(emptyJsonObject);

        when(obapiRequestContext.getMsgInfo()).thenReturn(msgInfoDTO);
        when(obapiRequestContext.getRequestPayload()).thenReturn(emptyJsonString);

        fdxdcrExecutor.preProcessRequest(obapiRequestContext);

        verify(obapiRequestContext).setModifiedPayload(emptyJsonString);
    }

    @Test
    public void testAddInteractionIdHeaderToResponseHeaders() {

        FDXDCRExecutor fdxdcrExecutor = spy(new FDXDCRExecutor());
        String interactionId = "770aef3-6784-41f7-8e0e-ff5f97bddb3";

        DCRExecutor.setUrlMap(urlMap);
        when(openBankingConfigurationService.getAllowedAPIs()).thenReturn(configuredAPIList);
        when(gatewayDataHolder.getOpenBankingConfigurationService())
                .thenReturn(openBankingConfigurationService);
        mockStatic(GatewayDataHolder.class);
        when(GatewayDataHolder.getInstance()).thenReturn(gatewayDataHolder);

        OBAPIResponseContext obapiResponseContext = mock(OBAPIResponseContext.class);
        MsgInfoDTO msgInfoDTO = mock(MsgInfoDTO.class);
        when(msgInfoDTO.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(obapiResponseContext.getMsgInfo()).thenReturn(msgInfoDTO);
        when(obapiResponseContext.getContextProperty(FDXGatewayConstants.INTERACTION_ID_HEADER))
                .thenReturn(interactionId);

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put(FDXGatewayConstants.INTERACTION_ID_HEADER, interactionId);

        fdxdcrExecutor.postProcessResponse(obapiResponseContext);
        verify(obapiResponseContext).setAddedHeaders(responseHeaders);
    }

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
}

