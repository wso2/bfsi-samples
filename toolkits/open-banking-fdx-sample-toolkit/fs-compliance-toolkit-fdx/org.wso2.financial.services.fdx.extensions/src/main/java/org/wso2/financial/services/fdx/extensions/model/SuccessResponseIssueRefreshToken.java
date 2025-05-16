package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Details of the context for SuccessResponseIssueRefreshToken
 */
@JsonTypeName("SuccessResponseIssueRefreshToken")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponseIssueRefreshToken implements Serializable {
    private static final long serialVersionUID = 1L;
    private String responseId;
    private StatusEnum status;
    private SuccessResponseIssueRefreshTokenData data;

    public SuccessResponseIssueRefreshToken() {
    }

    /**
     * Set the response id of success response issue refresh token
     **/
    public SuccessResponseIssueRefreshToken responseId(String responseId) {
        this.responseId = responseId;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("responseId")
    public String getResponseId() {
        return responseId;
    }

    @JsonProperty("responseId")
    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    /**
     * Set the status of success response issue refresh token
     **/
    public SuccessResponseIssueRefreshToken status(StatusEnum status) {
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

    /**
     * Set the data of success response issue refresh token
     **/
    public SuccessResponseIssueRefreshToken data(SuccessResponseIssueRefreshTokenData data) {
        this.data = data;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("data")
    @Valid
    public SuccessResponseIssueRefreshTokenData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(SuccessResponseIssueRefreshTokenData data) {
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
        SuccessResponseIssueRefreshToken successResponseIssueRefreshToken = (SuccessResponseIssueRefreshToken) o;
        return Objects.equals(this.responseId, successResponseIssueRefreshToken.responseId) &&
                Objects.equals(this.status, successResponseIssueRefreshToken.status) &&
                Objects.equals(this.data, successResponseIssueRefreshToken.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseId, status, data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponseIssueRefreshToken {\n");

        sb.append("    responseId: ").append(toIndentedString(responseId)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

