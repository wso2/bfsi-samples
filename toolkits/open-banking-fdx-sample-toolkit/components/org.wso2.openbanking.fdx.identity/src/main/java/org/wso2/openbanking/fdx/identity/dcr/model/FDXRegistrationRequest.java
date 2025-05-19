/**
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.openbanking.fdx.identity.dcr.model;

import com.google.gson.annotations.SerializedName;
import com.wso2.openbanking.accelerator.identity.dcr.model.RegistrationRequest;
import com.wso2.openbanking.accelerator.identity.dcr.validation.DCRCommonConstants;
import com.wso2.openbanking.accelerator.identity.dcr.validation.validationgroups.AttributeChecks;
import com.wso2.openbanking.accelerator.identity.dcr.validation.validationgroups.MandatoryChecks;
import org.wso2.openbanking.fdx.identity.dcr.validation.annotation.ValidateDurationPeriod;
import org.wso2.openbanking.fdx.identity.dcr.validation.annotation.ValidateDurationType;
import org.wso2.openbanking.fdx.identity.dcr.validation.annotation.ValidateMaximumPeriod;
import org.wso2.openbanking.fdx.identity.dcr.validation.annotation.ValidateScopes;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/**
 * Model class for FDX dcr registration request.
 */
@ValidateMaximumPeriod(message = "Invalid duration period or lookback period:" + DCRCommonConstants.INVALID_META_DATA,
        groups = AttributeChecks.class)
@ValidateDurationPeriod(message = "Duration period is required for time_bound duration type:" +
        DCRCommonConstants.INVALID_META_DATA, groups = AttributeChecks.class)
public class FDXRegistrationRequest extends RegistrationRequest {

    @SerializedName("client_name")
    private String clientName;

    @SerializedName("description")
    private String description;

    @SerializedName("logo_uri")
    private String logoUri;

    @SerializedName("client_uri")
    private String clientUri;

    @SerializedName("contacts")
    private List<String> contacts;

    @SerializedName("duration_type")
    private List<String> durationType;

    @SerializedName("duration_period")
    private Integer durationPeriod;

    @SerializedName("lookback_period")
    private Integer lookbackPeriod;

    @SerializedName("registry_references")
    @Valid
    private List<RegistryReference> registryReferences;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getClientUri() {
        return clientUri;
    }

    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    @ValidateDurationType(message = "Invalid duration type requested:" +
            DCRCommonConstants.INVALID_META_DATA, groups = AttributeChecks.class)
    public List<String> getDurationType() {
        return durationType;
    }

    public void setDurationType(List<String> durationType) {
        this.durationType = durationType;
    }

    @Positive(message = "Duration Period cannot be zero or negative:" +
            DCRCommonConstants.INVALID_META_DATA, groups = AttributeChecks.class)
    public Integer getDurationPeriod() {
        return durationPeriod;
    }

    public void setDurationPeriod(Integer durationPeriod) {
        this.durationPeriod = durationPeriod;
    }

    @Positive(message = "Lookback Period cannot be zero or negative:" +
            DCRCommonConstants.INVALID_META_DATA, groups = AttributeChecks.class)
    public Integer getLookbackPeriod() {
        return lookbackPeriod;
    }

    public void setLookbackPeriod(Integer lookbackPeriod) {
        this.lookbackPeriod = lookbackPeriod;
    }

    public List<RegistryReference> getRegistryReferences() {
        return registryReferences;
    }

    public void setRegistryReferences(List<RegistryReference> registryReferences) {
        this.registryReferences = registryReferences;
    }

    @NotBlank(message = "Required parameter Client Name cannot be null or empty:" +
            DCRCommonConstants.INVALID_META_DATA, groups = MandatoryChecks.class)
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    @NotEmpty(message = "Required parameter Redirect URIs can not be null or empty:" +
            DCRCommonConstants.INVALID_META_DATA, groups = MandatoryChecks.class)
    public List<String> getCallbackUris() {
        return super.getCallbackUris();
    }

    @Override
    @ValidateScopes(message = "Invalid scope requested:" +
            DCRCommonConstants.INVALID_META_DATA, groups = AttributeChecks.class)
    public String getScope() {
        return super.getScope();
    }
}

