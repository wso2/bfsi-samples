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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.openbanking.fdx.identity.dcr.utils.FDXDurationTypesEnum;
import org.wso2.openbanking.fdx.identity.dcr.validation.annotation.ValidateDurationType;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator class for duration type validation.
 */
public class DurationTypeValidator implements ConstraintValidator<ValidateDurationType, List<String>> {

    private static final Log log = LogFactory.getLog(DurationTypeValidator.class);

    @Override
    public boolean isValid(List<String> durationTypes, ConstraintValidatorContext constraintValidatorContext) {

        List<String> allowedDurationTypes = FDXDurationTypesEnum.getAllDurationTypes();

        for (String durationType: durationTypes) {
            if (!allowedDurationTypes.contains(durationType)) {
                log.error(String.format("Invalid duration type: %s" , durationType));
                return false;
            }
        }
        return true;
    }
}

