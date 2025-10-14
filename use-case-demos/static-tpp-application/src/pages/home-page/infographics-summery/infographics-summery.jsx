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

import './infographics-summery.scss'
import {
    ContentInnerSectionContainer,
    InfographicsInnerContainer
} from "../../../components/styled-components/styled-containers.jsx";
import TotalChart from "./total-chart.jsx";
import {Card, Grid} from "@oxygen-ui/react";

/**
 * Renders a financial summary section that presents total balance and chart visualization.
 *
 * This component utilizes custom **Styled Components** (`ContentInnerSectionContainer` and `InfographicsInnerContainer`)
 * to structure a two-part layout:
 * 1. The first section displays the **total aggregated balance** passed via the `total` prop.
 * 2. The second section renders the **`TotalChart`** component, receiving its data via the `chartData` prop.
 *
 * It is responsible for the overall layout of the infographic summary, but the data calculation (total and chart data preparation)
 * is expected to be handled by the parent component, as indicated by the props.
 *
 * @param {object} props - The component props.
 * @param {object} props.chartData - The prepared data object required by the `TotalChart` component.
 * @param {number|string} props.total - The pre-calculated total balance across all accounts.
 * @returns {JSX.Element} The rendered summary section.
 */
const InfographicsSummery = ({chartData,total}) => {

    return (
        <>
            <Grid container className='informatics-content-summery' spacing={2}>
                <Grid item xs={12} md={6}>
                    <Card className='total-card'>
                        <h2>Total Balance</h2>
                        <p>GBP<span>{total}</span></p>
                    </Card>
                </Grid>
                <Grid item xs={12} md={6}>
                    <Card className='summery-graph-card'>
                        <TotalChart chartData={chartData}/>
                    </Card>
                </Grid>
            </Grid>
        </>
    );
}

export default InfographicsSummery;
