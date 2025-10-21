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
import TotalChart from "./total-chart.jsx";
import {Card, Grid} from "@oxygen-ui/react";
import {formatCurrency} from "../../../hooks/utility.js";

/*
 * InfographicsSummery component displays the total balance and a distribution chart
 * in a responsive two-column grid layout.
 *
 * Params:
 * @param {object} chartData - Data for the distribution chart.
 * @param {number} total - The total balance amount.
 */
const InfographicsSummery = ({chartData,total}) => {

    return (
        <>
            <Grid container className='informatics-content-summery' spacing={2}>
                <Grid xs={12} md={6}>
                    <Card className='total-card'>
                        <h2>Total Balance</h2>
                        <p>GBP<span>{formatCurrency(total)}</span></p>
                    </Card>
                </Grid>
                <Grid xs={12} md={6}>
                    <Card className='summery-graph-card'>
                        <TotalChart chartData={chartData}/>
                    </Card>
                </Grid>
            </Grid>
        </>
    );
}

export default InfographicsSummery;
