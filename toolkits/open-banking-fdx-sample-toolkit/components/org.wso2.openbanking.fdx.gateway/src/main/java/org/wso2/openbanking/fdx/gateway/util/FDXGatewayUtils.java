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

package org.wso2.openbanking.fdx.gateway.util;

import com.wso2.openbanking.accelerator.common.error.OpenBankingErrorCodes;
import com.wso2.openbanking.accelerator.gateway.executor.model.OBAPIRequestContext;
import com.wso2.openbanking.accelerator.gateway.executor.model.OpenBankingExecutorError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.UUID;

 /**
  *  FDX Gateway Utils.
  */
public class FDXGatewayUtils {

     private static final Log log = LogFactory.getLog(FDXGatewayUtils.class);

     /**
      * Checks if the given string is a valid UUID.
      *
      * @param uuidString the string to validate
      * @return true if the string is a valid UUID, false otherwise
      */
    public static boolean isValidUUID(String uuidString) {

        try {
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

     /**
      * Method to handle invalid header fields error related to API requests.
      *
      * @param obapiRequestContext  Context of the Open Banking API request.
      * @param message              Error message describing the cause of the bad request.
      */
     public static void handleInvalidHeaderFieldsError(OBAPIRequestContext obapiRequestContext, String message) {

         OpenBankingExecutorError error = new OpenBankingExecutorError(OpenBankingErrorCodes.BAD_REQUEST_CODE,
                 "invalid_header_fields", message, String.valueOf(HttpStatus.SC_BAD_REQUEST));
         ArrayList<OpenBankingExecutorError> executorErrors = obapiRequestContext.getErrors();
         executorErrors.add(error);
         obapiRequestContext.setError(true);
         obapiRequestContext.setErrors(executorErrors);
     }
}

