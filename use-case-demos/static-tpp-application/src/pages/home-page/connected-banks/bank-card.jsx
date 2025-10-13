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
                <BankCardInfoContainer>
                    <p>{bankInformationWithTotal.name}</p>
                    <p>{bankInformationWithTotal.currency}<span>{formatCurrency(bankInformationWithTotal.totalBalance)}</span></p>
                </BankCardInfoContainer>
            </BankCardContainer>

        </>
    );
}

export default BankCard;