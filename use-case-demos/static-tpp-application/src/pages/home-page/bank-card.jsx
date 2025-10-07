import {Card, Grid} from "@oxygen-ui/react";

const BankCard = ()=>{
    return (
        <>
            <Grid item={true} sx={{height: "100%"}} xs={12} sm={12} md={4} lg={4}>
                <Card sx={{width:'100%',height:'100%'}}>
                    <Grid container sx={{width:'100%',height:'100%', display:'flex', justifyContent:'space-around', alignItems:"center", direction: "row", flexGrow:1}}>
                        <Grid item={true} xs={4} sm={6} lg={6} md={6} sx={{background:"blue", height:'100%'}}>

                        </Grid>
                        <Grid item={true} xs={8} sm={6} lg={6} md={6} sx={{background:"yellow",height:"100%"}}>
                            <Grid container alignItems="center">
                                <Grid item={true} xs={12} sm={12} lg={12} md={12}>ABC Bank Corporation</Grid>
                                <Grid item={true} xs={12} sm={12} lg={12} md={12}>ABC Bank Corporation</Grid>
                                <Grid item={true} xs={12} sm={12} lg={12} md={12}>ABC Bank Corporation</Grid>
                            </Grid>
                        </Grid>
                    </Grid>
                </Card>
            </Grid>

        </>
    );
}

export default BankCard;