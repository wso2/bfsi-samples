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
 * Defines the context data related to the amended authorization
 */
@JsonTypeName("AmendedAuthorization")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class AmendedAuthorization implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String type;
    private String status;
    @Valid
    private List<@Valid Resource> resources = new ArrayList<>();
    @Valid
    private List<@Valid AmendedResource> amendedResources = new ArrayList<>();

    public AmendedAuthorization() {
    }

    /**
     * Set the unique identifier for the amended authorization.
     **/
    public AmendedAuthorization id(String id) {
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
     * Set the type of the amended authorization.
     **/
    public AmendedAuthorization type(String type) {
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
     * Set the status of the amended authorization.
     **/
    public AmendedAuthorization status(String status) {
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
     * Set the list of resources associated with the amended authorization.
     **/
    public AmendedAuthorization resources(List<@Valid Resource> resources) {
        this.resources = resources;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("resources")
    @Valid
    public List<@Valid Resource> getResources() {
        return resources;
    }

    @JsonProperty("resources")
    public void setResources(List<@Valid Resource> resources) {
        this.resources = resources;
    }

    public AmendedAuthorization addResourcesItem(Resource resourcesItem) {
        if (this.resources == null) {
            this.resources = new ArrayList<>();
        }

        this.resources.add(resourcesItem);
        return this;
    }

    public AmendedAuthorization removeResourcesItem(Resource resourcesItem) {
        if (resourcesItem != null && this.resources != null) {
            this.resources.remove(resourcesItem);
        }

        return this;
    }

    /**
     * Set the list of amended resources associated with the amended authorization.
     **/
    public AmendedAuthorization amendedResources(List<@Valid AmendedResource> amendedResources) {
        this.amendedResources = amendedResources;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("amendedResources")
    @Valid
    public List<@Valid AmendedResource> getAmendedResources() {
        return amendedResources;
    }

    @JsonProperty("amendedResources")
    public void setAmendedResources(List<@Valid AmendedResource> amendedResources) {
        this.amendedResources = amendedResources;
    }

    public AmendedAuthorization addAmendedResourcesItem(AmendedResource amendedResourcesItem) {
        if (this.amendedResources == null) {
            this.amendedResources = new ArrayList<>();
        }

        this.amendedResources.add(amendedResourcesItem);
        return this;
    }

    public AmendedAuthorization removeAmendedResourcesItem(AmendedResource amendedResourcesItem) {
        if (amendedResourcesItem != null && this.amendedResources != null) {
            this.amendedResources.remove(amendedResourcesItem);
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
        AmendedAuthorization amendedAuthorization = (AmendedAuthorization) o;
        return Objects.equals(this.id, amendedAuthorization.id) &&
                Objects.equals(this.type, amendedAuthorization.type) &&
                Objects.equals(this.status, amendedAuthorization.status) &&
                Objects.equals(this.resources, amendedAuthorization.resources) &&
                Objects.equals(this.amendedResources, amendedAuthorization.amendedResources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, status, resources, amendedResources);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AmendedAuthorization {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    resources: ").append(toIndentedString(resources)).append("\n");
        sb.append("    amendedResources: ").append(toIndentedString(amendedResources)).append("\n");
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
