package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Details of the context for SuccessResponsePreProcessConsentCreation
 */
@JsonTypeName("SuccessResponsePreProcessConsentCreationData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponsePreProcessConsentCreationData implements Serializable {
    private static final long serialVersionUID = 1L;
    private DetailedConsentResourceData consentResource;

    public SuccessResponsePreProcessConsentCreationData() {
    }

    /**
     * Set the consent resource of success response pre process consent creation
     **/
    public SuccessResponsePreProcessConsentCreationData consentResource(DetailedConsentResourceData consentResource) {
        this.consentResource = consentResource;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("consentResource")
    @Valid
    public DetailedConsentResourceData getConsentResource() {
        return consentResource;
    }

    @JsonProperty("consentResource")
    public void setConsentResource(DetailedConsentResourceData consentResource) {
        this.consentResource = consentResource;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuccessResponsePreProcessConsentCreationData successResponsePreProcessConsentCreationData =
                (SuccessResponsePreProcessConsentCreationData) o;
        return Objects.equals(this.consentResource, successResponsePreProcessConsentCreationData.consentResource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consentResource);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponsePreProcessConsentCreationData {\n");

        sb.append("    consentResource: ").append(toIndentedString(consentResource)).append("\n");
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

