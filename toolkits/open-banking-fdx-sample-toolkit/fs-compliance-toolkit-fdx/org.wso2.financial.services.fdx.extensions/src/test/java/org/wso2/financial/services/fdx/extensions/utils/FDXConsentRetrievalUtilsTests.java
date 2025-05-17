package org.wso2.financial.services.fdx.extensions.utils;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FDXConsentRetrievalUtilsTests {

    private PopulateConsentAuthorizeScreenRequestBody requestBody;
    private PopulateConsentAuthorizeScreenData requestData;
    private JSONObject validationResponse;

    @BeforeMethod
    void setUp() {
        requestBody = mock(PopulateConsentAuthorizeScreenRequestBody.class);
        requestData = mock(PopulateConsentAuthorizeScreenData.class);
        validationResponse = new JSONObject();

        when(requestBody.getData()).thenReturn(requestData);
    }

    @Test
    void testRetrieveConsentDataSuccess() {
        // Mock input
        Map<String, Object> requestParams = new HashMap<>();
        JSONArray authDetails = new JSONArray();
        JSONObject consentRequest = new JSONObject()
                .put("durationType", "RECURRING")
                .put("durationPeriod", "30")
                .put("resources", new JSONArray().put(new JSONObject()
                        .put("resourceType", "ACCOUNTS")
                        .put("dataClusters", new JSONArray().put("TRANSACTIONS").put("BALANCES"))
                ));
        authDetails.put(new JSONObject().put("consentRequest", consentRequest));
        requestParams.put("authorization_details", authDetails);
        requestParams.put("redirect_uri", "https://callback");

        when(requestData.getRequestParameters()).thenReturn(requestParams);

        // Execute
        FDXConsentRetrievalUtils.retrieveConsentData(requestBody, validationResponse);

        // Assert
        Assert.assertEquals(validationResponse.get("status").toString(), "SUCCESS");
        Assert.assertTrue(validationResponse.has("consentData"));
    }

    @Test
    void testRetrieveConsentDataWithoutAuthorizationDetails() {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("someOtherKey", "value");
        when(requestData.getRequestParameters()).thenReturn(requestParams);

        // Execute
        FDXConsentRetrievalUtils.retrieveConsentData(requestBody, validationResponse);

        // Assert
        Assert.assertEquals(validationResponse.get("status").toString(), "ERROR");
        Assert.assertEquals(validationResponse.get("responseStatus"), 400);
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
                            () -> FDXConsentRetrievalUtils.getAccountsFromEndpoint(Mockito.anyString(),
                                    Mockito.anyMap(),
                                    Mockito.anyMap()))
                    .thenReturn(mockAccountResponse);

            JSONObject validationResponse = new JSONObject();

            // Execute
            mockedStatic.when(() -> FDXConsentRetrievalUtils.retrieveAccountData(requestBody, validationResponse))
                    .thenCallRealMethod();
            FDXConsentRetrievalUtils.retrieveAccountData(requestBody, validationResponse);

            // Assert
            Assert.assertEquals(validationResponse.get("status").toString(), "SUCCESS");
            Assert.assertTrue(validationResponse.has("consumerData"));
            JSONArray consumerData = validationResponse.getJSONArray("consumerData");
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
                            () -> FDXConsentRetrievalUtils.getAccountsFromEndpoint(Mockito.anyString(),
                                    Mockito.anyMap(),
                                    Mockito.anyMap()))
                    .thenReturn(new JSONObject().put("data", new JSONArray()).toString());

            JSONObject validationResponse = new JSONObject();

            // Execute
            mockedStatic.when(() -> FDXConsentRetrievalUtils.retrieveAccountData(requestBody, validationResponse))
                    .thenCallRealMethod();
            FDXConsentRetrievalUtils.retrieveAccountData(requestBody, validationResponse);

            // Assert
            Assert.assertEquals(validationResponse.get("status").toString(), "SUCCESS");
            Assert.assertTrue(validationResponse.has("consumerData"));
            Assert.assertEquals(validationResponse.getJSONArray("consumerData").length(), 0);
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
    public void testGetAccountsFromEndpoint_Non200Response() throws Exception {
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

        // Wrap in validationResponse
        JSONObject validationResponse = new JSONObject();
        validationResponse.put(FDXCommonConstants.STATUS,
                SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
        validationResponse.put(FDXCommonConstants.CONSENT_DATA, new JSONArray().put(consentDataJson));

        // Execute
        FDXConsentRetrievalUtils.retrieveDataClusterData(validationResponse);

        // Assert status unchanged
        Assert.assertEquals(
                validationResponse.get(FDXCommonConstants.STATUS),
                SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS
        );

        // Assert data requested has been populated
        JSONObject updatedConsentItem = validationResponse
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
