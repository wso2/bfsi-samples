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
 * Defines the context related to the request for enrich file upload response.
 */
@JsonTypeName("RequestForEnrichFileUploadResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class RequestForEnrichFileUploadResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String consentId;
    private String fileUploadCreatedTime;

    public RequestForEnrichFileUploadResponse() {
    }

    /**
     * To identify consent.
     **/
    public RequestForEnrichFileUploadResponse consentId(String consentId) {
        this.consentId = consentId;
        return this;
    }


    @ApiModelProperty(value = "To identify consent.")
    @JsonProperty("consentId")
    public String getConsentId() {
        return consentId;
    }

    @JsonProperty("consentId")
    public void setConsentId(String consentId) {
        this.consentId = consentId;
    }

    /**
     * Timestamp which the file was stored in the database.
     **/
    public RequestForEnrichFileUploadResponse fileUploadCreatedTime(String fileUploadCreatedTime) {
        this.fileUploadCreatedTime = fileUploadCreatedTime;
        return this;
    }


    @ApiModelProperty(value = "Timestamp which the file was stored in the database.")
    @JsonProperty("fileUploadCreatedTime")
    public String getFileUploadCreatedTime() {
        return fileUploadCreatedTime;
    }

    @JsonProperty("fileUploadCreatedTime")
    public void setFileUploadCreatedTime(String fileUploadCreatedTime) {
        this.fileUploadCreatedTime = fileUploadCreatedTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestForEnrichFileUploadResponse requestForEnrichFileUploadResponse = (RequestForEnrichFileUploadResponse) o;
        return Objects.equals(this.consentId, requestForEnrichFileUploadResponse.consentId) &&
                Objects.equals(this.fileUploadCreatedTime, requestForEnrichFileUploadResponse.fileUploadCreatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consentId, fileUploadCreatedTime);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RequestForEnrichFileUploadResponse {\n");

        sb.append("    consentId: ").append(toIndentedString(consentId)).append("\n");
        sb.append("    fileUploadCreatedTime: ").append(toIndentedString(fileUploadCreatedTime)).append("\n");
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

