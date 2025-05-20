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
 * Defines the context data related to the pre-process consent retrieval data.
 */
@JsonTypeName("PreProcessConsentRetrievalData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PreProcessConsentRetrievalData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String consentId;
    private StoredBasicConsentResourceData consentResource;
    private Object requestHeaders;
    private String consentResourcePath;

    public PreProcessConsentRetrievalData() {
    }

    /**
     * The consent id
     **/
    public PreProcessConsentRetrievalData consentId(String consentId) {
        this.consentId = consentId;
        return this;
    }


    @ApiModelProperty(value = "The consent id")
    @JsonProperty("consentId")
    public String getConsentId() {
        return consentId;
    }

    @JsonProperty("consentId")
    public void setConsentId(String consentId) {
        this.consentId = consentId;
    }

    /**
     * Set the consent resource of the pre-process consent retrieval data.
     **/
    public PreProcessConsentRetrievalData consentResource(StoredBasicConsentResourceData consentResource) {
        this.consentResource = consentResource;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("consentResource")
    @Valid
    public StoredBasicConsentResourceData getConsentResource() {
        return consentResource;
    }

    @JsonProperty("consentResource")
    public void setConsentResource(StoredBasicConsentResourceData consentResource) {
        this.consentResource = consentResource;
    }

    /**
     * Request headers sent by the TPP. Filtered set of headers are sent to the external service.
     **/
    public PreProcessConsentRetrievalData requestHeaders(Object requestHeaders) {
        this.requestHeaders = requestHeaders;
        return this;
    }


    @ApiModelProperty(
            value = "Request headers sent by the TPP. Filtered set of headers are sent to the external service.")
    @JsonProperty("requestHeaders")
    public Object getRequestHeaders() {
        return requestHeaders;
    }

    @JsonProperty("requestHeaders")
    public void setRequestHeaders(Object requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    /**
     * Set the Resource url of the pre-process consent retrieval data.
     **/
    public PreProcessConsentRetrievalData consentResourcePath(String consentResourcePath) {
        this.consentResourcePath = consentResourcePath;
        return this;
    }


    @ApiModelProperty(value = "Resource url")
    @JsonProperty("consentResourcePath")
    public String getConsentResourcePath() {
        return consentResourcePath;
    }

    @JsonProperty("consentResourcePath")
    public void setConsentResourcePath(String consentResourcePath) {
        this.consentResourcePath = consentResourcePath;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PreProcessConsentRetrievalData preProcessConsentRetrievalData = (PreProcessConsentRetrievalData) o;
        return Objects.equals(this.consentId, preProcessConsentRetrievalData.consentId) &&
                Objects.equals(this.consentResource, preProcessConsentRetrievalData.consentResource) &&
                Objects.equals(this.requestHeaders, preProcessConsentRetrievalData.requestHeaders) &&
                Objects.equals(this.consentResourcePath, preProcessConsentRetrievalData.consentResourcePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consentId, consentResource, requestHeaders, consentResourcePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PreProcessConsentRetrievalData {\n");

        sb.append("    consentId: ").append(toIndentedString(consentId)).append("\n");
        sb.append("    consentResource: ").append(toIndentedString(consentResource)).append("\n");
        sb.append("    requestHeaders: ").append(toIndentedString(requestHeaders)).append("\n");
        sb.append("    consentResourcePath: ").append(toIndentedString(consentResourcePath)).append("\n");
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

