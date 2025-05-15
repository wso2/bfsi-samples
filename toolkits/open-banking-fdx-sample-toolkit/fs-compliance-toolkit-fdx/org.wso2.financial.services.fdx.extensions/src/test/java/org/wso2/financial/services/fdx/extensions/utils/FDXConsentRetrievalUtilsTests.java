package org.wso2.financial.services.fdx.extensions.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.financial.services.fdx.extensions.model.PopulateConsentAuthorizeScreenData;
import org.wso2.financial.services.fdx.extensions.model.PopulateConsentAuthorizeScreenRequestBody;

import java.util.HashMap;
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
    void testRetrieveConsentData_success() {
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
    void testRetrieveAccountData_success() throws Exception {
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
    void testRetrieveAccountData_noAccounts() throws Exception {
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

}
