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

import I18NextHttpBackend from "i18next-http-backend";
import i18n from "i18next";
import {initReactI18next} from "react-i18next";

/*
 * i18n configuration file initializes the i18next library for internationalization.
 * It sets up the HTTP backend to load translation files and configures React integration.
 *
 * Configures:
 * - Fallback language ('en')
 * - Namespace ('translation')
 * - Backend load path for JSON files
 */
i18n.use(I18NextHttpBackend).use(initReactI18next).init({
    fallbackLng: 'en',
    ns: ['translation'],
    defaultNS: 'translation',
    backend: {
        loadPath: '/locales/{{lng}}/{{ns}}.json',
    },
    react:{
        useSuspense: true,
    },
    debug: true,
    interpolation: {
        escapeValue: false
    }
})

export default i18n;
