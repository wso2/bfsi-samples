import {Grid} from "@oxygen-ui/react";
import BankCard from "./bank-card.jsx";

const ConnectedBanks = ()=>{
    return(
        <>
            <Grid container={true} alignItems="center" sx={{background:"green",height: { md:"16rem",sm:"fit-content", xs:"fit-content"},display:"flex", direction:{lg:"row",sm:"column",md:"row",xs:"column"}, justifyContent:"space-between",alignItems:"center"}} spacing={2}>
                <BankCard />
                <BankCard />
                <BankCard />
            </Grid>
        </>
    );
}

export default ConnectedBanks;
