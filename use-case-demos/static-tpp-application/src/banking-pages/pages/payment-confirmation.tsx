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

import {Box, Button, FormControl, Grid} from "@oxygen-ui/react";
import {useOutletContext} from "react-router-dom";
import type {OutletContext} from "./login-page.tsx";
import '../banking.scss'

/**
 * @function PaymentConfirmation
 * @description A simulated component confirming payment details during the bank's authorization flow.
 * It displays the payee, amount, and reference passed through navigation state,
 * and upon clicking "Success," it calls `onSuccessHandler` to simulate payment execution.
 */
const PaymentConfirmation = ()=>{

    const { onSuccessHandler, navigationData } = useOutletContext<OutletContext>();

    return (
        <>
            <Grid container className={'login-container'}>
                <FormControl>
                    <p>Confirm the following payment <strong>{navigationData.current.formData.amount}</strong> in {navigationData.current.formData.currency} to {navigationData.current.formData.payeeAccount} under the reference {navigationData.current.formData.reference}.
                        Please confirm the payment to proceed.
                    </p>
                    <Box sx={{marginTop:'60%'}}>
                        <Button variant={'contained'} onClick={onSuccessHandler}>Success</Button>
                        <Button variant={'outlined'} >Cancel</Button>
                    </Box>
                </FormControl>
            </Grid>
        </>
    )
}

export default PaymentConfirmation
