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
 * Defines the context related to the event polling request.
 */
@JsonTypeName("EventPollingRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class EventPollingRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object eventPollingData;

    public EventPollingRequest() {
    }

    /**
     * Event polling data
     **/
    public EventPollingRequest eventPollingData(Object eventPollingData) {
        this.eventPollingData = eventPollingData;
        return this;
    }


    @ApiModelProperty(value = "Event polling data")
    @JsonProperty("eventPollingData")
    public Object getEventPollingData() {
        return eventPollingData;
    }

    @JsonProperty("eventPollingData")
    public void setEventPollingData(Object eventPollingData) {
        this.eventPollingData = eventPollingData;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventPollingRequest eventPollingRequest = (EventPollingRequest) o;
        return Objects.equals(this.eventPollingData, eventPollingRequest.eventPollingData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventPollingData);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EventPollingRequest {\n");

        sb.append("    eventPollingData: ").append(toIndentedString(eventPollingData)).append("\n");
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

