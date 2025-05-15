package org.wso2.financial.services.fdx.extensions.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.financial.services.accelerator.consent.mgt.extensions.common.ConsentException;
import org.wso2.financial.services.fdx.extensions.model.PersistAuthorizedConsent;
import org.wso2.financial.services.fdx.extensions.model.PersistAuthorizedConsentRequestBody;
import org.wso2.financial.services.fdx.extensions.model.UserGrantedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FDXConsentPersistUtilsTests {

    private FDXConsentPersistUtils fdxConsentPersistUtils;

    @BeforeMethod
    public void setUp() {
        fdxConsentPersistUtils = new FDXConsentPersistUtils();
    }

    @Test
    public void testPersistFDXConsentSuccess() {
        // Arrange
        PersistAuthorizedConsentRequestBody requestBody = new PersistAuthorizedConsentRequestBody();
        PersistAuthorizedConsent consentData = new PersistAuthorizedConsent();
        UserGrantedData userGrantedData = new UserGrantedData();

        // Authorized Resources (includes accountIds and cookies)
        JSONObject authorizedResources = new JSONObject();
        authorizedResources.put("accountIds", new JSONArray().put("1234567890"));
        authorizedResources.put("cookies", new JSONObject().put("commonAuthId", "common-auth-id-123"));

        // Request Parameters (includes authorizationDetails with consentRequest)
        JSONObject consentRequest = new JSONObject();
        consentRequest.put("durationType", "RECURRING");
        consentRequest.put("durationPeriod", 30);
        JSONArray dataClusters = new JSONArray().put("transactions").put("balances");
        JSONObject resource = new JSONObject().put("dataClusters", dataClusters);
        consentRequest.put("resources", new JSONArray().put(resource));

        JSONObject authorizationDetail = new JSONObject().put("consentRequest", consentRequest);
        JSONArray authorizationDetails = new JSONArray().put(authorizationDetail);
        JSONObject requestParameters = new JSONObject().put("authorization_details", authorizationDetails);

        // Set data to userGrantedData
        userGrantedData.setAuthorizedResources(authorizedResources.toMap());
        userGrantedData.setRequestParameters(requestParameters.toMap());
        userGrantedData.setUserId("user-123");

        consentData.setUserGrantedData(userGrantedData);
        requestBody.setData(consentData);

        Map<String, Object> validationResponse = new HashMap<>();

        // Act
        FDXConsentPersistUtils.persistFDXConsent(requestBody, validationResponse);

        // Assert
        Assert.assertEquals(validationResponse.get("status").toString(), "SUCCESS");
        Assert.assertEquals(validationResponse.get("consentStatus").toString(), "Authorised");
        Assert.assertEquals(validationResponse.get("type").toString(), "FDX_ACCOUNTS");
        Assert.assertEquals(validationResponse.get("isRecurring"), false);
        Assert.assertEquals(validationResponse.get("durationPeriod"), 30L);
        Assert.assertNotNull(validationResponse.get("receipt"));
        Assert.assertNotNull(validationResponse.get("authorizationResources"));
    }

    @Test(expectedExceptions = ConsentException.class)
    public void testPersistFDXConsentPersistWhenConsentDataIsNull() {
        // Arrange
        PersistAuthorizedConsentRequestBody requestBody = new PersistAuthorizedConsentRequestBody();
        requestBody.setData(null);

        Map<String, Object> validationResponse = new HashMap<>();

        // Act
        FDXConsentPersistUtils.persistFDXConsent(requestBody, validationResponse);
    }

    @Test(expectedExceptions = ConsentException.class)
    public void testPersistFDXConsentWithNullAccountIds() {
        // Arrange
        PersistAuthorizedConsentRequestBody requestBody = new PersistAuthorizedConsentRequestBody();
        PersistAuthorizedConsent consentData = new PersistAuthorizedConsent();
        UserGrantedData userGrantedData = new UserGrantedData();

        // Authorized Resources (includes accountIds and cookies)
        JSONObject authorizedResources = new JSONObject();
        authorizedResources.put("accountIds", Optional.empty());
        authorizedResources.put("cookies", new JSONObject().put("commonAuthId", "common-auth-id-123"));

        // Request Parameters (includes authorizationDetails with consentRequest)
        JSONObject consentRequest = new JSONObject();
        consentRequest.put("durationType", "RECURRING");
        consentRequest.put("durationPeriod", 30);
        JSONArray dataClusters = new JSONArray().put("transactions").put("balances");
        JSONObject resource = new JSONObject().put("dataClusters", dataClusters);
        consentRequest.put("resources", new JSONArray().put(resource));

        JSONObject authorizationDetail = new JSONObject().put("consentRequest", consentRequest);
        JSONArray authorizationDetails = new JSONArray().put(authorizationDetail);
        JSONObject requestParameters = new JSONObject().put("authorization_details", authorizationDetails);

        // Set data to userGrantedData
        userGrantedData.setAuthorizedResources(authorizedResources.toMap());
        userGrantedData.setRequestParameters(requestParameters.toMap());
        userGrantedData.setUserId("user-123");

        consentData.setUserGrantedData(userGrantedData);
        requestBody.setData(consentData);

        Map<String, Object> validationResponse = new HashMap<>();

        // Act
        FDXConsentPersistUtils.persistFDXConsent(requestBody, validationResponse);
    }

    @Test(expectedExceptions = ConsentException.class)
    public void testPersistFDXConsentWithNonJSONArrayAccountIds() {
        // Arrange
        PersistAuthorizedConsentRequestBody requestBody = new PersistAuthorizedConsentRequestBody();
        PersistAuthorizedConsent consentData = new PersistAuthorizedConsent();
        UserGrantedData userGrantedData = new UserGrantedData();

        // Authorized Resources (includes accountIds and cookies)
        JSONObject authorizedResources = new JSONObject();
        authorizedResources.put("accountIds", new HashMap<>());
        authorizedResources.put("cookies", new JSONObject().put("commonAuthId", "common-auth-id-123"));

        // Request Parameters (includes authorizationDetails with consentRequest)
        JSONObject consentRequest = new JSONObject();
        consentRequest.put("durationType", "RECURRING");
        consentRequest.put("durationPeriod", 30);
        JSONArray dataClusters = new JSONArray().put("transactions").put("balances");
        JSONObject resource = new JSONObject().put("dataClusters", dataClusters);
        consentRequest.put("resources", new JSONArray().put(resource));

        JSONObject authorizationDetail = new JSONObject().put("consentRequest", consentRequest);
        JSONArray authorizationDetails = new JSONArray().put(authorizationDetail);
        JSONObject requestParameters = new JSONObject().put("authorization_details", authorizationDetails);

        // Set data to userGrantedData
        userGrantedData.setAuthorizedResources(authorizedResources.toMap());
        userGrantedData.setRequestParameters(requestParameters.toMap());
        userGrantedData.setUserId("user-123");

        consentData.setUserGrantedData(userGrantedData);
        requestBody.setData(consentData);

        Map<String, Object> validationResponse = new HashMap<>();

        // Act
        FDXConsentPersistUtils.persistFDXConsent(requestBody, validationResponse);
    }

    @Test(expectedExceptions = ConsentException.class)
    public void testPersistFDXConsentWithIntegerAccountIds() {
        // Arrange
        PersistAuthorizedConsentRequestBody requestBody = new PersistAuthorizedConsentRequestBody();
        PersistAuthorizedConsent consentData = new PersistAuthorizedConsent();
        UserGrantedData userGrantedData = new UserGrantedData();

        // Authorized Resources (includes accountIds and cookies)
        JSONObject authorizedResources = new JSONObject();
        authorizedResources.put("accountIds", new JSONArray().put(1234567890));
        authorizedResources.put("cookies", new JSONObject().put("commonAuthId", "common-auth-id-123"));

        // Request Parameters (includes authorizationDetails with consentRequest)
        JSONObject consentRequest = new JSONObject();
        consentRequest.put("durationType", "RECURRING");
        consentRequest.put("durationPeriod", 30);
        JSONArray dataClusters = new JSONArray().put("transactions").put("balances");
        JSONObject resource = new JSONObject().put("dataClusters", dataClusters);
        consentRequest.put("resources", new JSONArray().put(resource));

        JSONObject authorizationDetail = new JSONObject().put("consentRequest", consentRequest);
        JSONArray authorizationDetails = new JSONArray().put(authorizationDetail);
        JSONObject requestParameters = new JSONObject().put("authorization_details", authorizationDetails);

        // Set data to userGrantedData
        userGrantedData.setAuthorizedResources(authorizedResources.toMap());
        userGrantedData.setRequestParameters(requestParameters.toMap());
        userGrantedData.setUserId("user-123");

        consentData.setUserGrantedData(userGrantedData);
        requestBody.setData(consentData);

        Map<String, Object> validationResponse = new HashMap<>();

        // Act
        FDXConsentPersistUtils.persistFDXConsent(requestBody, validationResponse);
    }

    @Test(expectedExceptions = ConsentException.class)
    public void testPersistFDXConsentWithEmptyAuthorizationDetails() {
        // Arrange
        PersistAuthorizedConsentRequestBody requestBody = new PersistAuthorizedConsentRequestBody();
        PersistAuthorizedConsent consentData = new PersistAuthorizedConsent();
        UserGrantedData userGrantedData = new UserGrantedData();

        // Authorized Resources (valid)
        JSONObject authorizedResources = new JSONObject();
        authorizedResources.put("accountIds", new JSONArray().put("1234567890"));
        authorizedResources.put("cookies", new JSONObject().put("commonAuthId", "common-auth-id-123"));

        // Request Parameters with empty authorizationDetails
        JSONArray authorizationDetails = new JSONArray();  // empty array
        JSONObject requestParameters = new JSONObject().put("authorizationDetails", authorizationDetails);

        // Set values to userGrantedData
        userGrantedData.setAuthorizedResources(authorizedResources.toMap());
        userGrantedData.setRequestParameters(requestParameters.toMap());
        userGrantedData.setUserId("user-123");

        consentData.setUserGrantedData(userGrantedData);
        requestBody.setData(consentData);

        Map<String, Object> validationResponse = new HashMap<>();

        // Act - should throw ConsentException
        FDXConsentPersistUtils.persistFDXConsent(requestBody, validationResponse);

        // If no exception, fail the test
        Assert.fail("Expected ConsentException due to empty authorizationDetails");
    }
}