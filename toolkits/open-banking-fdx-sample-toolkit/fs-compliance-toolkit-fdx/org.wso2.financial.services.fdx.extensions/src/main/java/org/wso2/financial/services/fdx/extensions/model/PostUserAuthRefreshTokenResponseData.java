package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;


@JsonTypeName("PostUserAuthRefreshTokenResponseData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PostUserAuthRefreshTokenResponseData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer refreshTokenValidityPeriod;

    public PostUserAuthRefreshTokenResponseData() {
    }

    /**
     *
     **/
    public PostUserAuthRefreshTokenResponseData refreshTokenValidityPeriod(Integer refreshTokenValidityPeriod) {
        this.refreshTokenValidityPeriod = refreshTokenValidityPeriod;
        return this;
    }


    @ApiModelProperty(example = "1000", value = "")
    @JsonProperty("refreshTokenValidityPeriod")
    public Integer getRefreshTokenValidityPeriod() {
        return refreshTokenValidityPeriod;
    }

    @JsonProperty("refreshTokenValidityPeriod")
    public void setRefreshTokenValidityPeriod(Integer refreshTokenValidityPeriod) {
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
        PostUserAuthRefreshTokenResponseData postUserAuthRefreshTokenResponseData =
                (PostUserAuthRefreshTokenResponseData) o;
        return Objects.equals(this.refreshTokenValidityPeriod,
                postUserAuthRefreshTokenResponseData.refreshTokenValidityPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refreshTokenValidityPeriod);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PostUserAuthRefreshTokenResponseData {\n");

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

