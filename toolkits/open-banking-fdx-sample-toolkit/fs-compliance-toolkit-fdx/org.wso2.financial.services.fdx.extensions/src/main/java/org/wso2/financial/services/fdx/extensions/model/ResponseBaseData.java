package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;


@JsonTypeName("ResponseBaseData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class ResponseBaseData implements Serializable {
    private static final long serialVersionUID = 1L;
    private @Valid List<@Valid ResponseBaseDataClaims> claims = new ArrayList<>();

    public ResponseBaseData() {
    }

    /**
     *
     **/
    public ResponseBaseData claims(List<@Valid ResponseBaseDataClaims> claims) {
        this.claims = claims;
        return this;
    }


    @ApiModelProperty(example = "[{\"key\":\"claim1\",\"value\":\"123\"},{\"key\":\"claim2\",\"value\":\"456\"}]",
            value = "")
    @JsonProperty("claims")
    @Valid
    public List<@Valid ResponseBaseDataClaims> getClaims() {
        return claims;
    }

    @JsonProperty("claims")
    public void setClaims(List<@Valid ResponseBaseDataClaims> claims) {
        this.claims = claims;
    }

    public ResponseBaseData addClaimsItem(ResponseBaseDataClaims claimsItem) {
        if (this.claims == null) {
            this.claims = new ArrayList<>();
        }

        this.claims.add(claimsItem);
        return this;
    }

    public ResponseBaseData removeClaimsItem(ResponseBaseDataClaims claimsItem) {
        if (claimsItem != null && this.claims != null) {
            this.claims.remove(claimsItem);
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
        ResponseBaseData responseBaseData = (ResponseBaseData) o;
        return Objects.equals(this.claims, responseBaseData.claims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claims);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResponseBaseData {\n");

        sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
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

