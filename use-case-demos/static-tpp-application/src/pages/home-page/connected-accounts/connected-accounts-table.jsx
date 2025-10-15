
import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@oxygen-ui/react";
import AccountInformation from "./account-information.jsx";

const ConnectedAccountsTable = ({accounts})=>{
    const headers = ["Logo", "Bank", "Account Id", "Balance"];
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
                    {accounts.map((account, index) =>{
                        let border = false
                        if (index === accounts.length-1){
                            border = true
                        }
                        return (
                            <AccountInformation account={account} borderStatus = {border} headers={headers} key={index}/>
                        )
                    })}

                </TableBody>
            </Table>
        </TableContainer>

    );
}

export default ConnectedAccountsTable;