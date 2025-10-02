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

import HeroSection from "./hero-section.jsx";
import useAuthContext from "../../hooks/use-auth-context.js";
import ApplicationLayout from "../../layouts/application-layout.jsx";

/**
 * The main component for the product's home page.
 * * It uses the **`useAuthContext`** hook to retrieve **user information** (or authentication state)
 * and passes this data to the **`HeroSection`** for personalized display.
 * * The component uses the **`AccountsCentral`** layout wrapper to provide the standard
 * product header and content structure.
 */
const Home = ({configurations}) => {
    const userInfo = useAuthContext();
    return (
        <>
            <ApplicationLayout configurations={configurations}>
                <HeroSection userInfo={userInfo}/>
            </ApplicationLayout>
        </>
    )
}

export default Home;

