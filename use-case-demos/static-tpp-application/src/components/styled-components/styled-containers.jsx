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

import styled from "@emotion/styled";
import {Box, Grid} from "@oxygen-ui/react";

/**
 * A styled component derived from the Oxygen UI `Grid` component, serving as the
 * **outer container for the Hero Section**.
 *
 * It applies a **fixed background image** with specific positioning and repetition.
 * The layout is primarily designed to be a **column** (stacking elements vertically)
 * on small/medium screens (`< lg`), and transitions to a **row** (side-by-side elements)
 * on large screens (`>= lg`), maintaining a consistent height (12rem to 14rem).
 *
 * @param {object} props - Styled component props automatically injected by Emotion/Styled-components.
 * @param {object} props.theme - The active Oxygen UI theme object.
 */
export const HeroOuterContainer = styled(Grid)(({ theme }) => ({
    background: 'url(/resources/assets/images/background/bg_image.webp) lightgray -18.644px -372.574px / 145.345% 527.151% no-repeat',
    padding: theme.spacing(3),
    display: 'flex',
    justifyContent: 'center',
    height: '12rem',
    flexDirection: 'column',

    [theme.breakpoints.down('lg')]: {
        height: '14rem',
    },
    [theme.breakpoints.up('lg')]: {
        flexDirection: 'row',
    },
}))

/**
 * A styled component derived from the Oxygen UI `Grid` component, designed to be used
 * as a flexible **inner container within the Hero Section**.
 *
 * It provides customizable layout control via props to define the dimensions and alignment:
 * - **`boxWidth`**: Sets the width of the container (e.g., '50%', '100%').
 * - **`justify`**: Controls the horizontal alignment using the CSS `justify-content` value (e.g., 'start', 'center', 'space-between').
 *
 * It maintains a full height (`100%`) relative to its parent and uses a standard flex layout
 * to vertically center content and apply a fixed gap between elements.
 *
 * @param {object} props - Styled component props automatically passed by Emotion/Styled-components.
 * @param {string} props.boxWidth - The width to apply to the container.
 * @param {string} props.justify - The CSS `justify-content` value for horizontal alignment.
 */
export const HeroInnerContainer = styled(Grid)(({ boxWidth , justify }) => ({
    height: '100%',
    width: boxWidth,
    display: "flex",
    alignItems: "center",
    justifyContent: justify,
    gap: "1rem",
}))

/**
 * A styled component derived from the Oxygen UI `Box` component, specifically designed
 * to wrap and format the content (icon and label) inside a Quick Action Button.
 *
 * It establishes a **column layout** to stack the icon and text vertically, with a fixed gap.
 * It applies a distinct visual style including a **white background**, **red text**,
 * **rounded corners**, and a **drop shadow**.
 *
 * This component implements responsiveness:
 * - The horizontal padding increases on small screens (`>= sm`).
 * - The base width is adjusted for medium (`>= md`) and then large (`>= lg`) screens.
 *
 * @param {object} props - Styled component props automatically injected by Emotion/Styled-components.
 * @param {object} props.theme - The active Oxygen UI theme object, used for spacing and breakpoints.
 */
export const ActionButtonContentOuter = styled(Box)(({theme}) => ({
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    width: '3rem',
    gap: '0.5rem',
    background: 'white',
    color: 'red',
    borderRadius: '5%',
    boxShadow: '4.5px 4.5px 5.625px -2.25px var(--oxygen-palette-shadows-main)',
    padding: '0.8rem 2rem',
    fontSize: '0.8rem',

    [theme.breakpoints.up('md')]: {
        width: '2rem',
    },
    [theme.breakpoints.up('lg')]: {
        width: '3rem',
    },
    [theme.breakpoints.up('sm')]: {
        padding: '1rem 2.5rem',
    },
}))

/**
 * A styled component derived from the Oxygen UI `Box` component, specifically designed
 * to serve as the **outer container for a user's profile image**.
 *
 * It enforces a **circular shape** (`borderRadius: '50%'`) and a fixed size of `6rem`
 * for both width and height. It uses flex properties for centering or layout control
 * within itself.
 *
 * This component is designed to be **hidden on small screens** (`< sm`)
 * via the `display: 'none'` breakpoint rule, optimizing for mobile viewports.
 *
 * @param {object} props - Styled component props automatically injected by Emotion/Styled-components.
 * @param {object} props.theme - The active Oxygen UI theme object, used for breakpoint definition.
 */
export const ProfileImageOuter = styled(Box)(({theme}) => ({
    width: '6rem',
    height: '6rem',
    borderRadius: '50%',
    marginLeft: '1rem',
    display: 'flex',

    [theme.breakpoints.down('sm')]: {
        display: 'none',
    }
}))

/**
 * A styled component derived from the Oxygen UI `Box` component, designed
 * to contain and style the **user's name and personalized greeting text**.
 *
 * It applies the primary font color (`var(--oxygen-palette-fontColor-main)`)
 * and sets a default font size of `1.5rem`.
 *
 * This component is **responsive**, reducing the font size to `1rem` on small screens (`< sm`)
 * to ensure readability on mobile devices.
 *
 * @param {object} props - Styled component props automatically injected by Emotion/Styled-components.
 * @param {object} props.theme - The active Oxygen UI theme object, used for breakpoint definition.
 */
export const UserInformationContainer = styled(Box)(({theme}) => ({
    color: 'var(--oxygen-palette-fontColor-main)',
    fontSize: '1.5rem',

    [theme.breakpoints.down('sm')]: {
        fontSize: '1rem',
    }
}))
