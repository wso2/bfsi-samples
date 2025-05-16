package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines the success response for event polling data.
 **/
@JsonTypeName("SuccessResponseForEnrichEventPolling_data")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponseForEnrichEventPollingData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object eventPollingResponse;

    public SuccessResponseForEnrichEventPollingData() {
    }

    /**
     * Event Polling Response
     **/
    public SuccessResponseForEnrichEventPollingData eventPollingResponse(Object eventPollingResponse) {
        this.eventPollingResponse = eventPollingResponse;
        return this;
    }


    @ApiModelProperty(value = "Event Polling Response")
    @JsonProperty("eventPollingResponse")
    public Object getEventPollingResponse() {
        return eventPollingResponse;
    }

    @JsonProperty("eventPollingResponse")
    public void setEventPollingResponse(Object eventPollingResponse) {
        this.eventPollingResponse = eventPollingResponse;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuccessResponseForEnrichEventPollingData successResponseForEnrichEventPollingData =
                (SuccessResponseForEnrichEventPollingData) o;
        return Objects.equals(this.eventPollingResponse, successResponseForEnrichEventPollingData.eventPollingResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventPollingResponse);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponseForEnrichEventPollingData {\n");

        sb.append("    eventPollingResponse: ").append(toIndentedString(eventPollingResponse)).append("\n");
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

