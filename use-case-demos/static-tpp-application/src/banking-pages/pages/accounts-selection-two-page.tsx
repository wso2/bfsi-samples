/*
 * *
 *  * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 *  *
 *  * WSO2 LLC. licenses this file to you under the Apache License,
 *  * Version 2.0 (the "License"); you may not use this file except
 *  * in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

import {Box, Button, Checkbox, FormControl, FormControlLabel, FormLabel, Grid, Switch, useTheme} from "@oxygen-ui/react";
import {useNavigate, useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import {useState} from "react";
import './inner-pages-stylings.scss'
import {useMediaQuery} from "@mui/material";

export interface SelectedAccountEntry {
    permission: string;
    accounts: string[];
}

const AccountsSelectionTwoPage = ()=>{

    const { onSuccessHandler,navigationData, accountsToAdd } = useOutletContext<OutletContext>();

    console.log(accountsToAdd);

    console.log(navigationData)

    const multiAccounts = ["iban DE 000023245320","iban DE 000023245321","iban DE 000023245322"];

    const listOfPermissions = ["Accounts read", "Accounts write", "Accounts basics"];

    const [selectedData, setSelectedData] = useState<SelectedAccountEntry[]>(() => {
        return listOfPermissions.map(permission => ({
            permission: permission,
            accounts: [] as string[]
        }));
    });

    console.log(selectedData)

    const handleAccountChange = (permission: string, accountId: string, checked: boolean) => {

        setSelectedData(prevData => {
            return prevData.map(entry => {

                if (entry.permission === permission) {

                    const accounts = checked ? [...entry.accounts, accountId] : entry.accounts.filter(id => id !== accountId);
                    console.log("accounts", accounts);
                    return { ...entry, accounts };
                }

                return entry;
            });
        });
    };

    const handleSubmit = () => {



        if(selectedData.length>0){
            accountsToAdd.current = {type:"multiple",data:[selectedData]};
            onSuccessHandler();
        }else{
            alert("You must select account to add")
        }
    };

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isSmallScreen ? '0.2rem' : '0.5rem';

    const navigate = useNavigate();

    return(
        <>
            <Grid container className={'content-page-container'} xs={12} sm={8} md={6} lg={4} sx={{padding:responsivePadding, flexGrow:1}}>

                <Grid className="page-name-container">
                    <h3>Account Authorization</h3>
                </Grid>

                <Grid className={"form-login-one-container"} sx={{maxHeight: '50vh'}}>

                    <FormControl>
                        <FormLabel id={"check-box-group"}>Select your account to add from the list</FormLabel>
                    </FormControl>

                    <Box sx={{display: "flex", justifyContent: "space-between", alignItems: "center",height:'fit-content'}}>
                        <FormControlLabel control={<Switch id={"account-one"} checked disabled={true}/>} label={"Recurring"} labelPlacement={'start'}/>
                        <p>Frequency : 4 Days</p>
                    </Box>

                    <FormControl sx={{display:'flex', flexDirection:'column', overflowY: 'auto'}}>
                        {listOfPermissions.map((item, index) => {
                            const currentAccounts = selectedData.find(d => d.permission === item)?.accounts || [];
                            return (
                                <Box key={index} sx={{display: 'flex', flexDirection: 'column'}}>

                                    <p>Permission to : </p> <h3>{item}</h3>

                                    {multiAccounts.map((account, index2) => {

                                        const isChecked = currentAccounts.includes(account);

                                        return (

                                            <FormControlLabel key={index2} control={<Checkbox id={account} checked={isChecked} onChange={(e) => handleAccountChange(item, account, e.target.checked)}/>} label={account}/>
                                        )
                                })}
                            </Box>)
                        })}
                    </FormControl>

                    <Box className="form-buttons-container">
                        <Button variant={'contained'} onClick={handleSubmit}>Confirm</Button>
                        <Button variant={'outlined'} onClick={()=>{navigate(-1)}}>Cancel</Button>
                    </Box>
                </Grid>
            </Grid>
        </>
    )
}

export default AccountsSelectionTwoPage;
