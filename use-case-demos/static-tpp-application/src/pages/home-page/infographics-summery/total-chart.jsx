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

import "./total-chart.scss";
import {Doughnut} from "react-chartjs-2";
import {ArcElement, Chart as ChartJS, Legend, Tooltip} from "chart.js";

ChartJS.register(ArcElement, Tooltip, Legend);

/**
 * A dedicated presentational component for rendering a Doughnut chart using `react-chartjs-2`.
 * It visualizes the distribution of balances based on pre-processed data.
 *
 * This component:
 * 1. Ensures necessary Chart.js elements (`ArcElement`, `Tooltip`, `Legend`) are registered globally.
 * 2. Defines static `options` for the chart, including responsiveness, legend positioning (right/start),
 * custom legend labels, and padding.
 * 3. Renders a title "Account Distribution" and the chart itself using the **`Doughnut`** component,
 * expecting the **`chartData`** prop to be the final data structure required by Chart.js.
 * 4. Note: Data transformation logic is expected to be handled by a parent component or custom hook
 * before being passed via `chartData`.
 *
 * @param {object} props - The component props.
 * @param {object} props.chartData - The prepared data object required by the `Doughnut` chart, containing `labels` and `datasets`.
 * @returns {JSX.Element} The rendered Doughnut chart component.
 */
const TotalChart = ({chartData})=>{

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'right',
                align: 'start',
                labels: {
                    usePointStyle: true,
                    boxWidth: 8,
                    padding: 10,
                }
            },

            tooltip: {}
        },
        layout:{
            padding: 5
        },
    };

    return (
        <div className='chart-outer'>
            <h2 >Account Distribution</h2>
            <div className='chart-container'>
                <Doughnut data={chartData} options={options} />
            </div>
        </div>
    );
}

export default TotalChart;
