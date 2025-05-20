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
 * Defines the context data related to the issue refresh token request data.
 */
@JsonTypeName("IssueRefreshTokenRequestData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class IssueRefreshTokenRequestData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String grantType;
    private Long consentCreatedTime;
    private Long consentValidityPeriod;
    private Long defaultRefreshTokenValidityPeriod;

    public IssueRefreshTokenRequestData() {
    }

    /**
     * Set the grant type of the issue refresh token request data.
     **/
    public IssueRefreshTokenRequestData grantType(String grantType) {
        this.grantType = grantType;
        return this;
    }


    @ApiModelProperty(example = "authorization_code", value = "")
    @JsonProperty("grantType")
    public String getGrantType() {
        return grantType;
    }

    @JsonProperty("grantType")
    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    /**
     * Set the consent created time of the issue refresh token request data.
     **/
    public IssueRefreshTokenRequestData consentCreatedTime(Long consentCreatedTime) {
        this.consentCreatedTime = consentCreatedTime;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("consentCreatedTime")
    public Long getConsentCreatedTime() {
        return consentCreatedTime;
    }

    @JsonProperty("consentCreatedTime")
    public void setConsentCreatedTime(Long consentCreatedTime) {
        this.consentCreatedTime = consentCreatedTime;
    }

    /**
     * Set the consent validity period of the issue refresh token request data.
     **/
    public IssueRefreshTokenRequestData consentValidityPeriod(Long consentValidityPeriod) {
        this.consentValidityPeriod = consentValidityPeriod;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("consentValidityPeriod")
    public Long getConsentValidityPeriod() {
        return consentValidityPeriod;
    }

    @JsonProperty("consentValidityPeriod")
    public void setConsentValidityPeriod(Long consentValidityPeriod) {
        this.consentValidityPeriod = consentValidityPeriod;
    }

    /**
     * Set the default refresh token validity period of the issue refresh token request data.
     **/
    public IssueRefreshTokenRequestData defaultRefreshTokenValidityPeriod(Long defaultRefreshTokenValidityPeriod) {
        this.defaultRefreshTokenValidityPeriod = defaultRefreshTokenValidityPeriod;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("defaultRefreshTokenValidityPeriod")
    public Long getDefaultRefreshTokenValidityPeriod() {
        return defaultRefreshTokenValidityPeriod;
    }

    @JsonProperty("defaultRefreshTokenValidityPeriod")
    public void setDefaultRefreshTokenValidityPeriod(Long defaultRefreshTokenValidityPeriod) {
        this.defaultRefreshTokenValidityPeriod = defaultRefreshTokenValidityPeriod;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssueRefreshTokenRequestData issueRefreshTokenRequestData = (IssueRefreshTokenRequestData) o;
        return Objects.equals(this.grantType, issueRefreshTokenRequestData.grantType) &&
                Objects.equals(this.consentCreatedTime, issueRefreshTokenRequestData.consentCreatedTime) &&
                Objects.equals(this.consentValidityPeriod, issueRefreshTokenRequestData.consentValidityPeriod) &&
                Objects.equals(this.defaultRefreshTokenValidityPeriod,
                        issueRefreshTokenRequestData.defaultRefreshTokenValidityPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grantType, consentCreatedTime, consentValidityPeriod, defaultRefreshTokenValidityPeriod);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IssueRefreshTokenRequestData {\n");

        sb.append("    grantType: ").append(toIndentedString(grantType)).append("\n");
        sb.append("    consentCreatedTime: ").append(toIndentedString(consentCreatedTime)).append("\n");
        sb.append("    consentValidityPeriod: ").append(toIndentedString(consentValidityPeriod)).append("\n");
        sb.append("    defaultRefreshTokenValidityPeriod: ").append(toIndentedString(defaultRefreshTokenValidityPeriod))
                .append("\n");
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

