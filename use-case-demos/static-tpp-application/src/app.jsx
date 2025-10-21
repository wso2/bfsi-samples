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
import AppThemeProvider from "./providers/app-theme-provider.jsx";
import Home from "./pages/home-page/home.jsx";
import useConfigContext from "./hooks/use-config-context.js";
import {Suspense} from "react";
import {useTranslation} from "react-i18next";

/*
 * App component serves as the main entry point for the application structure.
 * It manages the loading of application configuration via useConfigContext,
 * initializes the theme provider, and sets up client-side routing using React Router.
 */
function App() {

    const context = useConfigContext();
    const {t, i18n} = useTranslation();

    if (context.isLoading || !context.config) {
        return <div>Loading configuration...</div>;
    }

    return (<>
        <Suspense fallback={<div>Loading configuration...</div>}>
            <AppThemeProvider>
                <Routes>
                    <Route path={`/${context.config.route}/*`} element={
                        <Routes>
                            <Route path="home" element={<Home configurations={context}/>}/>
                        </Routes>
                    }/>
                </Routes>
            </AppThemeProvider>
        </Suspense>
    </>)
}

export default App

