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

import type {StandingOrders, TableConfigs, TransactionData} from "../hooks/config-interfaces.ts";
import {Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@oxygen-ui/react";
import {formatCurrency} from "../utility/number-formatter.ts";
// @ts-ignore
import {ArrowDownIcon, ArrowUpIcon} from "@oxygen-ui/react-icons";

interface TableComponentProps {
    tableData: TransactionData[] | StandingOrders[];
    dataConfigs?: TableConfigs[];
    tableType: "transaction" | "standing-order"|"";
    dataLimit?: number;
}

const TableComponent =
    ({tableData,dataConfigs,tableType, dataLimit=4}:TableComponentProps)=>{

    const renderAmount = (dataRow: TransactionData | StandingOrders) => {
        const currency = 'currency' in dataRow ? dataRow.currency : '';
        const amount = 'amount' in dataRow ? dataRow.amount : '0';
        const formattedAmount = `${currency} ${formatCurrency(amount)}`;
        return formattedAmount;
    };
    const keysList: string[] = dataConfigs?dataConfigs.flatMap(dataKey=> {
        return Object.keys(dataKey);
    }): []
    if (tableType === "transaction") {
        keysList.push("");
    }
    const valuesList:string[] = dataConfigs?dataConfigs.flatMap(dataValues=>{
        return Object.values(dataValues)
    }):[]
    return (
        <>
            <TableContainer >
                <Table>
                    <TableHead>
                        <TableRow sx={{backgroundColor:'var(--oxygen-palette-primary-tableHeaderBackground)'}}
                                  hideBorder={false}>
                            {keysList.map((headerKey,index)=>{
                                    const isHeaderAmount = headerKey === "Amount"
                                    return(
                                        <TableCell key={index}
                                                   sx={{color:'var(--oxygen-palette-primary-tableHeaderFontColor)',
                                            textAlign:isHeaderAmount?"right":"left",
                                                       paddingRight: isHeaderAmount? "2rem":""}}>{headerKey}</TableCell>
                                    );
                                }
                            )}
                        </TableRow>
                    </TableHead>
                    <TableBody sx={{backgroundColor:'white'}}>
                        {tableData.slice(0, dataLimit).map((dataRow:TransactionData|StandingOrders, index:number)=>{
                            const isTransactionData = (data: TransactionData | StandingOrders): data is TransactionData => {
                                return 'creditDebitStatus' in data;
                            };
                            const credDebitStatus = tableType === "transaction" && isTransactionData(dataRow)
                                ? (dataRow.creditDebitStatus === "c"
                                    ? <Box style={{color: 'var(--oxygen-palette-primary-redArrowColor)'}} aria-label="Credit transaction"><ArrowDownIcon size={24} /></Box>
                                    : <Box style={{color: 'var(--oxygen-palette-primary-greenArrowColor)'}} aria-label="Debit transaction"><ArrowUpIcon size={24} /></Box>)
                                : null;
                            const amount = renderAmount(dataRow);
                            return(
                                <TableRow key={index} hideBorder={false}>
                                    {valuesList.map((valuesData,cellIndex)=>{
                                            const isAmountColumn = valuesData === "amount";
                                            return (
                                                <TableCell sx={{textAlign:isAmountColumn? "end": "left", paddingRight:"2rem"}} key={cellIndex}>{valuesData === "amount" ? amount : (dataRow as any)[valuesData]}</TableCell>
                                            );
                                        }
                                    )}
                                    {tableType === "transaction" && (
                                        <TableCell sx={{textAlign: "center"}}>
                                            {credDebitStatus}
                                        </TableCell>
                                    )}
                                </TableRow>
                            );
                        })}
                    </TableBody>
                </Table>
            </TableContainer>
        </>
    );
}

export default TableComponent;










