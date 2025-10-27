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

import {createContext, type FC, useEffect, useState} from "react";

export interface AppConfigs {
    [key: string]: any;
}

export interface ConfigContextValue {
    configs: AppConfigs;
    loading: boolean;
}

export interface ConfigProviderProps {
    children: React.ReactNode;
}

export const ConfigContext = createContext<ConfigContextValue | undefined>(undefined);


export const ConfigProvider: FC<ConfigProviderProps> = ({ children }) => {

    const [configs, setConfig] = useState<AppConfigs>({});
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const getConfig = async () => {
            try {
                const response = await fetch("/configurations/config.json");

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const data = await response.json();
                // Assume 'data.configurations' matches the AppConfigs type
                setConfig(data.configurations as AppConfigs);
            } catch (e) {
                const errorMessage = e instanceof Error ? e.message : "An unknown error occurred during configuration fetch";
                console.error(errorMessage);
            } finally {
                setLoading(false);
            }
        }
        getConfig();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    // The value provided must strictly adhere to ConfigContextValue
    const contextValue: ConfigContextValue = { configs, loading };

    return (
        <ConfigContext.Provider value={contextValue}>
            {children}
        </ConfigContext.Provider>
    );
}

export default ConfigContext;
