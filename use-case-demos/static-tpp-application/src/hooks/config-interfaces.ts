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


export interface User {
    name: string;
    image: string;
    background: string;
}

export interface DynamicBanks {
    name: string;
    route: string;
    startingAccountNumbers: string;
}

export interface AppInfo {
    route: string;
    applicationName: string;
}

export interface Bank {
    name: string;
    image: string;
    currency: string;
    color: string;
    border: string;
    startingAccountNumbers: string;
    accounts:Account[];
    route: string;
    bankThemeId: number;
    standingOrders:StandingOrders[];

}

export interface Account {
    id: string;
    bank: string;
    name: string;
    balance: number;
    transactions:TransactionData[];
}

export interface Payee {
    name: string;
    bank: string;
    accountNumber: string;
}

export interface TransactionData{
    "id": string,
    "date": string,
    "reference": string,
    "bank": string,
    "account": string,
    "amount": string,
    "currency": string,
    "creditDebitStatus": string
}

export interface StandingOrders{
    "id": string,
    "reference": string,
    "bank": string,
    "nextDate": string,
    "status": string,
    "amount": string,
    "currency": string,
}

export interface Step {
    id: string;
    name: string;
    component: string;
}

export interface UseCase {
    id: string;
    title: string;
    steps: Step[];
}

export interface Type {
    id: string;
    title: string;
    useCases: UseCase[];
}

export interface TableConfigs{
    [key: string]: string;

}

export interface CustomColors{
    [key: string]: string;
}

export interface Config {
    user: User;
    name: AppInfo;
    banks: Bank[];
    payees: Payee[];
    types: Type[];
    transactionTableHeaderData: TableConfigs[];
    standingOrdersTableHeaderData: TableConfigs[];
    colors: CustomColors[];
    accountNumbersToAdd: string[];
}
