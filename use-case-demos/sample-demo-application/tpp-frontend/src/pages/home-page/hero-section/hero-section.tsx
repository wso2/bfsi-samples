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

import "../home.scss"
// @ts-ignore
import {ArrowLeftArrowRightIcon, UserGroupIcon, ClockAsteriskIcon, BoltIcon} from '@oxygen-ui/react-icons';
import {Box, Grid} from "@oxygen-ui/react";
import {useMediaQuery, useTheme} from "@mui/material";
import { useNavigate} from "react-router-dom";
import type {AppInfo, User} from "../../../hooks/config-interfaces.ts";
import QuickActionButton from "../../../components/quick-action-button/quick-action-button.tsx";

/**
 * A responsive dashboard header component that displays a personalized, time-of-day-sensitive
 * greeting and user avatar. It renders a row of quick-access action buttons (Payments, Transfer, etc.)
 * which navigate the user based on the configured app route. The layout adapts for mobile screens.
 */

interface ActionButton {
    icon: React.ReactNode;
    name: string;
}

interface HeroSectionProps {
    userInfo: User;
    appInfo: AppInfo;
}

/**
 * @function HeroSection
 * @description The top dashboard component that displays a personalized, time-sensitive greeting
 * and a row of quick-access action buttons (e.g., Pay Bills, Transfer).
 * The layout and component rendering are dynamically adjusted based on screen size (responsiveness).
 */
const HeroSection = ({userInfo, appInfo}:HeroSectionProps) => {
    const isLargeScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const navigate = useNavigate();
    const responsiveDirections = isLargeScreen ? 'column' : 'row';
    const responsiveMinHeight = isLargeScreen ? '16vh' : '8vh';
    const responsiveDisplay = isLargeScreen ? 'none' : 'flex';
    const responsivePadding = isLargeScreen ? '1rem' : '2rem';
    const actionButtons: ActionButton[] = [
        {icon: <BoltIcon size={'medium'}/>, name: "Pay Bills"},
        {icon: <ArrowLeftArrowRightIcon size={'medium'}/>, name: "Transfer"},
        {icon: <ClockAsteriskIcon size={'medium'}/>, name: "Schedule"},
        {icon: <UserGroupIcon size={'medium'}/>, name: "Payees"},
    ];

    const onClickHandlerActionButtons = (pathTo:string)=>{
        const absolutePath = "/"+appInfo.route+"/"+pathTo;
        navigate(absolutePath);
    }
    const greetingSelection = () => {
        const currentHour = new Date().getHours();
        if (currentHour >= 5 && currentHour < 12) {
            return ", Good Morning!";
        } else if (currentHour >= 0 && currentHour < 5) {
            return "";
        } else if (currentHour >= 12 && currentHour < 18) {
            return  ", Good Afternoon!";
        } else {
            return  ", Good Evening!";
        }
    }

    return (
        <>
            <Grid container className='hero-outer' direction={responsiveDirections} sx={{padding: responsivePadding, backgroundImage: `url(${userInfo.background})`}}>
                <Grid className='hero-inner-secton user-info'>
                    <Box className='avatar-container' sx={{display: responsiveDisplay}}>
                        <img src={userInfo.image} alt='avatar' className='avatar' />
                    </Box>
                    <p>Hello,<br/><span>{userInfo.name}{greetingSelection()}</span></p>
                </Grid>
                <Grid className='hero-inner-secton actions' sx={{minHeight: responsiveMinHeight}}>
                    {actionButtons.map((button, index) => {
                        return (
                            <QuickActionButton icon={button.icon} name={button.name} key={index} onClick={onClickHandlerActionButtons}/>
                        );
                    })}
                </Grid>
            </Grid>
        </>
    );
}

export default HeroSection;
