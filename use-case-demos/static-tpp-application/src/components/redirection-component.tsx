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

// @ts-ignore
import { Box } from "@oxygen-ui/react";

export const RedirectionComponent=()=>{
    return(
        <>
            <Box sx={{
                width: "100vw",
                height: "100vh",
                display: "flex",
                flexDirection: "column",
                justifyContent: "start",
                alignItems: "center",
                position: 'fixed',
                top: 0,
                left: 0,
                backgroundColor: "var(--oxygen-palette-background-default)",
                zIndex: 9999,
                textAlign: "center",
                gap: 2
            }}>
                    <p style={{marginTop:"10rem"}}>Redirecting....</p>
            </Box>

        </>
    );
}
