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
 * Defines the context related to the pre-process file upload request body.
 */
@JsonTypeName("PreProcessFileUploadRequestBody")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class PreProcessFileUploadRequestBody implements Serializable {
    private static final long serialVersionUID = 1L;
    private String requestId;
    private RequestForPreProcessFileUpload data;

    public PreProcessFileUploadRequestBody() {
    }

    /**
     * A unique correlation identifier
     **/
    public PreProcessFileUploadRequestBody requestId(String requestId) {
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
     * Set the data of the pre-process file upload request body.
     **/
    public PreProcessFileUploadRequestBody data(RequestForPreProcessFileUpload data) {
        this.data = data;
        return this;
    }


    @ApiModelProperty(value = "")
    @JsonProperty("data")
    @Valid
    public RequestForPreProcessFileUpload getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(RequestForPreProcessFileUpload data) {
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
        PreProcessFileUploadRequestBody preProcessFileUploadRequestBody = (PreProcessFileUploadRequestBody) o;
        return Objects.equals(this.requestId, preProcessFileUploadRequestBody.requestId) &&
                Objects.equals(this.data, preProcessFileUploadRequestBody.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PreProcessFileUploadRequestBody {\n");

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

