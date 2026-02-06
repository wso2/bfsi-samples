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

export interface AccountsWithPermissions {
    [permissions: string]: Account[];
}

export interface ChartData{
    label: string;
    labels: string[];
    data: number[];
    backgroundColor: string[];
    borderColor: string[];
    borderWidth: number;
    cutout: string;
}

export interface BanksWithAccounts{
    bank: Bank;
    accounts: Account[];
    total: number;
}

export interface OverlayDataProp{
    flag: boolean;
    overlayData:OverlayData;
}

export interface OverlayData{
    title: string;
    context: string;
    mainButtonText: string;
    secondaryButtonText: string;
    onMainButtonClick: () => void;
}

import {useEffect, useMemo, useState} from "react";
import type {Account, AppInfo, Bank, Config, User} from "./config-interfaces.ts";
import {useLocation, useNavigate} from "react-router-dom";
import {useConfig} from "./use-config.ts";
import {queryClient} from "../utility/query-client.ts";
import {processAllBankDates} from "../utility/date-utils.ts";

/**
 * @function useConfigContext
 * @description The central state management hook that fetches application configuration,
 * aggregates raw data into calculated properties (e.g., totals, chart data),
 * and critically, handles global state updates following bank redirection flows (payments, account linking).
 *
 * NOW INCLUDES: Automatic processing of standing orders to convert relative day numbers to actual dates.
 */
const CONFIG_QUER_KEY = ["appConfig"];

