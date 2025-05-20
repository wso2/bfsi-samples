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
 * Defines the context related to the stored authorization.
 */
@JsonTypeName("StoredAuthorization")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class StoredAuthorization implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String userId;
    private String type;
    private String status;
    @Valid
    private List<@Valid StoredResource> resources = new ArrayList<>();

    public StoredAuthorization() {
    }

    /**
     * Set the unique identifier for the stored authorization.
     **/
    public StoredAuthorization id(String id) {
        this.id = id;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set the unique identifier for the user associated with the stored authorization.
     **/
    public StoredAuthorization userId(String userId) {
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

    /**
     * Set the type of the stored authorization.
     **/
    public StoredAuthorization type(String type) {
        this.type = type;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Set the status of the stored authorization.
     **/
    public StoredAuthorization status(String status) {
        this.status = status;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Set the list of resources associated with the stored authorization.
     **/
    public StoredAuthorization resources(List<@Valid StoredResource> resources) {
        this.resources = resources;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("resources")
    @Valid
    public List<@Valid StoredResource> getResources() {
        return resources;
    }

    @JsonProperty("resources")
    public void setResources(List<@Valid StoredResource> resources) {
        this.resources = resources;
    }

    public StoredAuthorization addResourcesItem(StoredResource resourcesItem) {
        if (this.resources == null) {
            this.resources = new ArrayList<>();
        }

        this.resources.add(resourcesItem);
        return this;
    }

    public StoredAuthorization removeResourcesItem(StoredResource resourcesItem) {
        if (resourcesItem != null && this.resources != null) {
            this.resources.remove(resourcesItem);
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
        StoredAuthorization storedAuthorization = (StoredAuthorization) o;
        return Objects.equals(this.id, storedAuthorization.id) &&
                Objects.equals(this.userId, storedAuthorization.userId) &&
                Objects.equals(this.type, storedAuthorization.type) &&
                Objects.equals(this.status, storedAuthorization.status) &&
                Objects.equals(this.resources, storedAuthorization.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, type, status, resources);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StoredAuthorization {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    resources: ").append(toIndentedString(resources)).append("\n");
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

