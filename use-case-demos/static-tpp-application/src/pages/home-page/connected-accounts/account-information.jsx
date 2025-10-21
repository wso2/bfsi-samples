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

import {Box, TableCell, TableRow} from "@oxygen-ui/react";
import "./connected-accounts.scss"
import {formatCurrency} from "../../../hooks/utility.js";

/*
 * AccountInformation component renders a single row in the connected accounts table.
 * It uses a switch case to correctly format and display account data
 * (bank logo, ID, and formatted balance) based on the table header.
 *
 * Params:
 * @param {object} account - The individual account object containing bank, ID, balance, and image.
 * @param {boolean} borderStatus - Flag to hide the bottom border for the last row.
 * @param {Array<string>} headers - Array of table headers used to map and display the correct account value.
 */
const AccountInformation = ({account,borderStatus,headers})=>{

    const getAccountValues = (header) => {
        switch (header) {
            case "Bank":
                return (
                    <Box className="account-logo-container">
                        <img src={account.image} alt="" className="logo-image"/>
                        <p>{account.bank}</p>
                    </Box>
                );
            case "Account Id":
                return account.id;
            case "Balance":
                return account.currency+" "+formatCurrency(account.balance);
            default:
                return null;
        }
    }
    return (
        <TableRow hideBorder={borderStatus}>
            {headers.map((header) => {

                const cellContent = getAccountValues(header);

                return (

                    <TableCell key={header} sx={{fontSize:'1.2rem'}}>
                        {cellContent}
                    </TableCell>
                );
            })}
        </TableRow>
    );
}

export default AccountInformation;