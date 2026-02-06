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
import { Box, Button, Grid } from "@oxygen-ui/react";

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
    themeColor: string;

}

/**
 * @function BankingMainContentLayout
 * @description A layout component that structures the main content area for bank-specific
 * authorization flows. It displays a row of **UseCase** selection buttons, dynamically
 * styled with the bank's theme color, and renders the current step component (`children`) below.
 */
const BankingMainContentLayout = ({ children,usecasesList,selectedUsecaseIndex,usecaseSelectionHandler,themeColor}: BankingMainContentLayoutProps) => {

    const useCase= usecasesList.find((_u,index:number)=> index===selectedUsecaseIndex )

    return (
        <Grid container className={'banking-outer-layout'}>

            <Grid className="banking-usecase-layout">
                {usecasesList.map((useCase,index) => {

                    const isSelected = selectedUsecaseIndex === index;

                    return (
                        <Button sx={{'--oxygen-palette-gradients-primary-stop2':themeColor, '--oxygen-palette-gradients-primary-stop1':themeColor, borderColor:themeColor,'--oxygen-palette-primary-main':themeColor, width:"8rem", textTransform:"capitalize"}} key={index} variant={isSelected?'contained':'outlined'} onClick={()=>{usecaseSelectionHandler(index)}}>{useCase.id}</Button>
                    )
                })}

            </Grid>
            <Box sx={{display:"flex", flexDirection:'column', width:"90%"}}>
                <Box className="usecase-title"> {useCase?.id} - {useCase?.title}</Box>
                <Box className="usecase-descriptions"> <p>User verification: &nbsp;</p>  {useCase?.userVerification}</Box>
                <Box className="usecase-descriptions"><p>Consent verification: &nbsp;</p>{useCase?.consentDisplay}</Box>
            </Box>

            <Grid className={'banking-inner-component-container'}>
                {children}
            </Grid>
        </Grid>
    );
}

export default BankingMainContentLayout;
