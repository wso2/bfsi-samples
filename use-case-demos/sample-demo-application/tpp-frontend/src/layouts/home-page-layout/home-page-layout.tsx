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

import type {AppInfo, User} from "../../hooks/config-interfaces.ts";
import {Grid, useMediaQuery, useTheme} from "@mui/material";
import {Box} from "@oxygen-ui/react";
import HeroSection from "../../pages/home-page/hero-section/hero-section.tsx";

interface HomePageLayoutProps {
    children?: React.ReactNode;
    userInfo: User;
    appInfo: AppInfo;
}

/**
 * @function HomePageLayout
 * @description The structural component for the application's main dashboard.
 * It consistently places the **HeroSection** (user greeting and quick actions)
 * at the top and wraps the main content (`children`) below it, applying responsive padding.
 */
const HomePageLayout = ({children,userInfo, appInfo}:HomePageLayoutProps)=>{
    const isLargeScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isLargeScreen ? "2rem 1rem": "2rem 5rem";

    return(
        <>
            <Box className='home-page-outer'>
                <HeroSection userInfo={userInfo} appInfo={appInfo}/>
                <Grid container direction={'column'}sx={{padding: responsivePadding}} className="content-outer">
                    {children}
                </Grid>
            </Box>
        </>
    );
}

export default HomePageLayout;
