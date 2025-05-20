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
 * Defines the success response for enriching event subscription data.
 **/
@JsonTypeName("SuccessResponseForEnrichEventSubscription_data")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponseForEnrichEventSubscriptionData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object eventSubscriptionResponse;

    public SuccessResponseForEnrichEventSubscriptionData() {
    }

    /**
     * Event Subscription Response
     **/
    public SuccessResponseForEnrichEventSubscriptionData eventSubscriptionResponse(Object eventSubscriptionResponse) {
        this.eventSubscriptionResponse = eventSubscriptionResponse;
        return this;
    }


    @ApiModelProperty(value = "Event Subscription Response")
    @JsonProperty("eventSubscriptionResponse")
    public Object getEventSubscriptionResponse() {
        return eventSubscriptionResponse;
    }

    @JsonProperty("eventSubscriptionResponse")
    public void setEventSubscriptionResponse(Object eventSubscriptionResponse) {
        this.eventSubscriptionResponse = eventSubscriptionResponse;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuccessResponseForEnrichEventSubscriptionData successResponseForEnrichEventSubscriptionData =
                (SuccessResponseForEnrichEventSubscriptionData) o;
        return Objects.equals(this.eventSubscriptionResponse,
                successResponseForEnrichEventSubscriptionData.eventSubscriptionResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventSubscriptionResponse);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponseForEnrichEventSubscriptionData {\n");

        sb.append("    eventSubscriptionResponse: ").append(toIndentedString(eventSubscriptionResponse)).append("\n");
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

