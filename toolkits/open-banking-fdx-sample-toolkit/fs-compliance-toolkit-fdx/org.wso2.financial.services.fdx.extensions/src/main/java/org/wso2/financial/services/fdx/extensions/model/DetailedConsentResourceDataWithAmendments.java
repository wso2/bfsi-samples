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
 * Defines the context data related to the detailed consent resource data with amendments.
 */
@JsonTypeName("DetailedConsentResourceDataWithAmendments")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class DetailedConsentResourceDataWithAmendments implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;
    private String status;
    private Long validityTime;
    private Boolean recurringIndicator;
    private Integer frequency;
    private Object receipt;
    private Object attributes;
    @Valid
    private List<@Valid Authorization> authorizations = new ArrayList<>();
    @Valid
    private List<@Valid AmendedAuthorization> amendments = new ArrayList<>();

    public DetailedConsentResourceDataWithAmendments() {
    }

    /**
     * Set the type of the detailed consent resource data with amendments
     **/
    public DetailedConsentResourceDataWithAmendments type(String type) {
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
     * Set the status of the detailed consent resource data with amendments
     **/
    public DetailedConsentResourceDataWithAmendments status(String status) {
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
     * Set the validity time of the detailed consent resource data with amendments
     **/
    public DetailedConsentResourceDataWithAmendments validityTime(Long validityTime) {
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
     * Set the recurring indicator of the detailed consent resource data with amendments
     **/
    public DetailedConsentResourceDataWithAmendments recurringIndicator(Boolean recurringIndicator) {
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
     * Set the frequency of the detailed consent resource data with amendments
     **/
    public DetailedConsentResourceDataWithAmendments frequency(Integer frequency) {
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
     * Set the receipt of the detailed consent resource data with amendments
     **/
    public DetailedConsentResourceDataWithAmendments receipt(Object receipt) {
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
     * Set the attributes of the detailed consent resource data with amendments
     **/
    public DetailedConsentResourceDataWithAmendments attributes(Object attributes) {
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
     * Set the authorizations of the detailed consent resource data with amendments
     **/
    public DetailedConsentResourceDataWithAmendments authorizations(List<@Valid Authorization> authorizations) {
        this.authorizations = authorizations;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("authorizations")
    @Valid
    public List<@Valid Authorization> getAuthorizations() {
        return authorizations;
    }

    @JsonProperty("authorizations")
    public void setAuthorizations(List<@Valid Authorization> authorizations) {
        this.authorizations = authorizations;
    }

    public DetailedConsentResourceDataWithAmendments addAuthorizationsItem(Authorization authorizationsItem) {
        if (this.authorizations == null) {
            this.authorizations = new ArrayList<>();
        }

        this.authorizations.add(authorizationsItem);
        return this;
    }

    public DetailedConsentResourceDataWithAmendments removeAuthorizationsItem(Authorization authorizationsItem) {
        if (authorizationsItem != null && this.authorizations != null) {
            this.authorizations.remove(authorizationsItem);
        }

        return this;
    }

    /**
     * Set the amendments of the detailed consent resource data with amendments
     **/
    public DetailedConsentResourceDataWithAmendments amendments(List<@Valid AmendedAuthorization> amendments) {
        this.amendments = amendments;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("amendments")
    @Valid
    public List<@Valid AmendedAuthorization> getAmendments() {
        return amendments;
    }

    @JsonProperty("amendments")
    public void setAmendments(List<@Valid AmendedAuthorization> amendments) {
        this.amendments = amendments;
    }

    public DetailedConsentResourceDataWithAmendments addAmendmentsItem(AmendedAuthorization amendmentsItem) {
        if (this.amendments == null) {
            this.amendments = new ArrayList<>();
        }

        this.amendments.add(amendmentsItem);
        return this;
    }

    public DetailedConsentResourceDataWithAmendments removeAmendmentsItem(AmendedAuthorization amendmentsItem) {
        if (amendmentsItem != null && this.amendments != null) {
            this.amendments.remove(amendmentsItem);
        }

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DetailedConsentResourceDataWithAmendments detailedConsentResourceDataWithAmendments =
                (DetailedConsentResourceDataWithAmendments) o;
        return Objects.equals(this.type, detailedConsentResourceDataWithAmendments.type) &&
                Objects.equals(this.status, detailedConsentResourceDataWithAmendments.status) &&
                Objects.equals(this.validityTime, detailedConsentResourceDataWithAmendments.validityTime) &&
                Objects.equals(this.recurringIndicator, detailedConsentResourceDataWithAmendments.recurringIndicator) &&
                Objects.equals(this.frequency, detailedConsentResourceDataWithAmendments.frequency) &&
                Objects.equals(this.receipt, detailedConsentResourceDataWithAmendments.receipt) &&
                Objects.equals(this.attributes, detailedConsentResourceDataWithAmendments.attributes) &&
                Objects.equals(this.authorizations, detailedConsentResourceDataWithAmendments.authorizations) &&
                Objects.equals(this.amendments, detailedConsentResourceDataWithAmendments.amendments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, status, validityTime, recurringIndicator, frequency, receipt, attributes,
                authorizations, amendments);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DetailedConsentResourceDataWithAmendments {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    validityTime: ").append(toIndentedString(validityTime)).append("\n");
        sb.append("    recurringIndicator: ").append(toIndentedString(recurringIndicator)).append("\n");
        sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
        sb.append("    receipt: ").append(toIndentedString(receipt)).append("\n");
        sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
        sb.append("    authorizations: ").append(toIndentedString(authorizations)).append("\n");
        sb.append("    amendments: ").append(toIndentedString(amendments)).append("\n");
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

