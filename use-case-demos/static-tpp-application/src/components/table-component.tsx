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

interface TableComponentProps {
    tableData: TransactionData[] | StandingOrders[];
    dataConfigs?: TableConfigs[];
    tableType: string;
}



const TableComponent = ({tableData,dataConfigs,tableType}:TableComponentProps)=>{

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
                        <TableRow sx={{backgroundColor:'#F6F6F7'}}>
                            {keysList.map(headerKey=>
                                <TableCell sx={{color:'#6B7280'}}>{headerKey}</TableCell>
                            )}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {tableData.slice(0, 4).map((dataRow:TransactionData|StandingOrders, index:number)=>{
                            const credDebitStatus = (dataRow as TransactionData).status==="c"? <IconButton style={{color: '#2ecc71'}}><ArrowDownIcon size={24} /></IconButton> : <IconButton style={{color: '#c0392b'}}><ArrowUpIcon size={24} /></IconButton>

                            const amount = tableType === "transaction"? <Box style={{width:"60%", justifyContent:"space-between", display:'flex', gap:'1rem'}}>{(dataRow as any).currency+" "+formatCurrency((dataRow as any).amount)} {credDebitStatus}</Box> : <Box style={{width:"60%", justifyContent:"space-between", display:'flex', gap:'1rem'}}>{(dataRow as any).currency+" "+formatCurrency((dataRow as any).amount)}</Box>

                            return(
                                <TableRow key={index} hideBorder={true}>
                                    {valuesList.map(valuesData=>
                                        <TableCell>{valuesData === "amount" ? amount : (dataRow as any)[valuesData]}</TableCell>
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
