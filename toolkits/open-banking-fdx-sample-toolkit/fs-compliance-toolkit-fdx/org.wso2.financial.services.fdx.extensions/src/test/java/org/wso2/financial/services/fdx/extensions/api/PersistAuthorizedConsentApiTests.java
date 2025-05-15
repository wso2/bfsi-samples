package org.wso2.financial.services.fdx.extensions.api;

import org.json.JSONObject;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.financial.services.fdx.extensions.model.FailedResponseInConsent;
import org.wso2.financial.services.fdx.extensions.model.PersistAuthorizedConsentRequestBody;
import org.wso2.financial.services.fdx.extensions.model.SuccessResponsePersistAuthorizedConsent;
import org.wso2.financial.services.fdx.extensions.utils.FDXCommonConstants;
import org.wso2.financial.services.fdx.extensions.utils.FDXConsentPersistUtils;

import java.util.Map;
import javax.ws.rs.core.Response;

public class PersistAuthorizedConsentApiTests {
    private PersistAuthorizedConsentApi api;

    @BeforeMethod
    public void setUp() {
        api = new PersistAuthorizedConsentApi();
    }

    @Test
    void testPersistAuthorizedConsentPost_Success() {
        try (MockedStatic<FDXConsentPersistUtils> mockedUtils = Mockito.mockStatic(FDXConsentPersistUtils.class)) {
            // Mock the behavior of persistFDXConsent
            mockedUtils.when(() -> FDXConsentPersistUtils.persistFDXConsent(Mockito.any(), Mockito.any()))
                    .thenAnswer(invocation -> {
                        Map<String, Object> validationResponse = invocation.getArgument(1);
                        validationResponse.put(FDXCommonConstants.STATUS,
                                SuccessResponsePersistAuthorizedConsent.StatusEnum.SUCCESS);
                        validationResponse.put(FDXCommonConstants.TYPE, "mockType");
                        validationResponse.put(FDXCommonConstants.FDX_CONSENT_STATUS, "mockStatus");
                        return null;
                    });

            // Mock request body
            PersistAuthorizedConsentRequestBody requestBody = Mockito.mock(PersistAuthorizedConsentRequestBody.class);
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
    void testPersistAuthorizedConsentPost_Failure() {
        try (MockedStatic<FDXConsentPersistUtils> mockedUtils = Mockito.mockStatic(FDXConsentPersistUtils.class)) {
            // Mock the behavior of persistFDXConsent
            mockedUtils.when(() -> FDXConsentPersistUtils.persistFDXConsent(Mockito.any(), Mockito.any()))
                    .thenAnswer(invocation -> {
                        Map<String, Object> validationResponse = invocation.getArgument(1);
                        validationResponse.put(FDXCommonConstants.STATUS, FailedResponseInConsent.StatusEnum.ERROR);
                        validationResponse.put(FDXCommonConstants.RESPONSE_STATUS, 400);
                        validationResponse.put(FDXCommonConstants.DATA, "mockErrorData");
                        return null;
                    });

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
    void testPersistAuthorizedConsentPost_Exception() {
        try (MockedStatic<FDXConsentPersistUtils> mockedUtils = Mockito.mockStatic(FDXConsentPersistUtils.class)) {
            // Mock the behavior of persistFDXConsent to throw an exception
            mockedUtils.when(() -> FDXConsentPersistUtils.persistFDXConsent(Mockito.any(), Mockito.any()))
                    .thenThrow(new RuntimeException("Mock exception"));

            // Mock request body
            PersistAuthorizedConsentRequestBody requestBody = Mockito.mock(PersistAuthorizedConsentRequestBody.class);

            // Call the method under test and expect an exception
            Response response = api.persistAuthorizedConsentPost(requestBody);

            // Assert the response
            Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
            JSONObject responseBody = new JSONObject(response.getEntity().toString());
            Assert.assertEquals(responseBody.getString("status"), "ERROR");
        }
    }
}

