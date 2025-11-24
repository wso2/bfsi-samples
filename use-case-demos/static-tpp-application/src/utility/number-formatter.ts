/**
 * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
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

/**
 * @function formatCurrency
 * @description Safely formats a number, string, null, or undefined value into a currency string
 * using 'en-US' locale and ensuring exactly two decimal places.
 * Invalid inputs are defaulted to '0.00'.
 */
export const formatCurrency = (value: number | string | null | undefined): string => {

    let numericValue = parseFloat(String(value));

    if (isNaN(numericValue)) {
        numericValue = 0;
        console.warn(`Attempted to format non-numeric value: ${value}. Defaulted to 0.`);
    }

    return numericValue.toLocaleString('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
};
