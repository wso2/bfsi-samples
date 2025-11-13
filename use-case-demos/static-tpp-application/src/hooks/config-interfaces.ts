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
}

export interface AppInfo {
    route: string;
    applicationName: string;
    banksInfo: DynamicBanks[];
}

export interface Bank {
    name: string;
    image: string;
    currency: string;
    color: string;
    border: string;
}

export interface Account {
    id: string;
    bank: string;
    name: string;
    balance: number;
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
    "currency": string
}



export interface StandingOrders{
    "ID": string,
    "Reference": string,
    "Bank": string,
    "NextDate": string,
    "Status": string,
    "Amount": string,
    "Currency": string,
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


export interface Config {
    user: User;
    name: AppInfo;
    banks: Bank[];
    accounts: Account[];
    payees: Payee[];
    transactions: TransactionData[];
    standingOrders: StandingOrders[];
    types: Type[];
}

interface PaymentData {
    type: "payment";
    data: { 
        id: string; 
        amount: string; 
        currency: string; 
        account:string;
        bank:string;
        date:string;
        reference:string;
    };
}

interface SingleAccountState {
    type: "single";
    data: { 
        accountDetails: any; 
        bankInfo: string; 
    };
}

interface MultipleAccountState {
    type: "multiple";
    data: { 
        accountDetails: any; 
        bankInfo: string; 
    };
}

export type OperationState = PaymentData | SingleAccountState | MultipleAccountState;
