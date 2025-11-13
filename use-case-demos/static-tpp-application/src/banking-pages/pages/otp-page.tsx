/*
 * *
 *  * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 *  *
 *  * WSO2 LLC. licenses this file to you under the Apache License,
 *  * Version 2.0 (the "License"); you may not use this file except
 *  * in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

import {Box, Button, FormControl, Grid, OutlinedInput, useTheme} from "@oxygen-ui/react";
import {useNavigate, useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import {Controller, useForm} from "react-hook-form";
import {ErrorMessage} from "../../pages/payments-page/payment-form/payment-form.tsx";
import { useMediaQuery } from "@mui/material";
import './inner-pages-stylings.scss';

interface OtpFormData{
    code:string;
}

const OtpPage = ()=>{

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsivePadding = isSmallScreen ? '1rem' : '2rem';

    const { onSuccessHandler } = useOutletContext<OutletContext>();

    const {control,handleSubmit,formState:{errors}} = useForm<OtpFormData>({
        defaultValues:{
            code:''
        }
    })

    const onSubmitHandler=(data:OtpFormData)=>{
        if(data.code==='55555'){
            onSuccessHandler()
        }else{
            alert("Check your Otp and re-enter")
        }

    }

    const navigate = useNavigate();

    return(
        <>
            <Grid container className={'content-page-container'} xs={12} sm={8} md={6} lg={4} sx={{padding:responsivePadding}}>
                <Grid className="page-name-container">
                    <h3>SMS Authentication</h3>
                </Grid>
                
                <Grid className={"form-login-one-container"}>
                    <form onSubmit={handleSubmit(onSubmitHandler)}>
                        <FormControl fullWidth={true} margin={'normal'} >
                            <label>OTP code</label>
                            <Controller name={'code'} control={control} rules={{required:'Email address required'}}  render={({field}) => (
                                <OutlinedInput
                                    {...field}
                                    placeholder={"Enter otp"}
                                    type={"text"}
                                    error={!!errors.code}
                                />
                            )}/>
                            <ErrorMessage error={errors.code}/>
                        </FormControl>

                        <Box className="form-buttons-container">
                            <Button variant={'contained'} className="button-styles" type={'submit'}>Confirm</Button>
                            <Button variant={'outlined'} className="button-styles" onClick={()=>{navigate(-1)}}>Cancel</Button>
                        </Box>
                    </form>
                </Grid>
            </Grid>
        </>
    )
}

export default OtpPage;
