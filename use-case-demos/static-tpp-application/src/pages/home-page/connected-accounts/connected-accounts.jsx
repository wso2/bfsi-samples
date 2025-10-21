import {Accordion, AccordionDetails, AccordionSummary, Typography} from "@oxygen-ui/react";
import {ChevronDownIcon} from '@oxygen-ui/react-icons';
import './connected-accounts.scss'
import ConnectedAccountsTable from "./connected-accounts-table.jsx";

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