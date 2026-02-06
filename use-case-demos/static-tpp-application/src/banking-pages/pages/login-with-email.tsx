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

import {Box, Button, FormControl, Grid, Input} from "@oxygen-ui/react";
import {useOutletContext} from "react-router-dom";
import {Controller, useForm} from "react-hook-form";
import type {AppInfo} from "../../hooks/config-interfaces.ts";
import './inner-pages-stylings.scss';

export interface OutletContext{
    onSuccessHandler : () => void;
    navigationData : any;
    appInfo : AppInfo;
    themeColor : string;
    handleCancel:()=>void;
}

interface loginformData{
    email: string;
}

/**
 * @function LoginWithEmailPage
 * @description A simulated login component for the bank authorization flow focusing only on email input.
 * It uses `react-hook-form` for input and validation, and upon submitting the correct
 * email ("john@gmail.com"), it calls `onSuccessHandler` to advance the user to the next step.
 */
const LoginWithEmailPage = ()=>{

    const { onSuccessHandler, themeColor, handleCancel } = useOutletContext<OutletContext>();
    const {control, handleSubmit, formState: {errors}} = useForm<loginformData>({
        defaultValues:{
            email:''
        }
    })

    const onSubmit = (data:loginformData)=>{
        if (data.email != ''){
            onSuccessHandler()
        }else{
            alert("Email not matched")
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
                        <form onSubmit={handleSubmit(onSubmit)} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
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
                                )}/>
                            </FormControl>
                        </form>
                    </Box>

                    <Box className="form-buttons-container">
                        <Button
                            variant={'contained'}
                            onClick={handleSubmit(onSubmit)}
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

export default LoginWithEmailPage;
