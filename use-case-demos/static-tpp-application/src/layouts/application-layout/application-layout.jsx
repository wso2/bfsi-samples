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
import {useContext} from "react";
import ConfigContext from "../../providers/config-context.jsx";
import Header from "../../components/header/header.jsx";

/**
 * A standard application layout component that establishes a persistent **header**
 * and a main content area for rendering child components.
 *
 * It consumes the **`ConfigContext`** and passes the entire context object to the **`<Header />`**
 * component, enabling the header to dynamically render application details (like the name).
 * The main content is rendered within the `product-content-outer` div using the **`children`** prop.
 *
 * @param {object} props - The component props.
 * @param {React.ReactNode} props.children - The content (views or components) to be displayed in the main body of the application.
 * @returns {JSX.Element} The rendered application shell with a header and content area.
 */
export const ApplicationLayout = ({children}) => {

    const context = useContext(ConfigContext);

    return (
        <>
            <Header context={context}/>
            <div className="product-content-outer">
                {children}
            </div>
        </>
    );
}

export default ApplicationLayout;

