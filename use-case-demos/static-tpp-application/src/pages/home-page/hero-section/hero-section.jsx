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
import {ArrowLeftArrowRightIcon, UserGroupIcon, ClockAsteriskIcon, BoltIcon} from '@oxygen-ui/react-icons'
import {
    HeroInnerContainer,
    HeroOuterContainer,
    ProfileImageOuter, UserInformationContainer
} from "../../../components/styled-components/styled-containers.jsx";
import {Box, Grid} from "@oxygen-ui/react";

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
 * Renders the primary header or "hero" content for a product page using custom styled components
 * for a responsive layout.
 *
 * This component:
 * 1. Uses **Styled Components** (HeroOuterContainer, HeroInnerContainer, etc.) to structure
 * a two-part layout: the user info section and the quick actions section.
 * 2. Displays a **personalized greeting** by calling the **`greetingSelection`** function.
 * 3. Shows the user's **name and profile image**.
 * 4. Renders a static set of four **quick action buttons** defined in `quickActionsButtons`
 * (Payments, Transfer, Schedule, Payees) using the reusable `QuickActionButton` component.
 * 5. Displays a "Loading...." message while the `userInfo` prop is unavailable.
 *
 * @param {Object} props - The component props.
 * @param {Object} props.userInfo - The user information object, expected to contain `name` and `image` URL.
 * @returns {JSX.Element} The rendered hero section or a loading state.
 */
const HeroSection = ({userInfo}) => {

    if (!userInfo) {
        return <div>Loading....</div>
    }
    return (
        <>
            <Grid container className="outer-container" style={{background: `url(${userInfo.background}) lightgray -18.644px -372.574px / 145.345% 527.151% no-repeat`}}>

                <Grid item className='user-info'>
                    <Box className='user-profile'>
                        <img src={userInfo.image} alt="" className='profile-image' />
                    </Box>
                    <Box className='user-details'>
                        <p>Hello,</p>
                        <p>{userInfo.name}, {greetingSelection()}</p>
                    </Box>
                </Grid>

                <Grid item className='quick-actions'>
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
