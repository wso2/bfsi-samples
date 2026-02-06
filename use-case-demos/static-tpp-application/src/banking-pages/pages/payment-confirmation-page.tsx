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

import {Box, Button, Grid, List, ListItem} from "@oxygen-ui/react";
import {useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import './inner-pages-stylings.scss'

/**
 * @function PaymentConfirmationPage
 * @description A dynamic component simulating the final user authorization step for a payment.
 * It displays a summary of the full payment details (amount, payee, reference, source account),
 * and requires the user to click "Confirm" to trigger `onSuccessHandler` and finalize the transaction.
 */
const PaymentConfirmationPage = () => {

    const { onSuccessHandler, navigationData, themeColor, handleCancel } =
        useOutletContext<OutletContext>();

    return (
        <>
            <Grid container className={'content-page-container'} xs={12} sm={12} md={12} lg={12}>
                <Grid className="page-name-container" sx={{ whiteSpace: 'balance' }}>
                    <p style={{ whiteSpace: 'balanced', fontSize: '0.8rem' }}>
                        Accounts Central requests the consent to perform the following payment</p>
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
                            <strong>From Account:</strong> {navigationData.current?.formData?.userAccount ?? 'N/A'}
                        </Box>
                        <List
                            sx={{
                                listStyleType: 'disc',
                                paddingLeft: '2rem',
                                margin: 0
                            }}
                        >
                            <ListItem
                                sx={{
                                    display: 'list-item',
                                    padding: '0.25rem 0',
                                    fontSize: '0.95rem'
                                }}
                            >
                                Amount: {navigationData.current?.formData?.currency ?? ''} {navigationData.current?.formData?.amount ?? 'N/A'}
                            </ListItem>
                            <ListItem
                                sx={{
                                    display: 'list-item',
                                    padding: '0.25rem 0',
                                    fontSize: '0.95rem'
                                }}
                            >
                                Biller Name: {navigationData.current?.formData?.payeeAccount ?? 'N/A'}
                            </ListItem>
                            <ListItem
                                sx={{
                                    display: 'list-item',
                                    padding: '0.25rem 0',
                                    fontSize: '0.95rem'
                                }}
                            >
                                Reference: {navigationData.current?.formData?.reference ?? 'N/A'}
                            </ListItem>
                        </List>
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

export default PaymentConfirmationPage;
