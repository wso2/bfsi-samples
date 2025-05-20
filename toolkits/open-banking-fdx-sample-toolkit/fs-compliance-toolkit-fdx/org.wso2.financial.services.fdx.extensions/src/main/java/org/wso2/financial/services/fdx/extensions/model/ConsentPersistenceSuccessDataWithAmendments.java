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
 * Defines the context data related to the consent persistence success data with amendments.
 */
@JsonTypeName("ConsentPersistenceSuccessDataWithAmendments")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class ConsentPersistenceSuccessDataWithAmendments implements Serializable {
    private static final long serialVersionUID = 1L;
    private String consentType;
    private String consentStatus;
    private Long validityTime;
    private Boolean recurringIndicator;
    private Integer frequency;
    private Object consentPayload;
    @Valid
    private List<@Valid Authorization> authorizations = new ArrayList<>();
    @Valid
    private List<@Valid AmendedAuthorization> amendments = new ArrayList<>();

    public ConsentPersistenceSuccessDataWithAmendments() {
    }

    /**
     * Set the consent type of the consent persistence success data with amendments
     **/
    public ConsentPersistenceSuccessDataWithAmendments consentType(String consentType) {
        this.consentType = consentType;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("consentType")
    public String getConsentType() {
        return consentType;
    }

    @JsonProperty("consentType")
    public void setConsentType(String consentType) {
        this.consentType = consentType;
    }

    /**
     * Set the consent status of the consent persistence success data with amendments
     **/
    public ConsentPersistenceSuccessDataWithAmendments consentStatus(String consentStatus) {
        this.consentStatus = consentStatus;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("consentStatus")
    public String getConsentStatus() {
        return consentStatus;
    }

    @JsonProperty("consentStatus")
    public void setConsentStatus(String consentStatus) {
        this.consentStatus = consentStatus;
    }

    /**
     * Set the validity time of the consent persistence success data with amendments
     **/
    public ConsentPersistenceSuccessDataWithAmendments validityTime(Long validityTime) {
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
     * Set the recurring indicator of the consent persistence success data with amendments
     **/
    public ConsentPersistenceSuccessDataWithAmendments recurringIndicator(Boolean recurringIndicator) {
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
     * Set the frequency of the consent persistence success data with amendments
     **/
    public ConsentPersistenceSuccessDataWithAmendments frequency(Integer frequency) {
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
     * Set the consent payload of the consent persistence success data with amendments
     **/
    public ConsentPersistenceSuccessDataWithAmendments consentPayload(Object consentPayload) {
        this.consentPayload = consentPayload;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("consentPayload")
    public Object getConsentPayload() {
        return consentPayload;
    }

    @JsonProperty("consentPayload")
    public void setConsentPayload(Object consentPayload) {
        this.consentPayload = consentPayload;
    }

    /**
     * Set the authorizations of the consent persistence success data with amendments
     **/
    public ConsentPersistenceSuccessDataWithAmendments authorizations(List<@Valid Authorization> authorizations) {
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

    public ConsentPersistenceSuccessDataWithAmendments addAuthorizationsItem(Authorization authorizationsItem) {
        if (this.authorizations == null) {
            this.authorizations = new ArrayList<>();
        }

        this.authorizations.add(authorizationsItem);
        return this;
    }

    public ConsentPersistenceSuccessDataWithAmendments removeAuthorizationsItem(Authorization authorizationsItem) {
        if (authorizationsItem != null && this.authorizations != null) {
            this.authorizations.remove(authorizationsItem);
        }

        return this;
    }

    /**
     * Set the amendments of the consent persistence success data with amendments
     **/
    public ConsentPersistenceSuccessDataWithAmendments amendments(List<@Valid AmendedAuthorization> amendments) {
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

    public ConsentPersistenceSuccessDataWithAmendments addAmendmentsItem(AmendedAuthorization amendmentsItem) {
        if (this.amendments == null) {
            this.amendments = new ArrayList<>();
        }

        this.amendments.add(amendmentsItem);
        return this;
    }

    public ConsentPersistenceSuccessDataWithAmendments removeAmendmentsItem(AmendedAuthorization amendmentsItem) {
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
        ConsentPersistenceSuccessDataWithAmendments consentPersistenceSuccessDataWithAmendments =
                (ConsentPersistenceSuccessDataWithAmendments) o;
        return Objects.equals(this.consentType, consentPersistenceSuccessDataWithAmendments.consentType) &&
                Objects.equals(this.consentStatus, consentPersistenceSuccessDataWithAmendments.consentStatus) &&
                Objects.equals(this.validityTime, consentPersistenceSuccessDataWithAmendments.validityTime) &&
                Objects.equals(this.recurringIndicator,
                        consentPersistenceSuccessDataWithAmendments.recurringIndicator) &&
                Objects.equals(this.frequency, consentPersistenceSuccessDataWithAmendments.frequency) &&
                Objects.equals(this.consentPayload, consentPersistenceSuccessDataWithAmendments.consentPayload) &&
                Objects.equals(this.authorizations, consentPersistenceSuccessDataWithAmendments.authorizations) &&
                Objects.equals(this.amendments, consentPersistenceSuccessDataWithAmendments.amendments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consentType, consentStatus, validityTime, recurringIndicator, frequency, consentPayload,
                authorizations, amendments);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentPersistenceSuccessDataWithAmendments {\n");

        sb.append("    consentType: ").append(toIndentedString(consentType)).append("\n");
        sb.append("    consentStatus: ").append(toIndentedString(consentStatus)).append("\n");
        sb.append("    validityTime: ").append(toIndentedString(validityTime)).append("\n");
        sb.append("    recurringIndicator: ").append(toIndentedString(recurringIndicator)).append("\n");
        sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
        sb.append("    consentPayload: ").append(toIndentedString(consentPayload)).append("\n");
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

