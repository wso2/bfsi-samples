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

import {Navigate, Route, Routes} from "react-router-dom";
import Home from "./pages/home-page/home.jsx";
import useConfigContext from "./hooks/use-config-context.ts";
import AppThemeProvider from "./providers/app-theme-provider.tsx";
import PaymentsPage from "./pages/payments-page/payments-page.tsx";
import BankingHomePage from "./banking-pages/pages/banking-home-page.tsx";
import AddAccountsPage from "./pages/add-accounts-page/add-accounts-page.tsx";
import LoginPage from "./banking-pages/pages/login-page.tsx";
import OtpPage from "./banking-pages/pages/otp-page.tsx";
import RedirectionPage from "./banking-pages/pages/redirection-page.tsx";
import PaymentConfirmationPage from "./banking-pages/pages/payment-confirmation-page.tsx";
import LoginWithEmailPage from "./banking-pages/pages/login-with-email.tsx";
import SingleAccountSelectionPage from "./banking-pages/pages/single-account-selection-page.tsx";
import AccountsAuthorizationPage from "./banking-pages/pages/accounts-authorization-page.tsx";
import MultipleAccountsSelectionPage from "./banking-pages/pages/multiple-accounts-selection-page.tsx";
import MultipleAccountsAuthorizationPage from "./banking-pages/pages/multiple-accounts-authorization-page.tsx";
import AccountsSelectionWithPermissionsPage from "./banking-pages/pages/accounts-selection-with-permissions-page.tsx";
import AllTransactions from "./pages/all-transactions-page/all-transactions.tsx";
import AllStandingOrders from "./pages/all-standing-orders/all-standing-orders.tsx";



/**
 * @function App
 * @description The root component that sets up global theming and
 * defines the application's entire routing structure based on configuration data.
 * It manages paths for the main dashboard and dynamically for external bank flows.
 */
export function App() {

    const {banksInfomation, accountsNumbersToAdd,colors,standingOrdersTableHeaderData,transactionTableHeaderData,overlayInformation,appInfo,userInfo,total, chartInfo,banksWithAccounts,transactions,standingOrderList,payeesData,useCases,banksList} = useConfigContext();
    if (!appInfo || !banksInfomation) {
        return (
            <div style={{ padding: '50px', textAlign: 'center', fontSize: '1.5em' }}>
                Loading application configuration...
            </div>
        );
    }

    return (
        <>
            <AppThemeProvider color={colors}>
                <Routes>
                    <Route path={`/${appInfo.route}/*`} element={
                        <Routes>
                            <Route index element={<Navigate to="home" replace />} />
                            <Route path="home"
                                   element={
                                       <Home userInfo={userInfo}
                                             name={appInfo.applicationName}
                                             total={total}
                                             chartData={chartInfo}
                                             banksWithAccounts={banksWithAccounts}
                                             transactions={transactions}
                                             standingOrderList={standingOrderList}
                                             appInfo={appInfo}
                                             banksList={banksList}
                                             overlayInformation={overlayInformation}
                                             transactionTableHeaderData={transactionTableHeaderData}
                                             standingOrdersTableHeaderData={standingOrdersTableHeaderData}

                                       />
                                   }/>
                            <Route path="paybills" element={<PaymentsPage banksList={banksInfomation} payeeData={payeesData} banksWithAccounts={banksWithAccounts} appInfo={appInfo}/>}/>
                            <Route path="accounts" element={<AddAccountsPage bankInformations={banksInfomation}/>}/>
                            <Route path="transactions" element={<AllTransactions name={appInfo.applicationName} transactions={transactions} transactionTableHeaderData={transactionTableHeaderData}/>}/>
                            <Route path="standing-orders" element={<AllStandingOrders name={appInfo.applicationName} standingOrdersList={standingOrderList} standingOrdersTableHeaderData={standingOrdersTableHeaderData}/>}/>
                        </Routes>
                    } />

                    {banksInfomation.map((bank,index)=>(
                        <Route key={index} path={`/${bank.route}/*`} element={<BankingHomePage appInfo={appInfo} useCases={useCases} bank={bank} accountsNumbersToAdd={accountsNumbersToAdd}/>}>
                            <Route path={"login"} element={<LoginPage />}/>
                            <Route path={"otp"} element={<OtpPage />}/>
                            <Route path={"payment-confirmation"} element={<PaymentConfirmationPage/>}/>
                            <Route path={"redirecting"} element={<RedirectionPage appConfig={appInfo} />}/>
                            <Route path={"login-with-email"} element={<LoginWithEmailPage/>}/>
                            <Route path={"account-select"} element={<SingleAccountSelectionPage/>}/>
                            <Route path={"account-authorize"} element={<AccountsAuthorizationPage/>}/>
                            <Route path={"account-select-uc-2"} element={<MultipleAccountsSelectionPage/>}/>
                            <Route path={"account-authorization-uc-2"} element={<MultipleAccountsAuthorizationPage/>}/>
                            <Route path={"account-select-uc-3"} element={<AccountsSelectionWithPermissionsPage/>}/>
                        </Route>
                    ))}

                    <Route path="/" element={<Navigate to={`/${appInfo.route}`} replace />} />
                </Routes>
            </AppThemeProvider>
        </>
    )

}



