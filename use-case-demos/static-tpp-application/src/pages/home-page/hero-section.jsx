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

const onclickAction = () => {
    console.log("Quick action QuickActionButton clicked");
}

const quickActionsButtons = [{
    icon: PayBillsIcon,
    name: "Payments",
    onClick: onclickAction
}, {icon: ArrowLeftArrowRightIcon, name: "Transfer", onClick: onclickAction, size: "48", width: 24}, {
    icon: ScheduleIcon,
    name: "Schedule",
    onClick: onclickAction
}, {icon: UserGroupIcon, name: "Payees", onClick: onclickAction, size: "48", width: 24}];

/**
 * Renders the primary header or "hero" content for a product page.
 *
 * This component:
 * 1. Displays a **personalized greeting** calculated based on the current time of day.
 * 2. Shows the user's **name and profile image**.
 * 3. Renders a static set of **quick action buttons** defined in `quickActionsButtons`.
 * The component dynamically renders the icon for each action, correctly passing
 * any extra properties like `size` or `color` defined in the action object to the IconComponent.
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

    const currentHour = new Date().getHours();
    let greeting;
    if (currentHour >= 5 && currentHour < 12) {
        greeting = ", Good Morning!";
    }else if (currentHour >= 0 && currentHour < 5) {
        greeting = "";
    } else if (currentHour >= 12 && currentHour < 18) {
        greeting = ", Good Afternoon!";
    } else {
        greeting = ", Good Evening!";
    }

    return (<>
            <div className="product-home-header-outer">
                <div className="user-name-image-container">
                    <div className="user-image-container" style={{backgroundImage: `url(${userInfo.image})`}}></div>
                    <div className="product-user-name-greeting-container">
                        <p>Hello,</p>
                        <p className="product-user-name-greeting-information-para">{userInfo.name}{greeting}</p>
                    </div>
                </div>
                <div className="product-quick-actions-container">
                    {quickActionsButtons.map((action, index) => {
                        const IconComponent = action.icon;
                        const iconProps = {
                            ...(action.size && {size: action.size}), ...(action.width && {width: action.width})
                        };
                        return (<QuickActionButton key={index} onClick={action.onClick}>
                                <IconComponent {...iconProps} />
                                {action.name}
                            </QuickActionButton>)
                    })}
                </div>
            </div>
        </>)
}

export default HeroSection;

