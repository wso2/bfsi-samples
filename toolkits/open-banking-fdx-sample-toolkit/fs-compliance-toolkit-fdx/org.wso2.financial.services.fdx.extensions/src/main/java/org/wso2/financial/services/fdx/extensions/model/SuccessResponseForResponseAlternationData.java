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
 * Details of the context for SuccessResponseForResponseAlternation
 */
@JsonTypeName("SuccessResponseForResponseAlternationData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponseForResponseAlternationData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object responseHeaders;
    private Object modifiedResponse;

    public SuccessResponseForResponseAlternationData() {
    }

    /**
     * Headers to be included in the response.
     **/
    public SuccessResponseForResponseAlternationData responseHeaders(Object responseHeaders) {
        this.responseHeaders = responseHeaders;
        return this;
    }


    @ApiModelProperty(value = "Headers to be included in the response.")
    @JsonProperty("responseHeaders")
    public Object getResponseHeaders() {
        return responseHeaders;
    }

    @JsonProperty("responseHeaders")
    public void setResponseHeaders(Object responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    /**
     * Generated custom response body
     **/
    public SuccessResponseForResponseAlternationData modifiedResponse(Object modifiedResponse) {
        this.modifiedResponse = modifiedResponse;
        return this;
    }


    @ApiModelProperty(value = "Generated custom response body")
    @JsonProperty("modifiedResponse")
    public Object getModifiedResponse() {
        return modifiedResponse;
    }

    @JsonProperty("modifiedResponse")
    public void setModifiedResponse(Object modifiedResponse) {
        this.modifiedResponse = modifiedResponse;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuccessResponseForResponseAlternationData successResponseForResponseAlternationData =
                (SuccessResponseForResponseAlternationData) o;
        return Objects.equals(this.responseHeaders, successResponseForResponseAlternationData.responseHeaders) &&
                Objects.equals(this.modifiedResponse, successResponseForResponseAlternationData.modifiedResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseHeaders, modifiedResponse);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponseForResponseAlternationData {\n");

        sb.append("    responseHeaders: ").append(toIndentedString(responseHeaders)).append("\n");
        sb.append("    modifiedResponse: ").append(toIndentedString(modifiedResponse)).append("\n");
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

