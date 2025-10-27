/*
 * *
 *  * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 *  *
 *  * WSO2 LLC. licenses this file to you under the Apache License,
 *  * Version 2.0 (the "License"); you may not use this file except
 *  * in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

import {useEffect, useState} from "react";
import {api} from "../utility/api.ts";
import type {Account, AppInfo, Bank, Config, StandingOrders, TransactionData, User} from "./config-interfaces.ts";



const initialConfig: Config = {
    user: { name: '', image: '', background: '' },
    name: { route: '', applicationName: '' ,route_bank_one:''},
    banks: [],
    accounts: [],
    payees: [],
    transactions: [],
    standingOrders: [],
}

interface BankWithTotal{
    bank: Bank,
    total: number,
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



const useConfigContext = () => {

    const [config,setConfig] = useState<Config>(initialConfig)
    const [totalsOfBanks, setTotalsOfBanks] = useState<BankWithTotal[]>()
    const [chartDatas, setChartDatas] = useState<ChartData>({
        label: '',
        labels:[],
        data: [],
        backgroundColor: [],
        borderColor: [],
        borderWidth: 0,
        cutout: '0%'
    })
    const [totalBalances, setTotalBalances] = useState<number>(0)
    const [banksWithAllAccounts, setBanksWithAllAccounts] = useState<BanksWithAccounts[]>([])
    const [transactionDatas, setTransactionDatas] = useState<TransactionData[]>([])
    const [standingOrdersList, setStandingOrdersList] = useState<StandingOrders[]>([])

    useEffect(() => {
        const fetchData = async ()=>{

            const response = await api.get('config.json') as Config;
            setConfig(response);

            const totalsOfBanks = response.banks.map((bank) => {
                const totals = response.accounts.filter((account)=> account.bank===bank.name).reduce((sum,account)=>sum + account.balance,0);
                return {
                    bank: bank,
                    total: totals
                }
            });
            setTotalsOfBanks(totalsOfBanks)

            const data : ChartData ={
                label:'',
                labels:totalsOfBanks.map(bankdata=> bankdata.bank.name),
                data: totalsOfBanks.map(bankData=>bankData.total),
                backgroundColor: totalsOfBanks.map(bankData=>bankData.bank.color),
                borderColor: totalsOfBanks.map(bankData=>bankData.bank.border),
                borderWidth: 2,
                cutout: '35%'
            }
            setChartDatas(data)

            const totalBalance = totalsOfBanks.reduce((sum,bank) => sum + bank.total, 0)
            setTotalBalances(totalBalance)

            const banksWithAccounts = response.banks.map((bank)=>{
                const accounts = response.accounts.filter((account)=> account.bank===bank.name);
                const total = response.accounts.filter((account)=> account.bank===bank.name).reduce((sum,account)=>sum + account.balance,0);

                return{
                    bank: bank,
                    accounts: accounts,
                    total: total
                }
            })
            setBanksWithAllAccounts(banksWithAccounts)

            const transcations = response.transactions;
            setTransactionDatas(transcations);

            const standingOrders= response.standingOrders;
            setStandingOrdersList(standingOrders)
        }
        fetchData();
    },[])
    return { appInfo: config.name as AppInfo, userInfo: config.user as User, bankTotals: totalsOfBanks, chartInfo: chartDatas, total: totalBalances, banksWithAccounts: banksWithAllAccounts, transactions: transactionDatas, standingOrderList: standingOrdersList  }
}

export default useConfigContext;
