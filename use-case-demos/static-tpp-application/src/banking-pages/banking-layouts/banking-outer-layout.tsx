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

import {Grid } from "@oxygen-ui/react";
import './banking-layouts-styles.scss'
import { Card } from "@mui/material";

interface BankingOuterLayoutProps {
    children: React.ReactNode;
    image: string;
    bankName: string;
    themeColor: string;
}

/**
 * @function BankingOuterLayout
 * @description The highest-level layout component for bank-specific pages (e.g., authorization flows).
 * It displays the bank's logo and name, applies the bank's unique theme color to the title,
 * and acts as a wrapper for the dynamic process content (`children`).
 */
const BankingOuterLayout = ({children, image,bankName,themeColor}:BankingOuterLayoutProps)=>{
    return(
        <>
            <Grid container className="banks-pages-inner-container" sx={{ padding: 0, "&:last-child": { pb: 0 } }}>

                <Card className="banking-logo-container-card">
                    <Grid className="banking-logo-container">
                        <img src={image} alt="banking logo" />
                        <h3 style={{color: themeColor}}>{bankName}</h3>
                    </Grid>
                    <Grid className={'banking-dynamic-content'}>
                        {children}
                    </Grid>
                </Card>

            </Grid>
        </>
    )
}

export default BankingOuterLayout;
