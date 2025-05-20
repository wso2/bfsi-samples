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

package org.wso2.financial.services.fdx.extensions.api;

import org.json.JSONObject;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.financial.services.accelerator.consent.mgt.extensions.common.AuthErrorCode;
import org.wso2.financial.services.accelerator.consent.mgt.extensions.common.ConsentException;
import org.wso2.financial.services.accelerator.consent.mgt.extensions.common.ResponseStatus;
import org.wso2.financial.services.fdx.extensions.model.FailedResponseInConsent;
import org.wso2.financial.services.fdx.extensions.model.PersistAuthorizedConsentRequestBody;
import org.wso2.financial.services.fdx.extensions.model.SuccessResponsePersistAuthorizedConsent;
import org.wso2.financial.services.fdx.extensions.utils.FDXCommonConstants;
import org.wso2.financial.services.fdx.extensions.utils.FDXConsentPersistUtils;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;

public class PersistAuthorizedConsentApiTests {
    private PersistAuthorizedConsentApi api;

    @BeforeMethod
    public void setUp() {
        api = new PersistAuthorizedConsentApi();
    }

    @Test
    void testPersistAuthorizedConsentPostSuccess() {
        Map<String, Object> persistResponse = new HashMap<>();
        persistResponse.put(FDXCommonConstants.STATUS,
                SuccessResponsePersistAuthorizedConsent.StatusEnum.SUCCESS);
        persistResponse.put(FDXCommonConstants.TYPE, "mockType");
        persistResponse.put(FDXCommonConstants.FDX_CONSENT_STATUS, "mockStatus");

        // Mock request body
        PersistAuthorizedConsentRequestBody requestBody = Mockito.mock(PersistAuthorizedConsentRequestBody.class);

        try (MockedStatic<FDXConsentPersistUtils> mockedUtils = Mockito.mockStatic(FDXConsentPersistUtils.class)) {
            // Mock the behavior of persistFDXConsent
            mockedUtils.when(() -> FDXConsentPersistUtils.persistConsent(requestBody))
                    .thenReturn(persistResponse);

            Mockito.when(requestBody.getRequestId()).thenReturn("mockRequestId");

            // Call the method under test
            Response response = api.persistAuthorizedConsentPost(requestBody);

            // Assert the response
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            JSONObject responseBody = new JSONObject(response.getEntity().toString());
            Assert.assertEquals(responseBody.getString("status"), "SUCCESS");
        }
    }

    @Test
    void testPersistAuthorizedConsentPostFailure() {
        Map<String, Object> persistResponse = new HashMap<>();
        persistResponse.put(FDXCommonConstants.STATUS, FailedResponseInConsent.StatusEnum.ERROR);
        persistResponse.put(FDXCommonConstants.RESPONSE_STATUS, 400);
        persistResponse.put(FDXCommonConstants.DATA, "mockErrorData");

        try (MockedStatic<FDXConsentPersistUtils> mockedUtils = Mockito.mockStatic(FDXConsentPersistUtils.class)) {
            // Mock the behavior of persistFDXConsent
            mockedUtils.when(() -> FDXConsentPersistUtils.persistConsent(Mockito.any()))
                    .thenReturn(persistResponse);

            // Mock request body
            PersistAuthorizedConsentRequestBody requestBody = Mockito.mock(PersistAuthorizedConsentRequestBody.class);

            // Call the method under test
            Response response = api.persistAuthorizedConsentPost(requestBody);

            // Assert the response
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            JSONObject responseBody = new JSONObject(response.getEntity().toString());
            Assert.assertEquals(responseBody.getString("status"), "ERROR");
        }
    }

    @Test
    void testPersistAuthorizedConsentPostConsentException() {
        try (MockedStatic<FDXConsentPersistUtils> mockedUtils = Mockito.mockStatic(FDXConsentPersistUtils.class)) {
            // Mock the behavior of persistFDXConsent to throw an exception
            mockedUtils.when(() -> FDXConsentPersistUtils.persistConsent(Mockito.any()))
                    .thenThrow(new ConsentException(ResponseStatus.BAD_REQUEST, AuthErrorCode.SERVER_ERROR.name(),
                            "Consent data is not available"));

            // Mock request body
            PersistAuthorizedConsentRequestBody requestBody = Mockito.mock(PersistAuthorizedConsentRequestBody.class);

            // Call the method under test and expect an exception
            Response response = api.persistAuthorizedConsentPost(requestBody);

            // Assert the response
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            JSONObject responseBody = new JSONObject(response.getEntity().toString());
            Assert.assertEquals(responseBody.getString("status"), "ERROR");
        }
    }
}

