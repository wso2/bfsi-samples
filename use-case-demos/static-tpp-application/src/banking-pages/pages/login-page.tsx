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

import { Box, Button, FormControl, Grid, Input } from "@oxygen-ui/react";
import { useOutletContext } from "react-router-dom";
import { Controller, useForm } from "react-hook-form";
import { ErrorMessage } from "../../pages/payments-page/payment-form/payment-form.tsx";
import './inner-pages-stylings.scss'
import type { AppInfo } from "../../hooks/config-interfaces.ts";


export interface OutletContext {
    onSuccessHandler: () => void;
    navigationData: any;
    accountsToAdd: any;
    appInfo: AppInfo;
    themeColor: string;
    selectedAccountNumber: string;
    accountsNumbersToAdd: string[];
    handleCancel: () => void;
    isCancelled: boolean;
    toggleButtonState: boolean;
    setToggleButtonState: (state: boolean) => void;
}

interface loginformData {
    email: string;
    password: string;
}

/**
 * @function LoginPage
 * @description A simulated login component for the bank authorization flow.
 * It uses `react-hook-form` for input validation and state management, and upon
 * successful submission (simulated credential check), it calls `onSuccessHandler`
 * to proceed to the next step in the banking authorization sequence.
 */
const LoginPage = () => {

    const { onSuccessHandler, themeColor, handleCancel } = useOutletContext<OutletContext>();
    const { control, handleSubmit, formState: { errors } } = useForm<loginformData>({
        defaultValues: {
            email: '', password: ''
        }
    })

    const onSubmitHandler = (data: loginformData) => {
        console.log("Submitting...")
        if (data.email !== '' && data.password !== '') {
            onSuccessHandler();
        } else {
            alert('wrong email or password')
        }
    }

    return (
        <>
            <Grid container className={'content-page-container'} xs={12} sm={12} md={12} lg={12}>
                <Grid className="page-name-container" sx={{ whiteSpace: 'balance' }}>
                    <p style={{ whiteSpace: 'balanced', fontSize: '0.8rem' }}>Login to your account</p>
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
                        <form onSubmit={handleSubmit(onSubmitHandler)} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                            <FormControl fullWidth={true}>
                                <label>Email <span style={{ color: "var(--oxygen-palette-primary-requiredStar)" }}>*</span></label>
                                <Controller name={'email'} control={control} rules={{ required: true }} render={({ field }) => (
                                    <Input
                                        {...field}
                                        placeholder={"Enter your email"}
                                        type={"text"}
                                        error={!!errors.email}
                                        sx={{ '--oxygen-palette-primary-main': themeColor }}
                                    />
                                )} />
                                <ErrorMessage error={errors.email} />
                            </FormControl>

                            <FormControl fullWidth={true}>
                                <label>Password <span style={{ color: "var(--oxygen-palette-primary-requiredStar)" }}>*</span></label>
                                <Controller name={'password'} control={control} rules={{ required: true }} render={({ field }) => (
                                    <Input
                                        {...field}
                                        placeholder={"Enter your password"}
                                        type={"password"}
                                        error={!!errors.password}
                                        sx={{ '--oxygen-palette-primary-main': themeColor }}
                                    />
                                )} />
                                <ErrorMessage error={errors.password} />
                            </FormControl>
                        </form>
                    </Box>

                    <Box className="form-buttons-container">
                        <Button
                            variant={'contained'}
                            onClick={handleSubmit(onSubmitHandler)}
                            sx={{
                                width: '6rem',
                                height: '3rem',
                                '--oxygen-palette-gradients-primary-stop2': themeColor,
                                '--oxygen-palette-gradients-primary-stop1': themeColor
                            }}
                        >
                            Login
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

export default LoginPage;
