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
import type {Bank} from "../../hooks/config-interfaces.ts";
import {Box, IconButton} from "@oxygen-ui/react";
import './add-account.scss'


interface NavigationState {
    name: string;
    banksWithAccounts: Bank;
}

interface AddAccountsPageProps {
    bankInformations: Bank[];
}

/**
 * @function AddAccountsPage
 * @description A page component allowing users to initiate the process of connecting
 * a new bank account. It lists available banks and, upon selection, redirects the
 * user to the specific bank's authorization flow (via `react-router` state).
 */
const AddAccountsPage = ({bankInformations}:AddAccountsPageProps)=>{

    const navigate = useNavigate();
    const location = useLocation();
    const navigationState = location.state as NavigationState;
    const appName = navigationState?.name;

    const onAddAccountsHandler = (bankName:string)=>{
        const target = bankInformations.find((bank) =>
             bank.name === bankName
        );
        navigate("/"+target?.route+"/?type=account",{
            state:{
                formData: null,
                message: "confirmed payment information",
                bankInfo: target
            }
        });
    }
    return (
        <>
            <ApplicationLayout name={appName}>
                <PaymentAccountPageLayout title={"Add Account"}>
                    <h3 style={{marginBottom:"1.5rem"}}>Select your Bank here</h3>
                    <div className="accounts-buttons-container">
                        {bankInformations?.map((account, index) => (
                            <IconButton key={index} onClick={()=>{onAddAccountsHandler(account.name)}} >
                                <Box className={"account-button-outer"}>
                                    <Box className={"logo-container"} sx={{marginLeft:'2rem'}}>
                                        <img src={account.image} alt={`${account.name} logo`}/>
                                    </Box>
                                    <p>{account.name}</p>
                                </Box>
                            </IconButton>
                        ))}
                    </div>
                </PaymentAccountPageLayout>
            </ApplicationLayout>
        </>
    )
}

export default AddAccountsPage;
