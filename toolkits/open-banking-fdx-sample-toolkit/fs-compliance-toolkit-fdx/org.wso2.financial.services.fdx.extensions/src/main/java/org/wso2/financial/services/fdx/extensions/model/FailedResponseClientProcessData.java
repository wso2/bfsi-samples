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

/**
 * Defines the context data related to the failed response client process data.
 */
@JsonTypeName("FailedResponseClientProcess_data")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class FailedResponseClientProcessData implements Serializable {
    private static final long serialVersionUID = 1L;
    private ErrorEnum error;
    private String errorDescription;
    public FailedResponseClientProcessData() {
    }

    /**
     * Provides the error code for error.
     **/
    public FailedResponseClientProcessData error(ErrorEnum error) {
        this.error = error;
        return this;
    }

    @ApiModelProperty(value = "Provides the error code for error.")
    @JsonProperty("error")
    public ErrorEnum getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(ErrorEnum error) {
        this.error = error;
    }

    /**
     * Offers a detailed explanation of the error.
     **/
    public FailedResponseClientProcessData errorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
        return this;
    }

    @ApiModelProperty(value = "Offers a detailed explanation of the error.")
    @JsonProperty("errorDescription")
    public String getErrorDescription() {
        return errorDescription;
    }

    @JsonProperty("errorDescription")
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FailedResponseClientProcessData failedResponseClientProcessData = (FailedResponseClientProcessData) o;
        return Objects.equals(this.error, failedResponseClientProcessData.error) &&
                Objects.equals(this.errorDescription, failedResponseClientProcessData.errorDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, errorDescription);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FailedResponseClientProcessData {\n");

        sb.append("    error: ").append(toIndentedString(error)).append("\n");
        sb.append("    errorDescription: ").append(toIndentedString(errorDescription)).append("\n");
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

    /**
     * Enum for error
     */
    public enum ErrorEnum {

        INVALID_CLIENT_METADATA(String.valueOf("invalid_client_metadata")),
        INVALID_REDIRECT_URI(String.valueOf("invalid_redirect_uri")),
        INVALID_SOFTWARE_STATEMENT(String.valueOf("invalid_software_statement"));


        private String value;

        ErrorEnum(String v) {
            value = v;
        }

        /**
         * Convert a String into String, as specified in the
         * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">
         * See JAX RS 2.0 Specification, section 3.2, p. 12</a>
         */
        public static ErrorEnum fromString(String s) {
            for (ErrorEnum b : ErrorEnum.values()) {
                // using Objects.toString() to be safe if value type non-object type
                // because types like 'int' etc. will be auto-boxed
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        @JsonCreator
        public static ErrorEnum fromValue(String value) {
            for (ErrorEnum b : ErrorEnum.values()) {
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

