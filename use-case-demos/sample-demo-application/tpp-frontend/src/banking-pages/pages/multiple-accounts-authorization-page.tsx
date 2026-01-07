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

import {Box, Button, FormControl, FormControlLabel, Grid, List, ListItem, Switch, useTheme} from "@oxygen-ui/react";
import {useNavigate, useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import type {SelectedAccountEntry} from "./multiple-accounts-selection-page.tsx";
import './inner-pages-stylings.scss'
import {useMediaQuery} from "@mui/material";

/**
 * @function MultipleAccountsAuthorizationPage
 * @description A page component simulating the final authorization step for **multiple** selected accounts.
 * It displays a summary of the accounts being authorized, the specific permissions granted
 * for each (e.g., 'Recurring'), and the consent expiry date, leading to either confirmation
 * via `onSuccessHandler` or cancellation.
 */
const MultipleAccountsAuthorizationPage = ()=>{

    const { onSuccessHandler, accountsToAdd, themeColor } = useOutletContext<OutletContext>();
    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isSmallScreen ? '0.2rem' : '0.5rem';
    const navigate = useNavigate();

    return(
        <>
            <Grid container className={'content-page-container'} xs={12} sm={8} md={6} lg={6} sx={{padding:responsivePadding, flexGrow:1}}>
                <Grid className="page-name-container">
                    <p>Please confirm the authorization to your selected accounts</p>
                </Grid>
                <Grid className={"form-login-one-container"}>
                    <Box sx={{display: "flex", justifyContent: "space-between", alignItems: "center",height:'fit-content'}}>
                        <FormControlLabel control={<Switch id={"account-one"} checked disabled={true} sx={{"--oxygen-palette-primary-main": themeColor}}/>} label={"Recurring"} labelPlacement={'start'}/>
                        <p>Expires in : 4 Days</p>
                    </Box>
                    <FormControl sx={{display:'flex', flexDirection:'column'}}>
                        {(accountsToAdd?.current?.data?.[0] || []).map((account:SelectedAccountEntry,index:number)=>{
                            return (
                                <Box key={index} sx={{display:'flex', flexDirection:'column', gap:'1rem'}}>
                                    <p>Permission to :  {account.permission} </p>
                                    <List sx={{ listStyleType: 'disc', pl: 4 }}>
                                        {account.accounts.map((iban, idx) => (
                                            <ListItem key={idx} sx={{display: 'list-item'}}>{iban}</ListItem>
                                        ))}
                                    </List>
                                </Box>
                            )
                        })}

                    </FormControl>
                    <Box className="form-buttons-container">
                        <Button variant={'contained'} onClick={onSuccessHandler} sx={{width:'6rem',height:'3rem','--oxygen-palette-gradients-primary-stop2':themeColor, '--oxygen-palette-gradients-primary-stop1':themeColor}}>Confirm</Button>
                        <Button variant={'outlined'} onClick={()=>{navigate(-1)}} sx={{width:'6rem',height:'3rem','--oxygen-palette-primary-main':themeColor, borderColor:themeColor}}>Cancel</Button>
                    </Box>
                </Grid>
            </Grid>
        </>
    )
}

// @ts-ignore
export default MultipleAccountsAuthorizationPage;
