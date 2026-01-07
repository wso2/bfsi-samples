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

import '../components.scss'
import {Box, Button, Card} from "@oxygen-ui/react";

interface ConfirmationOverlayProps {
    onConfirm: () => void;
    onCancel?: () => void;
    title?: string;
    content?: string;
    mainButtonText?: string;
    secondaryButtonText?: string;
}

/**
 * @function OverlayConfirmationComponent
 * @description A modal component used to present a user with a mandatory confirmation or action prompt.
 * It renders a title, content message, a required primary action button (`onConfirm`),
 * and an optional secondary button (`onCancel`) for dismissal or negative actions.
 */
const OverlayConfirmationComponent: React.FC<ConfirmationOverlayProps> = ({onConfirm, onCancel, title,content, mainButtonText,secondaryButtonText}) =>{

    const shouldShowSecondaryButton = !!secondaryButtonText;

    return (
        <>
            <div className="overlay-outer">
                <Card className="overlay-inner">
                    <h4>{title}</h4>
                    <p>{content}</p>
                    <Box className="button-container">
                        <Button variant={"contained"} onClick={onConfirm}>{mainButtonText}</Button>
                        {shouldShowSecondaryButton &&
                            <Button variant={"outlined"} onClick={onCancel} >{secondaryButtonText}</Button>
                        }
                    </Box>
                </Card>
            </div>
        </>
    );
}

export default OverlayConfirmationComponent
