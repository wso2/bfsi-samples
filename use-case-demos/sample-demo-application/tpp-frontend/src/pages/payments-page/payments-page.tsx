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
import PaymentAccountPageLayout from "../../layouts/payment-account-page-layout/payment-account-page-layout.tsx";
import type {AppInfo, Bank, Payee} from "../../hooks/config-interfaces.ts";
import PaymentForm from "./payment-form/payment-form.tsx";
import type {BanksWithAccounts} from "../../hooks/use-config-context.ts";
import { Card } from "@oxygen-ui/react";

interface PaymentsPageProps {
    appInfo: AppInfo
    banksWithAccounts: BanksWithAccounts[];
    payeeData: Payee[];
    banksList: Bank[];
}

/**
 * @function PaymentsPage
 * @description The main page component for handling payments. It sets up the overall
 * application layout and the specific payment page layout, then wraps the core
 * payment form within a Card component, passing necessary configuration and data.
 */
const PaymentsPage = ({appInfo,banksWithAccounts, payeeData, banksList}:PaymentsPageProps) => {
    return (
        <>
            <ApplicationLayout name={appInfo.applicationName}>
                <PaymentAccountPageLayout title={"Payments"}>
                    <Card>
                        <PaymentForm banksList={banksList} payeeData={payeeData} appInfo={appInfo} banksWithAllAccounts={banksWithAccounts}/>
                    </Card>

                </PaymentAccountPageLayout>
            </ApplicationLayout>

        </>
    );
}

export default PaymentsPage;
