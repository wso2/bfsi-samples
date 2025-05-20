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

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.financial.services.fdx.extensions.model.FailedResponse;
import org.wso2.financial.services.fdx.extensions.model.PopulateConsentAuthorizeScreenRequestBody;
import org.wso2.financial.services.fdx.extensions.model.SuccessResponsePopulateConsentAuthorizeScreen;
import org.wso2.financial.services.fdx.extensions.utils.FDXCommonConstants;
import org.wso2.financial.services.fdx.extensions.utils.FDXConsentRetrievalUtils;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;


public class PopulateConsentAuthorizeScreenApiTests {

    private PopulateConsentAuthorizeScreenApi api;

    @BeforeMethod
    void setUp() {
        api = new PopulateConsentAuthorizeScreenApi();
    }

    @Test
    void testPopulateConsentAuthorizeScreenPostSuccess() {

        JSONObject response = new JSONObject();
        response.put(FDXCommonConstants.STATUS, SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
        response.put(FDXCommonConstants.CONSENT_DATA, "mockConsentData");
        response.put(FDXCommonConstants.CONSUMER_DATA, "mockConsumerData");

        try (MockedStatic<FDXConsentRetrievalUtils> mockedUtils = Mockito.mockStatic(FDXConsentRetrievalUtils.class)) {
            // Mock the behavior of retrieveConsentData
            mockedUtils.when(() -> FDXConsentRetrievalUtils.retrieveConsentData(any()))
                    .thenReturn(response);

            // Mock request body
            PopulateConsentAuthorizeScreenRequestBody requestBody =
                    Mockito.mock(PopulateConsentAuthorizeScreenRequestBody.class);
            Mockito.when(requestBody.getRequestId()).thenReturn("mockRequestId");

            // Call the method under test
            Response apiResponse = api.populateConsentAuthorizeScreenPost(requestBody);

            // Assert the apiResponse
            Assert.assertEquals(Response.Status.OK.getStatusCode(), apiResponse.getStatus());
            JSONObject responseBody = new JSONObject(apiResponse.getEntity().toString());
            Assert.assertEquals(responseBody.getString("status"), "SUCCESS");
            JSONObject responseData = responseBody.getJSONObject("data");
            Assert.assertEquals(responseData.getString("consentData"), "mockConsentData");
            Assert.assertEquals(responseData.getString("consumerData"), "mockConsumerData");
        }
    }

    @Test
    void testPopulateConsentAuthorizeScreenPostFailure() {
        JSONObject consentResponse = new JSONObject();
        consentResponse.put("status", FailedResponse.StatusEnum.ERROR);
        consentResponse.put("responseStatus", 400);
        consentResponse.put("data", "mockErrorData");
        try (MockedStatic<FDXConsentRetrievalUtils> mockedUtils = Mockito.mockStatic(FDXConsentRetrievalUtils.class)) {
            // Mock the behavior of utility methods
            mockedUtils.when(() -> FDXConsentRetrievalUtils.retrieveConsentData(any()))
                    .thenReturn(consentResponse);

            // Create a mock request body
            PopulateConsentAuthorizeScreenRequestBody requestBody =
                    Mockito.mock(PopulateConsentAuthorizeScreenRequestBody.class);

            // Call the API method
            Response response = api.populateConsentAuthorizeScreenPost(requestBody);

            // Assert the response
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            JSONObject responseBody = new JSONObject(response.getEntity().toString());
            Assert.assertEquals(responseBody.getString("status"), "ERROR");
        }
    }

    @Test
    void testPopulateConsentAuthorizeScreenPostJSONException() {

        try (MockedStatic<FDXConsentRetrievalUtils> mockedStatic = Mockito.mockStatic(FDXConsentRetrievalUtils.class)) {
            mockedStatic.when(() -> FDXConsentRetrievalUtils.retrieveConsentData(any()))
                    .thenThrow(new JSONException("Mock JSON error"));

            // Create a mock request body
            PopulateConsentAuthorizeScreenRequestBody requestBody =
                    Mockito.mock(PopulateConsentAuthorizeScreenRequestBody.class);

            try {
                api.populateConsentAuthorizeScreenPost(requestBody);
            } catch (JSONException e) {
                Assert.assertEquals(e.getMessage(), "Mock JSON error");
            }
        }
    }
}
