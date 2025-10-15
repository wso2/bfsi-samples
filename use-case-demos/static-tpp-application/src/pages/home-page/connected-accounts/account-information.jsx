import {Box, TableCell, TableRow} from "@oxygen-ui/react";
import "./account-information.scss"
import {formatCurrency} from "../../../hooks/utility.js";

const AccountInformation = ({account,borderStatus,headers})=>{

    const getAccountValues = (header) => {
        switch (header) {
            case "Logo":
                return (
                    <Box className="account-logo-container">
                        <img src={account.image} alt="" className="logo-image"/>

                    </Box>
                );
            case "Bank":
                return account.bank;
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

                    <TableCell key={header}>
                        {cellContent}
                    </TableCell>
                );
            })}
        </TableRow>
    );
}

export default AccountInformation;