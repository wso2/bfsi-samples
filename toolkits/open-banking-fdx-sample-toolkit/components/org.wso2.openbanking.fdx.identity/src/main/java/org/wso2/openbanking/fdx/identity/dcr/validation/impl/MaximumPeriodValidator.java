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

package org.wso2.openbanking.fdx.identity.dcr.validation.impl;

import com.wso2.openbanking.accelerator.identity.dcr.validation.DCRCommonConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.openbanking.fdx.common.config.OpenBankingFDXConfigParser;
import org.wso2.openbanking.fdx.identity.dcr.constants.FDXValidationConstants;
import org.wso2.openbanking.fdx.identity.dcr.model.FDXRegistrationRequest;
import org.wso2.openbanking.fdx.identity.dcr.validation.annotation.ValidateMaximumPeriod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator class for validating the Duration Period and Lookback Period parameters.
 */
public class MaximumPeriodValidator implements ConstraintValidator<ValidateMaximumPeriod, FDXRegistrationRequest> {

    private static final Log log = LogFactory.getLog(MaximumPeriodValidator.class);

    @Override
    public boolean isValid(FDXRegistrationRequest fdxRegistrationRequest, ConstraintValidatorContext
            constraintValidatorContext) {

        //get duration period and lookback period from registration request
        Integer durationPeriod = fdxRegistrationRequest.getDurationPeriod();
        Integer lookbackPeriod = fdxRegistrationRequest.getLookbackPeriod();

        //get maximum duration period and maximum lookback period from configs
        String maximumDurationPeriodStr = (String) OpenBankingFDXConfigParser.getInstance()
                .getConfiguration(FDXValidationConstants.DCR_MAXIMUM_DURATION_PERIOD);
        String maximumLookbackPeriodStr = (String) OpenBankingFDXConfigParser.getInstance()
                .getConfiguration(FDXValidationConstants.DCR_MAXIMUM_LOOKBACK_PERIOD);

        //validate duration period and look back period
        return validatePeriod(durationPeriod, maximumDurationPeriodStr, "Duration period",
                        constraintValidatorContext) &&
               validatePeriod(lookbackPeriod, maximumLookbackPeriodStr, "Lookback period",
                        constraintValidatorContext);
    }

    private boolean validatePeriod(Integer requestedPeriod, String maximumPeriodStr, String attributeName,
                                   ConstraintValidatorContext context) {

        try {
           //check whether maximum requestedPeriod value is provided as configs
           if (requestedPeriod != null && StringUtils.isNotEmpty(maximumPeriodStr)) {
               int maximumPeriod = Integer.parseInt(maximumPeriodStr);
               if (requestedPeriod > maximumPeriod) {
                   context.disableDefaultConstraintViolation();
                   log.error("Requested duration period/lookback period exceeds the maximum allowed period");
                   context.buildConstraintViolationWithTemplate(attributeName + " should not exceed " + maximumPeriod
                           + " days :" + DCRCommonConstants.INVALID_META_DATA).addConstraintViolation();
                   return false;
               }
           }
       } catch (NumberFormatException e) {
           log.error("Error while resolving configs", e);
           return false;
       }
       return true;
    }
}

