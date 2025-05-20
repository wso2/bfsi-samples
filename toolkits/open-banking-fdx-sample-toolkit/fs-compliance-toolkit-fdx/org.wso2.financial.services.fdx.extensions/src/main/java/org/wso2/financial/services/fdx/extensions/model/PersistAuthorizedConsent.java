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
 * Defines the context related to the persist authorized consent.
 */
@JsonTypeName("PersistAuthorizedConsent")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PersistAuthorizedConsent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String consentId;
    private Boolean isApproved;
    private UserGrantedData userGrantedData;
    private StoredDetailedConsentResourceData consentResource;

    public PersistAuthorizedConsent() {
    }

    /**
     * Set the unique identifier for the consent.
     **/
    public PersistAuthorizedConsent consentId(String consentId) {
        this.consentId = consentId;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("consentId")
    public String getConsentId() {
        return consentId;
    }

    @JsonProperty("consentId")
    public void setConsentId(String consentId) {
        this.consentId = consentId;
    }

    /**
     * Set the approval status of the consent.
     **/
    public PersistAuthorizedConsent isApproved(Boolean isApproved) {
        this.isApproved = isApproved;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("isApproved")
    public Boolean getIsApproved() {
        return isApproved;
    }

    @JsonProperty("isApproved")
    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    /**
     * Set the user granted data associated with the consent.
     **/
    public PersistAuthorizedConsent userGrantedData(UserGrantedData userGrantedData) {
        this.userGrantedData = userGrantedData;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("userGrantedData")
    @Valid
    public UserGrantedData getUserGrantedData() {
        return userGrantedData;
    }

    @JsonProperty("userGrantedData")
    public void setUserGrantedData(UserGrantedData userGrantedData) {
        this.userGrantedData = userGrantedData;
    }

    /**
     * Set the consent resource associated with the consent.
     **/
    public PersistAuthorizedConsent consentResource(StoredDetailedConsentResourceData consentResource) {
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
        PersistAuthorizedConsent persistAuthorizedConsent = (PersistAuthorizedConsent) o;
        return Objects.equals(this.consentId, persistAuthorizedConsent.consentId) &&
                Objects.equals(this.isApproved, persistAuthorizedConsent.isApproved) &&
                Objects.equals(this.userGrantedData, persistAuthorizedConsent.userGrantedData) &&
                Objects.equals(this.consentResource, persistAuthorizedConsent.consentResource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consentId, isApproved, userGrantedData, consentResource);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PersistAuthorizedConsent {\n");

        sb.append("    consentId: ").append(toIndentedString(consentId)).append("\n");
        sb.append("    isApproved: ").append(toIndentedString(isApproved)).append("\n");
        sb.append("    userGrantedData: ").append(toIndentedString(userGrantedData)).append("\n");
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

