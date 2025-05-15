package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;


@JsonTypeName("Response200ForErrorMapper")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class Response200ForErrorMapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private String responseId;
    private Integer errorCode;
    private Object data;

    public Response200ForErrorMapper() {
    }

    /**
     *
     **/
    public Response200ForErrorMapper responseId(String responseId) {
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
     *
     **/
    public Response200ForErrorMapper errorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("errorCode")
    public Integer getErrorCode() {
        return errorCode;
    }

    @JsonProperty("errorCode")
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Defines the custom error response.
     **/
    public Response200ForErrorMapper data(Object data) {
        this.data = data;
        return this;
    }


    @ApiModelProperty(value = "Defines the custom error response.")
    @JsonProperty("data")
    public Object getData() {
        return data;
    }

    @JsonProperty("data")
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
        Response200ForErrorMapper response200ForErrorMapper = (Response200ForErrorMapper) o;
        return Objects.equals(this.responseId, response200ForErrorMapper.responseId) &&
                Objects.equals(this.errorCode, response200ForErrorMapper.errorCode) &&
                Objects.equals(this.data, response200ForErrorMapper.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseId, errorCode, data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Response200ForErrorMapper {\n");

        sb.append("    responseId: ").append(toIndentedString(responseId)).append("\n");
        sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
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


}

