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
 * Details of the context for SuccessResponseForEventWithDetails
 */
@JsonTypeName("SuccessResponseForEventWithDetails_data")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponseForEventWithDetailsData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String callbackUrl;
    private String version;
    @Valid
    private List<String> eventTypes = new ArrayList<>();

    public SuccessResponseForEventWithDetailsData() {
    }

    /**
     * Set the callback URL of success response for event with details
     **/
    public SuccessResponseForEventWithDetailsData callbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("callbackUrl")
    public String getCallbackUrl() {
        return callbackUrl;
    }

    @JsonProperty("callbackUrl")
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    /**
     * Set the version of success response for event with details
     **/
    public SuccessResponseForEventWithDetailsData version(String version) {
        this.version = version;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Set the event types of success response for event with details
     **/
    public SuccessResponseForEventWithDetailsData eventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("eventTypes")
    public List<String> getEventTypes() {
        return eventTypes;
    }

    @JsonProperty("eventTypes")
    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public SuccessResponseForEventWithDetailsData addEventTypesItem(String eventTypesItem) {
        if (this.eventTypes == null) {
            this.eventTypes = new ArrayList<>();
        }

        this.eventTypes.add(eventTypesItem);
        return this;
    }

    public SuccessResponseForEventWithDetailsData removeEventTypesItem(String eventTypesItem) {
        if (eventTypesItem != null && this.eventTypes != null) {
            this.eventTypes.remove(eventTypesItem);
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
        SuccessResponseForEventWithDetailsData successResponseForEventWithDetailsData =
                (SuccessResponseForEventWithDetailsData) o;
        return Objects.equals(this.callbackUrl, successResponseForEventWithDetailsData.callbackUrl) &&
                Objects.equals(this.version, successResponseForEventWithDetailsData.version) &&
                Objects.equals(this.eventTypes, successResponseForEventWithDetailsData.eventTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callbackUrl, version, eventTypes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponseForEventWithDetailsData {\n");

        sb.append("    callbackUrl: ").append(toIndentedString(callbackUrl)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    eventTypes: ").append(toIndentedString(eventTypes)).append("\n");
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

