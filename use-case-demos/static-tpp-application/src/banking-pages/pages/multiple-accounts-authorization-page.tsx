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

import {Box, Button, FormControl, FormControlLabel, Grid, List, ListItem, Switch} from "@oxygen-ui/react";
import {useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import {getExpiryDate, type SelectedAccountEntry} from "./multiple-accounts-selection-page.tsx";
import './inner-pages-stylings.scss'

/**
 * @function MultipleAccountsAuthorizationPage
 * @description A page component simulating the final authorization step for **multiple** selected accounts.
 * It displays a summary of the accounts being authorized, the specific permissions granted
 * for each (e.g., 'Recurring'), and the consent expiry date, leading to either confirmation
 * via `onSuccessHandler` or cancellation.
 */
const MultipleAccountsAuthorizationPage = ()=>{

    const { onSuccessHandler, accountsToAdd, themeColor, handleCancel, toggleButtonState } = useOutletContext<OutletContext>();

    return(
        <>
            <Grid container className={'content-page-container'} xs={12} sm={12} md={12} lg={12}>
                <Grid className="page-name-container" sx={{ whiteSpace: 'balance' }}>
                    <p style={{ whiteSpace: 'balanced', fontSize: '0.8rem' }}>Please confirm the authorization to your selected accounts</p>
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
                                        disabled={true}
                                        sx={{ "--oxygen-palette-primary-main": themeColor }}
                                    />
                                }
                                label={"Recurring"}
                                labelPlacement={'start'}
                            />
                            <p style={{ fontSize: '0.95rem' }}><strong>Expires on:</strong> {getExpiryDate()}</p>
                        </Box>

                        <FormControl sx={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                            {(accountsToAdd?.current?.data?.[0] || []).map((account: SelectedAccountEntry, index: number) => {
                                return (
                                    <Box key={index} sx={{ display: 'flex', flexDirection: 'column' }}>
                                        <p style={{ fontSize: '0.95rem' }}><strong>Permission to:</strong> {account.permission}</p>
                                        <List
                                            sx={{
                                                listStyleType: 'disc',
                                                paddingLeft: '2rem',
                                                margin: 0
                                            }}
                                        >
                                            {account.accounts && account.accounts.length > 0 ? (
                                                account.accounts.map((iban: string, idx: number) => (
                                                    <ListItem
                                                        key={idx}
                                                        sx={{
                                                            display: 'list-item',
                                                            padding: '0.25rem 0',
                                                            fontSize: '0.95rem'
                                                        }}
                                                    >
                                                        {iban}
                                                    </ListItem>
                                                ))
                                            ) : (
                                                <p style={{ color: 'rgba(0, 0, 0, 0.6)', fontStyle: 'italic', fontSize: '0.95rem' }}>
                                                    No account selected
                                                </p>
                                            )}
                                        </List>
                                    </Box>
                                )
                            })}
                        </FormControl>
                    </Box>

                    <Box className="form-buttons-container">
                        <Button
                            variant={'contained'}
                            onClick={onSuccessHandler}
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

export default MultipleAccountsAuthorizationPage;
