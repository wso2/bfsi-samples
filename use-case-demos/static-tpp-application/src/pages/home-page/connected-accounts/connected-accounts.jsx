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

import {Accordion, AccordionDetails, AccordionSummary, Typography} from "@oxygen-ui/react";
import {ChevronDownIcon} from '@oxygen-ui/react-icons';
import './connected-accounts.scss'
import ConnectedAccountsTable from "./connected-accounts-table.jsx";

/*
 * ConnectedAccounts component displays the list of all connected financial accounts
 * within a collapsible accordion UI element.
 *
 * Params:
 * @param {Array<object>} accountsInfo - List of account objects to be passed to the ConnectedAccountsTable.
 */
const ConnectedAccounts = ({accountsInfo})=>{
    return (
        <div>
            <Accordion className='accordion-outer'>
                <AccordionSummary
                    expandIcon={<ChevronDownIcon />}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>Accounts Information</Typography>
                </AccordionSummary>
                <AccordionDetails>
                        <ConnectedAccountsTable accounts={accountsInfo} />
                </AccordionDetails>
            </Accordion>
        </div>
    );
}

export default ConnectedAccounts;

