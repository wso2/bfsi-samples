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

/*
 * QuickActionButton component provides a themed, interactive button
 * designed for primary actions, adjusting its size responsively for mobile screens.
 *
 * Params:
 * @param {ReactNode} children - The content inside the button (typically an icon and a name).
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
