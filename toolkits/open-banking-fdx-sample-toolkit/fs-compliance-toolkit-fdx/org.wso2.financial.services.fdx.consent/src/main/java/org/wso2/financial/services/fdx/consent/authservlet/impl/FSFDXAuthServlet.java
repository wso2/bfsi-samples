package org.wso2.financial.services.fdx.consent.authservlet.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.financial.services.accelerator.consent.mgt.extensions.authservlet.FSAuthServletInterface;
import org.wso2.financial.services.accelerator.consent.mgt.extensions.common.ConsentExtensionConstants;
import org.wso2.financial.services.fdx.consent.common.FDXConsentExtensionConstants;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

/**
 * The servlet responsible for displaying the consent details in the auth UI
 * flow.
 */
public class FSFDXAuthServlet implements FSAuthServletInterface {

    @Override
    public Map<String, Object> updateRequestAttribute(HttpServletRequest request, JSONObject dataSet,
                                                      ResourceBundle resourceBundle) {

        Map<String, Object> returnMaps = new HashMap<>();

        // Sets "data_requested" that contains the human-readable scope-requested information
        Map<String, List<String>> dataRequestedJsonArray = getDataRequested(dataSet);
        returnMaps.put(ConsentExtensionConstants.DATA_REQUESTED, dataRequestedJsonArray);

        // read expiry date
        Map<String, String> expiryDateMap = readExpiryDate(dataSet);
        request.setAttribute(ConsentExtensionConstants.EXPIRATION_DATE,
                expiryDateMap.get(FDXConsentExtensionConstants.EXPIRATION_DATE_TIME));
        request.setAttribute(FDXConsentExtensionConstants.DURATION_PERIOD,
                expiryDateMap.get(FDXConsentExtensionConstants.DURATION_PERIOD));

        // add accounts list
        request.setAttribute(ConsentExtensionConstants.ACCOUNT_DATA, addAccountList(dataSet));
        request.setAttribute(ConsentExtensionConstants.CONSENT_TYPE, FDXConsentExtensionConstants.FDX_TYPE);

        JSONArray consentDataArray = dataSet.getJSONArray(FDXConsentExtensionConstants.CONSENT_DATA);
        JSONObject consentDataItem = consentDataArray.getJSONObject(0);
        request.setAttribute(ConsentExtensionConstants.REDIRECT_URI,
                consentDataItem.get(FDXConsentExtensionConstants.REDIRECT_URL));

        return returnMaps;
    }

    public Map<String, String> readExpiryDate(JSONObject dataSet) {
        Map<String, String> expiryDateMap = new HashMap<>();

        JSONArray consentDataArray = dataSet.getJSONArray(FDXConsentExtensionConstants.CONSENT_DATA);
        JSONObject consentDataItem = consentDataArray.getJSONObject(0); // Assuming first object

        JSONArray authorizationDetailsArray =
                consentDataItem.getJSONArray(FDXConsentExtensionConstants.AUTHORIZATION_DETAILS);
        JSONObject authorizationDetails = authorizationDetailsArray.getJSONObject(0); // Assuming first object inside
        // authorization_details

        int durationPeriod = authorizationDetails.getInt(FDXConsentExtensionConstants.DURATION_PERIOD);
        String expiryDateTimeStr = authorizationDetails.getString(FDXConsentExtensionConstants.EXPIRATION_DATE_TIME);

        // Parse ISO 8601 OffsetDateTime string to LocalDate
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(expiryDateTimeStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String expiryDate = offsetDateTime.toLocalDate().toString();

        expiryDateMap.put(FDXConsentExtensionConstants.DURATION_PERIOD, String.valueOf(durationPeriod));
        expiryDateMap.put(FDXConsentExtensionConstants.EXPIRATION_DATE_TIME, expiryDate);

        return expiryDateMap;
    }


    public Object addAccountList(JSONObject dataSet) {
        // add accounts data to a list
        List<Map<String, String>> accountData = new ArrayList<>();
        JSONArray accountsArray = dataSet.getJSONArray("accounts");
        for (int accountIndex = 0; accountIndex < accountsArray.length(); accountIndex++) {
            JSONObject object = accountsArray.getJSONObject(accountIndex);
            String accountId = object.getString(FDXConsentExtensionConstants.ACCOUNT_ID);
            String accountType = object.getString(FDXConsentExtensionConstants.ACCOUNT_TYPE);
            String displayName = object.optString(FDXConsentExtensionConstants.ACCOUNT_ID_DISPLAYABLE, accountId);
            Map<String, String> data = new HashMap<>();
            data.put(ConsentExtensionConstants.AUTH_ACCOUNT_ID, accountId);
            data.put(ConsentExtensionConstants.DISPLAY_NAME, displayName);
            data.put(FDXConsentExtensionConstants.ACCOUNT_TYPE, accountType);
            accountData.add(data);
        }
        return accountData;
    }

    public Map<String, List<String>> getDataRequested(JSONObject dataSet) {
        Map<String, List<String>> dataRequestedJsonArray = new HashMap<>();

        JSONArray consentDataArray = dataSet.getJSONArray(FDXConsentExtensionConstants.CONSENT_DATA);

        for (int i = 0; i < consentDataArray.length(); i++) {
            JSONObject consentDataItem = consentDataArray.getJSONObject(i);

            // Convert consentDataItem to Map
            Map<String, Object> consentDataItemMap = consentDataItem.toMap();

            // Extract 'data_requested' safely
            Object rawDataRequestedObj = consentDataItemMap.get(FDXConsentExtensionConstants.DATA_REQUESTED);

            if (rawDataRequestedObj instanceof Map) {
                Map<String, Object> rawDataRequested = (Map<String, Object>) rawDataRequestedObj;

                for (Map.Entry<String, Object> entry : rawDataRequested.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof List) { // JSONArray -> List<Object>
                        List<String> convertedList = ((List<?>) value).stream()
                                .map(Object::toString)
                                .collect(Collectors.toList());

                        // Merge instead of overwrite
                        dataRequestedJsonArray.merge(key, new ArrayList<>(convertedList), (oldList, newList) -> {
                            oldList.addAll(newList);
                            oldList = oldList.stream().distinct().collect(Collectors.toList());
                            return oldList;
                        });
                    }
                }
            }
        }

        return dataRequestedJsonArray;
    }


    @Override
    public Map<String, Object> updateSessionAttribute(HttpServletRequest request, JSONObject dataSet,
                                                      ResourceBundle resourceBundle) {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, Object> updateConsentData(HttpServletRequest httpServletRequest) {
        Map<String, Object> returnMaps = new HashMap<>();

        String[] accounts = httpServletRequest.getParameter(
                FDXConsentExtensionConstants.ACCOUNTS_ARRAY).split(":");
        returnMaps.put(FDXConsentExtensionConstants.ACCOUNT_IDS, new JSONArray(accounts));
        return returnMaps;
    }

    @Override
    public Map<String, String> updateConsentMetaData(HttpServletRequest request) {
        // Implementation here
        return null;
    }

    @Override
    public String getJSPPath() {
        return "/fs_fdx.jsp";
    }
}
