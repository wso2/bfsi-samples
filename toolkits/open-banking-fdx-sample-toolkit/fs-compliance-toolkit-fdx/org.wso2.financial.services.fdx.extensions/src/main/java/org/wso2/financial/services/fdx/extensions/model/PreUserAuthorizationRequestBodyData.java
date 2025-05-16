package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Detail of the context of the full request object
 **/
@ApiModel(description = "full request object")
@JsonTypeName("PreUserAuthorizationRequestBodyData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PreUserAuthorizationRequestBodyData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object requestObject;

    public PreUserAuthorizationRequestBodyData() {
    }

    /**
     * Set the full request object.
     **/
    public PreUserAuthorizationRequestBodyData requestObject(Object requestObject) {
        this.requestObject = requestObject;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("requestObject")
    public Object getRequestObject() {
        return requestObject;
    }

    @JsonProperty("requestObject")
    public void setRequestObject(Object requestObject) {
        this.requestObject = requestObject;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PreUserAuthorizationRequestBodyData preUserAuthorizationRequestBodyData =
                (PreUserAuthorizationRequestBodyData) o;
        return Objects.equals(this.requestObject, preUserAuthorizationRequestBodyData.requestObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestObject);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PreUserAuthorizationRequestBodyData {\n");

        sb.append("    requestObject: ").append(toIndentedString(requestObject)).append("\n");
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

