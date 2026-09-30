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

import {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {Box} from "@oxygen-ui/react";
import useConfigContext from "../hooks/use-config-context.ts";
import type {Config} from "../hooks/config-interfaces.ts";
import {queryClient} from "../utility/query-client.ts";
import {generateTransactionId} from "../utility/transaction-utils.ts";

const ACCOUNTS_SESSION_KEY = "openbanking_added_accounts";

const parseUrlParams = (input: string) => {
    const params = new URLSearchParams();
    if (!input) return params;
    const entries = new URLSearchParams(input);
    entries.forEach((value, key) => {
        params.append(key, value);
    });
    return params;
};

const getOAuthParams = (location: ReturnType<typeof useLocation>) => {
    const allParams = new URLSearchParams(location.search);
    const rawHash = window.location.hash || "";
    if (rawHash.startsWith("#")) {
        const hashContent = rawHash.slice(1);
        parseUrlParams(hashContent).forEach((value, key) => {
            if (!allParams.has(key)) {
                allParams.append(key, value);
            }
        });
    }
    return allParams;
};

const getCurrentAppRoute = () => {
    const rawHash = window.location.hash || "";
    if (!rawHash.startsWith("#")) return "";
    const trimmed = rawHash.slice(1);
    const firstSegment = trimmed.split("?")[0].split("#")[0];
    const routeName = firstSegment.split("/")[1] || "";
    return routeName;
};

const handleAccountsResponse = (data: any) => {
    const oldConfig = queryClient.getQueryData<Config>(["appConfig"]);
    if (!oldConfig) return;

    const targetBank = oldConfig.banks[2];
    if (!targetBank) return;

    const newAccounts = (data.accounts || []).map((backendAccount: any) => ({
        id: backendAccount.id,
        bank: targetBank.name,
        name: backendAccount.name || "Open Banking Account",
        balance: backendAccount.balance ?? 0,
        consentId: backendAccount.consentId,
        transactions: (backendAccount.transactions || []).map((txn: any) => ({
            id: txn.id,
            date: txn.date,
            reference: txn.reference,
            bank: targetBank.name,
            account: txn.account,
            amount: txn.amount,
            currency: txn.currency,
            creditDebitStatus: txn.creditDebitStatus
        }))
    }));

    // Read existing accounts from sessionStorage and append new ones
    const existingAccountsRaw = sessionStorage.getItem(ACCOUNTS_SESSION_KEY);
    const existingAccounts: any[] = existingAccountsRaw ? JSON.parse(existingAccountsRaw) : [];

    // Avoid duplicates — replace if account id already exists, otherwise append
    const mergedAccounts = [
        ...existingAccounts.filter(
            (existing) => !newAccounts.some((na: any) => na.id === existing.id)
        ),
        ...newAccounts
    ];

    sessionStorage.setItem(ACCOUNTS_SESSION_KEY, JSON.stringify(mergedAccounts));

    queryClient.setQueryData(["appConfig"], {
        ...oldConfig,
        banks: oldConfig.banks.map((bank, index) =>
            index === 2 ? {...targetBank, accounts: mergedAccounts} : bank
        )
    });
};

const handlePaymentsResponse = (data: any) => {
    if (!data.success) return;

    const pendingRaw = sessionStorage.getItem("pendingPayment");
    if (!pendingRaw) return;

    const pending = JSON.parse(pendingRaw);
    const oldConfig = queryClient.getQueryData<Config>(["appConfig"]);
    if (!oldConfig) return;

    const firstHyphen = pending.userAccount.indexOf('-');
    const accountId = pending.userAccount.substring(firstHyphen + 1);
    const amount = parseFloat(pending.amount);
    const currentDate = new Date().toISOString().split('T')[0];

    // Read accounts from sessionStorage — this is the source of truth after a redirect
    // because the query cache for bank[2] may be empty at this point
    const savedAccountsRaw = sessionStorage.getItem(ACCOUNTS_SESSION_KEY);
    const savedAccounts: any[] = savedAccountsRaw ? JSON.parse(savedAccountsRaw) : [];

    // Use the shared generator rather than a timestamp, so the id matches the T + 8 digit shape
    // the rest of the data uses, and is checked against the ids already present.
    const existingTransactions = savedAccounts.flatMap((acc: any) => acc.transactions || []);

    const newTxn = {
        id: generateTransactionId(existingTransactions),
        date: currentDate,
        reference: pending.reference,
        bank: oldConfig.banks[2]?.name ?? "",
        account: accountId,
        amount: pending.amount,
        currency: pending.currency,
        // A payment sends money out of the account, so it is a debit - which is also what the
        // balance below assumes when it subtracts the amount.
        creditDebitStatus: "d"
    };

    // Update the accounts from sessionStorage directly
    const updatedAccounts = savedAccounts.map((acc: any) => {
        if (acc.id !== accountId) return acc;
        return {
            ...acc,
            balance: (acc.balance ?? 0) - amount,
            transactions: [newTxn, ...(acc.transactions || [])]
        };
    });
    sessionStorage.setItem(ACCOUNTS_SESSION_KEY, JSON.stringify(updatedAccounts));
    queryClient.setQueryData(["appConfig"], {
        ...oldConfig,
        banks: oldConfig.banks.map((bank, index) => {
            if (index !== 2) return bank;
            return {
                ...bank,
                accounts: updatedAccounts
            };
        })
    });
    sessionStorage.removeItem("pendingPayment");
};

const OAuthCallbackPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const {appInfo} = useConfigContext();
    const [status, setStatus] = useState("Processing OAuth callback...");
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const params = getOAuthParams(location);
        const code = params.get("code") || params.get("authorization_code") || params.get("auth_code");
        // With response_mode=jwt the whole authorization response is one signed JWT (JARM).
        // The code lives inside it, so hand it to the backend to read.
        const jarmResponse = params.get("response");
        const accessToken = params.get("access_token");
        const idToken = params.get("id_token");
        const errorParam = params.get("error") || params.get("error_description");
        if (errorParam) {
            setError(`OAuth callback returned an error: ${errorParam}`);
            setStatus("Failed to complete OAuth callback.");
            return;
        }
        if (!code && !jarmResponse && !accessToken && !idToken) {
            setError("No OAuth code, response JWT or token was found in the callback URL.");
            setStatus("Unable to complete OAuth callback.");
            return;
        }
        const completeAuth = async () => {
            try {
                setStatus("Completing OAuth login with the authorization response...");

                const backendBase = window.location.pathname.replace(/\/callback.*$/, "");
                const authParam = jarmResponse
                    ? `response=${encodeURIComponent(jarmResponse)}`
                    : `code=${encodeURIComponent(code!)}`;
                const response = await fetch(
                    `${window.location.origin}${backendBase}/init/processAuth?${authParam}`,
                    {method: "GET"}
                );
                if (!response.ok) {
                    const body = await response.text().catch(() => "");
                    throw new Error(`API returned ${response.status}: ${body}`);
                }
                const data = await response.json();
                if (data.type === "accounts") {
                    handleAccountsResponse(data);
                } else if (data.type === "payments") {
                    handlePaymentsResponse(data);
                }
                const routeName = getCurrentAppRoute() || appInfo?.route;
                const targetPath = routeName ? `/${routeName}` : `/${appInfo?.route}`;
                setStatus("OAuth login completed. Redirecting...");
                navigate(targetPath, {
                    replace: true,
                    state: {
                        operationState: {
                            type: data.type,
                            data
                        }
                    }
                });
            } catch (err) {
                setError(err instanceof Error ? err.message : String(err));
                setStatus("Failed to complete OAuth callback.");
            }
        };
        completeAuth();
    }, [location, navigate, appInfo]);

    return (
        <Box sx={{padding: "3rem", textAlign: "center"}}>
            <h2>{status}</h2>
            {error
                ? <p style={{color: "#d32f2f"}}>{error}</p>
                : <p>Please wait while we complete your account connection.</p>
            }
        </Box>
    );
};

export default OAuthCallbackPage;
