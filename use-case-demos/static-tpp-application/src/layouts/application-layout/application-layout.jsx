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

import "./application-layout.scss"
import Header from "../../components/header/header.jsx";

/*
 * ApplicationLayout component defines the main structure of the application page,
 * including the navigation header and a container for the page content.
 *
 * Params:
 * @param {ReactNode} children - The main content of the page (e.g., Home Content Layout).
 * @param {string} name - The application name to be displayed in the header.
 */
export const ApplicationLayout = ({children, name}) => {

    return (
        <>
            <Header name={name}/>
            <div className="product-content-outer">
                {children}
            </div>
        </>
    );
}

export default ApplicationLayout;

