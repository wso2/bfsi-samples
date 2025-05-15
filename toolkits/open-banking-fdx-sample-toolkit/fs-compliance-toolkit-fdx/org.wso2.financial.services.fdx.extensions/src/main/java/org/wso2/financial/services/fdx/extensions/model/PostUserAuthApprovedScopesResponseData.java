package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;


@JsonTypeName("PostUserAuthApprovedScopesResponseData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PostUserAuthApprovedScopesResponseData implements Serializable {
    private static final long serialVersionUID = 1L;
    private @Valid List<String> approvedScopes = new ArrayList<>();

    public PostUserAuthApprovedScopesResponseData() {
    }

    /**
     *
     **/
    public PostUserAuthApprovedScopesResponseData approvedScopes(List<String> approvedScopes) {
        this.approvedScopes = approvedScopes;
        return this;
    }


    @ApiModelProperty(example = "[\"accounts\",\"payments\",\"consentId\"]", value = "")
    @JsonProperty("approvedScopes")
    public List<String> getApprovedScopes() {
        return approvedScopes;
    }

    @JsonProperty("approvedScopes")
    public void setApprovedScopes(List<String> approvedScopes) {
        this.approvedScopes = approvedScopes;
    }

    public PostUserAuthApprovedScopesResponseData addApprovedScopesItem(String approvedScopesItem) {
        if (this.approvedScopes == null) {
            this.approvedScopes = new ArrayList<>();
        }

        this.approvedScopes.add(approvedScopesItem);
        return this;
    }

    public PostUserAuthApprovedScopesResponseData removeApprovedScopesItem(String approvedScopesItem) {
        if (approvedScopesItem != null && this.approvedScopes != null) {
            this.approvedScopes.remove(approvedScopesItem);
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
        PostUserAuthApprovedScopesResponseData postUserAuthApprovedScopesResponseData =
                (PostUserAuthApprovedScopesResponseData) o;
        return Objects.equals(this.approvedScopes, postUserAuthApprovedScopesResponseData.approvedScopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(approvedScopes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PostUserAuthApprovedScopesResponseData {\n");

        sb.append("    approvedScopes: ").append(toIndentedString(approvedScopes)).append("\n");
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

