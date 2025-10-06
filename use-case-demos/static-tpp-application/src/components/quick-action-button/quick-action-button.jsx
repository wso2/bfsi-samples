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

import "./quick-action-button.css"
import {Grid} from "@oxygen-ui/react";

/**
 * A reusable component designed to function as a Quick Action Button.
 * * It renders an interactive button displaying a primary icon and a text label
 * (passed as `children`). It applies a fixed style intended for quick action lists.
 * * @param {object} props - The component props.
 * @param {React.ReactNode} props.icon - The JSX or component for the button's icon.
 * @param {React.ReactNode} props.children - The text label or content displayed below the icon.
 * @param {function} props.onClick - The function to be executed when the button is clicked.
 */
export const QuickActionButton = ({children, onClick}) => {

    const [iconElement, nameText] = children;

    return (
        <Grid
            item={true}
            xs={2} sm={1.5} md={1.2} lg={1.6}
            sx={{height:{xs:"90%",sm:"100%",md:"90%",lg:"65%"},borderRadius:"10%", padding:"0.5rem"}}
            className="action-button">
            <div style={{ width: '100%', height: '100%',
                cursor: 'pointer', border: 'none', background: 'transparent' }} >
                <Grid
                    container direction="column" alignItems="center" justifyContent="center"
                    sx={{width:"100%", height:"100%"}} spacing={0}>
                    <Grid item={true} lg={12} md={12} sm={12} xs={12}
                          sx={{height:"75%", display: 'flex', alignItems: 'center',
                              justifyContent: 'center', color: "secondary.main"}}>
                        {iconElement}
                    </Grid>
                    <Grid
                        item={true} md={12} lg={12} sm={12} xs={12}
                        sx={{height:"25%",color: "secondary.main", display:"flex",
                            justifyContent:"center",alignItems:"center"}}>
                        {nameText}
                    </Grid>
                </Grid>
            </div>
        </Grid>
    );
};

