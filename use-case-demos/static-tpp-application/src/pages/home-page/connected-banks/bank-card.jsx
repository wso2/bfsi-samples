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

import {formatCurrency} from "../../../hooks/utility.js";
import './connected-banks.scss'
import {Card, Grid} from "@oxygen-ui/react";
import {useMediaQuery, useTheme} from "@mui/material";

/*
 * BankCard component displays detailed information for a single connected bank,
 * including the bank's logo and the total aggregated balance across its accounts.
 *
 * Params:
 * @param {object} bankInformationWithTotal - Object containing bank details (name, image, currency) and total balance.
 *
 */
const BankCard = ({bankInformationWithTotal})=>{

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));

    const width = isSmallScreen ? '6rem' : '8rem';
    const height = isSmallScreen ? '6rem' : '8rem';
    const justifyContent = isSmallScreen ? 'space-between' : 'center';

    return (
        <>
            <Grid xs={12} md={4} >
                <Card className='bank-card-outer' sx={{justifyContent: justifyContent}}>
                    <div className="logo-container" style={{width:width, height:height}}>
                        <img src={bankInformationWithTotal.image} alt="" className="bank-logo" />
                    </div>
                    <div className="bank-info-container">
                        <p>{bankInformationWithTotal.name}</p>
                        <p><span>{bankInformationWithTotal.currency}</span>{formatCurrency(bankInformationWithTotal.totalBalance)}</p>
                    </div>
                </Card>
            </Grid>
        </>
    );
}

export default BankCard;
