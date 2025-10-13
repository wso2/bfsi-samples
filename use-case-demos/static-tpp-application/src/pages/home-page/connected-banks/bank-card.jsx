import {formatCurrency} from "../../../hooks/utility.js";
import {
    BankCardContainer,
    BankCardInfoContainer,
    BankCardLogoContainer
} from "../../../components/styled-components/styled-containers.jsx";
import './bank-card.scss'

const BankCard = ({bankInformationWithTotal})=>{

    return (
        <>
            <BankCardContainer>
                <BankCardLogoContainer>
                    <img src={bankInformationWithTotal.image} alt="" className="bank-logo" />
                </BankCardLogoContainer>
                <BankCardInfoContainer className="bank-info">
                    <p>{bankInformationWithTotal.name}</p>
                    <p><span>{bankInformationWithTotal.currency}</span>{formatCurrency(bankInformationWithTotal.totalBalance)}</p>
                </BankCardInfoContainer>
            </BankCardContainer>

        </>
    );
}

export default BankCard;