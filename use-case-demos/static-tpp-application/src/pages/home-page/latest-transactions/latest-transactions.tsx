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

import {Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@oxygen-ui/react"
import type {TransactionData} from "../../../hooks/config-interfaces.ts";
import '../home.scss';
import {formatCurrency} from "../../../utility/number-formatter.ts";

interface LatestTransactionsProps {
    transactions: TransactionData[];
}

const LatestTransactions = ({transactions}:LatestTransactionsProps)=>{
    return (
        <>
            <Box className={'table-container'}>
                <TableContainer >
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>id</TableCell>
                                <TableCell>date</TableCell>
                                <TableCell>reference</TableCell>
                                <TableCell>bank</TableCell>
                                <TableCell>Account</TableCell>
                                <TableCell>Currency</TableCell>
                                <TableCell>Amount</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {transactions.map((transactionData, index)=>{
                                return(
                                    <TableRow key={index}>
                                        <TableCell>{transactionData.id}</TableCell>
                                        <TableCell>{transactionData.date}</TableCell>
                                        <TableCell>{transactionData.reference}</TableCell>
                                        <TableCell>{transactionData.bank}</TableCell>
                                        <TableCell>{transactionData.account}</TableCell>
                                        <TableCell>{transactionData.currency}</TableCell>
                                        <TableCell>{formatCurrency(transactionData.amount)}</TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Box>
        </>
    );
}

export default LatestTransactions;
