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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Defines the context data related to the pre-token introspection response.
 */
@JsonTypeName("Response200ForPreTokenIntrospection_data")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class Response200ForPreTokenIntrospectionData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Valid
    private List<@Valid ResponseBaseDataClaims> attributes = new ArrayList<>();

    public Response200ForPreTokenIntrospectionData() {
    }

    /**
     * Set the attributes of the pre-token introspection response data.
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

