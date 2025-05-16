package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Defines the context data related to the errors.
 **/
@ApiModel(description = "Defines the context data related to the errors.")
@JsonTypeName("ErrorMapperData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class ErrorMapperData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Error error;

    public ErrorMapperData() {
    }

    /**
     * Set the error data of the error mapper data
     **/
    public ErrorMapperData error(Error error) {
        this.error = error;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("error")
    @Valid
    public Error getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Error error) {
        this.error = error;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorMapperData errorMapperData = (ErrorMapperData) o;
        return Objects.equals(this.error, errorMapperData.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorMapperData {\n");

        sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

