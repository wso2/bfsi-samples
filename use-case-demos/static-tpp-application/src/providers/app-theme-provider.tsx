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

import {ThemeProvider, extendTheme} from '@oxygen-ui/react';
import type {CustomColors} from "../hooks/config-interfaces.ts";


/**
 * A root-level React Provider component designed to apply the custom WSO2 Oxygen UI theme
 * to an application.
 *
 * It uses Oxygen UI's `extendTheme` utility to:
 * 1. Set the global typography font family to 'Inter'.
 * 2. Define distinct 'light' and 'dark' color schemes with specific palette customizations,
 * such as the primary main color and various background and font colors for the 'light' mode.
 *
 * This theme is then provided to the component tree via the Oxygen UI's `ThemeProvider`,
 * allowing all descendant components to access the custom design tokens.
 */

interface ApplicationThemeProviderProps {
    children?: React.ReactNode;
    color?: CustomColors[];
}

const AppThemeProvider = ({children,color}:ApplicationThemeProviderProps) => {
    const customColors = (color || []).reduce((acc, currentObject) => {
        return { ...acc, ...currentObject };
    }, {});
    const theme = extendTheme({
        typography: {
            fontFamily: 'Inter',
        },
        colorSchemes: {
            light: {
                palette: {
                    primary: {
                        main: customColors.primary,
                        secondaryColor:customColors.secondaryColor,
                        button: customColors.button,
                        backgroundColor: customColors.backgroundColor,
                        tableBackground: customColors.tableBackground,
                        innerButtonBackground: customColors.innerButtonBackground,
                        bankColor1:customColors.bankColor1,
                        bankColor2:customColors.bankColor2,
                        bankColor3:customColors.bankColor3,
                        bankBackground:customColors.bankBackground,
                        formValidationError: customColors.formValidationError,
                        tableHeaderBackground: customColors.tableHeaderBackground,
                        tableHeaderFontColor: customColors.tableHeaderFontColor,
                        tableBodyColor: customColors.tableBackgroundColor,
                        greenArrowColor: customColors.greenArrowColor,
                        redArrowColor: customColors.redArrowColor,
                    },
                    fontColor: {
                        white: customColors.fontWhite,
                    },
                },
            },
            dark: {
                palette: {
                    primary: {
                        main: '#FF5456',
                    },
                },
            },
        },
    });

    return <ThemeProvider theme={theme}>{children}</ThemeProvider>;
};

export default AppThemeProvider;
