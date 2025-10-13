import {Grid} from "@oxygen-ui/react";
import BankCard from "./bank-card.jsx";
import {ContentInnerSectionContainer} from "../../../components/styled-components/styled-containers.jsx";

const ConnectedBanks = ({banksInformationWithTotals})=>{
    return(
        <>
            <ContentInnerSectionContainer>
                {/*<BankCard />*/}
                {/*<BankCard />*/}
                {/*<BankCard />*/}

                {banksInformationWithTotals.map((bank)=>{
                    return (
                        <BankCard key={bank.id} bankInformationWithTotal={bank} />
                    );
                })}

            </ContentInnerSectionContainer>
        </>
    );
}

export default ConnectedBanks;
