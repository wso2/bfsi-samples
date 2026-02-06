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
 * @namespace api
 * @description A reusable service client for fetching JSON data from the configured
 * base URL (`/configurations`). It wraps the native `fetch` API, handles basic
 * HTTP error checking, and uses TypeScript generics for type-safe data retrieval.
 */
// export const baseUrl = 'base url for config json file location';
export const baseUrl = './configurations';

/**
 * Asynchronously fetches JSON data from a specific API endpoint.
 *
 * @param {string} endpoint The specific path to append to the baseUrl (e.g., 'users/1').
 * @param {RequestInit} [options] Optional configuration object for the `fetch` request (e.g., headers, method).
 * @returns {Promise<any>} A promise that resolves with the parsed JSON response body.
 * @throws {Error} Throws an error if the network request fails or the HTTP response status is not OK (200-299).
 */
const fetchData = async (endpoint:string, options?:RequestInit)=>{

    const url = `${baseUrl}/${endpoint}`;
    try{
        const response = await fetch(url,options);
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return await response.json();
    }catch (e) {
        console.error(`error in fetchData: ${e}`);
        throw e;
    }
}

interface ApiService {
    get: <T>(endpoint:string) => Promise<T>;
}
export const api: ApiService = {
    get: <T>(endpoint: string): Promise<T> => fetchData(endpoint),
}

