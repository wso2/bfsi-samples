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
import AccountsSelectionPage from "./banking-pages/pages/accounts-selection-page.tsx";
import AccountsAuthorizationPage from "./banking-pages/pages/accounts-authorization-page.tsx";
import AccountsSelectionTwoPage from "./banking-pages/pages/accounts-selection-two-page.tsx";
import AccountsAuthorizationTwoPage from "./banking-pages/pages/accounts-authorization-two-page.tsx";
import AccountsSelectionThreePage from "./banking-pages/pages/accounts-selection-three-page.tsx";


/**
 * The root component of the application, responsible for setting up the main routing structure
 * and applying global theming via the `AppThemeProvider`.
 *
 * It consumes the **`ConfigContext`** to dynamically construct the base route path
 * using the configured router name (accessed via `context.routerName.route`).
 * It then defines **nested routes**, such as the 'home' page, within this main product route.
 */
export function App() {

    const {overlayInformation,appInfo,userInfo,total, chartInfo,banksWithAccounts,transactions,standingOrderList,payeesData,useCases,banksList} = useConfigContext();
    console.log("appInfo",useCases);

    if (!appInfo) {
        return (
            <div style={{ padding: '50px', textAlign: 'center', fontSize: '1.5em' }}>
                Loading application configuration...
            </div>
        );
    }

    return (<>
        
        <AppThemeProvider>
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
                                   />
                               }/>
                        <Route path="payments" element={<PaymentsPage banksList={banksList} payeeData={payeesData} banksWithAccounts={banksWithAccounts} appInfo={appInfo}/>}/>
                        <Route path="accounts" element={<AddAccountsPage appInfo={appInfo} banks={banksList}/>}/>
                    </Routes>
                } />


                {appInfo.banksInfo.map((bank,index)=>(
                    <Route key={index} path={`/${bank.route}/*`} element={<BankingHomePage appInfo={appInfo} useCases={useCases} bank={bank}/>}>
                        <Route path={"login"} element={<LoginPage />}/>
                        <Route path={"otp"} element={<OtpPage />}/>
                        <Route path={"payment-confirmation"} element={<PaymentConfirmationPage/>}/>
                        <Route path={"redirecting"} element={<RedirectionPage appConfig={appInfo} />}/>
                        <Route path={"login-with-email"} element={<LoginWithEmailPage/>}/>
                        <Route path={"account-select"} element={<AccountsSelectionPage/>}/>
                        <Route path={"account-authorize"} element={<AccountsAuthorizationPage/>}/>
                        <Route path={"account-select-uc-2"} element={<AccountsSelectionTwoPage/>}/>
                        <Route path={"account-authorization-uc-2"} element={<AccountsAuthorizationTwoPage/>}/>
                        <Route path={"account-select-uc-3"} element={<AccountsSelectionThreePage/>}/>
                    </Route>
                ))}


            </Routes>
        </AppThemeProvider>

        </>)

}



