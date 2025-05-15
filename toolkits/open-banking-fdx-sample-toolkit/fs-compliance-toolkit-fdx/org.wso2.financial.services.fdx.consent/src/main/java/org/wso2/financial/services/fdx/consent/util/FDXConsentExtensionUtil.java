package org.wso2.financial.services.fdx.consent.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Utility class for handling FDX Consent Extension related operations.
 */
public class FDXConsentExtensionUtil {
    /**
     * Retrieves an attribute from the request scope first and falls back to the session scope
     * if not found in the request. If the attribute is not found in either scope, a default
     * value is returned.
     *
     * @param request       the HttpServletRequest object to check for the attribute.
     * @param session       the HttpSession object to check for the attribute if not found in the request.
     * @param attributeName the name of the attribute to retrieve.
     * @param defaultValue  the default value to return if the attribute is not found in both the request and session.
     * @return the value of the attribute as an Object, or the default value if the attribute is not found.
     */
    public static Object getAttribute(HttpServletRequest request, HttpSession session, String attributeName,
                                      Object defaultValue) {
        // Check in the request first
        Object requestAttribute = request.getAttribute(attributeName);
        if (requestAttribute != null) {
            return requestAttribute;
        }

        String requestParameter = request.getParameter(attributeName);
        if (requestParameter != null) {
            return requestParameter;
        }

        // Fallback to session if not found in the request
        Object sessionAttribute = session.getAttribute(attributeName);
        if (sessionAttribute != null) {
            return sessionAttribute;
        }

        // Return the default value if not found
        return defaultValue;
    }
}
