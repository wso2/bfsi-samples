/**
 * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 * <p>
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.financial.services.fdx.identity.authorize.model;

import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetail;
import org.wso2.financial.services.fdx.identity.authorize.commons.FDXIdentityCommonConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents the FDX Authorization Details.
 * It contains information about the type of authorization and the consent request details.
 */
public class FDXAuthorizationDetails {
    private String type;
    private ConsentRequest consentRequest;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ConsentRequest getConsentRequest() {
        return consentRequest;
    }

    public void setConsentRequest(ConsentRequest consentRequest) {
        this.consentRequest = consentRequest;
    }

    /**
     * Represents the consent request details.
     * It contains information about the duration type, duration period, lookback period, and resources.
     */
    public static class ConsentRequest {
        private String durationType;
        private int durationPeriod;
        private int lookbackPeriod;
        private List<Resource> resources;

        public String getDurationType() {
            return durationType;
        }

        public void setDurationType(String durationType) {
            this.durationType = durationType;
        }

        public int getDurationPeriod() {
            return durationPeriod;
        }

        public void setDurationPeriod(int durationPeriod) {
            this.durationPeriod = durationPeriod;
        }

        public int getLookbackPeriod() {
            return lookbackPeriod;
        }

        public void setLookbackPeriod(int lookbackPeriod) {
            this.lookbackPeriod = lookbackPeriod;
        }

        public List<Resource> getResources() {
            return resources;
        }

        public void setResources(List<Resource> resources) {
            this.resources = resources;
        }
    }

    /**
     * Represents a resource in the consent request.
     * It contains information about the resource type and the data clusters associated with it.
     */
    public static class Resource {
        private String resourceType;
        private List<String> dataClusters;

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public List<String> getDataClusters() {
            return dataClusters;
        }

        public void setDataClusters(List<String> dataClusters) {
            this.dataClusters = dataClusters;
        }
    }

    /**
     * Parses the FDX Authorization Details from a map.
     *
     * @param authorizationDetails The map containing the FDX authorization details.
     * @return The parsed FDXAuthorizationDetails object, or null if the map is invalid.
     */
    @SuppressWarnings("unchecked")
    public static FDXAuthorizationDetails parseFDXAuthorizationDetails(AuthorizationDetail authorizationDetails) {
        if (authorizationDetails == null || !"fdx_v1.0".equals(authorizationDetails.getType())) {
            return null;
        }

        FDXAuthorizationDetails details = new FDXAuthorizationDetails();
        details.setType(authorizationDetails.getType());

        Map<String, Object> authDetails = authorizationDetails.getDetails();

        Map<String, Object> consentRequestMap =
                (Map<String, Object>) authDetails.get(FDXIdentityCommonConstants.CONSENT_REQUEST);
        if (consentRequestMap == null) {
            return null;
        }

        FDXAuthorizationDetails.ConsentRequest consentRequest = new FDXAuthorizationDetails.ConsentRequest();
        consentRequest.setDurationType((String) consentRequestMap.get(FDXIdentityCommonConstants.DURATION_TYPE));
        consentRequest.setDurationPeriod((Integer) consentRequestMap.get(FDXIdentityCommonConstants.DURATION_PERIOD));
        consentRequest.setLookbackPeriod((Integer) consentRequestMap.get(FDXIdentityCommonConstants.LOOKBACK_PERIOD));

        List<Map<String, Object>> resourcesList =
                (List<Map<String, Object>>) consentRequestMap.get(FDXIdentityCommonConstants.RESOURCES);
        if (resourcesList != null) {
            List<FDXAuthorizationDetails.Resource> parsedResources = new ArrayList<>();
            for (Map<String, Object> resourceMap : resourcesList) {
                FDXAuthorizationDetails.Resource resource = new FDXAuthorizationDetails.Resource();
                resource.setResourceType((String) resourceMap.get(FDXIdentityCommonConstants.RESOURCE_TYPE));

                List<String> dataClusters = (List<String>) resourceMap.get(FDXIdentityCommonConstants.DATA_CLUSTERS);
                resource.setDataClusters(dataClusters);

                parsedResources.add(resource);
            }
            consentRequest.setResources(parsedResources);
        }

        details.setConsentRequest(consentRequest);
        return details;
    }

}
