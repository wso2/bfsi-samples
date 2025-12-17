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

import {Box, Button, FormControl, Grid, Input, useTheme} from "@oxygen-ui/react";
import {useNavigate, useOutletContext} from "react-router-dom";
import {Controller, useForm} from "react-hook-form";
import {ErrorMessage} from "../../pages/payments-page/payment-form/payment-form.tsx";
import {useMediaQuery} from "@mui/material";
import type {AppInfo} from "../../hooks/config-interfaces.ts";

export interface OutletContext{
    onSuccessHandler : () => void;
    navigationData : any;
    appInfo : AppInfo;
    themeColor : string;
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

    const { onSuccessHandler, appInfo,themeColor} = useOutletContext<OutletContext>();
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
    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isSmallScreen ? '1rem' : '2rem';
    const navigate = useNavigate();
    return (
        <>
            <Grid container className={'content-page-container'} xs={12} sm={8} md={6} lg={6} sx={{padding:responsivePadding}}>
                <Grid className="page-name-container">
                    <p>Please login to your account</p>
                </Grid>
                <Grid className={"form-login-one-container"}>
                    <form onSubmit={handleSubmit(onSubmit)} className={"form-input"}>
                        <FormControl fullWidth={true} margin={'normal'} >
                            <label>Email</label>
                            <Controller name={'email'} control={control} render={({field}) => (
                                <Input
                                    {...field}
                                    placeholder={"Enter your email"}
                                    type={"text"}
                                    error={!!errors.email}
                                    sx={{marginLeft:'2rem', '--oxygen-palette-primary-main':themeColor}}
                                />
                            )}/>
                            <ErrorMessage error={errors.email}/>
                        </FormControl>
                        <Box className="form-buttons-container">
                            <Button variant={'contained'} type={'submit'} sx={{width:'6rem',height:'3rem','--oxygen-palette-gradients-primary-stop2':themeColor, '--oxygen-palette-gradients-primary-stop1':themeColor}}>Login</Button>
                            <Button variant={'outlined'} sx={{width:'6rem',height:'3rem','--oxygen-palette-primary-main':themeColor, borderColor:themeColor}} onClick={()=>{navigate(`/${appInfo.route}/home`)}}>Cancel</Button>
                        </Box>
                    </form>
                </Grid>
            </Grid>
        </>
    )
}

export default LoginWithEmailPage;
