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

import {Box, IconButton} from "@oxygen-ui/react";
import {ActionButtonContentOuter} from "../styled-components/styled-containers.jsx";
import './quick-action-button.scss'

/**
 * A reusable, styled button component optimized for display within quick action lists.
 *
 * It uses the **Oxygen UI `IconButton`** component for its base structure and wraps the content
 * with the **`ActionButtonContentOuter`** styled component to define the custom layout.
 * The component expects the **`children`** prop to be an array containing exactly two elements,
 * which are destructured into:
 * 1. The **icon** element (`iconElement`).
 * 2. The **text label** element (`nameText`).
 *
 * Note: The `onClick` prop has been removed from the component signature, as the functionality
 * is now inherited by the underlying `IconButton` (assuming the `IconButton` handles the click).
 *
 * @param {object} props - The component props.
 * @param {Array<React.ReactNode>} props.children - An array where the first element is the icon component and the second is the name text/label.
 * @returns {JSX.Element} The rendered quick action button.
 */
export const QuickActionButton = ({children}) => {

    const [iconElement, nameText] = children;

    return (
        <IconButton >
            <Box className='icon-container'>
                {iconElement}
                {nameText}
            </Box>
        </IconButton>
    );
};
