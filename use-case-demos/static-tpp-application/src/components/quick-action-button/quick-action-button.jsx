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
import './quick-action-button.scss'
import {useMediaQuery, useTheme} from "@mui/material";

/**
 * A reusable, styled button component optimized for display within quick action lists,
 * which dynamically adjusts its size based on the viewport.
 *
 * This component:
 * 1. Uses the **`useMediaQuery`** hook combined with **`useTheme`** to detect if the screen size is small (`< sm`).
 * 2. Dynamically sets the **width and height** of the internal **`Box`** container for responsiveness:
 * - Small screens: `5rem` width, `6rem` height.
 * - Larger screens: `6rem` width, `7rem` height.
 * 3. Wraps its content (`children`, expected to be the icon and label) within an
 * **Oxygen UI `IconButton`** component and a responsive **`Box`** container.
 *
 * @param {object} props - The component props.
 * @param {React.ReactNode} props.children - The content (typically an icon component and a text label) to be displayed inside the button.
 * @returns {JSX.Element} The rendered quick action button with responsive sizing.
 */
export const QuickActionButton = ({children}) => {

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('sm'));

    const width = isSmallScreen ? "5rem" : "6rem";
    const height = isSmallScreen ? "6rem" : "7rem";

    return (
        <IconButton >
            <Box className='icon-container' sx={{width:width,height:height}}>
                {children}
            </Box>
        </IconButton>
    );
};
