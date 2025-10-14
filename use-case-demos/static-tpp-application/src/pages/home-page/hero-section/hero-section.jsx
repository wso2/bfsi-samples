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
        return ", Good Morning!";
    } else if (currentHour >= 0 && currentHour < 5) {
        return "";
    } else if (currentHour >= 12 && currentHour < 18) {
        return  ", Good Afternoon!";
    } else {
        return  ", Good Evening!";
    }
}

/**
 * Renders the primary header or "hero" content for a product page with responsive layout adjustments.
 *
 * This component:
 * 1. Uses the **`useMediaQuery`** and **`useTheme`** hooks to determine if the user is on a small screen (`< md`),
 * enabling conditional rendering and styling.
 * 2. Displays a **loading state** if `userInfo` is not yet available.
 * 3. Renders a main container with a **dynamic background image** provided by `userInfo.background`.
 * 4. Hides the **user profile image** and adjusts the **container height** on small screens for mobile optimization.
 * 5. Displays a personalized greeting (via `greetingSelection()`) alongside the user's name.
 * 6. Renders a static set of **quick action buttons** by mapping over `quickActionsButtons`.
 *
 * @param {object} props - The component props.
 * @param {object} props.userInfo - The user information object, expected to contain `name`, `image`, and `background` URL.
 * @returns {JSX.Element} The rendered hero section or a loading state.
 */
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

                <Grid item className='user-info' style={{height:containerHeight}} xs={12} md={6}>
                    <Box className='user-profile'  sx={{display: displayStyle}}>
                        <img src={userInfo.image} alt="" className='profile-image' />
                    </Box>
                    <Box className='user-details'>
                        <p>Hello,</p>
                        <p>{userInfo.name} {greetingSelection()}</p>
                    </Box>
                </Grid>

                <Grid item className='quick-actions' xs={12} md={6}>
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
