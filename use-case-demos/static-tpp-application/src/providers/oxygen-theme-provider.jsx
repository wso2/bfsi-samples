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
 * A theme provider component that wraps the Material UI ThemeProvider to apply
 * a dynamic theme based on the `ConfigContext`.
 * @param configs
 * @param {React.ReactNode} children - The child components to be rendered within the theme provider.
 * @returns {JSX.Element} The ThemeProvider component with a dynamically created theme.
 */
const OxygenThemeProvider = ({configs,children}) => {

    // const {configs} = useContext(ConfigContext);
    const theme = extendTheme({
        typography: {
            fontFamily: configs.theme_font,
        },
        colorSchemes: {
            light: {
                palette: {
                    primary: {
                        main: configs.theme_primary_color,
                    },
                },
            },
            dark: {
                palette: {
                    primary: {
                        main: configs.theme_secondary_color,
                    },
                },
            },
        },
    });

    return <ThemeProvider theme={theme}>{children}</ThemeProvider>;
};

export default OxygenThemeProvider;

