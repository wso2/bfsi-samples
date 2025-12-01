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

import {Box, IconButton, } from "@oxygen-ui/react";
import type {StandingOrders, TableConfigs} from "../../hooks/config-interfaces.ts";
import {useState} from "react";
import {ChevronRight} from "@mui/icons-material";
import ApplicationLayout from "../../layouts/application-layout/application-layout.tsx";
import PaymentAccountPageLayout from "../../layouts/payment-account-page-layout/payment-account-page-layout.tsx";
import TableComponent from "../../components/table-component.tsx";

interface StandingOrdersTableProps {
    name:string;
    standingOrdersList:StandingOrders[];
    standingOrdersTableHeaderData?:TableConfigs[];
}

/**
 * @function AllStandingOrders
 * @description A dedicated page component for displaying a paginated view of all
 * recurring payments (standing orders) in a table.
 * It manages the pagination state to show 10 items at a time and integrates
 * into the main application layout.
 */
const AllStandingOrders = ({standingOrdersTableHeaderData,name, standingOrdersList}:StandingOrdersTableProps)=>{

    const [paginationIndex,setPaginatedIndex] = useState(10);
    const isDisabled =  standingOrdersList[paginationIndex+1] == null
    const onHandleNextButtonClick = () => {
        setPaginatedIndex(paginationIndex+10);
    }
    const standingOrdersToDisplay = standingOrdersList.slice(paginationIndex-10,paginationIndex)

    return (
        <>
            <ApplicationLayout name={name}>
                <PaymentAccountPageLayout title={"Standing Orders"}>
                    <Box className={'standing-orders-container-outer'}>
                        <TableComponent tableData={standingOrdersToDisplay} tableType={""} dataConfigs={standingOrdersTableHeaderData}/>
                    </Box>
                    <IconButton onClick={onHandleNextButtonClick} disabled={isDisabled}>
                        <p>Next</p>
                        <ChevronRight/>
                    </IconButton>
                </PaymentAccountPageLayout>
            </ApplicationLayout>
        </>
    )
}

export default AllStandingOrders;
