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

