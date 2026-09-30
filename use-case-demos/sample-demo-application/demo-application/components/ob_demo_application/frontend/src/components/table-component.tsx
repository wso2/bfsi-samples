/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
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
import {useMemo} from "react";

interface TableComponentProps {
    tableData: TransactionData[] | StandingOrders[];
    dataConfigs?: TableConfigs[];
    tableType: "transaction" | "standing-order"|"";
    dataLimit?: number;
}

/**
 * Formats transaction ID to ensure consistent 8-digit format
 * @param id - The transaction ID (e.g., "T001" or "T00123460")
 * @returns Formatted ID with exactly 8 digits (e.g., "T00123460")
 */
const formatTransactionId = (id: string | undefined): string => {
    // A transaction read from the bank can reach here without an id, so this has to tolerate one
    // missing: throwing here unmounts the whole page rather than leaving one cell empty.
    if (!id) {
        return "";
    }
    const match = id.match(/^([A-Z]+)(\d+)$/);
    if (match) {
        const prefix = match[1]; // e.g., "T"
        const number = match[2]; // e.g., "001" or "123460"
        const paddedNumber = number.padStart(8, '0');
        return `${prefix}${paddedNumber}`;
    }
    return id;
};

const TableComponent =
    ({tableData,dataConfigs,tableType, dataLimit=4}:TableComponentProps)=>{

        const sortedData = useMemo(() => {
            if (tableType === "transaction") {
                // Sort transactions by date (most recent first)
                // Dates are already in YYYY-MM-DD format from date-utils processing
                return [...tableData].sort((a, b) => {
                    const dateA = 'date' in a ? a.date : '0';
                    const dateB = 'date' in b ? b.date : '0';
                    // Compare as strings in YYYY-MM-DD format (works correctly)
                    return dateB.localeCompare(dateA); // Descending order (newest first)
                });
            } else if (tableType === "standing-order") {
                // Sort standing orders by nextDate (soonest first)
                return [...tableData].sort((a, b) => {
                    const dateA = 'nextDate' in a ? (a as StandingOrders).nextDate : '0';
                    const dateB = 'nextDate' in b ? (b as StandingOrders).nextDate : '0';
                    // Compare as strings in YYYY-MM-DD format
                    return dateA.localeCompare(dateB); // Ascending order (soonest first)
                });
            }
            return tableData;
        }, [tableData, tableType]);

        const renderAmount = (dataRow: TransactionData | StandingOrders) => {
            const currency = 'currency' in dataRow ? dataRow.currency : '';
            const amount = 'amount' in dataRow ? dataRow.amount : '0';
            const formattedAmount = `${currency} ${formatCurrency(amount)}`;
            return formattedAmount;
        };

        const renderCellValue = (dataRow: TransactionData | StandingOrders, valuesData: string) => {
            if (valuesData === "amount") {
                return renderAmount(dataRow);
            }

            if (valuesData === "id" && tableType === "transaction") {
                const value = (dataRow as any)[valuesData];
                return formatTransactionId(value);
            }

            return (dataRow as any)[valuesData];
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
                            {sortedData.slice(0, dataLimit).map((dataRow:TransactionData|StandingOrders, index:number)=>{
                                const isTransactionData = (data: TransactionData | StandingOrders): data is TransactionData => {
                                    return 'creditDebitStatus' in data;
                                };
                                // Credit is green and points up, debit is red and points down. Matched on
                                // the first letter so that both vocabularies work: the demo data says
                                // "c"/"d", while a transaction read from the bank carries the API's
                                // "Credit"/"Debit".
                                const isCredit = isTransactionData(dataRow)
                                    && String(dataRow.creditDebitStatus ?? "").trim().toLowerCase().startsWith("c");
                                const credDebitStatus = tableType === "transaction" && isTransactionData(dataRow)
                                    ? (isCredit
                                        ? <Box style={{color: 'var(--oxygen-palette-primary-greenArrowColor)'}} aria-label="Credit transaction"><ArrowUpIcon size={24} /></Box>
                                        : <Box style={{color: 'var(--oxygen-palette-primary-redArrowColor)'}} aria-label="Debit transaction"><ArrowDownIcon size={24} /></Box>)
                                    : null;

                                return(
                                    <TableRow key={index} hideBorder={false}>
                                        {valuesList.map((valuesData,cellIndex)=>{
                                                const isAmountColumn = valuesData === "amount";
                                                return (
                                                    <TableCell sx={{textAlign:isAmountColumn? "end": "left", paddingRight:"2rem"}} key={cellIndex}>
                                                        {renderCellValue(dataRow, valuesData)}
                                                    </TableCell>
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
