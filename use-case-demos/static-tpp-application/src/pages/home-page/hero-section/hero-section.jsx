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

import "./hero-section.scss"
import {QuickActionButton} from "../../../components/quick-action-button/quick-action-button.jsx";
import {ArrowLeftArrowRightIcon, UserGroupIcon, ClockAsteriskIcon, BoltIcon} from '@oxygen-ui/react-icons';
import {Box, Grid} from "@oxygen-ui/react";
import {useMediaQuery, useTheme} from "@mui/material";
import {t} from "i18next";

/*
 * HeroSection component displays a personalized welcome banner with user details,
 * a dynamic greeting based on the time of day, and quick action buttons.
 *
 * Params:
 * @param {object} userInfo - User profile information (name, image, background URL).
 */
const onclickAction = () => {
    console.log("Quick action action button clicked");
}

const quickActionsButtons = [
    {icon: BoltIcon, name: "Payments", onClick: onclickAction},
    {icon: ArrowLeftArrowRightIcon, name: "Transfer", onClick: onclickAction},
    {icon: ClockAsteriskIcon, name: "Schedule", onClick: onclickAction},
    {icon: UserGroupIcon, name: "Payees", onClick: onclickAction}
];

const greetingSelection = () => {
    const currentHour = new Date().getHours();

    if (currentHour >= 5 && currentHour < 12) {
        return `, ${t('good_morning')}`;
    } else if (currentHour >= 0 && currentHour < 5) {
        return "";
    } else if (currentHour >= 12 && currentHour < 18) {
        return  `, ${t('good_afternoon')}`;
    } else {
        return  `, ${t('good_evening')}`;
    }
}

const HeroSection = ({userInfo}) => {

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));

    if (!userInfo) {
        return <div>Loading....</div>
    }

    const displayStyle = isSmallScreen ? 'none' : 'flex';
    const containerHeight = isSmallScreen ? 'fit-content' : '14rem';

    return (
        <>
            <Grid container className="outer-container" style={{background: `url(${userInfo.background}) lightgray -18.644px -372.574px / 145.345% 527.151% no-repeat`}}>
                <Grid className='user-info' style={{height:containerHeight}} xs={12} md={6}>
                    <Box className='user-profile'  sx={{display: displayStyle}}>
                        <img src={userInfo.image} alt="" className='profile-image' />
                    </Box>
                    <Box className='user-details'>
                        <p>{t('hello')},</p>
                        <p>{userInfo.name} {greetingSelection()}</p>
                    </Box>
                </Grid>
                <Grid className='quick-actions' xs={12} md={6}>
                    {quickActionsButtons.map((action, index) => {
                        const IconComponent = action.icon;
                        return (<QuickActionButton key={index} onClick={action.onClick}>
                            <IconComponent size={24} />
                            {action.name}
                        </QuickActionButton>)})}
                </Grid>
            </Grid>
        </>
    )
}

export default HeroSection;
