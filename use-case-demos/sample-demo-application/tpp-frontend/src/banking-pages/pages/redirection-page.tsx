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

import {useNavigate, useOutletContext} from "react-router-dom";
import {useEffect} from "react";
import type {OutletContext} from "./login-page.tsx";
import type { AppInfo } from "../../hooks/config-interfaces.ts";

interface RedirectionPageProps {
    appConfig: AppInfo
}

/**
 * @function RedirectionPage
 * @description A utility component that simulates the final bank redirection/callback step
 * by compiling transaction or account information into a structured **`operationState`**.
 * It then navigates back to the application's home page after a 1-second delay,
 * passing the state necessary to update the global configuration and display feedback.
 */
const RedirectionPage = ({appConfig}:RedirectionPageProps)=>{

    const { navigationData,accountsToAdd} = useOutletContext<OutletContext>();
    let data = null;
    let state = null;

    if(navigationData.current?.formData != null){
        data = {
            "id": `T${Date.now()}`,
            "date": new Date().toLocaleDateString(),
            "reference": navigationData.current?.formData.reference,
            "bank": navigationData.current?.bankInfo.name,
            "account":navigationData.current?.formData.userAccount,
            "amount": navigationData.current?.formData.amount.toString().replace(/[^\d.-]/g, ''),
            "currency":navigationData.current?.formData.currency,
            "creditDebitStatus":"c"
        }
        state = {
            "type": "payment",
            "data": data
        }
    }else if(accountsToAdd.current?.data?.length > 0 && (accountsToAdd.current.type === "single" || accountsToAdd.current?.type === "multiple")){
        data = {
            accountDetails : accountsToAdd.current.data,
            bankInfo : navigationData.current?.bankInfo.name,
        }
        state = {
            "type": accountsToAdd.current.type,
            "data": data
        }
    }
    const navigate = useNavigate();
    useEffect(() => {
        const timer = setTimeout(()=>{
            navigate(`/${appConfig.route}/home`,{
                state:{
                    operationState : state
                }
            })
        },1000);
        return () => clearTimeout(timer);
    }, []);

    return (
        <>
            <div style={{width:'100%', display:'flex', justifyContent:'center'}}>
                Redirecting...
            </div>

        </>
    )
}

export default RedirectionPage;



