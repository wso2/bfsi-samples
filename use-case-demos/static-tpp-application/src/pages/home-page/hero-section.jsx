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

import "./hero-section.css"
import {QuickActionButton} from "../../components/quick-action-button/quick-action-button.jsx";
import PayBillsIcon from "/public/resources/assets/images/icons/pay_icon.svg?react"
import ScheduleIcon from "/public/resources/assets/images/icons/schedule_icon.svg?react"
import {ArrowLeftArrowRightIcon, UserGroupIcon} from '@oxygen-ui/react-icons'
import {Grid} from "@oxygen-ui/react";

const onclickAction = () => {
    console.log("Quick action action button clicked");
}

const quickActionsButtons = [
    {icon: PayBillsIcon, name: "Payments", onClick: onclickAction},
    {icon: ArrowLeftArrowRightIcon, name: "Transfer", onClick: onclickAction, size: 48},
    {icon: ScheduleIcon, name: "Schedule", onClick: onclickAction},
    {icon: UserGroupIcon, name: "Payees", onClick: onclickAction, size: 48}
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
 * Renders the primary header or "hero" content for a product page.
 *
 * This component:
 * 1. Displays a **personalized greeting** ("Good Morning!", "Good Afternoon!", etc.)
 * calculated based on the current time of day (5 AM to 12 PM, 12 PM to 6 PM, or 6 PM to 5 AM).
 * 2. Shows the user's **name and profile image**.
 * 3. Renders a static set of **quick action buttons** defined in `quickActionsButtons`.
 * It dynamically passes icon properties like `size` and `width` from the action object
 * to the rendered icon component.
 * 4. Displays a "Loading...." message while the `userInfo` prop is falsy.
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
        <Grid
            container={true}
            sx={{height: { xs: "12rem", sm: "12rem", md: "14rem" }}}
            direction={{xs:"column",sm:"column",md:"column",lg:"row"}}
            display={"flex"}
            className="hero-section-outer">
                <Grid
                    item={true}
                    sx={{height: { xs: "40%", sm: "40%", md: "40%",lg:"100%" },
                        display:"flex", alignItems:"center", justifyContent:{sm:"start",md:"start", lg:"center" },
                        padding: { xs: "0 0 0 1rem",sm: "0 0 0 2rem",md: "0 0 0 2rem",lg:"0" }}}
                    xs={12} sm={12} md={12} lg={6} alignItems={"center"} >
                    <Grid
                        container={true}
                        alignItems={"center"} sx={{width: "80%", height: "fit-content"}} spacing={2}>
                        <Grid
                            item={true}
                              sx={{width: {md:"4rem", lg:"8rem"},
                                  height: {sm:"4rem",md:"4rem", lg:"8rem"}, display:{xs:"none",sm:"flex"}}} >
                            <img src={userInfo.image} alt="profile-image"
                                 style={{objectFit: "cover", width: "100%",height:"100%", borderRadius:"50%"}}/>
                        </Grid>
                        <Grid item={true}
                              sx={{color:"white.main",fontSize:{xs:"1rem",sm:"1rem",md:"1rem",lg:"1.5rem"}}} >
                            <p>Hello,</p>
                            <p style={{fontWeight:"bold"}}>{userInfo.name}{greetingSelection()}</p>
                        </Grid>
                    </Grid>
                </Grid>
                <Grid
                    item={true}
                    sx={{height: { xs: "60%", sm: "60%", md: "60%",lg:"100%" },
                        padding:{xs:1,sm:2,md:2,lg:5} }} xs={12} sm={12} md={12} lg={6}
                    alignItems={"center"} display={"flex"} justifyContent={"center"}>

                    <Grid
                        container={true}
                        alignItems={"center"}
                        sx={{width: "100%", height: "100%", display:"flex",
                            justifyContent:"center",gap:{xs:"4%",sm:8, md:8,lg:4} }}>

                        {quickActionsButtons.map((action, index) => {
                            const IconComponent = action.icon;
                            const iconProps = {
                                ...(action.size && {size: action.size})
                            };
                            return (<QuickActionButton key={index} onClick={action.onClick}>
                                <IconComponent {...iconProps} />
                                {action.name}
                            </QuickActionButton>)
                        })}
                    </Grid>
                </Grid>
        </Grid>
    </>
    )
}

export default HeroSection;

