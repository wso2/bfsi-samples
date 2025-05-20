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

package org.wso2.financial.services.fdx.extensions.utils;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.financial.services.accelerator.common.util.HTTPClientUtils;
import org.wso2.financial.services.fdx.extensions.configurations.ConfigurableProperties;
import org.wso2.financial.services.fdx.extensions.model.PopulateConsentAuthorizeScreenData;
import org.wso2.financial.services.fdx.extensions.model.PopulateConsentAuthorizeScreenRequestBody;
import org.wso2.financial.services.fdx.extensions.model.SuccessResponsePopulateConsentAuthorizeScreen;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FDXConsentRetrievalUtilsTests {

    private PopulateConsentAuthorizeScreenRequestBody requestBody;
    private PopulateConsentAuthorizeScreenData requestData;
    private JSONObject retrievalResponse;

    @BeforeMethod
    void setUp() {
        requestBody = mock(PopulateConsentAuthorizeScreenRequestBody.class);
        requestData = mock(PopulateConsentAuthorizeScreenData.class);
        retrievalResponse = new JSONObject();

        when(requestBody.getData()).thenReturn(requestData);
    }

    @Test
    public void testRetrieveConsentDataWithValidInput() throws IOException {
        // Mocking request parameters
        Map<String, Object> requestParams = new HashMap<>();
        Map<String, Object> authorizationDetail = new HashMap<>();
        Map<String, Object> consentRequest = new HashMap<>();

        consentRequest.put("durationType", "PERSISTENT");
        consentRequest.put("durationPeriod", "30");

        List<Map<String, Object>> resources = new ArrayList<>();
        Map<String, Object> resourceItem = new HashMap<>();
        resourceItem.put("resourceType", "ACCOUNT");
        resourceItem.put("dataClusters", Arrays.asList("TRANSACTIONS", "BALANCE"));
        resources.add(resourceItem);

        consentRequest.put("resources", resources);
        authorizationDetail.put("consentRequest", consentRequest);

        List<Map<String, Object>> authDetails = new ArrayList<>();
        authDetails.add(authorizationDetail);

        requestParams.put("authorization_details", authDetails);
        requestParams.put("redirect_uri", "https://callback");

        // Mocking inner data class
        PopulateConsentAuthorizeScreenData dataMock = Mockito.mock(PopulateConsentAuthorizeScreenData.class);
        Mockito.when(dataMock.getRequestParameters()).thenReturn(requestParams);
        Mockito.when(dataMock.getUserId()).thenReturn("user123");

        // Mocking outer request body
        PopulateConsentAuthorizeScreenRequestBody requestBodyMock =
                Mockito.mock(PopulateConsentAuthorizeScreenRequestBody.class);
        Mockito.when(requestBodyMock.getData()).thenReturn(dataMock);

        // Mock HttpClient
        CloseableHttpClient mockClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = Mockito.mock(CloseableHttpResponse.class);
        Mockito.when(mockClient.execute(Mockito.any(HttpGet.class))).thenReturn(mockResponse);
        StatusLine mockStatusLine = Mockito.mock(StatusLine.class);

        // Mock behavior
        Mockito.when(mockStatusLine.getStatusCode()).thenReturn(200); // simulate not found
        Mockito.when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);

        // Inject the mock into your utility if possible (assumes HTTPClientUtils is mockable)
        try (MockedStatic<HTTPClientUtils> mockStatic = Mockito.mockStatic(HTTPClientUtils.class)) {
            mockStatic.when(HTTPClientUtils::getHttpsClient).thenReturn(mockClient);

            // Mock HttpEntity
            HttpEntity mockEntity = Mockito.mock(HttpEntity.class);
            Mockito.when(mockResponse.getEntity()).thenReturn(mockEntity);

            // Mock InputStream
            String mockContent = "{\"data\" : [{\"account_id\": \"12345\", \"type\": \"saving\"}]}";
            InputStream mockInputStream = new ByteArrayInputStream(mockContent.getBytes(StandardCharsets.UTF_8));
            Mockito.when(mockEntity.getContent()).thenReturn(mockInputStream);

            JSONObject response = FDXConsentRetrievalUtils.retrieveConsentData(requestBodyMock);

            // Validate response
            Assert.assertNotNull(response);
            Assert.assertEquals(response.get("status").toString(), "SUCCESS");
            Assert.assertTrue(response.has("consentData"));
        }
    }

    @Test
    void testRetrieveConsentDataWithoutAuthorizationDetails() {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("someOtherKey", "value");
        when(requestData.getRequestParameters()).thenReturn(requestParams);

        // Execute
        retrievalResponse = FDXConsentRetrievalUtils.retrieveConsentData(requestBody);

        // Assert
        Assert.assertEquals(retrievalResponse.get("status").toString(), "ERROR");
        Assert.assertEquals(retrievalResponse.get("responseStatus"), 400);
    }

    @Test
    void testRetrieveAccountDataSuccess() throws Exception {
        // Mock input
        when(requestData.getUserId()).thenReturn("user123");
        when(requestBody.getData()).thenReturn(requestData);

        String mockAccountResponse = new JSONObject()
                .put("data", new JSONArray().put(new JSONObject()
                        .put("account_id", "123456789")
                        .put("type", "CHECKING")))
                .toString();

        // Mock the HTTP call
        try (MockedStatic<FDXConsentRetrievalUtils> mockedStatic = Mockito.mockStatic(FDXConsentRetrievalUtils.class)) {
            mockedStatic.when(
                            () -> FDXConsentRetrievalUtils.getAccountsFromEndpoint(anyString(),
                                    Mockito.anyMap(),
                                    Mockito.anyMap()))
                    .thenReturn(mockAccountResponse);

            // Execute
            mockedStatic.when(() -> FDXConsentRetrievalUtils.retrieveAccountData(requestBody, retrievalResponse))
                    .thenCallRealMethod();
            FDXConsentRetrievalUtils.retrieveAccountData(requestBody, retrievalResponse);

            // Assert
            Assert.assertEquals(retrievalResponse.get("status").toString(), "SUCCESS");
            Assert.assertTrue(retrievalResponse.has("consumerData"));
            JSONArray consumerData = retrievalResponse.getJSONArray("consumerData");
            Assert.assertEquals(consumerData.length(), 1);
            Assert.assertEquals(consumerData.getJSONObject(0).getString("account_id"), "123456789");
            Assert.assertEquals(consumerData.getJSONObject(0).getString("type"), "CHECKING");
        }
    }

    @Test
    void testRetrieveAccountDataWithoutAccounts() throws Exception {
        // Mock input
        when(requestData.getUserId()).thenReturn("user123");
        when(requestBody.getData()).thenReturn(requestData);

        // Mock the HTTP call to return an empty response
        try (MockedStatic<FDXConsentRetrievalUtils> mockedStatic = Mockito.mockStatic(FDXConsentRetrievalUtils.class)) {
            mockedStatic.when(
                            () -> FDXConsentRetrievalUtils.getAccountsFromEndpoint(anyString(),
                                    Mockito.anyMap(),
                                    Mockito.anyMap()))
                    .thenReturn(new JSONObject().put("data", new JSONArray()).toString());

            // Execute
            mockedStatic.when(() -> FDXConsentRetrievalUtils.retrieveAccountData(requestBody, retrievalResponse))
                    .thenCallRealMethod();
            FDXConsentRetrievalUtils.retrieveAccountData(requestBody, retrievalResponse);

            // Assert
            Assert.assertEquals(retrievalResponse.get("status").toString(), "SUCCESS");
            Assert.assertTrue(retrievalResponse.has("consumerData"));
            Assert.assertEquals(retrievalResponse.getJSONArray("consumerData").length(), 0);
        }
    }

    @Test(expectedExceptions = JSONException.class)
    void testRetrieveAccountDataWithJSONException() throws Exception {
        // Mock input
        when(requestData.getUserId()).thenReturn("user123");
        when(requestBody.getData()).thenReturn(requestData);

        // Mock the HTTP call to return an empty response
        try (MockedStatic<FDXConsentRetrievalUtils> mockedStatic = Mockito.mockStatic(FDXConsentRetrievalUtils.class)) {
            mockedStatic.when(
                            () -> FDXConsentRetrievalUtils.getAccountsFromEndpoint(anyString(),
                                    Mockito.anyMap(),
                                    Mockito.anyMap()))
                    .thenReturn("data");

            // Execute
            mockedStatic.when(() -> FDXConsentRetrievalUtils.retrieveAccountData(requestBody, retrievalResponse))
                    .thenCallRealMethod();
            FDXConsentRetrievalUtils.retrieveAccountData(requestBody, retrievalResponse);
        }
    }

    @Test
    void testGetConsentExpiryDateTimeWithZero() {
        // Mock input
        long durationPeriod = 0;

        // Execute
        OffsetDateTime result = (OffsetDateTime) FDXConsentRetrievalUtils.getConsentExpiryDateTime(durationPeriod);
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        // Assert - allow for a 1 second difference
        long secondsDifference = Math.abs(result.toEpochSecond() - now.toEpochSecond());
        Assert.assertTrue(secondsDifference <= 1, "Time difference is more than 1 second");
    }

    @Test
    public void testHandleBadRequests() {
        JSONObject response = new JSONObject();
        FDXConsentRetrievalUtils.handleBadRequests(response, 400, "Bad Request");
        Assert.assertEquals(response.get("status").toString(), "ERROR");
        Assert.assertEquals(response.getInt("responseStatus"), 400);
        Assert.assertTrue(response.getJSONObject("data").getString("data").contains("Bad Request"));
    }

    @Test
    public void testGetAccountsFromEndpointWithNon200Response() throws Exception {
        // Mock URL
        String url = ConfigurableProperties.SHARABLE_ENDPOINT;

        // Mock HttpClient
        CloseableHttpClient mockClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = Mockito.mock(CloseableHttpResponse.class);
        StatusLine mockStatusLine = Mockito.mock(StatusLine.class);

        // Mock behavior
        Mockito.when(mockStatusLine.getStatusCode()).thenReturn(404); // simulate not found
        Mockito.when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        Mockito.when(mockClient.execute(Mockito.any(HttpGet.class))).thenReturn(mockResponse);

        // Inject the mock into your utility if possible (assumes HTTPClientUtils is mockable)
        try (MockedStatic<HTTPClientUtils> mockStatic = Mockito.mockStatic(HTTPClientUtils.class)) {
            mockStatic.when(HTTPClientUtils::getHttpsClient).thenReturn(mockClient);
            // Call the method
            String result =
                    FDXConsentRetrievalUtils.getAccountsFromEndpoint(url, Collections.emptyMap(),
                            Collections.emptyMap());

            // Assert
            Assert.assertNull(result);
        }
    }

    @Test
    void testRetrieveDataClusterDataWithMultipleResources() {
        // Prepare the authorization details
        Map<String, Object> authorizationDetail = new HashMap<>();
        Map<String, List<String>> resources = new HashMap<>();
        resources.put("resource1", Arrays.asList("INVESTMENTS", "TRANSACTIONS"));
        resources.put("resource2", Arrays.asList("TRANSACTIONS", "PAYMENT_SUPPORT"));
        authorizationDetail.put(FDXCommonConstants.RESOURCES, resources);

        List<Map<String, Object>> authorizationDetails = new ArrayList<>();
        authorizationDetails.add(authorizationDetail);

        // Prepare one consentData item with the above authDetails
        Map<String, Object> consentDataItem = new HashMap<>();
        consentDataItem.put(FDXCommonConstants.AUTHORIZATION_DETAILS, authorizationDetails);

        JSONObject consentDataJson = new JSONObject(consentDataItem);

        // Wrap in retrievalResponse
        retrievalResponse.put(FDXCommonConstants.STATUS,
                SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
        retrievalResponse.put(FDXCommonConstants.CONSENT_DATA, new JSONArray().put(consentDataJson));

        // Execute
        FDXConsentRetrievalUtils.retrieveDataClusterData(retrievalResponse);

        // Assert status unchanged
        Assert.assertEquals(
                retrievalResponse.get(FDXCommonConstants.STATUS),
                SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS
        );

        // Assert data requested has been populated
        JSONObject updatedConsentItem = retrievalResponse
                .getJSONArray(FDXCommonConstants.CONSENT_DATA)
                .getJSONObject(0);

        Assert.assertTrue(updatedConsentItem.has(FDXCommonConstants.DATA_REQUESTED));
    }

    @Test
    void testAccountIdLengthLessThan4() {
        String accountId = "123"; // Length = 3
        String masked = FDXConsentRetrievalUtils.getMaskedAccountNumber(accountId);
        Assert.assertEquals(masked, "**3");
    }

    @Test
    void testAccountIdLengthGreaterThan4() {
        String accountId = "123456789"; // Length = 9
        String masked = FDXConsentRetrievalUtils.getMaskedAccountNumber(accountId);
        Assert.assertEquals(masked, "*****6789");
    }
}
