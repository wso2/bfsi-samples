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

import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.scss'
import App from './app.jsx'
import {BrowserRouter} from "react-router-dom";
import './i18n.js';

/**
 * The main entry point for the React application.
 * It initializes the React root, wrapping the entire application in:
 * 1. `StrictMode` for development-time checks and warnings.
 * 2. `BrowserRouter` to enable client-side routing.
 * 3. `ConfigProvider` to handle global configuration fetching and state management.
 *
 * It finally renders the top-level `App` component, providing it access to the router and global configurations.
 */
createRoot(document.getElementById('root')).render(
    <StrictMode>
        <BrowserRouter>
            <App/>
        </BrowserRouter>
    </StrictMode>,
)

