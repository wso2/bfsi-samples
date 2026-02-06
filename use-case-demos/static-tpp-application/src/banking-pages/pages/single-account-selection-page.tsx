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
    RadioGroup
} from "@oxygen-ui/react";
import { useOutletContext } from "react-router-dom";
import type { OutletContext } from "./login-page.tsx";
import { useState } from "react";
import './inner-pages-stylings.scss'

/**
 * @function SingleAccountSelectionPage
 * @description A dynamic component simulating an account selection step during a bank connection flow.
 * It presents a list of mock accounts for the user to select using radio buttons.
 * Upon successful selection, it updates the `accountsToAdd` context and proceeds to the
 * next authorization step via `onSuccessHandler`.
 */
const SingleAccountSelectionPage = () => {

    const {
        accountsNumbersToAdd,
        onSuccessHandler,
        navigationData,
        accountsToAdd,
        selectedAccountNumber,
        themeColor,
        handleCancel
    } = useOutletContext<OutletContext>();
    const accountsList = (accountsNumbersToAdd || []).map(
        (account) => selectedAccountNumber + account
    );
    const [selectedAccount, setSelectedAccount] = useState<string>('');
    const handleAccountSelection = () => {
        if (selectedAccount.length > 0) {
            accountsToAdd.current = { type: "single", data: [selectedAccount] };
            onSuccessHandler();
        }
    };
    const handleRadioChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSelectedAccount(event.target.value);
    };
    const bankName = navigationData.current?.bankInfo?.name || 'Bank';

    return (
        <Grid container className={'content-page-container'} xs={12} sm={12} md={12} lg={12}>
            <Grid className="page-name-container" sx={{ whiteSpace: 'balance' }}>
                <p style={{ whiteSpace: 'balanced', fontSize: '0.8rem' }}>
                    Select the account you wish to authorize</p>
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
                        <RadioGroup
                            aria-label="select-account"
                            name="account-selection-group"
                            value={selectedAccount}
                            onChange={handleRadioChange}
                        >
                            {accountsList.map((account, index) => (
                                <FormControlLabel
                                    key={index}
                                    control={
                                        <Radio
                                            sx={{ '--oxygen-palette-primary-main': themeColor }}
                                        />
                                    }
                                    label={`${bankName}-${account}`}
                                    value={account}
                                    sx={{ fontSize: '0.95rem' }}
                                />
                            ))}
                        </RadioGroup>
                    </FormControl>
                </Box>

                <Box className="form-buttons-container">
                    <Button
                        variant={'contained'}
                        onClick={handleAccountSelection}
                        disabled={selectedAccount.length === 0}
                        sx={{
                            width: '6rem',
                            height: '3rem',
                            '--oxygen-palette-gradients-primary-stop2': themeColor,
                            '--oxygen-palette-gradients-primary-stop1': themeColor,
                            '&.Mui-disabled': {
                                opacity: 0.45,
                                cursor: 'not-allowed',
                                pointerEvents: 'auto'
                            }
                        }}
                    >
                        Next
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
    );
}

export default SingleAccountSelectionPage;
