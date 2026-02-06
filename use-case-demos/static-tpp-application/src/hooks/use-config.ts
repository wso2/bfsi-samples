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

import { useQuery} from "@tanstack/react-query";
import type { Config } from "./config-interfaces";
import { api } from "../utility/api";
import {queryClient} from "../utility/query-client.ts";

/**
 * @function useConfig
 * @description A custom hook utilizing **@tanstack/react-query** to fetch the application's
 * primary configuration object (`config.json`). It attempts to retrieve cached data first
 * and, if missing, fetches it via the API, storing the result for future use.
 */
export const useConfig = () =>

    useQuery<Config, Error>({
        queryKey: ["appConfig"],
        queryFn: async () => {
            try {
                const raw = queryClient.getQueryData(["appConfig"]) as Config;
                if (raw) {
                    return raw;
                }
            } catch {
                console.log("Error getting config query");
            }
            const res = await api.get<Config>("config.json");
            const data = ((res as any)?.data ?? res) as Config;
            try {
                queryClient.setQueryData(["appConfig"],data as Config);
            } catch {
                console.log("Error getting config query");
            }
            return data;
        },
        staleTime: Infinity,
    });
