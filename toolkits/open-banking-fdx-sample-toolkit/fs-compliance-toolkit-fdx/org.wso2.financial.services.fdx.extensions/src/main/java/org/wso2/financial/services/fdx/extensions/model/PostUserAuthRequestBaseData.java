package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;


@JsonTypeName("PostUserAuthRequestBaseData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PostUserAuthRequestBaseData implements Serializable {
    private static final long serialVersionUID = 1L;
    private @Valid List<String> scopes = new ArrayList<>();
    private String consentId;
    private Integer validityPeriod;

    public PostUserAuthRequestBaseData() {
    }

    /**
     *
     **/
    public PostUserAuthRequestBaseData scopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }


    @ApiModelProperty(example = "[\"accounts\",\"payments\"]", value = "")
    @JsonProperty("scopes")
    public List<String> getScopes() {
        return scopes;
    }

    @JsonProperty("scopes")
    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public PostUserAuthRequestBaseData addScopesItem(String scopesItem) {
        if (this.scopes == null) {
            this.scopes = new ArrayList<>();
        }

        this.scopes.add(scopesItem);
        return this;
    }

    public PostUserAuthRequestBaseData removeScopesItem(String scopesItem) {
        if (scopesItem != null && this.scopes != null) {
            this.scopes.remove(scopesItem);
        }

        return this;
    }

    /**
     *
     **/
    public PostUserAuthRequestBaseData consentId(String consentId) {
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

    /**
     *
     **/
    public PostUserAuthRequestBaseData validityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
        return this;
    }


    @ApiModelProperty(example = "1000", value = "")
    @JsonProperty("validityPeriod")
    public Integer getValidityPeriod() {
        return validityPeriod;
    }

    @JsonProperty("validityPeriod")
    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostUserAuthRequestBaseData postUserAuthRequestBaseData = (PostUserAuthRequestBaseData) o;
        return Objects.equals(this.scopes, postUserAuthRequestBaseData.scopes) &&
                Objects.equals(this.consentId, postUserAuthRequestBaseData.consentId) &&
                Objects.equals(this.validityPeriod, postUserAuthRequestBaseData.validityPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scopes, consentId, validityPeriod);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PostUserAuthRequestBaseData {\n");

        sb.append("    scopes: ").append(toIndentedString(scopes)).append("\n");
        sb.append("    consentId: ").append(toIndentedString(consentId)).append("\n");
        sb.append("    validityPeriod: ").append(toIndentedString(validityPeriod)).append("\n");
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

