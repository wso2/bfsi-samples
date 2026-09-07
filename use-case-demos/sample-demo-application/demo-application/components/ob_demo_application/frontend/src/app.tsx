/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
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

import React, { useEffect, useRef, useState } from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { useAuthContext, type BasicUserInfo } from "@asgardeo/auth-react";
import Home from "./pages/home-page/home.tsx";
import AppThemeProvider from "./providers/app-theme-provider.tsx";
import useConfigContext from "./hooks/use-config-context.ts";
import AddAccountsPage from "./pages/add-accounts-page/add-accounts-page.tsx";
import DevConsole from "./components/dev-console/dev-console.tsx";
import PaymentsPage from "./pages/payments-page/payments-page.tsx";
import AllTransactionsPage from "./pages/all-transactions-page/all-transactions.tsx";
import AllStandingOrders from "./pages/all-standing-orders/all-standing-orders.tsx";
import OAuthCallbackPage from "./pages/oauth-callback-page.tsx";

const App: React.FC = () => {
    const { state, signIn, signOut, getBasicUserInfo } = useAuthContext();
    const [user, setUser] = useState<BasicUserInfo | null>(null);
    const [userError, setUserError] = useState<Error | null>(null);

    // Guards to prevent double-invoke in React 19 Strict Mode
    const hasSignedIn = useRef(false);
    const hasFetchedUser = useRef(false);

    const {
        isLoading,
        appInfo,
        userInfo,
        total,
        chartInfo,
        banksWithAccounts,
        transactions,
        standingOrderList,
        banksList,
        allBanksInfomation,
        overlayInformation,
        transactionTableHeaderData,
        standingOrdersTableHeaderData,
        colors,
        payeesData,
        disconnectBank,
    } = useConfigContext();

    useEffect(() => {
        if (!state.isAuthenticated && !state.isLoading && !hasSignedIn.current) {
            hasSignedIn.current = true;
            signIn();
        }
    }, [state.isAuthenticated, state.isLoading, signIn]);

    useEffect(() => {
        if (state.isAuthenticated && !hasFetchedUser.current) {
            hasFetchedUser.current = true;
            getBasicUserInfo()
                .then((info) => setUser(info))
                .catch((err) => {
                    console.error("getBasicUserInfo error:", err);
                    setUserError(err);
                });
        }
    }, [state.isAuthenticated, getBasicUserInfo]);

    if (state.isLoading || isLoading) return <div>Loading...</div>;
    if (userError) return (
        <div>
            Failed to load profile.{" "}
            <button onClick={() => signOut()}>Sign out</button>
        </div>
    );
    if (!state.isAuthenticated || !user) return <div>Loading...</div>;

    return (
        <AppThemeProvider color={colors}>
            <Routes>
                <Route
                    path="/"
                    element={<Navigate to={`/${appInfo.route}`} replace />}
                />
                <Route
                    path="/callback"
                    element={<OAuthCallbackPage />}
                />
                <Route
                    path={`/${appInfo.route}`}
                    element={
                        <Home
                            name={appInfo.applicationName}
                            userInfo={{
                                name:
                                    user?.givenName && user?.familyName
                                        ? `${user.givenName} ${user.familyName}`
                                        : user?.username || user?.displayName || "User",
                                image: userInfo.image,
                                background: userInfo.background,
                            }}
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
                            onBankRemoved={disconnectBank}
                            onSignOut={signOut}
                        />
                    }
                />
                <Route
                    path={`/${appInfo.route}/accounts`}
                    element={<AddAccountsPage bankInformations={allBanksInfomation} />}
                />
                <Route
                    path={`/${appInfo.route}/paybills`}
                    element={
                        <PaymentsPage
                            appInfo={appInfo}
                            banksWithAccounts={banksWithAccounts}
                            payeeData={payeesData}
                            banksList={banksList}
                        />
                    }
                />
                <Route
                    path={`/${appInfo.route}/transactions`}
                    element={
                        <AllTransactionsPage
                            name={appInfo.applicationName}
                            transactions={transactions}
                            transactionTableHeaderData={transactionTableHeaderData}
                        />
                    }
                />
                <Route
                    path={`/${appInfo.route}/standing-orders`}
                    element={
                        <AllStandingOrders
                            name={appInfo.applicationName}
                            standingOrdersList={standingOrderList}
                            standingOrdersTableHeaderData={standingOrdersTableHeaderData}
                        />
                    }
                />
                <Route
                    path="*"
                    element={<Navigate to={`/${appInfo.route}`} replace />}
                />
            </Routes>
            <DevConsole />
        </AppThemeProvider>
    );
};

export default App;