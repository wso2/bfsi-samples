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

import {IconButton} from "@oxygen-ui/react";
import {ArrowRightFromBracketIcon} from "@oxygen-ui/react-icons";
import './header.css'

/**
 * A dedicated component for rendering the application's top header bar.
 *
 * It dynamically displays the application's title, which is retrieved from the
 * **`context`** prop (`context.routerName.applicationName`). It also renders a static
 * **logout icon button** using Oxygen UI components (`IconButton` and `ArrowRightFromBracketIcon`)
 * for user exit functionality.
 *
 * @param {object} props - The component props.
 * @param {object} props.context - The configuration context object (from `ConfigContext`) containing application details like `routerName.applicationName`.
 * @returns {JSX.Element} The rendered header bar.
 */
const Header = ({context}) => {
    return(
        <div className="product-header-outer">
            <p>{context.routerName.applicationName}</p>
            <IconButton style={{color:'white'}}>
                <ArrowRightFromBracketIcon size={'1.5rem'}/>
            </IconButton>
        </div>
    );
}

export default Header;

