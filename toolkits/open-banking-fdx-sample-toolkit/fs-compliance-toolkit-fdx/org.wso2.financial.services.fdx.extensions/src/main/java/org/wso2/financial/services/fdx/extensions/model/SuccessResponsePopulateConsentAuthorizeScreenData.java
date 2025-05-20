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
 * Details of the context for SuccessResponsePopulateConsentAuthorizeScreen
 */
@JsonTypeName("SuccessResponsePopulateConsentAuthorizeScreenData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponsePopulateConsentAuthorizeScreenData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object consentData;
    private Object consumerData;

    public SuccessResponsePopulateConsentAuthorizeScreenData() {
    }

    /**
     * consent data to be populated on consent grant UI
     **/
    public SuccessResponsePopulateConsentAuthorizeScreenData consentData(Object consentData) {
        this.consentData = consentData;
        return this;
    }


    @ApiModelProperty(value = "consent data to be populated on consent grant UI")
    @JsonProperty("consentData")
    public Object getConsentData() {
        return consentData;
    }

    @JsonProperty("consentData")
    public void setConsentData(Object consentData) {
        this.consentData = consentData;
    }

    /**
     * Data provider&#39;s backend data
     **/
    public SuccessResponsePopulateConsentAuthorizeScreenData consumerData(Object consumerData) {
        this.consumerData = consumerData;
        return this;
    }


    @ApiModelProperty(value = "Data provider's backend data")
    @JsonProperty("consumerData")
    public Object getConsumerData() {
        return consumerData;
    }

    @JsonProperty("consumerData")
    public void setConsumerData(Object consumerData) {
        this.consumerData = consumerData;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuccessResponsePopulateConsentAuthorizeScreenData successResponsePopulateConsentAuthorizeScreenData =
                (SuccessResponsePopulateConsentAuthorizeScreenData) o;
        return Objects.equals(this.consentData, successResponsePopulateConsentAuthorizeScreenData.consentData) &&
                Objects.equals(this.consumerData, successResponsePopulateConsentAuthorizeScreenData.consumerData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consentData, consumerData);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponsePopulateConsentAuthorizeScreenData {\n");

        sb.append("    consentData: ").append(toIndentedString(consentData)).append("\n");
        sb.append("    consumerData: ").append(toIndentedString(consumerData)).append("\n");
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

