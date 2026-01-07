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

import { IconButton } from "@oxygen-ui/react";
import type {FC} from "react";
// @ts-ignore
import {ArrowRightFromBracketIcon} from  '@oxygen-ui/react-icons'
import '../components.scss'

export interface HeaderContext {
    routerName: {
        applicationName: string;
    };
}

export interface HeaderProps {
    name: string;
}

/**
 * A basic application header component that displays the application's name/title.
 * It also renders a right-aligned **Logout icon** (`ArrowRightFromBracketIcon`)
 * wrapped in an `IconButton` for user session termination or sign-out functionality.
 */
const Header: FC<HeaderProps> = ({ name }) => {
    return(
        <div className="header-outer">
            <p>{name}</p>
            <IconButton style={{ color: 'white' }}>
                <ArrowRightFromBracketIcon size={24} />
            </IconButton>
        </div>
    );
}

export default Header;
