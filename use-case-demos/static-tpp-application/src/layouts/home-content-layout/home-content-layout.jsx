import {ContentOuterContainer} from "../../components/styled-components/styled-containers.jsx";
import {Box} from "@oxygen-ui/react";
import './home-content-layout.scss'

const HomeContentLayout = ({children}) =>{
    return (
        <>
            <Box className='home-content-outer'>
                {children}
            </Box>
        </>
    );
}

export default HomeContentLayout;