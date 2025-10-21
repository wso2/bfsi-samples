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

import {useEffect, useState} from "react";
import {api} from "../api.js";

/*
 * useConfigContext hook fetches and processes the main application configuration data.
 * It loads bank details and accounts, calculates total balances, structures data for the
 * donut chart, and organizes account information for display components.
 *
 * Returns:
 * @returns {object} An object containing: config, connectedBankDetails, bankInfoWithTotals,
 * isLoading status, accountInfoWithBankInfo, chartData, and the total balance.
 */
const useConfigContext = () => {
    const [config, setLoadedConfig] = useState(null);
    const [connectedBankDetails, setConnectedBankDetails] = useState(null);
    const [bankInfoWithTotals, setBankInfoWithTotals] = useState(null);
    const [accountInfoWithBankInfo, setAccountInfoWithBankInfo] = useState(null);
    const [chartData, setChartData] = useState(null);
    const [total, setTotal] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect( () => {

        const fetchData = async () => {

            const bankNames = [];
            const totals = [];
            const fillColors = [];
            const borderColors = [];

            try {
                const response = await api.get("config.json")
                setLoadedConfig(response.name)
                setConnectedBankDetails(response.banks)

                const totalBalance = response.accounts.reduce((acc, current) => {return acc+current.balance},0)
                setTotal(totalBalance)

                const banksWithTotal = response.banks.map((bank) => {
                    const total = response.accounts.filter((account) => account.bank === bank.name).reduce((sum, account) => sum+account.balance,0);
                    bankNames.push(bank.name);
                    totals.push(total);
                    fillColors.push(bank.color);
                    borderColors.push(bank.border);
                    return{
                        ...bank,
                        totalBalance: total,
                    }
                })
                setBankInfoWithTotals(banksWithTotal)

                const accountWithBankInfo = response.accounts.map(account => {
                    const bank = response.banks.find((b) => b.name === account.bank)
                    return {
                        ...account,
                        ...bank,
                    }
                })
                setAccountInfoWithBankInfo(accountWithBankInfo)

                const chartInfo = {
                    labels: bankNames,
                    datasets: [
                        {
                            label: 'Total Balance',
                            data: totals,
                            backgroundColor: fillColors,
                            borderColor: borderColors,
                            borderWidth: 2,
                            cutout: '35%',
                        },
                    ],
                };
                setChartData(chartInfo);

                setIsLoading(false)
            }catch (e) {
                console.log(e.message);
            }
        }
        fetchData();
    }, []);
    return {config,connectedBankDetails, bankInfoWithTotals, isLoading, accountInfoWithBankInfo, chartData, total,};
}

export default useConfigContext;
