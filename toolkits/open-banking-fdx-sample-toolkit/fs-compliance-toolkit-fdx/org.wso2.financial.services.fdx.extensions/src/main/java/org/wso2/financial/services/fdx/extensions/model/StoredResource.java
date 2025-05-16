package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines the context related to the stored resource.
 */
@JsonTypeName("StoredResource")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class StoredResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String accountId;
    private String permission;
    private String status;

    public StoredResource() {
    }

    /**
     * Set the unique identifier for the stored resource.
     **/
    public StoredResource id(String id) {
        this.id = id;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set the unique identifier for the account associated with the stored resource.
     **/
    public StoredResource accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("accountId")
    public String getAccountId() {
        return accountId;
    }

    @JsonProperty("accountId")
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Set the permission associated with the stored resource.
     **/
    public StoredResource permission(String permission) {
        this.permission = permission;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("permission")
    public String getPermission() {
        return permission;
    }

    @JsonProperty("permission")
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * Set the status of the stored resource.
     **/
    public StoredResource status(String status) {
        this.status = status;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoredResource storedResource = (StoredResource) o;
        return Objects.equals(this.id, storedResource.id) &&
                Objects.equals(this.accountId, storedResource.accountId) &&
                Objects.equals(this.permission, storedResource.permission) &&
                Objects.equals(this.status, storedResource.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, permission, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StoredResource {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
        sb.append("    permission: ").append(toIndentedString(permission)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

