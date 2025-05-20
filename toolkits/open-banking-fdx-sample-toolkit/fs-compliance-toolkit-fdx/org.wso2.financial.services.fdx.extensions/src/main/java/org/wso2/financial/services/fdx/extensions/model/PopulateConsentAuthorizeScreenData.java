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

package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Defines the context data related to the populate consent authorize screen data.
 */
@JsonTypeName("PopulateConsentAuthorizeScreenData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PopulateConsentAuthorizeScreenData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String consentId;
    private String userId;
    private Object requestParameters;
    private StoredDetailedConsentResourceData consentResource;

    public PopulateConsentAuthorizeScreenData() {
    }

    /**
     * Set the unique consent id for the populate consent authorize screen data.
     **/
    public PopulateConsentAuthorizeScreenData consentId(String consentId) {
        this.consentId = consentId;
        return this;
    }


    @ApiModelProperty(example = "An UUID", value = "")
    @JsonProperty("consentId")
    public String getConsentId() {
        return consentId;
    }

    @JsonProperty("consentId")
    public void setConsentId(String consentId) {
        this.consentId = consentId;
    }

    /**
     * Set the unique user id for the populate consent authorize screen data.
     **/
    public PopulateConsentAuthorizeScreenData userId(String userId) {
        this.userId = userId;
        return this;
    }


    @ApiModelProperty(example = "Username", value = "")
    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Custom object with request parameters
     **/
    public PopulateConsentAuthorizeScreenData requestParameters(Object requestParameters) {
        this.requestParameters = requestParameters;
        return this;
    }


    @ApiModelProperty(value = "Custom object with request parameters")
    @JsonProperty("requestParameters")
    public Object getRequestParameters() {
        return requestParameters;
    }

    @JsonProperty("requestParameters")
    public void setRequestParameters(Object requestParameters) {
        this.requestParameters = requestParameters;
    }

    /**
     * Set the consent resource data for the populate consent authorize screen data.
     **/
    public PopulateConsentAuthorizeScreenData consentResource(StoredDetailedConsentResourceData consentResource) {
        this.consentResource = consentResource;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("consentResource")
    @Valid
    public StoredDetailedConsentResourceData getConsentResource() {
        return consentResource;
    }

    @JsonProperty("consentResource")
    public void setConsentResource(StoredDetailedConsentResourceData consentResource) {
        this.consentResource = consentResource;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PopulateConsentAuthorizeScreenData populateConsentAuthorizeScreenData = (PopulateConsentAuthorizeScreenData) o;
        return Objects.equals(this.consentId, populateConsentAuthorizeScreenData.consentId) &&
                Objects.equals(this.userId, populateConsentAuthorizeScreenData.userId) &&
                Objects.equals(this.requestParameters, populateConsentAuthorizeScreenData.requestParameters) &&
                Objects.equals(this.consentResource, populateConsentAuthorizeScreenData.consentResource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consentId, userId, requestParameters, consentResource);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PopulateConsentAuthorizeScreenData {\n");

        sb.append("    consentId: ").append(toIndentedString(consentId)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    requestParameters: ").append(toIndentedString(requestParameters)).append("\n");
        sb.append("    consentResource: ").append(toIndentedString(consentResource)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


}

