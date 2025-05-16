package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines the context related to the post user auth request base data.
 */
@JsonTypeName("PostUserAuthResponseBase")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PostUserAuthResponseBase implements Serializable {
    private static final long serialVersionUID = 1L;
    private String responseId;
    private StatusEnum status;

    public PostUserAuthResponseBase() {
    }

    /**
     * Set the unique response id for the post user auth request.
     **/
    public PostUserAuthResponseBase responseId(String responseId) {
        this.responseId = responseId;
        return this;
    }

    @ApiModelProperty(example = "Ec1wMjmiG8", value = "")
    @JsonProperty("responseId")
    public String getResponseId() {
        return responseId;
    }

    @JsonProperty("responseId")
    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    /**
     * Set the status of the post user auth request.
     **/
    public PostUserAuthResponseBase status(StatusEnum status) {
        this.status = status;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("status")
    public StatusEnum getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostUserAuthResponseBase postUserAuthResponseBase = (PostUserAuthResponseBase) o;
        return Objects.equals(this.responseId, postUserAuthResponseBase.responseId) &&
                Objects.equals(this.status, postUserAuthResponseBase.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseId, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PostUserAuthResponseBase {\n");

        sb.append("    responseId: ").append(toIndentedString(responseId)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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
     * Enum for status
     */
    public enum StatusEnum {

        SUCCESS(String.valueOf("SUCCESS"));


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

