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
import * as React from "react";
import '../components.scss'

interface ActionButtonProps {
    icon?: React.ReactNode;
    name?: string;
    onClick?: (path:string) => void;
}

/**
 * A highly reusable and styled action button component designed for quick navigation or tasks.
 * It renders an `IconButton` displaying an optional `icon` and a text `name`.
 *
 * When clicked, it triggers the provided `onClick` handler, passing the button's `name`
 * (converted to lowercase) as the navigation path argument. It renders nothing if no `onClick` handler is provided.
 */
const QuickActionButton = ({icon,name, onClick} : ActionButtonProps)=>{

    if (!onClick) return null;

    const isDisabled = name === "Payments"? false : true;

    return (
        <>
            <IconButton className={'action-button'} disabled={isDisabled} onClick={()=> onClick(`${name?.toLowerCase()}`)}>
                {icon}
                <p>{name}</p>
            </IconButton>
        </>
    );
}

export default QuickActionButton;
