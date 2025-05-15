package org.wso2.financial.services.fdx.consent.authservlet.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class FSFDXAuthServletImplTest {

    private FSFDXAuthServlet servlet;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private ResourceBundle mockResourceBundle;

    @BeforeMethod
    public void setUp() {
        servlet = new FSFDXAuthServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResourceBundle = Mockito.mock(ResourceBundle.class);
    }

    @Test
    public void testUpdateRequestAttribute() {
        JSONObject dataSet = new JSONObject();
        dataSet.put("consentData", new JSONArray("[{\"data_requested\": {\"Transactions\": [\"Historical and current " +
                "transactions\", \"Transaction types\", \"Amounts\", \"Dates and descriptions\"], \"Investments\": " +
                "[\"Investment contributions\", \"Investment loans\", \"Pension data\", \"Vesting and account holding" +
                " details\"], \"Account Information - Details\": [\"Account display name\", \"Masked account " +
                "number\", \"Account type and Description\", \"Account balances\", \"Credit limits\", \"Due dates and" +
                " Interest rates\"]}, \"authorization_details\": [{\"expiryDate\": \"2025-07-01T09:46:01.835400Z\", " +
                "\"resources\": {\"ACCOUNT\": [\"ACCOUNT_DETAILED\", \"TRANSACTIONS\", \"INVESTMENTS\"]}, " +
                "\"durationPeriod\": 60}], \"redirect_uri\": \"https://www.google.com/redirects/redirect1\", " +
                "\"type\": \"FDX_ACCOUNTS\"}]"));
        dataSet.put("application", "FDX-App");
        dataSet.put("accounts", new JSONArray("[{\"account_id\": \"30080012343456\", \"accountIdToDisplay\": " +
                "\"**********3456\", \"type\": \"TRANS_AND_SAVINGS_ACCOUNTS\"}]"));

        Map<String, Object> result = servlet.updateRequestAttribute(mockRequest, dataSet, mockResourceBundle);

        assertNotNull(result);
    }

    @Test(expectedExceptions = JSONException.class)
    public void testUpdateRequestAttributeWithEmptyDataset() {
        servlet.updateRequestAttribute(mockRequest, new JSONObject(), mockResourceBundle);
    }

    @Test
    public void testReadExpiryDate() {
        JSONObject dataSet = new JSONObject();
        dataSet.put("consentData", new JSONArray("[{\"data_requested\": {\"Transactions\": [\"Historical and current " +
                "transactions\", \"Transaction types\", \"Amounts\", \"Dates and descriptions\"], \"Investments\": " +
                "[\"Investment contributions\", \"Investment loans\", \"Pension data\", \"Vesting and account holding" +
                " details\"], \"Account Information - Details\": [\"Account display name\", \"Masked account " +
                "number\", \"Account type and Description\", \"Account balances\", \"Credit limits\", \"Due dates and" +
                " Interest rates\"]}, \"authorization_details\": [{\"expiryDate\": \"2025-07-01T09:46:01.835400Z\", " +
                "\"resources\": {\"ACCOUNT\": [\"ACCOUNT_DETAILED\", \"TRANSACTIONS\", \"INVESTMENTS\"]}, " +
                "\"durationPeriod\": 60}], \"redirect_uri\": \"https://www.google.com/redirects/redirect1\", " +
                "\"type\": \"FDX_ACCOUNTS\"}]"));

        Map<String, String> expiryDateMap = servlet.readExpiryDate(dataSet);

        assertEquals(expiryDateMap.get("durationPeriod"), "60");
        assertEquals(expiryDateMap.get("expiryDate"), "2025-07-01");
    }

    @Test
    public void testAddAccountList() {
        JSONObject dataSet = new JSONObject();
        dataSet.put("accounts", new JSONArray("[{\"account_id\": \"12345\", \"accountIdToDisplay\": \"12345\", " +
                "\"type\": \"Savings\"}]"));

        Object accountList = servlet.addAccountList(dataSet);

        assertNotNull(accountList);
        List<Map<String, String>> accounts = (List<Map<String, String>>) accountList;
        assertEquals(accounts.size(), 1);
        assertEquals(accounts.get(0).get("account_id"), "12345");
        assertEquals(accounts.get(0).get("display_name"), "12345");
        assertEquals(accounts.get(0).get("type"), "Savings");
    }

    @Test
    public void testUpdateConsentData() {
        Mockito.when(mockRequest.getParameter("accounts[]")).thenReturn("12345:67890");

        Map<String, Object> result = servlet.updateConsentData(mockRequest);

        assertNotNull(result);
        JSONArray accountIds = (JSONArray) result.get("accountIds");
        assertEquals(accountIds.length(), 2);
        assertEquals(accountIds.getString(0), "12345");
        assertEquals(accountIds.getString(1), "67890");
    }

    @Test
    public void testGetJSPPath() {
        String jspPath = servlet.getJSPPath();
        assertEquals(jspPath, "/fs_fdx.jsp");
    }
}
