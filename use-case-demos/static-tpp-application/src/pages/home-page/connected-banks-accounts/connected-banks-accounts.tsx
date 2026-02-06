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

import {Accordion, AccordionDetails, AccordionSummary, Card, Grid, Table, TableBody, TableCell, TableRow,
    Typography} from "@oxygen-ui/react";
// @ts-ignore
import {ChevronDownIcon} from "@oxygen-ui/react-icons";
import type {BanksWithAccounts} from "../../../hooks/use-config-context.ts";
import '../home.scss'
import {formatCurrency} from "../../../utility/number-formatter.ts";
import CustomTitle from "../../../components/custom-title/custom-title.tsx";
import './connected-banks-accounts.scss'
import {useMediaQuery, useTheme} from "@mui/material";

interface ConnectedBanksAccountsProps{
    bankAndAccountsInfo: BanksWithAccounts[];
}

/**
 * @function ConnectedBanksAccounts
 * @description Renders a comprehensive view of all connected financial institutions
 * and their respective accounts. It displays an overview of bank totals using Cards
 * and details individual accounts within responsive Accordion components.
 */
const ConnectedBanksAccounts=
    ({bankAndAccountsInfo}:ConnectedBanksAccountsProps)=>{

    const isLargeScreen = useMediaQuery(useTheme().breakpoints.down('md'));
    const responsiveDirections = isLargeScreen ? 'column' : 'row';

    return(
        <>
            <div className="main-connected-banks-outer" style={{display:"flex", flexDirection:"column"}}>
                <Grid className="card-outer" sx={{flexDirection: responsiveDirections}}>
                    {bankAndAccountsInfo.map((bank,index)=>(
                        <Card key={index} className={'card-inner-bank-info'} >
                            <div className="card-top-container">
                                <div className="logo">
                                    <img src={bank.bank.image} alt=""/>
                                </div>
                                <div className="bank-name-container">
                                    <p>{bank.bank.name}</p>
                                </div>
                            </div>
                            <div className="card-total-container">
                                <p>{bank.bank.currency}</p>
                                <p>{formatCurrency(bank.total)}</p>
                            </div>
                        </Card>
                    ))}

                </Grid>
                    <CustomTitle title={"Connected Accounts"}></CustomTitle>
                <div>
                    {bankAndAccountsInfo.map((bank,index)=>{
                        return (
                            <Accordion>
                                <AccordionSummary expandIcon={<ChevronDownIcon/>} aria-controls={`${index}`}
                                                  id= {`${index}-header`}>
                                    <div className="accordian-header-container">
                                        <div className="bank-container">
                                            <div className="bank-logo-container">
                                                <img src={bank.bank.image} alt="bank logo" className={'bank-logo'} />
                                            </div>
                                            <Typography>{bank.bank.name} - Connected Accounts</Typography>
                                        </div>
                                    </div>
                                </AccordionSummary>
                                <AccordionDetails>
                                    <Table>
                                        <TableBody >
                                            {bank.accounts.map((account,index)=>{
                                                let border = false
                                                if (index === bank.accounts.length - 1) {
                                                    border = true
                                                }
                                                return(
                                                    <TableRow key={index} hideBorder={border} className={"table-row"}>
                                                        <TableCell className={"table-body"}>{account.name}</TableCell>
                                                        <TableCell className={"table-body"}>{account.id}</TableCell>
                                                        <TableCell className={"table-body"}>
                                                            {bank.bank.currency}
                                                        </TableCell>
                                                        <TableCell className={"table-body"}>
                                                            {formatCurrency(account.balance)}
                                                        </TableCell>
                                                    </TableRow>
                                                );
                                            })}
                                        </TableBody>
                                    </Table>
                                </AccordionDetails>
                            </Accordion>
                        )
                    })}
                </div>
            </div>
        </>
    );
}

export default ConnectedBanksAccounts;
