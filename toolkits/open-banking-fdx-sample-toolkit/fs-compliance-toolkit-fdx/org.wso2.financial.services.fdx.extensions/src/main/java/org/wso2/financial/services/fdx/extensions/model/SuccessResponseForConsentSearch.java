package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;


@JsonTypeName("SuccessResponseForConsentSearch")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessResponseForConsentSearch implements Serializable {
    private static final long serialVersionUID = 1L;
    private String responseId;
    private SuccessResponseForConsentSearchData data;

    public SuccessResponseForConsentSearch() {
    }

    /**
     *
     **/
    public SuccessResponseForConsentSearch responseId(String responseId) {
        this.responseId = responseId;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("responseId")
    public String getResponseId() {
        return responseId;
    }

    @JsonProperty("responseId")
    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    /**
     *
     **/
    public SuccessResponseForConsentSearch data(SuccessResponseForConsentSearchData data) {
        this.data = data;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("data")
    @Valid
    public SuccessResponseForConsentSearchData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(SuccessResponseForConsentSearchData data) {
        this.data = data;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuccessResponseForConsentSearch successResponseForConsentSearch = (SuccessResponseForConsentSearch) o;
        return Objects.equals(this.responseId, successResponseForConsentSearch.responseId) &&
                Objects.equals(this.data, successResponseForConsentSearch.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseId, data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessResponseForConsentSearch {\n");

        sb.append("    responseId: ").append(toIndentedString(responseId)).append("\n");
        sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

