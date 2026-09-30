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

import ApplicationLayout from "../../layouts/application-layout/application-layout.tsx";
import {useLocation} from 'react-router-dom';
import PaymentAccountPageLayout from "../../layouts/payment-account-page-layout/payment-account-page-layout.tsx";
import type {Bank} from "../../hooks/config-interfaces.ts";
import {Box, Button, Card} from "@oxygen-ui/react";
import './add-account.scss'
import {useState} from "react";
import useConfigContext from "../../hooks/use-config-context.ts";
import {RedirectionComponent} from "../../components/redirection-component.tsx";

interface NavigationState {
    name: string;
    banksWithAccounts: Bank[];
}

interface AddAccountsPageProps {
    bankInformations: Bank[];
}

const AddAccountsPage = ({bankInformations}: AddAccountsPageProps) => {

    const {appInfo} = useConfigContext();
    const location = useLocation();
    const navigationState = location.state as NavigationState;
    const appName = navigationState?.name;
    const [isRedirecting, setIsRedirecting] = useState(false);
    const [apiError, setApiError] = useState<string | null>(null);

    const currentPathName = window.location.pathname.replace(/\/$/, "");
    const routeSegment = `/${appInfo?.route}`;
    const routeIndex = appInfo?.route ? currentPathName.indexOf(routeSegment) : -1;
    const rootBasePath = routeIndex >= 0 ? currentPathName.substring(0, routeIndex) : currentPathName;
    const normalizedBasePath = rootBasePath === "/" ? "" : rootBasePath;

    const onAddAccountsHandler = async (bankName: string) => {
        const target = bankInformations.find((bank) => bank.name === bankName);
        if (!target) {
            setApiError("Selected bank could not be found.");
            return;
        }

        setApiError(null);
        setIsRedirecting(true);

        try {
            const apiBaseUrl = `${window.location.origin}${normalizedBasePath}`;

            const response = await fetch(`${apiBaseUrl}/init/add-accounts`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({bankName})
            });

            if (!response.ok) {
                const errorBody = await response.text().catch(() => "");
                throw new Error(`Add account API returned ${response.status}: ${errorBody}`);
            }

            const responseData = await response.json().catch(() => null);
            if (responseData?.redirect) {
                window.location.href = responseData.redirect;
                return;
            }

        } catch (error) {
            console.error("Failed to call add account API:", error);
            setApiError("Unable to start the account linking flow. Please try again.");
            setIsRedirecting(false);
        }
    };

    const isOAuthReturn = !!(location.state as any)?.operationState;

    if (isRedirecting || isOAuthReturn) {
        return <RedirectionComponent/>;
    }

    const accountsToAdd = bankInformations.length > 2 ? [bankInformations[2]] : [];
    const alreadyAddedAccounts = bankInformations.slice(0, 2);

    return (

        <ApplicationLayout name={appName} onStartTour={undefined}>
            <PaymentAccountPageLayout title={"Add Account"}>
                <Box className="accounts-outer">

                    {apiError && (
                        <div className="api-error-message"
                             style={{color: "#d32f2f", marginBottom: "1rem"}}>
                            {apiError}
                        </div>
                    )}

                    {/* Section 1: Accounts to Add */}
                    <Box style={{width: '100%', marginBottom: '2.5rem'}}>
                        <h3 style={{marginBottom: '1.5rem', marginTop: 0, textAlign: 'center'}}>Accounts to Add</h3>
                        <div className="accounts-buttons-container" style={{alignItems: 'center'}}>
                            {accountsToAdd.length === 0 ? (
                                <p style={{color: 'var(--oxygen-palette-text-secondary)'}}>
                                    No accounts available to add.
                                </p>
                            ) : (
                                accountsToAdd.map((account, index) => (
                                    <Button
                                        key={index}
                                        onClick={() => onAddAccountsHandler(account.name)}
                                    >
                                        <Card>
                                            <Box className={"account-button-outer"}>
                                                <Box className={"logo-container"} sx={{marginLeft: '2rem'}}>
                                                    <img
                                                        src={account.image}
                                                        alt={`${account.name} logo`}
                                                    />
                                                </Box>
                                                <p>{account.name}</p>
                                            </Box>
                                        </Card>
                                    </Button>
                                ))
                            )}
                        </div>
                    </Box>

                    {/* Section 2: Already Added Accounts */}
                    <Box style={{width: '100%'}}>
                        <h3 style={{marginBottom: '1.5rem', marginTop: 0,textAlign: 'center'}}>Already Added Accounts</h3>
                        <Box style={{
                            display: 'flex',
                            flexDirection: 'row',
                            gap: '1rem',
                            flexWrap: 'wrap',
                            justifyContent: 'center'
                        }}>
                            {alreadyAddedAccounts.map((account, index) => (
                                <Button
                                    key={index}
                                    disabled
                                    style={{
                                        opacity: 0.45,
                                        cursor: 'not-allowed',
                                        padding: 0,
                                        border: 'none',
                                        background: 'none'
                                    }}
                                >
                                    <Card>
                                        <Box className={"account-button-outer"}>
                                            <Box className={"logo-container"} sx={{marginLeft: '2rem'}}>
                                                <img
                                                    src={account.image}
                                                    alt={`${account.name} logo`}
                                                />
                                            </Box>
                                            <p>{account.name}</p>
                                        </Box>
                                    </Card>
                                </Button>
                            ))}
                        </Box>
                    </Box>

                </Box>
            </PaymentAccountPageLayout>
        </ApplicationLayout>

    );
};

// @ts-ignore
export default AddAccountsPage;
