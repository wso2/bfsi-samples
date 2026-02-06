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

import {Box, Button} from "@oxygen-ui/react";

interface TitleProps {
    title: string;
    buttonName?: string;
    buttonType?: "contained"|"outlined";
    onPress?: (buttonName:string,title?:string) => void;
}

/**
 * @function CustomTitle
 * @description A reusable header component that displays a section `title`.
 * It optionally renders a dynamic `Button` (either contained or outlined)
 * which triggers an `onPress` callback, passing both the button's name and the title.
 */
const CustomTitle = ({title,buttonName,buttonType, onPress}:TitleProps)=>{

    const visibility = buttonName? "flex" : "none";

    let onboardingClass;

    if (buttonName === "Add Account"){
        onboardingClass = "add-account-btn"
    }else if(buttonName === "View More" && title === "Latest Transactions"){
        onboardingClass = "view-all-transactions"
    }else if(buttonName === "View More" && title === "Standing Orders"){
        onboardingClass = "view-all-standing-orders"
    }else{
        onboardingClass = ""
    }

    return(
        <>
            <Box className={"title-container"}>
                <p>{title}</p>
                <Button className={`${onboardingClass}`} sx={{display:visibility}} variant={buttonType} onClick={()=>{onPress?.(buttonName||'',title)}}>{buttonName}</Button>
            </Box>
        </>
    );
}

export default CustomTitle;
