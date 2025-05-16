package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines the context related to the request base data.
 */
@JsonTypeName("RequestBaseData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class RequestBaseData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String consentId;

    public RequestBaseData() {
    }

    /**
     * Set the user ID of the request base data.
     **/
    public RequestBaseData userId(String userId) {
        this.userId = userId;
        return this;
    }


    @ApiModelProperty(example = "ben@wso2.com", value = "")
    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Set the consent ID of the request base data.
     **/
    public RequestBaseData consentId(String consentId) {
        this.consentId = consentId;
        return this;
    }


    @ApiModelProperty(example = "123", value = "")
    @JsonProperty("consentId")
    public String getConsentId() {
        return consentId;
    }

    @JsonProperty("consentId")
    public void setConsentId(String consentId) {
        this.consentId = consentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestBaseData requestBaseData = (RequestBaseData) o;
        return Objects.equals(this.userId, requestBaseData.userId) &&
                Objects.equals(this.consentId, requestBaseData.consentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, consentId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RequestBaseData {\n");

        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    consentId: ").append(toIndentedString(consentId)).append("\n");
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

