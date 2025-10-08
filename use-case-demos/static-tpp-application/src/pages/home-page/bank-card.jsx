import {Card, Grid} from "@oxygen-ui/react";
import {Box} from "@mui/material";
import {formatCurrency} from "../../hooks/utility.js";

const BankCard = ({bankInformationWithTotal})=>{

    return (
        <>
            <Grid item={true} direction="row" justify="space-between" xs={12} sm={12} md={4}>
                <Card className="shadow">
                    <Grid container xs={12} sm={12} md={12} sx={{display:"flex", flexDirection:"row"}} >
                        <Grid item={true} xs={6} sm={6} md={6} sx={{height:"12rem", display:"flex", justifyContent:"start", alignItems:"center"}}>
                            <Box sx={{width:{md:"10rem", sm:"12rem",lg:'12rem',xs:'12rem'},height:{md:'10rem',sm:'12rem',lg:'12rem',xs:'12rem'}, backgroundColor:'green'}}>
                                <img src={bankInformationWithTotal.image} alt="" style={{width:'100%',height:'100%', objectFit:'cover'}} />
                            </Box>
                        </Grid>
                        <Grid item={true} xs={6} sm={6} md={6} sx={{ height:"12rem",alignItems:"center",justifyContent:"center",display:'flex',flexDirection:"column",gap:"4rem"}}  >
                            <p style={{fontSize:'1.5rem', fontWeight:'bold'}}>{bankInformationWithTotal.name}</p>
                            <p style={{fontWeight:'bold',fontSize:'0.6rem'}}>{bankInformationWithTotal.currency}<span style={{fontSize:'1.6rem',marginLeft:"1rem"}}>{formatCurrency(bankInformationWithTotal.totalBalance)}</span></p>
                        </Grid>
                    </Grid>
                </Card>
            </Grid>

        </>
    );
}

export default BankCard;