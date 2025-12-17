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
import type {
    AppInfo,
    Bank,
    StandingOrders,
    TableConfigs,
    TransactionData,
    User
} from "../../hooks/config-interfaces.ts";
import type {BanksWithAccounts, ChartData, OverlayDataProp} from "../../hooks/use-config-context.ts";
import {InfographicsContent} from "./infographics-content/infographics-content.tsx";
import ConnectedBanksAccounts from "./connected-banks-accounts/connected-banks-accounts.tsx";
import CustomTitle from "../../components/custom-title/custom-title.tsx";
import {useNavigate} from "react-router-dom";
import OverlayConfirmation from "../../components/overlay-confirmation/overlay-confirmation.tsx";
import TableComponent from "../../components/table-component.tsx";

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
    transactionTableHeaderData?:TableConfigs[];
    standingOrdersTableHeaderData?:TableConfigs[];
}

export interface SideButtonProps {
    name: string;
}

/**
 * @function Home
 * @description The main dashboard component that composes various UI elements
 * like infographics, connected accounts, and transaction lists, into the
 * central application layout. It handles specific button clicks to navigate
 * to other functional pages (e.g., 'Add Account', 'view more').
 */
const Home = ({standingOrdersTableHeaderData,name,userInfo,total,chartData,banksWithAccounts,transactions,standingOrderList,appInfo,banksList,overlayInformation,transactionTableHeaderData}:AccountsCentralLayoutProps)=>{

    const navigate = useNavigate();

    const addAccount =()=>{
        navigate(`/${appInfo.route}/accounts`,{
            state:{
                name:appInfo.applicationName,
                banksWithAccounts:banksList,
            }
        });
    }

    const viewMore=(title?:string)=>{
        const route = title === "Latest Transactions"? "transactions": "standing-orders";
        navigate(`/${appInfo.route}/${route}`);
    }

    const onButtonHandler = (buttonName:string,title?:string) => {
        if(buttonName === "Add Account"){
            addAccount();
        }else if(buttonName === "view more"){
            viewMore(title);

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
                        <CustomTitle title={"Connected Banks"} buttonName={"Add Account"} buttonType={"contained"} onPress={onButtonHandler}/>
                        <ConnectedBanksAccounts bankAndAccountsInfo={banksWithAccounts}/>
                    </Grid>
                    <Grid className={'transactions-container'}>
                        <CustomTitle title={"Latest Transactions"} buttonName={"view more"} buttonType={"outlined"} onPress={onButtonHandler}/>
                        <TableComponent tableData={transactions} tableType={"transaction"} dataConfigs={transactionTableHeaderData}/>
                    </Grid>
                    <Grid className={'standing-orders-container'}>
                        <CustomTitle title={"Standing Orders"} buttonName={"view more"} buttonType={"outlined"} onPress={onButtonHandler}/>
                        <TableComponent tableData={standingOrderList} dataConfigs={standingOrdersTableHeaderData} tableType={""}/>
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

