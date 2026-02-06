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

import {Box, Button, Checkbox, FormControl, FormControlLabel, Grid, Switch} from "@oxygen-ui/react";
import {useOutletContext} from "react-router-dom";
import type {SelectedAccountEntry} from "./accounts-selection-with-permissions-page.tsx";
import type {OutletContext} from "./login-page.tsx";
import {useState} from "react";
import './inner-pages-stylings.scss'

/**
 * @function MultipleAccountsSelectionPage
 * @description A complex selection component simulating a multi-account consent process.
 * It allows the user to select multiple accounts and define which specific **permissions**
 * (e.g., read/write) apply to each selected account using checkboxes, before submitting
 * the data to proceed via `onSuccessHandler`.
 */

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

const MultipleAccountsSelectionPage = ()=>{

    const { accountsNumbersToAdd, onSuccessHandler, accountsToAdd, selectedAccountNumber, themeColor, handleCancel, setToggleButtonState, toggleButtonState } = useOutletContext<OutletContext>();
    const multiAccounts = accountsNumbersToAdd.map((account) => selectedAccountNumber + account);
    const listOfPermissions = ["Read Accounts", "Read Balances", "Read Transactions"];

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

    const handleToggleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setToggleButtonState(event.target.checked);
    };

    const hasSelectedAccounts = selectedData.some(entry => entry.accounts.length > 0);

    const handleSubmit = () => {
        if(hasSelectedAccounts){
            accountsToAdd.current = {type:"multiple", data:[selectedData]};
            onSuccessHandler();
        }else{
            alert("You must select an account to add")
        }
    };

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
                        <Box sx={{ marginBottom: '0.5rem' }}>
                            <FormControlLabel
                                sx={{ ml: 0, pl: 0 }}
                                control={
                                    <Switch
                                        checked={toggleButtonState ?? true}
                                        sx={{ "--oxygen-palette-primary-main": themeColor }}
                                        onChange={handleToggleChange}
                                    />
                                }
                                label={"Recurring"}
                                labelPlacement={'start'}
                            />
                            <p style={{ fontSize: '0.95rem' }}><strong>Expires on:</strong> {getExpiryDate()}</p>
                        </Box>

                        <FormControl sx={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                            {listOfPermissions.map((item, index) => {
                                const currentAccounts = selectedData.find(d => d.permission === item)?.accounts || [];
                                return (
                                    <Box key={index} sx={{ display: 'flex', flexDirection: 'column' }}>
                                        <p style={{ fontSize: '0.95rem' }}><strong>Permission to:</strong> {item}</p>
                                        <p style={{ fontSize: '0.95rem' }}>Select the accounts you wish to authorize</p>
                                        <Box sx={{ display: 'flex', flexDirection: 'column' }}>
                                            {multiAccounts.map((account, index2) => {
                                                const isChecked = currentAccounts.includes(account);
                                                return (
                                                    <FormControlLabel
                                                        key={index2}
                                                        control={
                                                            <Checkbox
                                                                checked={isChecked}
                                                                onChange={(e) => handleAccountChange(item, account, e.target.checked)}
                                                                sx={{ '--oxygen-palette-primary-main': themeColor }}
                                                            />
                                                        }
                                                        label={account}
                                                        sx={{ fontSize: '0.95rem' }}
                                                    />
                                                );
                                            })}
                                        </Box>
                                    </Box>
                                );
                            })}
                        </FormControl>
                    </Box>

                    <Box className="form-buttons-container">
                        <Button
                            variant={'contained'}
                            onClick={handleSubmit}
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

export default MultipleAccountsSelectionPage;
