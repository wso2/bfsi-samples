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

import {createContext, useEffect, useState} from "react";

/**
 * Creates the React Context object used to share configuration data across the component tree.
 */
const ConfigContext = createContext();

/**
 * A central React Provider component responsible for **asynchronously fetching** global application configurations.
 *
 * It manages the **loading state**, the application's main **configs** object, and configured **routerName** data.
 * The configuration is fetched once from `/configurations/config.json` on component mount.
 *
 * - **Loading State**: Displays a "Loading..." message until the data fetch is complete.
 * - **Context Value**: Exposes `configs`, the `loading` status, and the `routerName` (derived from the fetched name).
 *
 * @param {object} props - The component props.
 * @param {React.ReactNode} props.children - The child components that will consume the provided context values.
 */
// TODO: This Provider component is marked for removal and its logic is planned to be migrated to a custom hook in a future PR.
export const ConfigProvider = ({children}) => {
    const [configs, setConfig] = useState({});
    const [configuredNames, setConfiguredNames] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const getConfig = async () => {
            try {
                const response = await fetch("/configurations/config.json");
                const data = await response.json();
                setConfig(data.configurations);
                setConfiguredNames(data.name)
            } catch (e) {
                console.log(e.message);
            } finally {
                setLoading(false);
            }
        }
        getConfig();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }
    return (<ConfigContext.Provider value={{configs, loading, routerName: configuredNames}}>{children}</ConfigContext.Provider>);
}

export default ConfigContext;

