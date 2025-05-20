/**
 * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 * <p>
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.financial.services.fdx.extensions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines the context data related to the client registration.
 **/
@ApiModel(description = "Defines the context data related to the client registration.")
@JsonTypeName("ClientProcessData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class ClientProcessData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object clientData;
    private Object softwareStatement;
    private Object existingClientData;

    public ClientProcessData() {
    }

    /**
     * Client Registration Data. Mandatory for pre-process-client-creation and pre-process-client-update.
     **/
    public ClientProcessData clientData(Object clientData) {
        this.clientData = clientData;
        return this;
    }


    @ApiModelProperty(
            value = "Client Registration Data. Mandatory for pre-process-client-creation and " +
                    "pre-process-client-update.")
    @JsonProperty("clientData")
    public Object getClientData() {
        return clientData;
    }

    @JsonProperty("clientData")
    public void setClientData(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Parameters of the decoded SSA. Mandatory for pre-process-client-creation, pre-process-client-update and
     * pre-process-client-retrieval.
     **/
    public ClientProcessData softwareStatement(Object softwareStatement) {
        this.softwareStatement = softwareStatement;
        return this;
    }


    @ApiModelProperty(
            value = "Parameters of the decoded SSA. Mandatory for pre-process-client-creation, " +
                    "pre-process-client-update and pre-process-client-retrieval.")
    @JsonProperty("softwareStatement")
    public Object getSoftwareStatement() {
        return softwareStatement;
    }

    @JsonProperty("softwareStatement")
    public void setSoftwareStatement(Object softwareStatement) {
        this.softwareStatement = softwareStatement;
    }

    /**
     * properties of the existing client application. Mandatory for pre-process-client-update.
     **/
    public ClientProcessData existingClientData(Object existingClientData) {
        this.existingClientData = existingClientData;
        return this;
    }


    @ApiModelProperty(value = "properties of the existing client application. Mandatory for pre-process-client-update.")
    @JsonProperty("existingClientData")
    public Object getExistingClientData() {
        return existingClientData;
    }

    @JsonProperty("existingClientData")
    public void setExistingClientData(Object existingClientData) {
        this.existingClientData = existingClientData;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientProcessData clientProcessData = (ClientProcessData) o;
        return Objects.equals(this.clientData, clientProcessData.clientData) &&
                Objects.equals(this.softwareStatement, clientProcessData.softwareStatement) &&
                Objects.equals(this.existingClientData, clientProcessData.existingClientData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientData, softwareStatement, existingClientData);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientProcessData {\n");

        sb.append("    clientData: ").append(toIndentedString(clientData)).append("\n");
        sb.append("    softwareStatement: ").append(toIndentedString(softwareStatement)).append("\n");
        sb.append("    existingClientData: ").append(toIndentedString(existingClientData)).append("\n");
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

