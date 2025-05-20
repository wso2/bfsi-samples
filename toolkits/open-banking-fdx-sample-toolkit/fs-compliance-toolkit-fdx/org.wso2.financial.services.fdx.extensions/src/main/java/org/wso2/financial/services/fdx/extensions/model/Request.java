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
 * Defines the context related to the request.
 */
@JsonTypeName("Request")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object consentInitiationData;
    private Object requestHeaders;
    private String consentResourcePath;

    public Request() {
    }

    /**
     * The initiation payload used by third parties which includes detailed information on data access request.
     **/
    public Request consentInitiationData(Object consentInitiationData) {
        this.consentInitiationData = consentInitiationData;
        return this;
    }


    @ApiModelProperty(
            value = "The initiation payload used by third parties which includes detailed information on data access " +
                    "request.")
    @JsonProperty("consentInitiationData")
    public Object getConsentInitiationData() {
        return consentInitiationData;
    }

    @JsonProperty("consentInitiationData")
    public void setConsentInitiationData(Object consentInitiationData) {
        this.consentInitiationData = consentInitiationData;
    }

    /**
     * Request headers sent by the TPP. Filtered set of headers are sent to the external service.
     **/
    public Request requestHeaders(Object requestHeaders) {
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
     * To identify requested consent type
     **/
    public Request consentResourcePath(String consentResourcePath) {
        this.consentResourcePath = consentResourcePath;
        return this;
    }


    @ApiModelProperty(value = "To identify requested consent type")
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
        Request request = (Request) o;
        return Objects.equals(this.consentInitiationData, request.consentInitiationData) &&
                Objects.equals(this.requestHeaders, request.requestHeaders) &&
                Objects.equals(this.consentResourcePath, request.consentResourcePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consentInitiationData, requestHeaders, consentResourcePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Request {\n");

        sb.append("    consentInitiationData: ").append(toIndentedString(consentInitiationData)).append("\n");
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

