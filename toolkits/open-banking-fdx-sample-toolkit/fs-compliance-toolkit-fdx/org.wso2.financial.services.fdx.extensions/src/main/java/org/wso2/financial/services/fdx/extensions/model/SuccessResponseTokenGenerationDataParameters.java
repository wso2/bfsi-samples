package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Details of the context for SuccessResponseTokenGeneration
 */
@JsonTypeName("SuccessResponseTokenGenerationDataParameters")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponseTokenGenerationDataParameters implements Serializable {
    private static final long serialVersionUID = 1L;
    private String key;
    private String value;

    public SuccessResponseTokenGenerationDataParameters() {
    }

    /**
     * Set the key of success response token generation data parameters
     **/
    public SuccessResponseTokenGenerationDataParameters key(String key) {
        this.key = key;
        return this;
    }


    @ApiModelProperty(example = "consent-id", value = "")
    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Set the value of success response token generation data parameters
     **/
    public SuccessResponseTokenGenerationDataParameters value(String value) {
        this.value = value;
        return this;
    }


    @ApiModelProperty(example = "123", value = "")
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuccessResponseTokenGenerationDataParameters successResponseTokenGenerationDataParameters =
                (SuccessResponseTokenGenerationDataParameters) o;
        return Objects.equals(this.key, successResponseTokenGenerationDataParameters.key) &&
                Objects.equals(this.value, successResponseTokenGenerationDataParameters.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponseTokenGenerationDataParameters {\n");

        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

