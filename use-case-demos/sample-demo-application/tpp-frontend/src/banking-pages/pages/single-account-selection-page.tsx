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

import {
    Box,
    Button,
    FormControl,
    FormControlLabel,
    Grid,
    Radio,
    RadioGroup, useTheme
} from "@oxygen-ui/react";
import {useNavigate, useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import {useState} from "react";
import {useMediaQuery} from "@mui/material";
import './inner-pages-stylings.scss'

/**
 * @function SingleAccountSelectionPage
 * @description A dynamic component simulating an account selection step during a bank connection flow.
 * It presents a list of mock accounts for the user to select using radio buttons.
 * Upon successful selection, it updates the `accountsToAdd` context and proceeds to the
 * next authorization step via `onSuccessHandler`.
 */
const SingleAccountSelectionPage = ()=>{

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isSmallScreen ? '1rem' : '2rem';
    const { accountsNumbersToAdd,onSuccessHandler,navigationData, accountsToAdd,selectedAccountNumber,themeColor } = useOutletContext<OutletContext>();
    const accountsList = (accountsNumbersToAdd || []).map((account) => {return selectedAccountNumber+account});
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
            <Grid container className={'content-page-container'} xs={12} sm={8} md={6} lg={6} sx={{padding:responsivePadding}}>
                <Grid className="page-name-container">
                    <p>Please select your account from the list</p>
                </Grid>
                <Grid className={"form-login-one-container"}>
                    <FormControl sx={{display:'flex', flexDirection:'column', alignItems:'center', marginTop:'5%'}}>
                        <RadioGroup aria-label="select-account" name="account-selection-group" value={selectedAccount} onChange={handleRadioChange}>
                            {accountsList.map((account, index) => {
                                return (
                                    <FormControlLabel key={index} control={<Radio sx={{'--oxygen-palette-primary-main': themeColor}} />} label={`${navigationData.current.bankInfo.name || 'Bank'}-${account}`} value={account}/>
                                );
                            })}
                        </RadioGroup>
                    </FormControl>
                    <Box className="form-buttons-container">
                        <Button variant={'contained'} onClick={handleAccountSelection} sx={{width:'6rem',height:'3rem','--oxygen-palette-gradients-primary-stop2':themeColor, '--oxygen-palette-gradients-primary-stop1':themeColor}}>Done</Button>
                        <Button variant={'outlined'} onClick={()=>{navigate(-1)}} sx={{width:'6rem',height:'3rem','--oxygen-palette-primary-main':themeColor, borderColor:themeColor}}>Cancel</Button>
                    </Box>
                </Grid>
            </Grid>
        </>
    )
}

export default SingleAccountSelectionPage;
