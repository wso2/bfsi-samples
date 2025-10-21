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

import "./infographics-summery.scss";
import {Doughnut} from "react-chartjs-2";
import {ArcElement, Chart as ChartJS, Legend, Tooltip} from "chart.js";
import {useMediaQuery, useTheme} from "@mui/material";

ChartJS.register(ArcElement, Tooltip, Legend);

/*
 * Chart component that visualizes the distribution of account balances (Doughnut Chart).
 * It dynamically adjusts the legend position based on screen size (mocked).
 *
 * Params:
 * @param {object} chartData - Data structure for the Doughnut chart (labels, datasets).
 */
const TotalChart = ({chartData}) => {

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));

    const position = isSmallScreen ? 'top' : 'right';

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: position,
                align: 'start',
                labels: {
                    usePointStyle: true,
                    boxWidth: 8,
                    padding: 10,
                }
            },
            tooltip: {}
        },
    };

    return (
        <div className='chart-outer'>
            <h2>Account Distribution</h2>
            <div className='chart-container'>
                <Doughnut data={chartData} options={options}/>
            </div>
        </div>
    );
}

export default TotalChart;
