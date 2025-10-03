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

const useConfigContext = () => {
    const [config, setLoadedConfig] = useState(null);
    const [connectedBankDetails, setConnectedBankDetails] = useState(null);
    const [bankInfoWithTotals, setBankInfoWithTotals] = useState(null);
    const [accountInfoWithBankInfo, setAccountInfoWithBankInfo] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect( () => {

        const fetchData = async () => {
            try {
                const response = await api.get("config.json")
                setLoadedConfig(response)
                setConnectedBankDetails(response.banks)

                const banksWithTotal = response.banks.map((bank) => {
                    const total = response.accounts.filter((account) => account.bank === bank.name).reduce((sum, account) => sum+account.balance,0);
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

                setIsLoading(false)
            }catch (e) {
                console.log(e.message);
            }
        }
        fetchData();
    }, []);
    return {config,connectedBankDetails, bankInfoWithTotals, isLoading, accountInfoWithBankInfo};
}

export default useConfigContext;

