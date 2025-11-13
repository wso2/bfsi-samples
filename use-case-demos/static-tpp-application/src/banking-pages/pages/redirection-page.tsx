/*
 * *
 *  * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 *  *
 *  * WSO2 LLC. licenses this file to you under the Apache License,
 *  * Version 2.0 (the "License"); you may not use this file except
 *  * in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */


import {useNavigate, useOutletContext} from "react-router-dom";
import {useEffect} from "react";
import type {OutletContext} from "./login-page.tsx";
import type { AppInfo } from "../../hooks/config-interfaces.ts";

interface RedirectionPageProps {
    appConfig: AppInfo
}

const RedirectionPage = ({appConfig}:RedirectionPageProps)=>{

    const { navigationData,accountsToAdd} = useOutletContext<OutletContext>();

    let data = null;
    let id = 2345;
    let state = null;

    if(navigationData.current?.formData != null){
        id = id+1;
        data = {
            "id": "T0000"+ id,
            "date": new Date().toLocaleDateString(),
            "reference": navigationData.current?.formData.reference,
            "bank": navigationData.current?.bankInfo.name,
            "account":navigationData.current?.formData.userAccount,
            "amount": navigationData.current?.formData.amount.toString().replace(/[^\d.-]/g, ''),
            "currency":navigationData.current?.formData.currency,
        }

        state = {
            "type": "payment",
            "data": data
        }
    }else if(accountsToAdd.current?.data?.length > 0 && accountsToAdd.current?.type === "single" ){

        data={
            accountDetails : accountsToAdd.current.data,
            bankInfo : navigationData.current?.bankInfo.name,
        }

        state = {
            "type": "single",
            "data": data
        }

    }else if(accountsToAdd.current?.data.length > 0 && accountsToAdd.current?.type === "multiple"){

        data={
            accountDetails : accountsToAdd.current.data,
            bankInfo : navigationData.current?.bankInfo.name,
        }

        state = {
            "type": "multiple",
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
            Redirecting...
        </>
    )
}

export default RedirectionPage;



