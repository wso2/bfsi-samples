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

import {Controller, useForm} from "react-hook-form";
import {Box, Button, FormControl, MenuItem, OutlinedInput, Select} from "@oxygen-ui/react";
import {NumericFormat} from "react-number-format";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import type {AppInfo, Bank, Payee} from "../../../hooks/config-interfaces.ts";
import OverlayConfirmation from "../../../components/overlay-confirmation/overlay-confirmation.tsx";
import '../payments-page.scss'
import useMediaQuery from "@mui/material/useMediaQuery";
import {useTheme} from "@mui/material";
import type {BanksWithAccounts} from "../../../hooks/use-config-context.ts";

export interface PaymentFormData {
    userAccount: string;
    payeeAccount: string;
    currency: string;
    amount: number;
    reference: string;
    appInfo: AppInfo;
}

interface PaymentFormProps {
    banksWithAllAccounts: BanksWithAccounts[];
    payeeData: Payee[];
    banksList: Bank[];
}

const currency = ["GBP","EURO","USD"]

export const ErrorMessage = ({error}:{error:any})=>{
    if (!error)return null;

    return <p className={"error-message-payments"}>{error.message}</p>
}

/**
 * @function PaymentForm
 * @description Manages the user interface, state, and validation for initiating a new payment.
 * It collects payment details and, upon confirmation, redirects the user to the
 * corresponding bank's authorization flow (via `react-router` state).
 */
