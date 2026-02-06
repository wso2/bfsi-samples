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

import {Box, Button, Checkbox, FormControl, FormControlLabel, FormLabel, Grid, List, ListItem, Switch} from "@oxygen-ui/react";
import {useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import {useState} from "react";
import './inner-pages-stylings.scss'

// Re-using the getExpiryDate function from our previous conversation
export const getExpiryDate = () => {
    const date = new Date();
    date.setDate(date.getDate() + 5);
    const day = date.getDate();
    const month = date.toLocaleString('en-GB', { month: 'long' });
    const year = date.getFullYear();

    const getOrdinalSuffix = (n: number) => {
        if (n > 3 && n < 21) return 'th';
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    };
    return `${day}${getOrdinalSuffix(day)} ${month} ${year}`;
};

const AccountsSelectionWithPermissionsPage = ()=>{

    const {accountsNumbersToAdd, onSuccessHandler, navigationData, accountsToAdd, selectedAccountNumber, themeColor, handleCancel} = useOutletContext<OutletContext>();
    const accountsList = accountsNumbersToAdd.map((account) => selectedAccountNumber + account);
    const permissions = ["Read Accounts", "Read Balances", "Read Transactions"];

    const [selectedAccounts, setSelectedAccounts] = useState<string[]>([]);

    const handleCheckboxChange = (account: string, checked: boolean) => {
        setSelectedAccounts(prev =>
            checked ? [...prev, account] : prev.filter(a => a !== account)
        );
    };

    const handleAccountSelection = () => {
        if(selectedAccounts.length > 0){
            const formattedData = [{
                permission: "All Permissions",
                accounts: selectedAccounts
            }];

            accountsToAdd.current = {type:"multiple", data:[formattedData]};
            console.log(accountsToAdd.current);
            onSuccessHandler();
        } else {
            alert("Please select at least one account.");
        }
    }

    return(
        <>
            <Grid container className={'content-page-container'} xs={12} sm={12} md={12} lg={12}>
                <Grid className="page-name-container" sx={{ whiteSpace: 'balance' }}>
                    <p style={{ whiteSpace: 'balanced', fontSize: '0.8rem' }}>Accounts Central requests the consent to access following details</p>
                </Grid>
                <Grid className={'form-login-one-container'}>
                    <Box
                        sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            gap: '0.5rem',
                            margin: '0 auto',
                            width: '100%',
                            maxWidth: '500px',
                            padding: '1.5rem',
                            border: '1px solid #e0e0e0',
                            borderRadius: '8px',
                            backgroundColor: '#f9f9f9'
                        }}
                    >
                        <FormControl>
                            <FormLabel>Requesting consents for following permissions</FormLabel>
                            <List
                                sx={{
                                    listStyleType: 'disc',
                                    paddingLeft: '2rem',
                                    margin: 0
                                }}
                            >
                                {permissions.map((item, index) => (
                                    <ListItem
                                        key={index}
                                        sx={{
                                            display: 'list-item',
                                            padding: '0.25rem 0',
                                            fontSize: '0.95rem'
                                        }}
                                    >
                                        {item}
                                    </ListItem>
                                ))}
                            </List>
                        </FormControl>

                        <Box sx={{ marginTop: '0.5rem' }}>
                            <FormControlLabel
                                sx={{ ml: 0, pl: 0 }}
                                control={<Switch sx={{ "--oxygen-palette-primary-main": themeColor }} />}
                                label={"Recurring"}
                                labelPlacement={'start'}
                            />
                            <p style={{ fontSize: '0.95rem' }}>Expires in: {getExpiryDate()}</p>
                        </Box>

                        <Box sx={{ marginTop: '0.5rem' }}>
                            <strong>Select the accounts you wish to authorize</strong>
                            <Box sx={{ display: 'flex', flexDirection: 'column', width: '100%' }}>
                                {accountsList.map((account, index) => {
                                    const isChecked = selectedAccounts.includes(account);
                                    return (
                                        <FormControlLabel
                                            key={index}
                                            control={
                                                <Checkbox
                                                    checked={isChecked}
                                                    onChange={(e) => handleCheckboxChange(account, e.target.checked)}
                                                    sx={{ '--oxygen-palette-primary-main': themeColor }}
                                                />
                                            }
                                            label={`${navigationData.current?.bankInfo?.name ?? 'Bank'}-${account}`}
                                            sx={{ fontSize: '0.95rem' }}
                                        />
                                    );
                                })}
                            </Box>
                        </Box>
                    </Box>

                    <Box className="form-buttons-container">
                        <Button
                            variant={'contained'}
                            onClick={handleAccountSelection}
                            sx={{
                                width: '6rem',
                                height: '3rem',
                                '--oxygen-palette-gradients-primary-stop2': themeColor,
                                '--oxygen-palette-gradients-primary-stop1': themeColor
                            }}
                        >
                            Confirm
                        </Button>
                        <Button
                            variant={'outlined'}
                            onClick={handleCancel}
                            sx={{
                                width: '6rem',
                                height: '3rem',
                                '--oxygen-palette-primary-main': themeColor,
                                borderColor: themeColor
                            }}
                        >
                            Cancel
                        </Button>
                    </Box>
                </Grid>
            </Grid>
        </>
    )
}

export default AccountsSelectionWithPermissionsPage;
