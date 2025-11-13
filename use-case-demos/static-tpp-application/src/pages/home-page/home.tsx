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


import ApplicationLayout from "../../layouts/application-layout/application-layout.tsx";

import { Grid } from "@mui/material";
import HomePageLayout from "../../layouts/home-page-layout/home-page-layout.tsx";
import type {AppInfo, Bank, StandingOrders, TransactionData, User} from "../../hooks/config-interfaces.ts";
import type {BanksWithAccounts, ChartData, OverlayDataProp} from "../../hooks/use-config-context.ts";
import {InfographicsContent} from "./infographics-content/infographics-content.tsx";
import ConnectedBanksAccounts from "./connected-banks-accounts/connected-banks-accounts.tsx";
import CustomTitle from "../../components/custom-title/custom-title.tsx";
import LatestTransactions from "./latest-transactions/latest-transactions.tsx";
import StandingOrdersTable from "./standing-orders/standing-orders.tsx";
import {useNavigate} from "react-router-dom";
import OverlayConfirmation from "../../components/overlay-confirmation/overlay-confirmation.tsx";

/**
 * The main component for the product's home page.
 * It fetches user information using the `useAuthContext` custom hook and
 * passes this data to the `QuickActions` component to display user-specific content.
 */
interface AccountsCentralLayoutProps {
    name: string;
    userInfo: User
    total: number;
    chartData: ChartData
    banksWithAccounts: BanksWithAccounts[];
    transactions: TransactionData[];
    standingOrderList: StandingOrders[];
    appInfo: AppInfo;
    banksList: Bank[];
    overlayInformation: OverlayDataProp;
}

export interface SideButtonProps {
    name: string;
}

const Home = ({name,userInfo,total,chartData,banksWithAccounts,transactions,standingOrderList,appInfo,banksList,overlayInformation}:AccountsCentralLayoutProps)=>{

    const navigate = useNavigate();

    console.log(overlayInformation)

    const onButtonHandler = (buttonName:string) => {
        if(buttonName === "Add Account"){
            navigate(`/${appInfo.route}/accounts`,{
                state:{
                    name:appInfo.applicationName,
                    banksWithAccounts:banksList,
                }
            });
        }
    }

    return (
        <>
            <ApplicationLayout name={name}>
                <HomePageLayout userInfo={userInfo} appInfo={appInfo}>
                    <Grid className={'info-graphic'}>
                        <InfographicsContent total={total} chartInfo={chartData}/>
                    </Grid>
                    <Grid className={'accounts-container'}>
                        <CustomTitle title={"Connected Banks And Accounts"} buttonName={"Add Account"} buttonType={"contained"} onPress={onButtonHandler}/>
                        <ConnectedBanksAccounts bankAndAccountsInfo={banksWithAccounts}/>
                    </Grid>
                    <Grid className={'transactions-container'}>
                        <CustomTitle title={"Latest Transactions"} buttonName={"view more"} buttonType={"outlined"}/>
                        <LatestTransactions transactions={transactions}/>
                    </Grid>
                    <Grid className={'standing-orders-container'}>
                        <CustomTitle title={"Standing Orders"} buttonName={"view more"} buttonType={"outlined"}/>
                        <StandingOrdersTable standingOrderList={standingOrderList}/>
                    </Grid>

                </HomePageLayout>
            </ApplicationLayout>

            {overlayInformation.flag &&
                <OverlayConfirmation
                    onConfirm={overlayInformation.overlayData.onMainButtonClick}
                    onCancel={()=>{}}
                    mainButtonText={overlayInformation.overlayData.mainButtonText}
                    secondaryButtonText={overlayInformation.overlayData.secondaryButtonText}
                    content={overlayInformation.overlayData.context}
                    title={overlayInformation.overlayData.title}/>

            }
        </>
    );
}

export default Home;

