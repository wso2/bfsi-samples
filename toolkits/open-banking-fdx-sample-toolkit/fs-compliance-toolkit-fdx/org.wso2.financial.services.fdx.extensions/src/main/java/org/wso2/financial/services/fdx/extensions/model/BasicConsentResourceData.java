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

/**
 * Defines the context data related to the consent resource data.
 */
@JsonTypeName("BasicConsentResourceData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class BasicConsentResourceData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object receipt;
    private String type;
    private String status;
    private Integer frequency;
    private Long validityTime;
    private Boolean recurringIndicator;
    private Object attributes;

    public BasicConsentResourceData() {
    }

    /**
     * Set the receipt associated with the consent resource data.
     **/
    public BasicConsentResourceData receipt(Object receipt) {
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
     * Set the type of the consent resource data.
     **/
    public BasicConsentResourceData type(String type) {
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
     * Set the status of the consent resource data.
     **/
    public BasicConsentResourceData status(String status) {
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
     * Set the frequency of the consent resource data.
     **/
    public BasicConsentResourceData frequency(Integer frequency) {
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
     * Set the validity time of the consent resource data.
     **/
    public BasicConsentResourceData validityTime(Long validityTime) {
        this.validityTime = validityTime;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("validityTime")
    public Long getValidityTime() {
        return validityTime;
    }

    @JsonProperty("validityTime")
    public void setValidityTime(Long validityTime) {
        this.validityTime = validityTime;
    }

    /**
     * Set the recurring indicator of the consent resource data.
     **/
    public BasicConsentResourceData recurringIndicator(Boolean recurringIndicator) {
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
     * Set the attributes of the consent resource data.
     **/
    public BasicConsentResourceData attributes(Object attributes) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BasicConsentResourceData basicConsentResourceData = (BasicConsentResourceData) o;
        return Objects.equals(this.receipt, basicConsentResourceData.receipt) &&
                Objects.equals(this.type, basicConsentResourceData.type) &&
                Objects.equals(this.status, basicConsentResourceData.status) &&
                Objects.equals(this.frequency, basicConsentResourceData.frequency) &&
                Objects.equals(this.validityTime, basicConsentResourceData.validityTime) &&
                Objects.equals(this.recurringIndicator, basicConsentResourceData.recurringIndicator) &&
                Objects.equals(this.attributes, basicConsentResourceData.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receipt, type, status, frequency, validityTime, recurringIndicator, attributes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BasicConsentResourceData {\n");

        sb.append("    receipt: ").append(toIndentedString(receipt)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
        sb.append("    validityTime: ").append(toIndentedString(validityTime)).append("\n");
        sb.append("    recurringIndicator: ").append(toIndentedString(recurringIndicator)).append("\n");
        sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
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

