/**
 * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {Box, Grid, IconButton} from "@oxygen-ui/react";
import { useNavigate } from "react-router-dom";
// @ts-ignore
import {ChevronLeftIcon} from "@oxygen-ui/react-icons";

interface PageLayoutProps {
    children?: React.ReactNode;
    title: string;
}

/**
 * @function PaymentAccountPageLayout
 * @description A layout component that wraps content for pages like payments and account management.
 * It provides a standardized header with a dynamic title, a back button for navigation,
 * and uses a responsive Grid system to size the content area based on the page title.
 */
const PaymentAccountPageLayout = ({children,title}:PageLayoutProps)=>{
    const navigate = useNavigate();

    const handleBackNavigation = ()=>{
        navigate(-1);
    }

    const pagewidth = title === "Transactions" || title ==="Standing Orders" ? [12,12,12] : [12,6,4];
    return (
        <>
            <Box className='title-and-back-container'>
                <IconButton onClick={handleBackNavigation}>
                    <ChevronLeftIcon size={'24'}/>
                </IconButton>
                <h3>{title}</h3>

            </Box>
            <Grid container className={'payments-layout'}>
                <Grid xs={pagewidth[0]} sm={pagewidth[1]} md={pagewidth[2]}>
                    {children}
                </Grid>
            </Grid>
        </>
    );
}

export default PaymentAccountPageLayout;
