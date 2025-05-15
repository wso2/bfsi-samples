package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;


@JsonTypeName("SuccessResponseConsentRevocationAdminData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponseConsentRevocationAdminData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String revocationStatusName;
    private String revocationReason;
    private Boolean requireTokenRevocation;

    public SuccessResponseConsentRevocationAdminData() {
    }

    /**
     * Name for the revoked status
     **/
    public SuccessResponseConsentRevocationAdminData revocationStatusName(String revocationStatusName) {
        this.revocationStatusName = revocationStatusName;
        return this;
    }


    @ApiModelProperty(value = "Name for the revoked status")
    @JsonProperty("revocationStatusName")
    public String getRevocationStatusName() {
        return revocationStatusName;
    }

    @JsonProperty("revocationStatusName")
    public void setRevocationStatusName(String revocationStatusName) {
        this.revocationStatusName = revocationStatusName;
    }

    /**
     * Reason for the revoked event
     **/
    public SuccessResponseConsentRevocationAdminData revocationReason(String revocationReason) {
        this.revocationReason = revocationReason;
        return this;
    }


    @ApiModelProperty(value = "Reason for the revoked event")
    @JsonProperty("revocationReason")
    public String getRevocationReason() {
        return revocationReason;
    }

    @JsonProperty("revocationReason")
    public void setRevocationReason(String revocationReason) {
        this.revocationReason = revocationReason;
    }

    /**
     * Require access token to be revoked
     **/
    public SuccessResponseConsentRevocationAdminData requireTokenRevocation(Boolean requireTokenRevocation) {
        this.requireTokenRevocation = requireTokenRevocation;
        return this;
    }


    @ApiModelProperty(value = "Require access token to be revoked")
    @JsonProperty("requireTokenRevocation")
    public Boolean getRequireTokenRevocation() {
        return requireTokenRevocation;
    }

    @JsonProperty("requireTokenRevocation")
    public void setRequireTokenRevocation(Boolean requireTokenRevocation) {
        this.requireTokenRevocation = requireTokenRevocation;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuccessResponseConsentRevocationAdminData successResponseConsentRevocationAdminData =
                (SuccessResponseConsentRevocationAdminData) o;
        return Objects.equals(this.revocationStatusName,
                successResponseConsentRevocationAdminData.revocationStatusName) &&
                Objects.equals(this.revocationReason, successResponseConsentRevocationAdminData.revocationReason) &&
                Objects.equals(this.requireTokenRevocation,
                        successResponseConsentRevocationAdminData.requireTokenRevocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(revocationStatusName, revocationReason, requireTokenRevocation);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponseConsentRevocationAdminData {\n");

        sb.append("    revocationStatusName: ").append(toIndentedString(revocationStatusName)).append("\n");
        sb.append("    revocationReason: ").append(toIndentedString(revocationReason)).append("\n");
        sb.append("    requireTokenRevocation: ").append(toIndentedString(requireTokenRevocation)).append("\n");
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

