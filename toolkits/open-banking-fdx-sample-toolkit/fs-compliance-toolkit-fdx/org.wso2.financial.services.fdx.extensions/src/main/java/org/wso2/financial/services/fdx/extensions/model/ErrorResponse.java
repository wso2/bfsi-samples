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
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Defines the context related to the error response.
 */
@JsonTypeName("ErrorResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private StatusEnum status;
    private Object data;
    public ErrorResponse() {
    }

    @JsonCreator
    public ErrorResponse(
            @JsonProperty(required = true, value = "status") StatusEnum status,
            @JsonProperty(required = true, value = "data") Object data
    ) {
        this.status = status;
        this.data = data;
    }

    @ApiModelProperty(required = true,
            value = "Indicates the outcome of the request. For an error operation, this should be set to ERROR.")
    @JsonProperty(required = true, value = "status")
    @NotNull
    public StatusEnum getStatus() {
        return status;
    }

    @JsonProperty(required = true, value = "status")
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
     * Indicates the outcome of the request. For an error operation, this should be set to ERROR.
     **/
    public ErrorResponse status(StatusEnum status) {
        this.status = status;
        return this;
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

    /**
     * :\&quot;Custom error object to response back\&quot;
     **/
    public ErrorResponse data(Object data) {
        this.data = data;
        return this;
    }


    @ApiModelProperty(required = true, value = ":\"Custom error object to response back\"")
    @JsonProperty(required = true, value = "data")
    @NotNull
    public Object getData() {
        return data;
    }

    @JsonProperty(required = true, value = "data")
    public void setData(Object data) {
        this.data = data;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorResponse errorResponse = (ErrorResponse) o;
        return Objects.equals(this.status, errorResponse.status) &&
                Objects.equals(this.data, errorResponse.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorResponse {\n");

        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    data: ").append(toIndentedString(data)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Enum for status
     **/
    public enum StatusEnum {

        ERROR(String.valueOf("ERROR"));


        private String value;

        StatusEnum(String v) {
            value = v;
        }

        /**
         * Convert a String into String, as specified in the
         * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">
         * See JAX RS 2.0 Specification, section 3.2, p. 12</a>
         */
        public static StatusEnum fromString(String s) {
            for (StatusEnum b : StatusEnum.values()) {
                // using Objects.toString() to be safe if value type non-object type
                // because types like 'int' etc. will be auto-boxed
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        @JsonCreator
        public static StatusEnum fromValue(String value) {
            for (StatusEnum b : StatusEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        public String value() {
            return value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }


}

