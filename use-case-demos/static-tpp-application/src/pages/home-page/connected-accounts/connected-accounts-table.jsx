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

import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@oxygen-ui/react";
import AccountInformation from "./account-information.jsx";

/*
 * ConnectedAccountsTable component renders a full table view of all connected financial accounts.
 * It sets the table structure, defines headers, and iterates through the accounts,
 * delegating the rendering of individual rows to the AccountInformation component.
 *
 * Params:
 * @param {Array<object>} accounts - List of account objects to display in the table.
 */
const ConnectedAccountsTable = ({accounts}) => {
    const headers = ["Bank", "Account Id", "Balance"];
    return (
        <TableContainer>
            <Table>
                <TableHead>
                    <TableRow>
                        {headers.map(header => (
                            <TableCell key={header}>{header}</TableCell>
                        ))}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {accounts.map((account, index) => {
                        let border = false
                        if (index === accounts.length - 1) {
                            border = true
                        }
                        return (
                            <AccountInformation account={account} borderStatus={border} headers={headers} key={index}/>
                        )
                    })}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default ConnectedAccountsTable;
