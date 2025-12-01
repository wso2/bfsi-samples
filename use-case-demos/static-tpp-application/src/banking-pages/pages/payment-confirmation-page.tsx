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

import {Box, Button, Grid, List, ListItem, useTheme,} from "@oxygen-ui/react";
import {useNavigate, useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import './inner-pages-stylings.scss'
import { useMediaQuery } from "@mui/material";

/**
 * @function PaymentConfirmationPage
 * @description A dynamic component simulating the final user authorization step for a payment.
 * It displays a summary of the full payment details (amount, payee, reference, source account),
 * and requires the user to click "Confirm" to trigger `onSuccessHandler` and finalize the transaction.
 */
const PaymentConfirmationPage = ()=>{

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isSmallScreen ? '1rem' : '2rem';
    const { onSuccessHandler,navigationData, themeColor } = useOutletContext<OutletContext>();
    const navigate = useNavigate();

    return(
        <>
            <Grid container className={'content-page-container'} xs={12} sm={8} md={6} lg={6} sx={{padding:responsivePadding}}>
                <Grid className="page-name-container">
                    <p>Please authorize following payment</p>
                </Grid>
                <Grid className={"form-login-one-container"}>
                   <Box sx={{width:'100%', marginTop:'1rem', display:'flex',flexDirection:'column',gap:'1rem'}}>
                        <p>Debited Account : <br />
                        <span style={{fontWeight:'600', marginLeft:'1rem'}}>{navigationData.current.formData.userAccount}</span></p>
                        <List sx={{listStyleType: 'disc', pl: 8, display: 'flex', flexDirection: 'column', gap: '1rem'}}>
                            <ListItem sx={{display: 'list-item', padding:'0 1rem'}}>Amount : {navigationData.current.formData?.amount}</ListItem>
                            <ListItem sx={{display: 'list-item', padding:'0 1rem'}}>Currency : {navigationData.current.formData?.currency}</ListItem>
                            <ListItem sx={{display: 'list-item', padding:'0 1rem',}}>Reciver : <br /><span style={{ fontWeight:'600'}}>{navigationData.current.formData?.payeeAccount}</span></ListItem>
                            <ListItem sx={{display: 'list-item', padding:'0 1rem'}}>Reference : {navigationData.current.formData?.reference}</ListItem>
                        </List>
                   </Box>
                    <Box className="form-buttons-container">
                        <Button variant={'contained'} sx={{'--oxygen-palette-gradients-primary-stop2':themeColor, '--oxygen-palette-gradients-primary-stop1':themeColor}} onClick={onSuccessHandler} className="button-styles">Confirm</Button>
                        <Button variant={'outlined'} sx={{'--oxygen-palette-primary-main':themeColor, borderColor:themeColor}} className="button-styles" onClick={()=>{navigate(-1)}}>Cancel</Button>
                    </Box>
                </Grid>
            </Grid>
        </>
    )
}

// @ts-ignore
export default PaymentConfirmationPage;
