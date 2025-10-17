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

import {Grid} from "@oxygen-ui/react";
import './connected-banks.scss'
import BankCard from "./bank-card.jsx";

/**
 * A component responsible for displaying a list of connected banks using responsive cards.
 *
 * It iterates over the **`banksInformationWithTotals`** array and renders a **`<BankCard />`**
 * component for each bank, passing the bank's summary data as a prop.
 * The bank cards are organized within an **Oxygen UI `Grid` container** to ensure a responsive,
 * spaced layout.
 *
 * @param {object} props - The component props.
 * @param {Array<object>} props.banksInformationWithTotals - An array of bank objects, where each object contains the necessary details (e.g., ID, logo, name, total balance) for the BankCard component.
 * @returns {JSX.Element} The rendered list of connected bank cards within a grid layout.
 */
const ConnectedBanks = ({banksInformationWithTotals})=>{
    return(
        <>
            <Grid container spacing={2} className='banks-container'>

                {banksInformationWithTotals.map((bank,index)=>{
                    return (
                        <BankCard key={index} bankInformationWithTotal={bank} />);
                })}

            </Grid>
        </>
    );
}

export default ConnectedBanks;
