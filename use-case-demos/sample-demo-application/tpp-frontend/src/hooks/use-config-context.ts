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

/**
 * @function useConfigContext
 * @description The central state management hook that fetches application configuration,
 * aggregates raw data into calculated properties (e.g., totals, chart data),
 * and critically, handles global state updates following bank redirection flows (payments, account linking).
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

    const totals = useMemo(() => {
        if (!configData) return [];
        return configData.banks.map((bank) => {
            const total = bank.accounts
                .reduce((sum, acc) => sum + acc.balance, 0);
            return { bank, total };
        });
    }, [configData]);

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
        return configData.banks.map((bank) => {
            const accounts = bank.accounts;
            const total = accounts.reduce((s, a) => s + a.balance, 0);
            return { bank, accounts, total };
        });
    }, [configData]);

    useEffect(() => {
        if (!configData || (!location.state && !redirectState)) {
            return;
        }

        if (redirectState?.type === "payment") {

            try {
                const newTransactionData = redirectState.data;
                const fullAccountNumber = newTransactionData.account;
                const firstHyphenIndex = fullAccountNumber.indexOf('-');
                const formattedAccountNumber = fullAccountNumber.substring(firstHyphenIndex + 1);
                newTransactionData.account = formattedAccountNumber;

                const transactionAmount = parseFloat(newTransactionData.amount);
                const sourceBankName = newTransactionData.bank;

                const newConfig = queryClient.setQueryData(CONFIG_QUER_KEY, (oldConfig: Config | undefined) => {
                    const baseConfig = oldConfig || configData;
                    const updatedBanks = baseConfig.banks.map(bank => {
                        if (bank.name === sourceBankName) {
                            const updatedAccounts = bank.accounts.map((account: Account) => {
                                if (account.id === formattedAccountNumber || account.id === fullAccountNumber) {
                                    const newBalance = (account.balance ?? 0) - transactionAmount;
                                    if (newBalance < 0) {
                                        throw new Error(`Insufficient funds: cannot process payment of ${transactionAmount} from account with balance ${account.balance}`);
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

                const paymentOverlayText = `Your payment of ${newTransactionData.amount} ${newTransactionData.currency} has been successfully processed.`;
                setOverlayInformation({
                    flag: true,
                    overlayData: {
                        context: paymentOverlayText,
                        secondaryButtonText: "",
                        title: "Payment Success",
                        onMainButtonClick: handleOverlayClose,
                        mainButtonText: "OK"
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
                const newAccounts = newConfigWithAccount.banks.find(bank => bank.name===newAccountData.bankInfo)?.accounts || [];
                const singleAccountOverlay = `The new account ${newAccounts[newAccounts.length-1]?.id} was added successfully. You can now access it and start making transactions.`;
                setOverlayInformation({flag:true,overlayData:{context:singleAccountOverlay,secondaryButtonText:"",mainButtonText:"Ok",title:"Account added Successfully",onMainButtonClick:handleOverlayClose}});
                navigate(location.pathname, { replace: true, state: {} });
            }catch (error){
                console.error(error);
            }


        }else if(redirectState?.type === "multiple"){

            try{
                const newAccounts = redirectState.data;
                let generatedAccounts:Account[] = [];
                const newConfigWithAccount = queryClient.setQueryData(CONFIG_QUER_KEY, (oldConfig:Config | undefined)=> {
                    const baseConfig = oldConfig || configData;
                    const structuredPermissionsData = newAccounts.accountDetails[0];
                    const bankName = newAccounts.bankInfo;
                    const accNumbers = structuredPermissionsData.flatMap((permissionEntry: {
                        permission: "",
                        accounts: string[]
                    }) => {
                        return permissionEntry.accounts || [];
                    });
                    const generatedNewAccounts: Account[] = accNumbers.map((entry:string) => {
                        return {
                            id: entry,
                            bank: bankName,
                            name: "savings (M)",
                            balance: 500,
                        };
                    });
                    generatedAccounts = generatedNewAccounts; //scope issue
                    const updatedBanks = baseConfig.banks.map(bank => {
                        if (bank.name === bankName) {
                            return {
                                ...bank,
                                accounts: [...(bank.accounts || []), ...generatedNewAccounts]
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
                const multipleAccountOverlayContext = `The new account ${generatedAccounts?.map((account)=>account.id).join(", ") } were added successfully. You can now access it and start making transactions.`;
                setOverlayInformation({flag:true,overlayData:{context:multipleAccountOverlayContext,secondaryButtonText:"",mainButtonText:"Ok",title:"Multiple Accounts add Successfully",onMainButtonClick:handleOverlayClose}});
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
        transactions: configData?.banks.flatMap(bank =>
            bank.accounts.flatMap(account => account.transactions || [])
        ) ?? [],
        standingOrderList: configData?.banks.flatMap(bank=>bank.standingOrders||[]) ?? [],
        payeesData: configData?.payees ?? [],
        useCases: configData?.types ?? [],
        banksList: configData?.banks ?? [],
        overlayInformation: overlayInformation,
        transactionTableHeaderData:configData?.transactionTableHeaderData,
        standingOrdersTableHeaderData:configData?.standingOrdersTableHeaderData,
        colors: configData?.colors,
        accountsNumbersToAdd: configData?.accountNumbersToAdd,
        banksInfomation: configData?.banks ?? []
    };
};

export default useConfigContext;
