/*
 * *
 *  * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 *  *
 *  * WSO2 LLC. licenses this file to you under the Apache License,
 *  * Version 2.0 (the "License"); you may not use this file except
 *  * in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

import ApplicationLayout from "../../layouts/application-layout/application-layout.tsx";
import PaymentAccountPageLayout from "../../layouts/payment-account-page-layout/payment-account-page-layout.tsx";
import {Box, IconButton, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@oxygen-ui/react";
import {formatCurrency} from "../../utility/number-formatter.ts";
import type {TransactionData} from "../../hooks/config-interfaces.ts";
// @ts-ignore
import {ArrowDownIcon, ArrowUpIcon} from "@oxygen-ui/react-icons";
import {useState} from "react";
import {ChevronRight} from "@mui/icons-material";


interface AllTransactionsProps {
    name: string;
    transactions: TransactionData[];
}

const AllTransactionsPage = ({name,transactions}:AllTransactionsProps) => {

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
                        <TableContainer >
                            <Table>
                                <TableHead>
                                    <TableRow sx={{backgroundColor:'#F6F6F7'}}>
                                        <TableCell sx={{color:'#6B7280'}}>id</TableCell>
                                        <TableCell sx={{color:'#6B7280'}}>date</TableCell>
                                        <TableCell sx={{color:'#6B7280'}}>reference</TableCell>
                                        <TableCell sx={{color:'#6B7280'}}>bank</TableCell>
                                        <TableCell sx={{color:'#6B7280'}}>Account</TableCell>
                                        <TableCell sx={{color:'#6B7280'}}>Amount</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {transactionsToDisplay.map((transactionData, index)=>{
                                        const credDebitStatus = transactionData.status === "c"? <IconButton style={{color: '#2ecc71'}}><ArrowDownIcon size={24} /></IconButton> : <IconButton style={{color: '#c0392b'}}><ArrowUpIcon size={24} /></IconButton>
                                        return(
                                            <TableRow key={index} hideBorder={true}>
                                                <TableCell>{transactionData.id}</TableCell>
                                                <TableCell>{transactionData.date}</TableCell>
                                                <TableCell>{transactionData.reference}</TableCell>
                                                <TableCell>{transactionData.bank}</TableCell>
                                                <TableCell>{transactionData.account}</TableCell>
                                                <TableCell><Box style={{width:"60%", justifyContent:"space-between", display:'flex', gap:'1rem'}}>{transactionData.currency+" "+formatCurrency(transactionData.amount)} {credDebitStatus}</Box></TableCell>
                                            </TableRow>
                                        );
                                    })}
                                </TableBody>
                            </Table>
                        </TableContainer>
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
