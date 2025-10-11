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
// import {Card, Grid} from "@oxygen-ui/react";
// import TotalChart from "../total-chart.jsx";
import {
    ContentInnerSectionContainer,
    InfographicsInnerContainer
} from "../../../components/styled-components/styled-containers.jsx";
import TotalChart from "./total-chart.jsx";

/**
 * Renders a summary section displaying key financial infographics using a responsive `Grid` layout.
 *
 * This component:
 * 1. Calculates the **total balance** across all bank accounts from the `bankInfoWithTotals` prop.
 * 2. Uses the **Oxygen UI `Grid`** system to create a responsive, two-column layout (on large/medium screens).
 * 3. The first column displays the calculated **"Total Balance"** inside an Oxygen UI `Card`.
 * 4. The second column renders a **`TotalChart`** component inside another `Card`, which visualizes
 * the balance distribution based on the provided bank data.
 *
 * @param {object} props - The component props.
 * @param {Array<object>} props.bankInfoWithTotals - An array of bank account objects, each containing a `totalBalance` field used for calculation and charting.
 * @returns {JSX.Element} The rendered summary section.
 */
const InfographicsSummery = ({chartData,total}) => {

    // const total = bankInfoWithTotals.reduce((acc, current) => {return acc+current.totalBalance},0)

    return (
        <>
                {/*<Grid container={true} lg={12} md={12} sm={12} xs={12} sx={{height:"fit-content"}} spacing={2}>*/}
                {/*    <Grid item={true} lg={6} md={6} sm={12} xs={12} direction={{lg:"row", sm:"column", md:"row", xs:"column"}} >*/}
                {/*        <Card sx={{height: "100%",display:"flex", justifyContent:"center", alignItems:"center", flexDirection:"column", gap:"1.5rem"}} className={"shadow"}>*/}
                {/*            <p style={{fontSize:"1.5rem",fontWeight:"bold"}}>Total Balance</p>*/}
                {/*            <div style={{display:"flex", justifyContent:"center", alignItems:"center", gap:"1rem"}}>*/}
                {/*                <p>GBP</p><span style={{fontSize:"2rem",fontWeight:"bold"}}>{total}</span>*/}
                {/*            </div>*/}
                {/*        </Card>*/}
                {/*    </Grid>*/}
                {/*    <Grid item={true} lg={6} md={6} sm={12} xs={12} direction={{lg:"row", sm:"column", md:"row", xs:"column"}}>*/}
                {/*        <Card className={"shadow"}>*/}
                {/*            <TotalChart bankInfoAndTotals={bankInfoWithTotals}/>*/}
                {/*        </Card>*/}
                {/*    </Grid>*/}
                {/*</Grid>*/}

            <ContentInnerSectionContainer>
                <InfographicsInnerContainer className="total-container">
                    <p>Total Balance</p>
                    <p>GBP<span>{total}</span></p>
                </InfographicsInnerContainer>
                <InfographicsInnerContainer>
                    <TotalChart chartData={chartData}/>
                </InfographicsInnerContainer>
            </ContentInnerSectionContainer>
        </>
    );
}

export default InfographicsSummery;

