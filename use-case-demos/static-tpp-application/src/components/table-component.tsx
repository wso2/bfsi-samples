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
import {Box, IconButton, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@oxygen-ui/react";
import {formatCurrency} from "../utility/number-formatter.ts";
// @ts-ignore
import {ArrowDownIcon, ArrowUpIcon} from "@oxygen-ui/react-icons";
import type {JSX} from "react";

interface TableComponentProps {
    tableData: TransactionData[] | StandingOrders[];
    dataConfigs?: TableConfigs[];
    tableType: "transaction" | "standing-order"|"";
}

const TableComponent = ({tableData,dataConfigs,tableType}:TableComponentProps)=>{

    const renderAmount = (dataRow: TransactionData | StandingOrders, credDebitStatus: JSX.Element | null) => {
            const currency = 'currency' in dataRow ? dataRow.currency : '';
            const amount = 'amount' in dataRow ? dataRow.amount : '0';
            const formattedAmount = `${currency} ${formatCurrency(amount)}`;

            return (
                <Box style={{width:"60%", justifyContent:"space-between", display:'flex', gap:'1rem'}}>
                    {formattedAmount}
                    {tableType === "transaction" && credDebitStatus}
                </Box>
                );
        };
    const keysList: string[] = dataConfigs?dataConfigs.flatMap(dataKey=> {
        return Object.keys(dataKey);
    }): []
    const valuesList:string[] = dataConfigs?dataConfigs.flatMap(dataValues=>{
        return Object.values(dataValues)
    }):[]
    return (
        <>
            <TableContainer >
                <Table>
                    <TableHead>
                        <TableRow sx={{backgroundColor:'var(--oxygen-palette-primary-tableHeaderBackground)'}}>
                            {keysList.map((headerKey,index)=>
                                <TableCell key={index} sx={{color:'var(--oxygen-palette-primary-tableHeaderFontColor)'}}>{headerKey}</TableCell>
                            )}
                        </TableRow>
                    </TableHead>
                    <TableBody sx={{backgroundColor:'white'}}>
                        {tableData.slice(0, 4).map((dataRow:TransactionData|StandingOrders, index:number)=>{

                            const isTransactionData = (data: TransactionData | StandingOrders): data is TransactionData => {
                                return 'creditDebitStatus' in data;
                                };

                            const credDebitStatus = tableType === "transaction" && isTransactionData(dataRow)
                            ? (dataRow.creditDebitStatus === "c"
                                ? <IconButton style={{color: 'var(--oxygen-palette-primary-greenArrowColor)'}} aria-label="Credit transaction"><ArrowDownIcon size={24} /></IconButton>
                                    : <IconButton style={{color: 'var(--oxygen-palette-primary-redArrowColor)'}} aria-label="Debit transaction"><ArrowUpIcon size={24} /></IconButton>)
                                : null;

                            const amount = renderAmount(dataRow, credDebitStatus);
                            return(
                                <TableRow key={index} hideBorder={true}>
                                    {valuesList.map((valuesData,cellIndex)=>
                                        <TableCell key={cellIndex}>{valuesData === "amount" ? amount : (dataRow as any)[valuesData]}</TableCell>
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
