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

import HeroSection from "./hero-section/hero-section.jsx";
import useAuthContext from "../../hooks/use-auth-context.js";
import ApplicationLayout from "../../layouts/application-layout/application-layout.jsx";
import InfographicsSummery from "./infographics-summery/infographics-summery.jsx";
import HomeContentLayout from "../../layouts/home-content-layout/home-content-layout.jsx";

/**
 * The main component for the product's home page, responsible for aggregating
 * user data, configuration, and bank information for display.
 *
 * This component:
 * 1. Retrieves user authentication details using the **`useAuthContext`** hook.
 * 2. Accepts pre-fetched application **configurations and bank data** (including total balance and chart data) via props.
 * 3. Displays a **loading state** while the initial data is being fetched (`isLoading`).
 * 4. Renders the application structure using **`<ApplicationLayout>`** and populates the content
 * with **`<HeroSection>`** (for user greetings/actions) and **`<InfographicsSummery>`**
 * (for financial data visualization).
 *
 * @param {object} props - The component props.
 * @param {object} props.configurations - An object containing application state: `config` (app config), `isLoading` (boolean), `chartData` (chart structure), and `total` (total balance).
 * @returns {JSX.Element} The rendered home page layout or a loading message.
 */
const Home = ({configurations}) => {
    const userInfo = useAuthContext();
    const {config, isLoading, chartData, total} = configurations;

    if (isLoading) {
        return <div>Loading configuration and bank data...</div>;
    }

    return (
        <>
            <ApplicationLayout appInfo={config}>
                <HeroSection userInfo={userInfo}/>
                <HomeContentLayout>
                    <InfographicsSummery total={total} chartData={chartData}/>
                </HomeContentLayout>
            </ApplicationLayout>
        </>
    )
}

export default Home;

