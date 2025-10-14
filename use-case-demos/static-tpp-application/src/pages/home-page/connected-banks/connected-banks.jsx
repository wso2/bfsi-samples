import {Card, Grid} from "@oxygen-ui/react";
import './connected-banks.scss'
import BankCard from "./bank-card.jsx";
import {ContentInnerSectionContainer} from "../../../components/styled-components/styled-containers.jsx";
import {useMediaQuery, useTheme} from "@mui/material";

const ConnectedBanks = ({banksInformationWithTotals})=>{





    return(
        <>
            {/*<ContentInnerSectionContainer>*/}
            {/*    /!*<BankCard />*!/*/}
            {/*    /!*<BankCard />*!/*/}
            {/*    /!*<BankCard />*!/*/}

                {/*{banksInformationWithTotals.map((bank)=>{*/}
                {/*    return (*/}
                {/*        <BankCard key={bank.id} bankInformationWithTotal={bank} />*/}
                {/*    );*/}
            {/*    })}*/}

            {/*</ContentInnerSectionContainer>*/}

            <Grid container spacing={2} className='banks-container'>

                {banksInformationWithTotals.map((bank)=>{
                    return (
                        <BankCard key={bank.id} bankInformationWithTotal={bank} />);
                })}

            </Grid>
        </>
    );
}

export default ConnectedBanks;
