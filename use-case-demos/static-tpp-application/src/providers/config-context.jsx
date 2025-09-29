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
import {api} from "../api.js";

const ConfigContext = createContext();

/**
 * A React provider component that fetches application configurations and makes them
 * available to all child components via the Context API.
 * * It manages a loading state and fetches data from '/configurations/config.json'
 * only once when the component mounts. While loading, it displays a "Loading..." message.
 * * @param {object} props - The component props.
 * @param {React.ReactNode} props.children - The child components to be rendered within the provider's scope.
 */
//TODO:this file will be remove and move this context to custom hook in next PR
export const ConfigProvider = ({children}) => {
    const [configs, setConfig] = useState({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const getConfig = async () => {
            try {
                const response = await api.get("config.json");
                setConfig(response.configurations);
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
    return (<ConfigContext.Provider value={{configs, loading}}>{children}</ConfigContext.Provider>);
}

export default ConfigContext;

