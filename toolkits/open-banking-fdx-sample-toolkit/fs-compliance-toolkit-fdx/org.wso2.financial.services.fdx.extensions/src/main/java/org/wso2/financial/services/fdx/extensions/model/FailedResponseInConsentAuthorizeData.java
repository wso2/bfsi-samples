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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Defines the context data related to the failed response in consent authorize data.
 */
@JsonTypeName("FailedResponseInConsentAuthorizeData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class FailedResponseInConsentAuthorizeData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private String newConsentStatus;

    public FailedResponseInConsentAuthorizeData() {
    }

    @JsonCreator
    public FailedResponseInConsentAuthorizeData(
            @JsonProperty(required = true, value = "errorMessage") String errorMessage
    ) {
        this.errorMessage = errorMessage;
    }

    /**
     * Error message to be displayed in the URL
     **/
    public FailedResponseInConsentAuthorizeData errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }


    @ApiModelProperty(required = true, value = "Error message to be displayed in the URL")
    @JsonProperty(required = true, value = "errorMessage")
    @NotNull
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty(required = true, value = "errorMessage")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * New consent status to be set to the consent
     **/
    public FailedResponseInConsentAuthorizeData newConsentStatus(String newConsentStatus) {
        this.newConsentStatus = newConsentStatus;
        return this;
    }


    @ApiModelProperty(value = "New consent status to be set to the consent")
    @JsonProperty("newConsentStatus")
    public String getNewConsentStatus() {
        return newConsentStatus;
    }

    @JsonProperty("newConsentStatus")
    public void setNewConsentStatus(String newConsentStatus) {
        this.newConsentStatus = newConsentStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FailedResponseInConsentAuthorizeData failedResponseInConsentAuthorizeData =
                (FailedResponseInConsentAuthorizeData) o;
        return Objects.equals(this.errorMessage, failedResponseInConsentAuthorizeData.errorMessage) &&
                Objects.equals(this.newConsentStatus, failedResponseInConsentAuthorizeData.newConsentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage, newConsentStatus);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FailedResponseInConsentAuthorizeData {\n");

        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
        sb.append("    newConsentStatus: ").append(toIndentedString(newConsentStatus)).append("\n");
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

