package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Details of the context for SuccessResponseIssueRefreshTokenGeneration
 */
@JsonTypeName("SuccessResponseIssueRefreshTokenGenerationData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponseIssueRefreshTokenGenerationData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean issueRefreshToken;
    private Long refreshTokenValidityPeriod;

    public SuccessResponseIssueRefreshTokenGenerationData() {
    }

    /**
     * Set the issue refresh token of success response issue refresh token generation
     **/
    public SuccessResponseIssueRefreshTokenGenerationData issueRefreshToken(Boolean issueRefreshToken) {
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
     * Set the refresh token validity period of success response issue refresh token generation
     **/
    public SuccessResponseIssueRefreshTokenGenerationData refreshTokenValidityPeriod(Long refreshTokenValidityPeriod) {
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
        SuccessResponseIssueRefreshTokenGenerationData successResponseIssueRefreshTokenGenerationData =
                (SuccessResponseIssueRefreshTokenGenerationData) o;
        return Objects.equals(this.issueRefreshToken,
                successResponseIssueRefreshTokenGenerationData.issueRefreshToken) &&
                Objects.equals(this.refreshTokenValidityPeriod,
                        successResponseIssueRefreshTokenGenerationData.refreshTokenValidityPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueRefreshToken, refreshTokenValidityPeriod);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponseIssueRefreshTokenGenerationData {\n");

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

