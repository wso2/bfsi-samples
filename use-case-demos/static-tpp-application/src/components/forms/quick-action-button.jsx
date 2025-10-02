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

import "./quick-action-button.css"

/**
 * A reusable component designed to function as a Quick Action Button.
 * * It renders an interactive button displaying a primary icon and a text label
 * (passed as `children`). It applies a fixed style intended for quick action lists.
 * * @param {object} props - The component props.
 * @param {React.ReactNode} props.icon - The JSX or component for the button's icon.
 * @param {React.ReactNode} props.children - The text label or content displayed below the icon.
 * @param {function} props.onClick - The function to be executed when the button is clicked.
 */
export const QuickActionButton = ({icon, children, onClick}) => {

    return (
        <div className='quick-action-button-outer'>
            <button onClick={onClick}>
                {icon}
                <p>{children}</p>
            </button>
        </div>
    );
};

