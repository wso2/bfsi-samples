package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Defines the context data related to the access token generation request body
 */
@JsonTypeName("AccessTokenGenerationRequestBody")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class AccessTokenGenerationRequestBody implements Serializable {
    private static final long serialVersionUID = 1L;
    @Valid
    private List<String> scopes = new ArrayList<>();
    private String grantType;

    public AccessTokenGenerationRequestBody() {
    }

    /**
     * Set scopes for the access token generation request
     **/
    public AccessTokenGenerationRequestBody scopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }


    @ApiModelProperty(example = "[\"accounts\",\"payments\",\"consentId\"]", value = "")
    @JsonProperty("scopes")
    public List<String> getScopes() {
        return scopes;
    }

    @JsonProperty("scopes")
    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public AccessTokenGenerationRequestBody addScopesItem(String scopesItem) {
        if (this.scopes == null) {
            this.scopes = new ArrayList<>();
        }

        this.scopes.add(scopesItem);
        return this;
    }

    public AccessTokenGenerationRequestBody removeScopesItem(String scopesItem) {
        if (scopesItem != null && this.scopes != null) {
            this.scopes.remove(scopesItem);
        }

        return this;
    }

    /**
     * Set grant type for the access token generation request
     **/
    public AccessTokenGenerationRequestBody grantType(String grantType) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessTokenGenerationRequestBody accessTokenGenerationRequestBody = (AccessTokenGenerationRequestBody) o;
        return Objects.equals(this.scopes, accessTokenGenerationRequestBody.scopes) &&
                Objects.equals(this.grantType, accessTokenGenerationRequestBody.grantType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scopes, grantType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AccessTokenGenerationRequestBody {\n");

        sb.append("    scopes: ").append(toIndentedString(scopes)).append("\n");
        sb.append("    grantType: ").append(toIndentedString(grantType)).append("\n");
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

