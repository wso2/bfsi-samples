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

import {Route, Routes} from "react-router-dom";
import Home from "./pages/home-page/home.jsx";
import {useEffect, useState} from "react";
import {api} from "./api.js";
import AppThemeProvider from "./providers/app-theme-provider.jsx";
import useConfigContext from "./hooks/use-config-context.js";

/**
 * The root component of the application, responsible for initial setup,
 * fetching global theme configurations, managing the loading state, and
 * defining the top-level routing structure.
 *
 * It uses a `useEffect` hook to fetch theme configurations once on mount
 * via the `api.get` utility. While loading or if configurations are unavailable,
 * it displays a loading screen.
 *
 * Once loaded, it renders the primary route for the '/accounts-central/*' path,
 * wrapping the rest of the application's routes and components with the
 * `OxygenThemeProvider` to apply dynamic styling based on the fetched configurations.
 */
function App() {

    const {config:configurations} = useConfigContext()

    if (!configurations) {
        return <div>Loading Application Configuration...</div>;
    }

    console.log(configurations);

    return (<>
        <AppThemeProvider>
            <Routes>
                <Route path={`/${configurations.name.route}/*`} element={
                    <Routes>
                        <Route path="home" element={<Home configurations={configurations} />}/>
                    </Routes>
                }/>
            </Routes>
        </AppThemeProvider>
    </>)
}

export default App

