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

import { Box, Button, FormControl, Grid, OutlinedInput, useTheme } from "@oxygen-ui/react";
import {useNavigate, useOutletContext} from "react-router-dom";
import { Controller, useForm } from "react-hook-form";
import { ErrorMessage } from "../../pages/payments-page/payment-form/payment-form.tsx";
import './inner-pages-stylings.scss'
import { useMediaQuery } from "@mui/material";
import type {AppInfo} from "../../hooks/config-interfaces.ts";

export interface OutletContext {
    onSuccessHandler: () => void;
    navigationData: any;
    accountsToAdd: any;
    appInfo: AppInfo;
}

interface loginformData {
    email: string;
    password: string;
}

const LoginPage = () => {

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isSmallScreen ? '1rem' : '2rem';

    const navigate = useNavigate();
    const { onSuccessHandler,appInfo } = useOutletContext<OutletContext>();
    const { control, handleSubmit, formState: { errors } } = useForm<loginformData>({
        defaultValues: {
            email: '', password: ''
        }
    })

    const onSubmitHandler = (data: loginformData) => {
        console.log("Submitting...")
        if (data.email === 'john@gmail.com' && data.password !== '') {
            onSuccessHandler();
        } else {
            alert('wrong email or password')
        }
    }

    return (
        <>
            <Grid container className={'content-page-container'} xs={12} sm={8} md={6} lg={4} sx={{padding:responsivePadding}}>
                <Grid className="page-name-container">
                    <h3>Login Page</h3>
                </Grid>

                <Grid className="form-login-one-container">
                    <form onSubmit={handleSubmit(onSubmitHandler)}>

                        <FormControl fullWidth={true} >
                            <label>Email</label>
                            <Controller name={'email'} control={control} rules={{ required: 'Email address required' }} render={({ field }) => (
                                <OutlinedInput
                                    {...field}
                                    placeholder={"Enter your email"}
                                    type={"text"}
                                    error={!!errors.email}
                                />
                            )} />
                            <ErrorMessage error={errors.email} />
                        </FormControl>

                        <FormControl fullWidth={true}>
                            <label>password</label>
                            <Controller name={'password'} control={control} rules={{ required: 'Password required to proceed' }} render={({ field }) => (
                                <OutlinedInput
                                    {...field}
                                    placeholder={"Enter your password"}
                                    type={"password"}
                                    error={!!errors.password}
                                />
                            )} />
                            <ErrorMessage error={errors.password} />
                        </FormControl>

                        <Box className="form-buttons-container">
                            <Button className="button-styles" variant={'contained'} type={'submit'}>Login</Button>
                            <Button variant={'outlined'} className="button-styles" onClick={()=>{navigate(`/${appInfo.route}/home`)}}>Cancel</Button>
                        </Box>
                    </form>
                </Grid>
            </Grid>
        </>
    )
}

export default LoginPage;