const useConfigContext = () => {
    const { data: configData } = useConfig() ;
    const location = useLocation();
    const navigate = useNavigate();
    const redirectState = location.state?.operationState;
    const [overlayInformation, setOverlayInformation] = useState<OverlayDataProp>({flag:false,overlayData:{context:"",secondaryButtonText:"",mainButtonText:"",title:"",onMainButtonClick:()=>{}}});

    const handleOverlayClose = () => {
        setOverlayInformation({flag:false,overlayData:{context:"",secondaryButtonText:"",mainButtonText:"",title:"",onMainButtonClick:()=>{}}})
    }

    const updateSessionStorage = (updatedConfig: Config) => {
        try {
            queryClient.setQueryData(CONFIG_QUER_KEY, updatedConfig);
        } catch (e) {
            console.error("Failed to update session storage");
        }
    }
    const processedBanks = useMemo(() => {
        if (!configData?.banks) return [];
        return processAllBankDates(configData.banks);
    }, [configData?.banks]);
    const allTransactions = useMemo(() => {
        return processedBanks.flatMap(bank =>
            bank.accounts.flatMap((account: { transactions: any; }) => account.transactions || [])
        );
    }, [processedBanks]);
    const [accountsToTransactions, setAccountsToTransactions] = useState<any[]>([]);
    console.log(accountsToTransactions)
    useEffect(() => {
        setAccountsToTransactions(allTransactions);
    }, [allTransactions]);
    const totals = useMemo(() => {
        if (!configData) return [];
        return processedBanks.map((bank) => {
            const total = bank.accounts
                .reduce((sum: any, acc: { balance: any; }) => sum + acc.balance, 0);
            return { bank, total };
        });
    }, [processedBanks, configData]);
    const chartInfo = useMemo(() => {
        if (!configData) return { label: '', labels: [], data: [], backgroundColor: [], borderColor: [], borderWidth: 0, cutout: '0%' };
        return {
            label: '',
            labels: totals.map((t) => t.bank.name),
            data: totals.map((t) => t.total),
            backgroundColor: totals.map((t) => t.bank.color),
            borderColor: totals.map((t) => t.bank.border),
            borderWidth: 2,
            cutout: '35%'
        }
    }, [configData, totals]);
    const totalBalances = useMemo(() => {
        return totals.reduce((s, b) => s + b.total, 0);
    }, [totals]);
    const banksWithAllAccounts = useMemo(() => {
        if (!configData) return [];
        return processedBanks.map((bank) => {
            const uniqueAccountsMap = new Map();
            bank.accounts.forEach((acc: { id: any; }) => {
                if (!uniqueAccountsMap.has(acc.id)) {
                    uniqueAccountsMap.set(acc.id, acc);
                }
            });
            const uniqueAccounts = Array.from(uniqueAccountsMap.values());
            const total = uniqueAccounts.reduce((sum, acc) => sum + (acc.balance ?? 0), 0);
            return {
                bank,
                accounts: uniqueAccounts,
                total
            };
        });
    }, [processedBanks, configData]);
    const standingOrdersWithDates = useMemo(() => {
        return processedBanks.flatMap(bank => bank.standingOrders || []);
    }, [processedBanks]);
    useEffect(() => {
        if (!configData || (!location.state && !redirectState)) {
            return;
        }
        if (redirectState?.type === "cancelled"){
            setOverlayInformation({
                flag: true,
                overlayData: {
                    context: "The operation has been cancelled.",
                    secondaryButtonText: "",
                    mainButtonText: "Ok",
                    title: "Operation Cancelled",
                    onMainButtonClick: handleOverlayClose
                }
            });
            navigate(location.pathname, { replace: true, state: {} });
            return;
        }else if (redirectState?.type === "payment") {
            try {
                const newTransactionData = redirectState.data;
                const fullAccountNumber = newTransactionData.account;
                const firstHyphenIndex = fullAccountNumber.indexOf('-');
                const formattedAccountNumber = fullAccountNumber.substring(firstHyphenIndex + 1);
                newTransactionData.account = formattedAccountNumber;
                const transactionAmount = parseFloat(newTransactionData.amount);
                const sourceBankName = newTransactionData.bank;
                const newConfig = queryClient
                    .setQueryData(CONFIG_QUER_KEY, (oldConfig: Config | undefined) => {
                    const baseConfig = oldConfig || configData;
                    const updatedBanks = baseConfig.banks.map(bank => {
                        if (bank.name === sourceBankName) {
                            const updatedAccounts =
                                bank.accounts.map((account: Account) => {
                                if (account.id === formattedAccountNumber || account.id === fullAccountNumber) {
                                    const newBalance = (account.balance ?? 0) - transactionAmount;
                                    if (newBalance < 0) {
                                        throw new Error(`Insufficient funds: cannot process payment of 
                                        ${transactionAmount} from account with balance ${account.balance}`);
                                    }
                                    const currentTransactions = account.transactions || [];
                                    return {
                                        ...account,
                                        balance: newBalance,
                                        transactions: [newTransactionData, ...currentTransactions]
                                    };
                                }
                                return account;
                            });
                            return {
                                ...bank,
                                accounts: updatedAccounts
                            };
                        }
                        return bank;
                    });
                    return {
                        ...baseConfig!,
                        banks: updatedBanks,
                    };
                });
                queryClient.invalidateQueries({ queryKey: CONFIG_QUER_KEY });
                updateSessionStorage(newConfig as Config);
                const paymentOverlayText = `Your payment of ${newTransactionData.currency} ${newTransactionData.amount}  
                has been successfully processed.`;
                setOverlayInformation({
                    flag: true,
                    overlayData: {
                        context: paymentOverlayText,
                        secondaryButtonText: "",
                        title: "Payment Successful",
                        onMainButtonClick: handleOverlayClose,
                        mainButtonText: "Ok"
                    }
                });
                navigate(location.pathname, { replace: true, state: {} });
            }catch (error){
                console.error(error);
            }
        }else if(redirectState?.type === "single"){
            try{
                const newAccountData = redirectState.data;
                const bankName = newAccountData.bankInfo;
                const newAccountId = newAccountData.accountDetails[0];
                const newConfigWithAccount = queryClient.setQueryData(CONFIG_QUER_KEY, (oldConfig:Config | undefined)=> {
                    const baseConfig = oldConfig || configData;
                    const accountToBeAdded = {
                        id: newAccountId,
                        bank: bankName,
                        name: "savings account",
                        balance: 500
                    };
                    const updatedBanks = baseConfig.banks.map(bank => {
                        if (bank.name === bankName) {
                            return {
                                ...bank,
                                accounts: [...(bank.accounts || []), accountToBeAdded]
                            };
                        }
                        return bank;
                    });
                    return {
                        ...baseConfig!,
                        banks: updatedBanks,
                    };
                }) as Config;
                queryClient.invalidateQueries({ queryKey: CONFIG_QUER_KEY });
                updateSessionStorage(newConfigWithAccount);
                const newAccounts = newConfigWithAccount
                    .banks.find(bank => bank.name===newAccountData.bankInfo)?.accounts || [];
                const singleAccountOverlay =
                    `The new account ${newAccounts[newAccounts.length-1]?.id} was added successfully.`;
                setOverlayInformation({flag:true,overlayData:{context:singleAccountOverlay
                        ,secondaryButtonText:"",mainButtonText:"Ok",title:"Account added Successfully"
                        ,onMainButtonClick:handleOverlayClose}});
                navigate(location.pathname, { replace: true, state: {} });
            }catch (error){
                console.error(error);
            }
        }else if(redirectState?.type === "multiple"){
            try{
                const newAccounts = redirectState.data;
                const formattedAccounts: AccountsWithPermissions = {};
                const detailsArray = newAccounts.accountDetails[0];
                detailsArray.forEach((item: any) => {
                    const permissionName = item.permission;
                    const accountList = item.accounts;
                    formattedAccounts[permissionName] = accountList;
                });
                let generatedNewAccounts: Account[] = [];
                const newConfigWithAccount = queryClient
                    .setQueryData(CONFIG_QUER_KEY, (oldConfig:Config | undefined)=> {
                    const baseConfig = oldConfig || configData;
                    const structuredPermissionsData = newAccounts.accountDetails[0];
                    const bankName = newAccounts.bankInfo;
                    const readAccountNumbers = structuredPermissionsData
                        .flatMap((entry: any) => entry.accounts || []);
                    const uniqueAccountNumbers = [...new Set(readAccountNumbers)] as string[];
                    generatedNewAccounts = uniqueAccountNumbers.map((accNo: string) => {
                        return {
                            id: accNo,
                            bank: bankName,
                            name: "savings (M)",
                            balance: 500,
                            transactions: []
                        };
                    });
                    const updatedBanks = baseConfig.banks.map(bank => {
                        if (bank.name === bankName) {
                            const existingIds = new Set(bank.accounts.map(a => a.id));
                            const filteredNew = generatedNewAccounts
                                .filter(a => !existingIds.has(a.id));
                            return {
                                ...bank,
                                accounts: [...(bank.accounts || []), ...filteredNew]
                            };
                        }
                        return bank;
                    });
                    return {
                        ...baseConfig!,
                        banks: updatedBanks
                    }
                });
                const config = newConfigWithAccount as Config;
                queryClient.invalidateQueries({ queryKey: CONFIG_QUER_KEY });
                updateSessionStorage(config);
                const multipleAccountOverlayContext = `The new accounts 
                ${generatedNewAccounts?.map((account)=>account.id).join(", ") } 
                were added new accounts successfully. `;
                setOverlayInformation({flag:true,overlayData:{context:multipleAccountOverlayContext
                        ,secondaryButtonText:"",mainButtonText:"Ok",title:"Accounts added successfully"
                        ,onMainButtonClick:handleOverlayClose}});
                navigate(location.pathname, { replace: true, state: {} });
            }catch (error){
                console.error(error);
            }
        }
    },[redirectState])


    return {
        appInfo: configData?.name as AppInfo ,
        userInfo: configData?.user as User,
        bankTotals: totals,
        chartInfo: chartInfo,
        total: totalBalances,
        banksWithAccounts: banksWithAllAccounts,
        transactions: processedBanks.flatMap(bank =>
            bank.accounts.flatMap((account: { transactions: any; }) =>  account.transactions || [])
        ),
        standingOrderList: standingOrdersWithDates,
        payeesData: configData?.payees ?? [],
        useCases: configData?.types ?? [],
        banksList: processedBanks,
        overlayInformation: overlayInformation,
        transactionTableHeaderData:configData?.transactionTableHeaderData,
        standingOrdersTableHeaderData:configData?.standingOrdersTableHeaderData,
        colors: configData?.colors,
        accountsNumbersToAdd: configData?.accountNumbersToAdd,
        banksInfomation: processedBanks
    };
};

export default useConfigContext;
