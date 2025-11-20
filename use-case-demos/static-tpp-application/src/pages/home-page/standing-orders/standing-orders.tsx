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

import {Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@oxygen-ui/react";
import type {StandingOrders} from "../../../hooks/config-interfaces.ts";
import {formatCurrency} from "../../../utility/number-formatter.ts";

interface StandingOrdersProps {
    standingOrderList: StandingOrders[];
}

const StandingOrdersTable = ({standingOrderList}:StandingOrdersProps)=>{
    return (
        <>
            <Box className={'standing-orders-container-outer'}>
                <TableContainer>
                    <Table>
                        <TableHead>
                            <TableRow sx={{backgroundColor:'#F6F6F7'}}>
                                <TableCell sx={{color:'#6B7280'}}>ID</TableCell>
                                <TableCell sx={{color:'#6B7280'}}>Reference</TableCell>
                                <TableCell sx={{color:'#6B7280'}}>Bank</TableCell>
                                <TableCell sx={{color:'#6B7280'}}>Next Date</TableCell>
                                <TableCell sx={{color:'#6B7280'}}>Status</TableCell>
                                <TableCell sx={{color:'#6B7280'}}>Amount</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {standingOrderList.map((standingOrder) => (
                                <TableRow hideBorder={true}>
                                    <TableCell>{standingOrder.ID}</TableCell>
                                    <TableCell>{standingOrder.Reference}</TableCell>
                                    <TableCell>{standingOrder.Bank}</TableCell>
                                    <TableCell>{standingOrder.NextDate}</TableCell>
                                    <TableCell>{standingOrder.Status}</TableCell>
                                    <TableCell>{formatCurrency(standingOrder.Amount)}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Box>
        </>
    );
}

export default StandingOrdersTable;

