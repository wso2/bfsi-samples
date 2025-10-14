import {formatCurrency} from "../../../hooks/utility.js";
import {
    BankCardContainer,
    BankCardInfoContainer,
    BankCardLogoContainer
} from "../../../components/styled-components/styled-containers.jsx";
import './bank-card.scss'
import {Card, Grid} from "@oxygen-ui/react";
import {useMediaQuery, useTheme} from "@mui/material";

const BankCard = ({bankInformationWithTotal})=>{

    const isSmallScreen = useMediaQuery(useTheme().breakpoints.down('md'));

    const width = isSmallScreen ? '6rem' : '8rem';
    const height = isSmallScreen ? '6rem' : '8rem';
    const justifyContent = isSmallScreen ? 'space-between' : 'center';

    return (
        <>
            {/*<BankCardContainer>*/}
            {/*    <BankCardLogoContainer>*/}
            {/*        <img src={bankInformationWithTotal.image} alt="" className="bank-logo" />*/}
            {/*    </BankCardLogoContainer>*/}
            {/*    <BankCardInfoContainer className="bank-info">*/}
            {/*        <p>{bankInformationWithTotal.name}</p>*/}
            {/*        <p><span>{bankInformationWithTotal.currency}</span>{formatCurrency(bankInformationWithTotal.totalBalance)}</p>*/}
            {/*    </BankCardInfoContainer>*/}
            {/*</BankCardContainer>*/}

            <Grid item xs={12} md={4} >
                <Card className='bank-card-outer' sx={{justifyContent: justifyContent}}>
                    <div className="logo-container" style={{width:width, height:height}}>
                        <img src={bankInformationWithTotal.image} alt="" className="bank-logo" />
                    </div>

                    <div className="bank-info-container">
                        <p>{bankInformationWithTotal.name}</p>
                        <p><span>{bankInformationWithTotal.currency}</span>{formatCurrency(bankInformationWithTotal.totalBalance)}</p>
                    </div>
                </Card>
            </Grid>

        </>
    );
}

export default BankCard;