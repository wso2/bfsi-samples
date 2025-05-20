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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines the context data related to the consent search data.
 */
@JsonTypeName("ConsentSearchData")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
        date = "2025-05-07T09:57:13.986407+05:30[Asia/Colombo]", comments = "Generator version: 7.12.0")
public class ConsentSearchData implements Serializable {
    private static final long serialVersionUID = 1L;

    private SearchTypeEnum searchType;
    private Object enrichmentParams;
    private Object searchResult;

    public ConsentSearchData() {
    }

    /**
     * Set the search type of the consent search data
     **/
    public ConsentSearchData searchType(SearchTypeEnum searchType) {
        this.searchType = searchType;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("searchType")
    public SearchTypeEnum getSearchType() {
        return searchType;
    }

    @JsonProperty("searchType")
    public void setSearchType(SearchTypeEnum searchType) {
        this.searchType = searchType;
    }

    /**
     * query params
     **/
    public ConsentSearchData enrichmentParams(Object enrichmentParams) {
        this.enrichmentParams = enrichmentParams;
        return this;
    }

    /**
     * payload
     **/
    public ConsentSearchData searchResult(Object searchResult) {
        this.searchResult = searchResult;
        return this;
    }


    @ApiModelProperty(value = "payload")
    @JsonProperty("searchResult")
    public Object getSearchResult() {
        return searchResult;
    }

    @JsonProperty("searchResult")
    public void setSearchResult(Object searchResult) {
        this.searchResult = searchResult;
    }

    @ApiModelProperty(value = "query params")
    @JsonProperty("enrichmentParams")
    public Object getEnrichmentParams() {
        return enrichmentParams;
    }

    @JsonProperty("enrichmentParams")
    public void setEnrichmentParams(Object enrichmentParams) {
        this.enrichmentParams = enrichmentParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConsentSearchData consentSearchData = (ConsentSearchData) o;
        return Objects.equals(this.searchType, consentSearchData.searchType) &&
                Objects.equals(this.searchResult, consentSearchData.searchResult) &&
                Objects.equals(this.enrichmentParams, consentSearchData.enrichmentParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchType, searchResult, enrichmentParams);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentSearchData {\n");

        sb.append("    searchType: ").append(toIndentedString(searchType)).append("\n");
        sb.append("    searchResult: ").append(toIndentedString(searchResult)).append("\n");
        sb.append("    enrichmentParams: ").append(toIndentedString(enrichmentParams)).append("\n");
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

    /**
     * The search type of the consent search data
     **/
    public enum SearchTypeEnum {

        BULK_SERACH(String.valueOf("BULK_SERACH")), AMENDMENT_HISTORY(String.valueOf("AMENDMENT_HISTORY"));


        private String value;

        SearchTypeEnum(String v) {
            value = v;
        }

        /**
         * Convert a String into String, as specified in the
         * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">
         * See JAX RS 2.0 Specification, section 3.2, p. 12</a>
         */
        public static SearchTypeEnum fromString(String s) {
            for (SearchTypeEnum b : SearchTypeEnum.values()) {
                // using Objects.toString() to be safe if value type non-object type
                // because types like 'int' etc. will be auto-boxed
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        @JsonCreator
        public static SearchTypeEnum fromValue(String value) {
            for (SearchTypeEnum b : SearchTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        public String value() {
            return value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }


}

