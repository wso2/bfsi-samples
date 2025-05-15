package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;


@JsonTypeName("Response200ForPreTokenIntrospection_data")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class Response200ForPreTokenIntrospectionData implements Serializable {
    private static final long serialVersionUID = 1L;
    private @Valid List<@Valid ResponseBaseDataClaims> attributes = new ArrayList<>();

    public Response200ForPreTokenIntrospectionData() {
    }

    /**
     *
     **/
    public Response200ForPreTokenIntrospectionData attributes(List<@Valid ResponseBaseDataClaims> attributes) {
        this.attributes = attributes;
        return this;
    }


    @ApiModelProperty(example = "[{\"key\":\"attribute1\",\"value\":\"123\"},{\"key\":\"attribute2\"," +
            "\"value\":\"456\"}]", value = "")
    @JsonProperty("attributes")
    @Valid
    public List<@Valid ResponseBaseDataClaims> getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(List<@Valid ResponseBaseDataClaims> attributes) {
        this.attributes = attributes;
    }

    public Response200ForPreTokenIntrospectionData addAttributesItem(ResponseBaseDataClaims attributesItem) {
        if (this.attributes == null) {
            this.attributes = new ArrayList<>();
        }

        this.attributes.add(attributesItem);
        return this;
    }

    public Response200ForPreTokenIntrospectionData removeAttributesItem(ResponseBaseDataClaims attributesItem) {
        if (attributesItem != null && this.attributes != null) {
            this.attributes.remove(attributesItem);
        }

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response200ForPreTokenIntrospectionData response200ForPreTokenIntrospectionData =
                (Response200ForPreTokenIntrospectionData) o;
        return Objects.equals(this.attributes, response200ForPreTokenIntrospectionData.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Response200ForPreTokenIntrospectionData {\n");

        sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
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

