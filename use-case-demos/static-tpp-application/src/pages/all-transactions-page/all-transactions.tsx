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
import PaymentAccountPageLayout from "../../layouts/payment-account-page-layout/payment-account-page-layout.tsx";
import {Box, IconButton,} from "@oxygen-ui/react";
import type {TableConfigs, TransactionData} from "../../hooks/config-interfaces.ts";
// @ts-ignore
import {ArrowDownIcon, ArrowUpIcon} from "@oxygen-ui/react-icons";
import {useState} from "react";
import {ChevronRight} from "@mui/icons-material";
import TableComponent from "../../components/table-component.tsx";


interface AllTransactionsProps {
    name: string;
    transactions: TransactionData[];
    transactionTableHeaderData?: TableConfigs[];
}

/**
 * @function AllTransactionsPage
 * @description A page component dedicated to displaying a paginated list of all
 * transaction data within the application layout. It handles state for pagination
 * and renders transaction details in a table, including credit/debit status.
 */
const AllTransactionsPage = ({name,transactions, transactionTableHeaderData}:AllTransactionsProps) => {

    const [paginationIndex,setPaginatedIndex] = useState(10);
    const isDisabled =  transactions[paginationIndex+1] == null
    const onHandleNextButtonClick = () => {
        setPaginatedIndex(paginationIndex+10);
    }
    const transactionsToDisplay = transactions.slice(paginationIndex-10,paginationIndex)
    return (
        <>
            <ApplicationLayout name={name}>
                <PaymentAccountPageLayout title={"Transactions"}>
                    <Box className={'table-container'}>
                        <TableComponent tableData={transactionsToDisplay} tableType={"transaction"} dataConfigs={transactionTableHeaderData}/>
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

export default AllTransactionsPage;
