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

import {Box, Button, FormControl, FormLabel, Grid, List, ListItem, useTheme} from "@oxygen-ui/react";
import {useNavigate, useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import './inner-pages-stylings.scss'
import {useMediaQuery} from "@mui/material";

/**
 * @function AccountsAuthorizationPage
 * @description A dynamic component simulating the final step in a single account connection flow.
 * It prompts the user to **authorize specific permissions** for a selected account,
 * displays the consent duration, and uses `onSuccessHandler` to simulate the successful
 * authorization callback, or cancels the flow using `Maps(-1)`.
 */
const AccountsAuthorizationPage = ()=>{

    const { onSuccessHandler, accountsToAdd,themeColor } = useOutletContext<OutletContext>();
    const permissions = ["Read the accounts balances","Read defaults","Write the accounts balance","Write defaults"];
    const getFutureDate = () => {
        const futureDate = new Date();
        futureDate.setMonth(futureDate.getMonth() + 2);
        return futureDate.toLocaleDateString();
    };
    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isSmallScreen ? '1rem' : '2rem';
    const navigate = useNavigate();
    return(
        <>
            <Grid container className={'content-page-container'} xs={12} sm={8} md={6} lg={6} sx={{padding:responsivePadding}}>
                <Grid className="page-name-container">
                    <p>Please authorize the following permissions to your account</p>
                </Grid>
                <Grid className={"form-login-one-container"}>
                    <FormControl>
                        <FormLabel id={"check-box-group"}>Account : {accountsToAdd?.current?.data?.[0]?? 'N/A'}</FormLabel>
                    </FormControl>
                    <List sx={{ listStyleType: 'disc', pl: 4 }}>
                        {permissions.map((item, index) => {
                            return (<ListItem key={index} sx={{display: 'list-item'}}>{item}</ListItem>)
                        })}
                    </List>
                    <p>Permission expired on : {getFutureDate()}</p>
                    <Box className="form-buttons-container">
                        <Button variant={'contained'} onClick={onSuccessHandler}  sx={{width:'6rem',height:'3rem','--oxygen-palette-gradients-primary-stop2':themeColor, '--oxygen-palette-gradients-primary-stop1':themeColor}}>Confirm</Button>
                        <Button variant={'outlined'} onClick={()=>{navigate(-1)}} sx={{width:'6rem',height:'3rem','--oxygen-palette-primary-main':themeColor, borderColor:themeColor}}>Cancel</Button>
                    </Box>
                </Grid>
            </Grid>
        </>
    )
}

export default AccountsAuthorizationPage;
