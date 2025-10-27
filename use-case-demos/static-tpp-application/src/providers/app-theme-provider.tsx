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

/**
 * A root theme provider component that wraps the Material UI ThemeProvider from `@oxygen-ui/react`
 * to apply a custom, statically defined application theme.
 *
 * It uses `extendTheme` to configure:
 * - **Typography**: Sets the global font family to 'Inter'.
 * - **Light Color Scheme**: Defines an extended palette including primary, secondary (with a specific 'yellow' key),
 * custom `fontColor`, `shadows` color, and `backgroundColor` main values.
 * - **Dark Color Scheme**: Defines a minimal dark palette with a specific primary color.
 *
 * All child components are rendered within this theme context, allowing them to utilize
 * the custom color and typography definitions.
 *
 * @param {object} props - The component props.
 * @param {React.ReactNode} props.children - The child components to be rendered within the theme provider's scope.
 * @returns {JSX.Element} The ThemeProvider component with the defined theme applied.
 */

interface ApplicationThemeProviderProps {
    children?: React.ReactNode;
}

const AppThemeProvider = ({children}:ApplicationThemeProviderProps) => {

    const theme = extendTheme({
        typography: {
            fontFamily: 'Inter',
        },
        colorSchemes: {
            light: {
                palette: {
                    primary: {
                        main: '#FF5100',
                        button: '#FFFFFF',
                        backgroundColor: '#FFF5EE',
                        tableBackground: '#FFFFFF',
                    },
                    fontColor: {
                        white: '#FFFFFF',
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
