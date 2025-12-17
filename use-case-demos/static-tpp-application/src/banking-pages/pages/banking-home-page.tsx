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

import {useSearchParams, useNavigate, Outlet, useLocation} from "react-router-dom";
import {useBankNavigationHook} from "../banking-hooks/use-bank-navigation-hook.ts";
import {useEffect, useRef} from "react";
import BankingOuterLayout from "../banking-layouts/banking-outer-layout.tsx";
import BankingMainContentLayout from "../banking-layouts/banking-main-content-layout.tsx";
import type {AppInfo, Bank, Type} from "../../hooks/config-interfaces.ts";

export interface BankingHomePageProps {
    useCases: Type[];
    bank: Bank
    appInfo: AppInfo;
    accountsNumbersToAdd?: string[];

}

export interface  accountsToAddContent {
    type: string;
    data: []
}

/**
 * @function BankingHomePage
 * @description The main controller component for bank authorization and flow simulation.
 * It initializes the bank's flow management hook (`useBankNavigationHook`), determines
 * the bank's theme color, and uses `BankingMainContentLayout` to orchestrate the rendering
 * of the specific step components (`Outlet`) based on the current step in the flow.
 */
const BankingHomePage = ({ accountsNumbersToAdd,useCases,bank,appInfo }: BankingHomePageProps) => {

    const navigate = useNavigate();
    const location = useLocation();
    const navigationData = useRef(location.state)
    const accountsToAdd = useRef({type:"",data:[]});
    const [params] = useSearchParams();
    const type = params.get("type") || '';
    const {usecasesList,usecaseSelectionHandler,currentStep,onSuccessHandler, selectedUsecaseIndex} = useBankNavigationHook({usecase: useCases, type: type });

    let themeColor;
    if (bank.bankThemeId === 1){
        themeColor = "var(--oxygen-palette-primary-bankColor1)";
    }else if(bank.bankThemeId === 2){
        themeColor = "var(--oxygen-palette-primary-bankColor2)"
    }else if(bank.bankThemeId === 3){
        themeColor = "var(--oxygen-palette-primary-bankColor3)"
    }else{
        themeColor = "var(--oxygen-palette-primary-bankColor4)"
    }
    const selectedAccountNumber = bank.startingAccountNumbers;

    useEffect(() => {
        const path = currentStep?.component
        navigate(`/${bank.route}/`+path)
    },[currentStep])
    return(
        <>
            <BankingMainContentLayout usecasesList={usecasesList} selectedUsecaseIndex={selectedUsecaseIndex} usecaseSelectionHandler={usecaseSelectionHandler} themeColor={themeColor} >
                <BankingOuterLayout image={navigationData.current?.bankInfo.image} bankName={bank.name} themeColor={themeColor}>
                    <Outlet context={{accountsNumbersToAdd,onSuccessHandler, navigationData,accountsToAdd,appInfo,themeColor,selectedAccountNumber}} />
                </BankingOuterLayout>
            </BankingMainContentLayout>
        </>
    )
}

export default BankingHomePage;