const PaymentForm = ({banksWithAllAccounts, payeeData, banksList}:PaymentFormProps) => {

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsiveDirection = isSmallScreen ? 'column' : 'row';
    const navigate = useNavigate();
    const {control, handleSubmit, formState: {errors},reset} = useForm<PaymentFormData>({
        defaultValues: {
            userAccount: '',
            payeeAccount: '',
            currency: '',
            amount: 0,
            reference: ''
        }
    });

    const [isConfirming, setIsConfirming] = useState(false);
    const [formDataToSubmit, setFormDataToSubmit] = useState<PaymentFormData | null>(null)
    const onSubmit = (data: PaymentFormData) => {
        setFormDataToSubmit(data);
        setIsConfirming(true);

    }
    const handleConfirmedAndRedirect = () => {
        if (formDataToSubmit){
            setIsConfirming(false);
            const bankName = formDataToSubmit.userAccount.split('-')[0];
            const target = banksList.find((bank)=>{
                return bank.name === bankName;
            })
            if(!target){
                console.log(`Bank "${bankName}" not found in banksList`)
                return;
            }

            navigate("/"+target.route+"/login?type=payment",{
                state:{
                    formData: formDataToSubmit,
                    message: "payment",
                    bankInfo: target,
                }
            });
        }
    }

    const handleCancelConfirmation = () => {
        setIsConfirming(false);
        setFormDataToSubmit(null);
    }
    const paymentConfirmationMsg = `Are you sure you want to proceed with the payment of ${formDataToSubmit?.currency} ${formDataToSubmit?.amount} to payee ${formDataToSubmit?.payeeAccount}? `
    return (
        <>
            <h2 className={"payment-form-heading"}>Payment Information</h2>
            <form onSubmit={handleSubmit(onSubmit)}>
                <FormControl fullWidth={true} margin={'dense'}>
                    <label>User Account</label>
                    <Controller name={'userAccount'} control={control} rules={{required: 'User required'}} render={({field}) => (
                        <Select {...field}
                                displayEmpty
                                renderValue={(value) => {
                                    const selected = value as string
                                    if (selected === "") {
                                        return (
                                            <span style={{color: 'rgba(0, 0, 0, 0.38)'}}>Select payee account from here</span>
                                        );
                                    }
                                    return selected;
                                }}
                                error={!!errors.userAccount}>
                            {banksWithAllAccounts.map((bankWithAccounts)=>
                                bankWithAccounts.accounts.map((account)=>(
                                    <MenuItem key={`${bankWithAccounts.bank.name}-${account.id}`} value={`${bankWithAccounts.bank.name}-${account.id}`}>{bankWithAccounts.bank.name}-{account.id}</MenuItem>
                                ))
                            )}
                        </Select>
                    )}/>
                    <ErrorMessage error={errors.userAccount}/>
                </FormControl>
                <FormControl fullWidth={true} margin={'dense'}>
                    <label>Biller Info</label>
                    <Controller name={'payeeAccount'} control={control} rules={{required:'Payee required'}} render={({field}) => (
                        <Select {...field}
                                displayEmpty
                                renderValue={(value) => {
                                    const selected = value as string
                                    if (selected === "") {
                                        return (
                                            <span style={{color: 'rgba(0, 0, 0, 0.38)'}}>Select payee account from here</span>
                                        );
                                    }
                                    return selected;
                                }}
                                error={!!errors.payeeAccount}>
                            {payeeData.map((payee,index)=>(
                                <MenuItem key={index} value={`${payee.name}-${payee.bank}`}>{payee.name}-{payee.bank}</MenuItem>
                            ))}
                        </Select>
                    )}/>
                    <ErrorMessage error={errors.payeeAccount}/>
                </FormControl>
                <div style={{display: 'flex',gap:'1rem'}}>
                    <FormControl fullWidth={true} margin={'dense'}>
                        <label>Currency</label>
                        <Controller name={'currency'} control={control} rules={{required:'Currency must select'}} render={({field}) => (
                            <Select {...field}
                                    displayEmpty
                                    renderValue={(value) => {
                                        const selected = value as string
                                        if (selected === "") {
                                            return (
                                                <span style={{color: 'rgba(0, 0, 0, 0.38)'}}>Select currency</span>
                                            );
                                        }
                                        return selected;
                                    }}
                                    error={!!errors.currency}>
                                {currency.map((unit)=>(
                                    <MenuItem key={unit} value={`${unit}`}>{unit}</MenuItem>
                                ))}
                            </Select>
                        )}/>
                        <ErrorMessage error={errors.currency}/>
                    </FormControl>
                    <FormControl fullWidth={true} margin={'dense'}>
                        <label>Amount</label>
                        <Controller name={'amount'} control={control} rules={{required:'Add amount to transfer',min: {
                                value: 0.01,
                                message: 'Amount must be greater than 0.00'
                            }}} render={({field}) => (
                            <NumericFormat
                                {...field}
                                value={field.value === 0 ? '' : field.value}
                                customInput={OutlinedInput}
                                thousandSeparator={true}
                                decimalScale={2}
                                fixedDecimalScale={true}
                                allowLeadingZeros={false}
                                allowNegative={false}
                                onValueChange={(values) => {
                                    field.onChange(values.floatValue || 0);

                                }}
                                error={!!errors.amount}
                                placeholder="0.00"
                                type="text"
                            />
                        )}/>
                        <ErrorMessage error={errors.amount}/>
                    </FormControl>
                </div>
                <FormControl fullWidth={true} margin={'dense'} sx={{height: '10vh'}}>
                    <label>Reference</label>
                    <Controller name={'reference'} control={control} rules={{required:'Reference need to be added'}} render={({field}) => (
                        <OutlinedInput
                            {...field}
                            placeholder={"Enter your reference"}
                            type={"text"}
                            error={!!errors.reference}
                        />
                    )}/>
                    <ErrorMessage error={errors.reference}/>
                </FormControl>
                <Box className={"payment-button-container"} flexDirection={responsiveDirection}>
                    <FormControl fullWidth={true} margin={'dense'}>
                        <Button variant={"contained"} type={"submit"}>Pay Now</Button>
                    </FormControl>
                    <FormControl fullWidth={true} margin={'dense'}>
                        <Button variant={"outlined"} type={"button"} onClick={()=>{reset()}}>Reset</Button>
                    </FormControl>
                </Box>
                {isConfirming && (
                    <OverlayConfirmation title={"Payment Confirmation"} content={paymentConfirmationMsg} onConfirm={handleConfirmedAndRedirect} onCancel={handleCancelConfirmation} mainButtonText={"Confirm"} secondaryButtonText={"Cancel"}/>
                )}
            </form>
        </>
    );
}

export default PaymentForm;
