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
import java.util.Objects;

/**
 * Details of the context for UserGrantedData
 */
@JsonTypeName("UserGrantedData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class UserGrantedData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object requestParameters;
    private Object authorizedResources;
    private String userId;

    public UserGrantedData() {
    }

    /**
     * Set the request parameters of user granted data
     **/
    public UserGrantedData requestParameters(Object requestParameters) {
        this.requestParameters = requestParameters;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("requestParameters")
    public Object getRequestParameters() {
        return requestParameters;
    }

    @JsonProperty("requestParameters")
    public void setRequestParameters(Object requestParameters) {
        this.requestParameters = requestParameters;
    }

    /**
     * Set the authorized resources of user granted data
     **/
    public UserGrantedData authorizedResources(Object authorizedResources) {
        this.authorizedResources = authorizedResources;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("authorizedResources")
    public Object getAuthorizedResources() {
        return authorizedResources;
    }

    @JsonProperty("authorizedResources")
    public void setAuthorizedResources(Object authorizedResources) {
        this.authorizedResources = authorizedResources;
    }

    /**
     * Set the user id of user granted data
     **/
    public UserGrantedData userId(String userId) {
        this.userId = userId;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserGrantedData userGrantedData = (UserGrantedData) o;
        return Objects.equals(this.requestParameters, userGrantedData.requestParameters) &&
                Objects.equals(this.authorizedResources, userGrantedData.authorizedResources) &&
                Objects.equals(this.userId, userGrantedData.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestParameters, authorizedResources, userId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserGrantedData {\n");

        sb.append("    requestParameters: ").append(toIndentedString(requestParameters)).append("\n");
        sb.append("    authorizedResources: ").append(toIndentedString(authorizedResources)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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

