package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Defines the context related to the pre-process bulk consent retrieval request body.
 */
@JsonTypeName("PreProcessBulkConsentRetrievalRequestBody")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PreProcessBulkConsentRetrievalRequestBody implements Serializable {
    private static final long serialVersionUID = 1L;
    private String requestId;
    private ConsentSearchData data;

    public PreProcessBulkConsentRetrievalRequestBody() {
    }

    /**
     * A unique correlation identifier
     **/
    public PreProcessBulkConsentRetrievalRequestBody requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }


    @ApiModelProperty(example = "Ec1wMjmiG8", value = "A unique correlation identifier")
    @JsonProperty("requestId")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("requestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Set the data of the pre-process bulk consent retrieval request body
     **/
    public PreProcessBulkConsentRetrievalRequestBody data(ConsentSearchData data) {
        this.data = data;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("data")
    @Valid
    public ConsentSearchData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(ConsentSearchData data) {
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
        PreProcessBulkConsentRetrievalRequestBody preProcessBulkConsentRetrievalRequestBody =
                (PreProcessBulkConsentRetrievalRequestBody) o;
        return Objects.equals(this.requestId, preProcessBulkConsentRetrievalRequestBody.requestId) &&
                Objects.equals(this.data, preProcessBulkConsentRetrievalRequestBody.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PreProcessBulkConsentRetrievalRequestBody {\n");

        sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
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

