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

import ApplicationLayout from "../../layouts/application-layout/application-layout.tsx";
import {useLocation, useNavigate} from 'react-router-dom';
import PaymentAccountPageLayout from "../../layouts/payment-account-page-layout/payment-account-page-layout.tsx";
import type {AppInfo, Bank} from "../../hooks/config-interfaces.ts";
import {Box, IconButton} from "@oxygen-ui/react";
import './add-account.scss'


interface NavigationState {
    name: string;
    banksWithAccounts: Bank[];
}

interface AddAccountsPageProps {
    appInfo: AppInfo;
    banks: Bank[];
}

const AddAccountsPage = ({appInfo,banks}:AddAccountsPageProps)=>{

    const navigate = useNavigate();
    const location = useLocation();
    const navigationState = location.state as NavigationState;
    const appName = navigationState?.name;
    const banksList = navigationState?.banksWithAccounts;

    const onAddAccoutsHandler = (bankName:string)=>{

        console.log(bankName)

        const relaventBank = banks.find((bank)=>{return bank.name === bankName})
        console.log(relaventBank)

        console.log("=============================================")
        console.log(relaventBank)

        const target = appInfo.banksInfo.find((bank) => {
            return bank.name === bankName;
        });
        console.log(target)
        navigate("/"+target?.route+"/?type=account",{
            state:{
                formData: null,
                message: "confirmed payment information",
                bankInfo: relaventBank
            }
        });
    }

    return (
        <>
            <ApplicationLayout name={appName}>
                <PaymentAccountPageLayout title={"Add Account"}>
                    <h3 style={{marginBottom:"1.5rem"}}>Select your Bank here</h3>
                    {banksList?.map((account, index) => (
                        <IconButton key={index} onClick={()=>{onAddAccoutsHandler(account.name)}}>
                            <Box className={"account-button-outer"}>
                                <Box className={"logo-container"}>
                                    <img src={account.image} alt=""/>
                                </Box>
                                <p>{account.name}</p>
                            </Box>
                        </IconButton>
                    ))}
                </PaymentAccountPageLayout>
            </ApplicationLayout>
        </>

    )
}

export default AddAccountsPage;
