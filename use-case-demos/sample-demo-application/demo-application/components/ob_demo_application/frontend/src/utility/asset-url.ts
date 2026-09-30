/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
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
 * @function resolveAssetUrl
 * @description Turns an asset path from `config.json` into a URL that works wherever the
 * application is served from.
 *
 * The paths in `config.json` are relative, and a relative URL is resolved against whatever the
 * document URL happens to be at the moment the browser reads it. That is not stable here: the
 * application is served at its bare context root (`/api-ob-demo-1.0.0`, with no trailing slash,
 * because the Identity Server redirects the slash away), where a relative `resources/...`
 * resolves against the *server* root and 404s - while on any deeper route it resolves correctly.
 * The result is an asset that appears or not depending on which URL the page was opened at.
 *
 * Anchoring every path to the build's base path removes that dependence. Already absolute URLs
 * are left alone, and applying this twice is harmless.
 *
 * @param {string} path Asset path as it appears in the configuration.
 * @returns {string} A URL relative to the application's base path.
 */
export const resolveAssetUrl = (path: string): string => {
    if (!path) {
        return path;
    }
    const base = import.meta.env.BASE_URL;
    if (/^[a-z][a-z0-9+.-]*:/i.test(path) || path.startsWith("//") || path.startsWith(base)) {
        return path;
    }
    return `${base}${path.replace(/^\.?\/*/, "")}`;
};
