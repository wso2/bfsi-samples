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
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Defines the context related to the success persist authorized consent with amendments data.
 */
@JsonTypeName("SuccessPersistAuthorizedConsentWithAmendmentsData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04" +
        "-25T07:03:50.021891+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class SuccessPersistAuthorizedConsentWithAmendmentsData implements Serializable {
    private static final long serialVersionUID = 1L;
    private ConsentPersistenceSuccessDataWithAmendments data;

    public SuccessPersistAuthorizedConsentWithAmendmentsData() {
    }

    /**
     * Set the data of the success persist authorized consent with amendments data.
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

