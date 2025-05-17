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
        try (MockedStatic<FDXConsentRetrievalUtils> mockedUtils = Mockito.mockStatic(FDXConsentRetrievalUtils.class)) {
            // Mock the behavior of retrieveConsentData
            mockedUtils.when(() -> FDXConsentRetrievalUtils.retrieveConsentData(any(), any()))
                    .thenAnswer(invocation -> {
                        JSONObject argResponse = invocation.getArgument(1);
                        argResponse.put("status", SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
                        argResponse.put("consentData", "mockConsentData");
                        return null;
                    });

            // Mock the behavior of retrieveDataClusterData
            mockedUtils.when(() -> FDXConsentRetrievalUtils.retrieveDataClusterData(any()))
                    .thenAnswer(invocation -> null);

            // Mock the behavior of retrieveAccountData
            mockedUtils.when(() -> FDXConsentRetrievalUtils.retrieveAccountData(any(), any()))
                    .thenAnswer(invocation -> {
                        JSONObject argResponse = invocation.getArgument(1);
                        argResponse.put("consumerData", "mockConsumerData");
                        return null;
                    });

            // Mock request body
            PopulateConsentAuthorizeScreenRequestBody requestBody =
                    Mockito.mock(PopulateConsentAuthorizeScreenRequestBody.class);
            Mockito.when(requestBody.getRequestId()).thenReturn("mockRequestId");

            // Call the method under test
            Response response = api.populateConsentAuthorizeScreenPost(requestBody);

            // Assert the response
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            JSONObject responseBody = new JSONObject(response.getEntity().toString());
            Assert.assertEquals(responseBody.getString("status"), "SUCCESS");
        }

    }

    @Test
    void testPopulateConsentAuthorizeScreenPostFailure() {
        try (MockedStatic<FDXConsentRetrievalUtils> mockedUtils = Mockito.mockStatic(FDXConsentRetrievalUtils.class)) {
            // Mock the behavior of utility methods
            mockedUtils.when(() -> FDXConsentRetrievalUtils.retrieveConsentData(any(), any()))
                    .thenAnswer(invocation -> {
                        JSONObject validationResponse = invocation.getArgument(1);
                        validationResponse.put("status", FailedResponse.StatusEnum.ERROR);
                        validationResponse.put("responseStatus", 400);
                        validationResponse.put("data", "mockErrorData");
                        return null;
                    });

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
    void testPopulateConsentAuthorizeScreenPostException() {
        try (MockedStatic<FDXConsentRetrievalUtils> mockedUtils = Mockito.mockStatic(FDXConsentRetrievalUtils.class)) {
            // Mock the behavior of utility methods to throw an exception
            mockedUtils.when(() -> FDXConsentRetrievalUtils.retrieveConsentData(any(), any()))
                    .thenAnswer(invocation -> {
                        JSONObject argResponse = invocation.getArgument(1);
                        argResponse.put("status", SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
                        argResponse.put("consentData", "mockConsentData");
                        return null;
                    });

            // Mock the behavior of retrieveDataClusterData
            mockedUtils.when(() -> FDXConsentRetrievalUtils.retrieveDataClusterData(any()))
                    .thenAnswer(invocation -> null);

            mockedUtils.when(() -> FDXConsentRetrievalUtils.retrieveAccountData(any(), any()))
                    .thenThrow(new RuntimeException("Mock exception"));

            // Create a mock request body
            PopulateConsentAuthorizeScreenRequestBody requestBody =
                    Mockito.mock(PopulateConsentAuthorizeScreenRequestBody.class);

            // Call the API method and expect an exception
            try {
                api.populateConsentAuthorizeScreenPost(requestBody);
            } catch (RuntimeException e) {
                Assert.assertEquals(e.getMessage(), "java.lang.RuntimeException: Mock exception");
            }
        }
    }

    @Test
    void testPopulateConsentAuthorizeScreenPostJSONException() {
        try (MockedStatic<FDXConsentRetrievalUtils> mockedStatic = Mockito.mockStatic(FDXConsentRetrievalUtils.class)) {
            mockedStatic.when(() -> FDXConsentRetrievalUtils.retrieveConsentData(any(), any()))
                    .thenAnswer(invocation -> {
                        JSONObject argResponse = invocation.getArgument(1);
                        argResponse.put("status", SuccessResponsePopulateConsentAuthorizeScreen.StatusEnum.SUCCESS);
                        argResponse.put("consentData", "mockConsentData");
                        return null;
                    });

            mockedStatic.when(() -> FDXConsentRetrievalUtils.retrieveDataClusterData(any()))
                    .thenAnswer(invocation -> null);

            mockedStatic.when(() -> FDXConsentRetrievalUtils.retrieveAccountData(any(), any()))
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
