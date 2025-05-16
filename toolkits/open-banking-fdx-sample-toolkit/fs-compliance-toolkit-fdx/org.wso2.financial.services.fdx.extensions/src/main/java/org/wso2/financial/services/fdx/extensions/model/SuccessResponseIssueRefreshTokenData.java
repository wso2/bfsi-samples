package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Details of the context for SuccessResponseIssueRefreshToken
 */
@JsonTypeName("SuccessResponseIssueRefreshTokenData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponseIssueRefreshTokenData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean issueRefreshToken;
    private Long refreshTokenValidityPeriod;

    public SuccessResponseIssueRefreshTokenData() {
    }

    /**
     * Set the issue refresh token of success response issue refresh token
     **/
    public SuccessResponseIssueRefreshTokenData issueRefreshToken(Boolean issueRefreshToken) {
        this.issueRefreshToken = issueRefreshToken;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("issueRefreshToken")
    public Boolean getIssueRefreshToken() {
        return issueRefreshToken;
    }

    @JsonProperty("issueRefreshToken")
    public void setIssueRefreshToken(Boolean issueRefreshToken) {
        this.issueRefreshToken = issueRefreshToken;
    }

    /**
     * Set the refresh token validity period of success response issue refresh token
     **/
    public SuccessResponseIssueRefreshTokenData refreshTokenValidityPeriod(Long refreshTokenValidityPeriod) {
        this.refreshTokenValidityPeriod = refreshTokenValidityPeriod;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("refreshTokenValidityPeriod")
    public Long getRefreshTokenValidityPeriod() {
        return refreshTokenValidityPeriod;
    }

    @JsonProperty("refreshTokenValidityPeriod")
    public void setRefreshTokenValidityPeriod(Long refreshTokenValidityPeriod) {
        this.refreshTokenValidityPeriod = refreshTokenValidityPeriod;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuccessResponseIssueRefreshTokenData successResponseIssueRefreshTokenData =
                (SuccessResponseIssueRefreshTokenData) o;
        return Objects.equals(this.issueRefreshToken, successResponseIssueRefreshTokenData.issueRefreshToken) &&
                Objects.equals(this.refreshTokenValidityPeriod,
                        successResponseIssueRefreshTokenData.refreshTokenValidityPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueRefreshToken, refreshTokenValidityPeriod);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponseIssueRefreshTokenData {\n");

        sb.append("    issueRefreshToken: ").append(toIndentedString(issueRefreshToken)).append("\n");
        sb.append("    refreshTokenValidityPeriod: ").append(toIndentedString(refreshTokenValidityPeriod)).append("\n");
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

