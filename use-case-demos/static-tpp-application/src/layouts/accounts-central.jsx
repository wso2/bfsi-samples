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

import "./accounts-central.css"
import {IconButton} from "@oxygen-ui/react";
import {ArrowRightFromBracketIcon} from "@oxygen-ui/react-icons";

/**
 * A layout component that establishes the standard structure for the 'Accounts Central' product view.
 *
 * It consumes the **`ConfigContext`** to dynamically display the application's name
 * (from `context.routerName.applicationName`) in the header.
 * The fixed product header also includes an **`IconButton`** from Oxygen UI
 * with a logout icon (`ArrowRightFromBracketIcon`).
 * The main content area is populated by the **`children`** prop.
 *
 * @param {object} props - The component props.
 * @param {React.ReactNode} props.children - The content (usually nested routes or components) to be rendered in the main product content area.
 * @returns {JSX.Element} The rendered layout with dynamic header and content area.
 */
export const AccountsCentral = ({configuration, children}) => {

    return (
        <>
            <div className="product-header-outer">
                <p>{configuration.name.applicationName}</p>
                <IconButton style={{color:'white'}}>
                    <ArrowRightFromBracketIcon size={'1.5rem'}/>
                </IconButton>
            </div>

            <div className="product-content-outer">
                {children}
            </div>
        </>
    );
}

export default AccountsCentral;

