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
 * A specialized React component for rendering a Doughnut chart using `react-chartjs-2`.
 * It visualizes the distribution of total balances across different bank accounts.
 *
 * This component:
 * 1. Registers necessary Chart.js elements (`ArcElement`, `Tooltip`, `Legend`).
 * 2. Uses a `useEffect` hook to transform the input `bankInfoAndTotals` prop into the
 * required `chartData` format whenever the input data changes.
 * 3. Dynamically extracts bank names (for labels), total balances (for data), and
 * predefined colors/borders from the bank objects.
 * 4. Renders the chart with specific styling options (responsive, legend position right, 75% cutout).
 * 5. Displays a loading message if chart data is not yet processed.
 *
 * @param {object} props - The component props.
 * @param {Array<object>} props.bankInfoAndTotals - An array of objects, where each object contains `name`, `totalBalance`, `color`, and `border` properties.
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
