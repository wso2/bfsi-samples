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

import {Box, Card } from "@oxygen-ui/react";
import type { ChartOptions} from "chart.js";
import useMediaQuery from "@mui/material/useMediaQuery";
import {useTheme} from "@mui/material";
import {Doughnut} from "react-chartjs-2";
import {ArcElement, Chart as ChartJS, Legend, Tooltip} from "chart.js";
import type {ChartData} from "../../../hooks/use-config-context.ts";
import {formatCurrency} from "../../../utility/number-formatter.ts";

ChartJS.register(ArcElement, Tooltip, Legend);

interface InfographicsContentProps {
    total: number;
    chartInfo: ChartData & { labels: string[], cutout: string };
}

/**
 * @function InfographicsContent
 * @description A composite component that displays key financial overview data,
 * including the **Total Balance** (formatted currency) and an **Account Distribution**
 * Doughnut chart. It uses responsive styling and Chart.js to render the visual data.
 */
export const InfographicsContent = ({total,chartInfo}:InfographicsContentProps)=>{

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const displayDirection = isSmallScreen ? 'column' : 'row';
    const responsiveGap = isSmallScreen ? '0.2rem' : '1rem';
    const chartjsData = {
        labels: chartInfo.labels,
        datasets: [
            {
                label : chartInfo.label,
                data: chartInfo.data ?? [],
                backgroundColor: chartInfo.backgroundColor,
                borderColor: chartInfo.borderColor,
                borderWidth: 2,
            }
        ]
    }

    const options: ChartOptions<'doughnut'> = {
        responsive: true,
        maintainAspectRatio: false,
        cutout:chartInfo.cutout,
        layout:{
            autoPadding:true,
        },
        plugins: {
            legend: {
                position: "right",
                align: 'center',
                labels: {
                    usePointStyle: true,
                    boxWidth: 20,
                    padding: 25,
                }
            },
            tooltip: {}
        },
    }
    return(
        <>
            <Box flexDirection={displayDirection} display={"flex"} width={"100%"} gap={'1rem'} >
                <Card className={'info-graphic-card'} style={{gap: responsiveGap}}>
                    <h3>Total Balance</h3>
                    <p className={'display-total'} ><span>GBP</span>{formatCurrency(total)}</p>
                    <div className="lastupdate-container">
                        <p>Last update 1 min ago</p>
                    </div>
                </Card>
                <Card className={'info-graphic-card'}>
                    <h3>Account Distribution</h3>
                    <div className="chart-container">
                        <Doughnut data={chartjsData} options={options}/>
                    </div>
                </Card>
            </Box>

        </>
    );
}
