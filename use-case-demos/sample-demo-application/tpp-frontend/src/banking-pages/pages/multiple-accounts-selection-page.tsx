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

import {Box, Button, Checkbox, FormControl, FormControlLabel, Grid, Switch, useTheme} from "@oxygen-ui/react";
import {useNavigate, useOutletContext} from "react-router-dom";
import type { SelectedAccountEntry } from "./accounts-selection-with-permissions-page.tsx";
import type {OutletContext} from "./login-page.tsx";
import {useState} from "react";
import './inner-pages-stylings.scss'
import {useMediaQuery} from "@mui/material";

/**
 * @function MultipleAccountsSelectionPage
 * @description A complex selection component simulating a multi-account consent process.
 * It allows the user to select multiple accounts and define which specific **permissions**
 * (e.g., read/write) apply to each selected account using checkboxes, before submitting
 * the data to proceed via `onSuccessHandler`.
 */
const MultipleAccountsSelectionPage = ()=>{

    const { accountsNumbersToAdd,onSuccessHandler, accountsToAdd,selectedAccountNumber, themeColor } = useOutletContext<OutletContext>();
    const multiAccounts = accountsNumbersToAdd.map((account) => {return selectedAccountNumber+account});
    const listOfPermissions = ["Accounts read", "Accounts write", "Accounts basics"];
    const [selectedData, setSelectedData] = useState<SelectedAccountEntry[]>(() => {
        return listOfPermissions.map(permission => ({
            permission: permission,
            accounts: [] as string[]
        }));
    });
    const handleAccountChange = (permission: string, accountId: string, checked: boolean) => {
        setSelectedData(prevData => {
            return prevData.map(entry => {
                if (entry.permission === permission) {

                    const accounts = checked ? [...entry.accounts, accountId] : entry.accounts.filter(id => id !== accountId);
                    return { ...entry, accounts };
                }
                return entry;
            });
        });
    };

    const hasSelectedAccounts = selectedData.some(entry => entry.accounts.length > 0);
    const handleSubmit = () => {
        if(hasSelectedAccounts){
            accountsToAdd.current = {type:"multiple",data:[selectedData]};
            onSuccessHandler();
        }else{
            alert("You must select an account to add")
        }
    };
    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isSmallScreen ? '0.2rem' : '0.5rem';
    const navigate = useNavigate();

    return(
        <>
            <Grid container className={'content-page-container'} xs={12} sm={8} md={6} lg={6} sx={{padding:responsivePadding, flexGrow:1}}>
                <Grid className="page-name-container">
                    <p>Please select your accounts to add</p>
                </Grid>
                <Grid className={"form-login-one-container"}>
                    <Box sx={{display: "flex", justifyContent: "space-between", alignItems: "center",height:'fit-content'}}>
                        <FormControlLabel control={<Switch   id={"account-one"} checked disabled={true} sx={{"--oxygen-palette-primary-main": themeColor}}/>} label={"Recurring"} labelPlacement={'start'}/>
                        <p>Expire in : 4 Days</p>
                    </Box>

                    <FormControl sx={{display:'flex', flexDirection:'column', gap:'2rem'}}>
                        {listOfPermissions.map((item, index) => {
                            const currentAccounts = selectedData.find(d => d.permission === item)?.accounts || [];
                            return (
                                <Box key={index} sx={{display: 'flex', flexDirection: 'column'}}>
                                    <p>Permission to :  {item}</p>
                                    {multiAccounts.map((account, index2) => {
                                        const isChecked = currentAccounts.includes(account);
                                        return (
                                            <FormControlLabel key={index2} control={<Checkbox id={account} sx={{'--oxygen-palette-primary-main':themeColor}} checked={isChecked} onChange={(e) => handleAccountChange(item, account, e.target.checked)}/>} label={account}/>
                                        )
                                })}
                            </Box>)
                        })}
                    </FormControl>
                    <Box className="form-buttons-container">
                        <Button variant={'contained'} onClick={handleSubmit} sx={{width:'6rem',height:'3rem','--oxygen-palette-gradients-primary-stop2':themeColor, '--oxygen-palette-gradients-primary-stop1':themeColor}}>Confirm</Button>
                        <Button variant={'outlined'} onClick={()=>{navigate(-1)}} sx={{width:'6rem',height:'3rem','--oxygen-palette-primary-main':themeColor, borderColor:themeColor}}>Cancel</Button>
                    </Box>
                </Grid>
            </Grid>
        </>
    )
}

export default MultipleAccountsSelectionPage;
