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

import {Box} from "@oxygen-ui/react";
import './home-content-layout.scss'

/*
 * HomeContentLayout component serves as a simple wrapper/container
 * for the main content blocks displayed on the Home dashboard.
 *
 * Params:
 * @param {ReactNode} children - The content (e.g., summary cards, bank lists) to be rendered inside the layout.
 */
const HomeContentLayout = ({children}) =>{
    return (
        <>
            <Box className='home-content-outer'>
                {children}
            </Box>
        </>
    );
}

export default HomeContentLayout;
