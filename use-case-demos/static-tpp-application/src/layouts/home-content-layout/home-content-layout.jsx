import {ContentOuterContainer} from "../../components/styled-components/styled-containers.jsx";

const HomeContentLayout = ({children}) =>{
    return (
        <>
            <ContentOuterContainer>
                {children}
            </ContentOuterContainer>
        </>
    );
}

export default HomeContentLayout;