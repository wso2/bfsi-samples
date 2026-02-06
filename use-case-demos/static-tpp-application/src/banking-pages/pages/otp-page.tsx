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

import {Box, Button, FormControl, Grid, Input, Typography} from "@oxygen-ui/react";
import {useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import {Controller, useForm} from "react-hook-form";
import {ErrorMessage} from "../../pages/payments-page/payment-form/payment-form.tsx";
import './inner-pages-stylings.scss';

interface OtpFormData{
    code:string;
}

/**
 * @function OtpPage
 * @description A component simulating a **One-Time Password (OTP)** authentication step
 * within a bank flow. It uses `react-hook-form` for input validation and checks for a
 * hardcoded valid code ("55555"). Upon success, it calls `onSuccessHandler` to advance
 * the user to the next step in the flow.
 */
const OtpPage = ()=>{

    const { onSuccessHandler, themeColor, handleCancel } = useOutletContext<OutletContext>();
    const {control, handleSubmit, formState:{errors}} = useForm<OtpFormData>({
        defaultValues:{
            code:''
        }
    })
    const onSubmitHandler = (data: OtpFormData) => {
        if(data.code){
            onSuccessHandler()
        }else{
            alert("Check your Otp and re-enter")
        }
    }
    return(
        <>
            <Grid container className={'content-page-container'} xs={12} sm={12} md={12} lg={12}>
                <Grid className="page-name-container" sx={{ whiteSpace: 'balance' }}>
                    <p style={{ whiteSpace: 'balanced', fontSize: '0.8rem' }}>OTP Verification</p>
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
                        <Typography sx={{ opacity: 0.4, fontSize: '0.875rem' }}>
                            A text message with the verification code was sent to (xxx) xxx 4098
                        </Typography>
                        <form onSubmit={handleSubmit(onSubmitHandler)} style={{ gap: '1rem', display: 'flex', flexDirection: 'column' }}>
                            <FormControl fullWidth={true}>
                                <label>Verification code <span style={{ color: "var(--oxygen-palette-primary-requiredStar)" }}>*</span></label>
                                <Controller name={'code'} control={control} rules={{ required: true }} render={({ field }) => (
                                    <Input
                                        {...field}
                                        placeholder={"Otp input field"}
                                        type={"text"}
                                        error={!!errors.code}
                                        sx={{ '--oxygen-palette-primary-main': themeColor }}
                                    />
                                )}/>
                                <ErrorMessage error={errors.code}/>
                            </FormControl>
                            <Typography sx={{ fontWeight: 300, opacity: 0.4, fontSize: '0.875rem' }}>
                                Code will expire after 01:00 min
                            </Typography>
                        </form>
                    </Box>
                    <Box className="form-buttons-container">
                        <Button
                            variant={'contained'}
                            type={'submit'}
                            form="otp-form"
                            onClick={handleSubmit(onSubmitHandler)}
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

export default OtpPage;
