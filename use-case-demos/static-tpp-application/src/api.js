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

/*
 * Utility function for standardizing API calls against the base URL,
 * applying default headers, and centralizing error handling.
 *
 * Params:
 * @param {string} endpoint - The resource path relative to the base URL (e.g., 'users' or 'data/items').
 * @param {object} [options={}] - Custom fetch options (e.g., method, body, custom headers).
 * @returns {Promise<object>} The JSON response body.
 *
 */
const baseUrl = "/configurations"

async function fetchData(endpoint, options = {}) {

    const url = `${baseUrl}/${endpoint}`;

    const defaultHeaders = {
        'Content-Type': 'application/json',
    }

    const config = {
        ...options, headers: {
            ...defaultHeaders, ...options.headers,
        },
    };

    try {
        const response = await fetch(url, config);

        return await response.json();

    } catch (error) {
        console.error(`API Error for ${endpoint}:`, error);
        throw error;
    }
}

export const api = {
    get: (endpoint, options = {}) => fetchData(endpoint, {method: 'GET', ...options}),
}

