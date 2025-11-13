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

import type { AppInfo, DynamicBanks, Type, UseCase } from "../../hooks/config-interfaces";
import './banking-main-content-layout.scss';
import { Button, Grid } from "@oxygen-ui/react";

export interface BankingHomePageProps {
    useCases: Type[];
    bank: DynamicBanks
    appInfo: AppInfo
}

export interface  accountsToAddContent {
    type: string;
    data: []
}

interface BankingMainContentLayoutProps{
    children: React.ReactNode;
    usecasesList: UseCase[];
    selectedUsecaseIndex: number;
    usecaseSelectionHandler: (index: number) => void;
    bankName:string;
}


const BankingMainContentLayout = ({ children,usecasesList,selectedUsecaseIndex,usecaseSelectionHandler,bankName}: BankingMainContentLayoutProps) => {

    return (
        <Grid container className={'banking-outer-layout'}>
            <Grid className="banking-bank-name-container">
                <h3>{bankName}</h3>
            </Grid>
            
            <Grid className="banking-usecase-layout">
                {usecasesList.map((useCase,index) => {

                    const isSelected = selectedUsecaseIndex === index;

                    return (
                        <Button key={index} variant={isSelected?'contained':'outlined'} onClick={()=>{usecaseSelectionHandler(index)}}>{useCase.title}</Button>
                    )
                })}
            </Grid>

            <Grid className={'banking-inner-component-container'}>
                {children}
            </Grid>
        </Grid>
    );
}

export default BankingMainContentLayout;
