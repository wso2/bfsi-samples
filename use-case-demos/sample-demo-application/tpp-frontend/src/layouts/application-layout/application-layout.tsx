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

import {type FC, type ReactNode} from "react";
import Header from "../../components/header/header";

export interface ApplicationLayoutProps {
    name: string;
    children: ReactNode;
}

/**
 * @function ApplicationLayout
 * @description The highest-level wrapper component that provides the consistent
 * overall structure for the application. It renders the global `Header`
 * and contains the main content (`children`) of the current page below it.
 */
export const ApplicationLayout: FC<ApplicationLayoutProps> = ({ name,children }) => {
    return (
        <>
            <Header name={name} />
            <div className="product-content-outer">
                {children}
            </div>
        </>
    );
};

export default ApplicationLayout;
