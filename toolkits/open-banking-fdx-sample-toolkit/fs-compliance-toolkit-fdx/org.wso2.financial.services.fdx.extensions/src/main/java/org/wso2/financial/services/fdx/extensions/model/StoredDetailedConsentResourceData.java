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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Defines the context related to the stored detailed consent resource data.
 */
@JsonTypeName("StoredDetailedConsentResourceData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class StoredDetailedConsentResourceData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private Object receipt;
    private Integer createdTime;
    private Integer updatedTime;
    private String clientId;
    private String type;
    private String status;
    private Integer frequency;
    private Integer validityTime;
    private Boolean recurringIndicator;
    private Object attributes;
    @Valid
    private List<@Valid StoredAuthorization> authorizations = new ArrayList<>();
    private String fileContent;

    public StoredDetailedConsentResourceData() {
    }

    /**
     * Set the unique identifier for the stored detailed consent resource data.
     **/
    public StoredDetailedConsentResourceData id(String id) {
        this.id = id;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set the receipt associated with the stored detailed consent resource data.
     **/
    public StoredDetailedConsentResourceData receipt(Object receipt) {
        this.receipt = receipt;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("receipt")
    public Object getReceipt() {
        return receipt;
    }

    @JsonProperty("receipt")
    public void setReceipt(Object receipt) {
        this.receipt = receipt;
    }

    /**
     * Set the time when the stored detailed consent resource data was created.
     **/
    public StoredDetailedConsentResourceData createdTime(Integer createdTime) {
        this.createdTime = createdTime;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("createdTime")
    public Integer getCreatedTime() {
        return createdTime;
    }

    @JsonProperty("createdTime")
    public void setCreatedTime(Integer createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * Set the time when the stored detailed consent resource data was last updated.
     **/
    public StoredDetailedConsentResourceData updatedTime(Integer updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("updatedTime")
    public Integer getUpdatedTime() {
        return updatedTime;
    }

    @JsonProperty("updatedTime")
    public void setUpdatedTime(Integer updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * Set the client ID associated with the stored detailed consent resource data.
     **/
    public StoredDetailedConsentResourceData clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("clientId")
    public String getClientId() {
        return clientId;
    }

    @JsonProperty("clientId")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Set the type of the stored detailed consent resource data.
     **/
    public StoredDetailedConsentResourceData type(String type) {
        this.type = type;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Set the status of the stored detailed consent resource data.
     **/
    public StoredDetailedConsentResourceData status(String status) {
        this.status = status;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Set the frequency of the stored detailed consent resource data.
     **/
    public StoredDetailedConsentResourceData frequency(Integer frequency) {
        this.frequency = frequency;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("frequency")
    public Integer getFrequency() {
        return frequency;
    }

    @JsonProperty("frequency")
    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    /**
     * Set the validity time of the stored detailed consent resource data.
     **/
    public StoredDetailedConsentResourceData validityTime(Integer validityTime) {
        this.validityTime = validityTime;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("validityTime")
    public Integer getValidityTime() {
        return validityTime;
    }

    @JsonProperty("validityTime")
    public void setValidityTime(Integer validityTime) {
        this.validityTime = validityTime;
    }

    /**
     * Set the recurring indicator of the stored detailed consent resource data.
     **/
    public StoredDetailedConsentResourceData recurringIndicator(Boolean recurringIndicator) {
        this.recurringIndicator = recurringIndicator;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("recurringIndicator")
    public Boolean getRecurringIndicator() {
        return recurringIndicator;
    }

    @JsonProperty("recurringIndicator")
    public void setRecurringIndicator(Boolean recurringIndicator) {
        this.recurringIndicator = recurringIndicator;
    }

    /**
     * Set the attributes of the stored detailed consent resource data.
     **/
    public StoredDetailedConsentResourceData attributes(Object attributes) {
        this.attributes = attributes;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("attributes")
    public Object getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    /**
     * Set the list of authorizations associated with the stored detailed consent resource data.
     **/
    public StoredDetailedConsentResourceData authorizations(List<@Valid StoredAuthorization> authorizations) {
        this.authorizations = authorizations;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("authorizations")
    @Valid
    public List<@Valid StoredAuthorization> getAuthorizations() {
        return authorizations;
    }

    @JsonProperty("authorizations")
    public void setAuthorizations(List<@Valid StoredAuthorization> authorizations) {
        this.authorizations = authorizations;
    }

    public StoredDetailedConsentResourceData addAuthorizationsItem(StoredAuthorization authorizationsItem) {
        if (this.authorizations == null) {
            this.authorizations = new ArrayList<>();
        }

        this.authorizations.add(authorizationsItem);
        return this;
    }

    public StoredDetailedConsentResourceData removeAuthorizationsItem(StoredAuthorization authorizationsItem) {
        if (authorizationsItem != null && this.authorizations != null) {
            this.authorizations.remove(authorizationsItem);
        }

        return this;
    }

    /**
     * Set the content of the uploaded file.
     **/
    public StoredDetailedConsentResourceData fileContent(String fileContent) {
        this.fileContent = fileContent;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("fileContent")
    public String getFileContent() {
        return fileContent;
    }

    @JsonProperty("fileContent")
    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoredDetailedConsentResourceData storedDetailedConsentResourceData = (StoredDetailedConsentResourceData) o;
        return Objects.equals(this.id, storedDetailedConsentResourceData.id) &&
                Objects.equals(this.receipt, storedDetailedConsentResourceData.receipt) &&
                Objects.equals(this.createdTime, storedDetailedConsentResourceData.createdTime) &&
                Objects.equals(this.updatedTime, storedDetailedConsentResourceData.updatedTime) &&
                Objects.equals(this.clientId, storedDetailedConsentResourceData.clientId) &&
                Objects.equals(this.type, storedDetailedConsentResourceData.type) &&
                Objects.equals(this.status, storedDetailedConsentResourceData.status) &&
                Objects.equals(this.frequency, storedDetailedConsentResourceData.frequency) &&
                Objects.equals(this.validityTime, storedDetailedConsentResourceData.validityTime) &&
                Objects.equals(this.recurringIndicator, storedDetailedConsentResourceData.recurringIndicator) &&
                Objects.equals(this.attributes, storedDetailedConsentResourceData.attributes) &&
                Objects.equals(this.authorizations, storedDetailedConsentResourceData.authorizations) &&
                Objects.equals(this.fileContent, storedDetailedConsentResourceData.fileContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, receipt, createdTime, updatedTime, clientId, type, status, frequency, validityTime,
                recurringIndicator, attributes, authorizations, fileContent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StoredDetailedConsentResourceData {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    receipt: ").append(toIndentedString(receipt)).append("\n");
        sb.append("    createdTime: ").append(toIndentedString(createdTime)).append("\n");
        sb.append("    updatedTime: ").append(toIndentedString(updatedTime)).append("\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
        sb.append("    validityTime: ").append(toIndentedString(validityTime)).append("\n");
        sb.append("    recurringIndicator: ").append(toIndentedString(recurringIndicator)).append("\n");
        sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
        sb.append("    authorizations: ").append(toIndentedString(authorizations)).append("\n");
        sb.append("    fileContent: ").append(toIndentedString(fileContent)).append("\n");
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

