package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;


@JsonTypeName("SuccessPersistAuthorizedConsentWithAmendmentsData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessPersistAuthorizedConsentWithAmendmentsData implements Serializable {
    private static final long serialVersionUID = 1L;
    private ConsentPersistenceSuccessDataWithAmendments data;

    public SuccessPersistAuthorizedConsentWithAmendmentsData() {
    }

    /**
     *
     **/
    public SuccessPersistAuthorizedConsentWithAmendmentsData data(ConsentPersistenceSuccessDataWithAmendments data) {
        this.data = data;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("data")
    @Valid
    public ConsentPersistenceSuccessDataWithAmendments getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(ConsentPersistenceSuccessDataWithAmendments data) {
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
        SuccessPersistAuthorizedConsentWithAmendmentsData successPersistAuthorizedConsentWithAmendmentsData =
                (SuccessPersistAuthorizedConsentWithAmendmentsData) o;
        return Objects.equals(this.data, successPersistAuthorizedConsentWithAmendmentsData.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SuccessPersistAuthorizedConsentWithAmendmentsData {\n");

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

