import {Box, TableCell, TableRow} from "@oxygen-ui/react";
import "./connected-accounts.scss"
import {formatCurrency} from "../../../hooks/utility.js";

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