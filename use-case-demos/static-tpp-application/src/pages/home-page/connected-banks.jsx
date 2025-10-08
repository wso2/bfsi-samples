import {Grid} from "@oxygen-ui/react";
import BankCard from "./bank-card.jsx";

const ConnectedBanks = ({banksInformationWithTotals})=>{
    return(
        <>
            <Grid container={true} alignItems="center" sx={{height: { md:"16rem",sm:"fit-content", xs:"fit-content"},display:"flex", direction:{lg:"row",sm:"column",md:"row",xs:"column"}, justifyContent:"space-between",alignItems:"center"}} spacing={2}>
                {/*<BankCard />*/}
                {/*<BankCard />*/}
                {/*<BankCard />*/}

                {banksInformationWithTotals.map((bank)=>{
                    return (
                        <BankCard key={bank.id} bankInformationWithTotal={bank} />
                    );
                })}

            </Grid>
        </>
    );
}

export default ConnectedBanks;
