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

import {Box, Button, FormControl, FormControlLabel, FormLabel, Grid, List, ListItem, Radio, RadioGroup, Switch, useTheme} from "@oxygen-ui/react";
import {useNavigate, useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import {useState} from "react";
import './inner-pages-stylings.scss'
import {useMediaQuery} from "@mui/material";

export interface SelectedAccountEntry {
    permission: string;
    accounts: string[];
}

const AccountsSelectionThreePage = ()=>{

    const { onSuccessHandler,navigationData, accountsToAdd } = useOutletContext<OutletContext>();

    console.log(accountsToAdd);

    console.log(navigationData)

    const accountsList = ["0006-0566-1212","0006-0045-2020","0006-0400-1010"];
    const permissions = ["Read the accounts balances","Read defaults","Write the accounts balance","Write defaults"];
    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isSmallScreen ? '0.2rem' : '0.5rem';

    const [selectedAccount, setSelectedAccount] = useState<string>('');

    const handleAccountSelection = () => {

        if(selectedAccount.length>0){
            accountsToAdd.current = {type:"single",data:[selectedAccount]};
            onSuccessHandler();
        }
    }

    const handleRadioChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSelectedAccount(event.target.value);
    };

    const navigate = useNavigate();

    return(
        <>
            <Grid container className={'content-page-container'} xs={12} sm={8} md={6} lg={4} sx={{padding:responsivePadding, flexGrow:1}}>

                <Grid className="page-name-container">
                    <h3>Account Authorization</h3>
                </Grid>



                <Grid className={"form-login-one-container"} sx={{maxHeight: '50vh', overflowY: 'auto', justifyContent:'flex-start', alignItems: 'stretch'}}>

                    <FormControl>
                        <FormLabel id={"check-box-group"}>Following permissions Granted</FormLabel>
                        <List sx={{ listStyleType: 'disc', pl: 4 }}>
                            {permissions.map((item, index) => {
                                return (<ListItem key={index} sx={{display: 'list-item'}}>{item}</ListItem>)
                            })}
                        </List>
                    </FormControl>

                    <Box sx={{display: "flex", justifyContent: "space-between", alignItems: "center",height:'fit-content'}}>

                        <FormControlLabel control={<Switch id={"account-one"} checked disabled={true}/>} label={"Recurring"} labelPlacement={'start'}/>

                        <p>Frequency : 4 Days</p>

                    </Box>

                    <FormControl sx={{display:'flex', flexDirection:'column', alignItems:'center', marginTop:'5%'}}>

                        <RadioGroup aria-label="select-account" name="account-selection-group" value={selectedAccount} onChange={handleRadioChange}>
                            {accountsList.map((account, index) => {
                                return (
                                    <FormControlLabel key={index} control={<Radio />} label={`${navigationData.current.bankInfo.name}-${account}`} value={account}/>
                                );
                            })}
                        </RadioGroup>

                    </FormControl>


                    <Box className="form-buttons-container" justifyContent="end">
                        <Button variant={'contained'} onClick={handleAccountSelection}>Done</Button>
                        <Button variant={'outlined'} onClick={()=>{navigate(-1)}}>Cancel</Button>
                    </Box>
                </Grid>


            </Grid>
        </>
    )
}

// @ts-ignore
export default AccountsSelectionThreePage;
