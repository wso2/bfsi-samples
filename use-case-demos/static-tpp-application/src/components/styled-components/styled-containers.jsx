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
import {Box, Card, Grid} from "@oxygen-ui/react";

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
export const HeroOuterContainer = styled(Grid)(({ theme, background }) => ({
    background: `url(${background}) lightgray -18.644px -372.574px / 145.345% 527.151% no-repeat`,
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
export const HeroInnerContainer = styled(Grid)(({ theme,boxWidth , justify }) => ({
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
    background: 'var(--oxygen-palette-backgroundColor-main)',
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

/**
 * A styled component derived from the Oxygen UI `Grid` component, designed to serve
 * as a general-purpose **outer container for main application content**.
 *
 * It establishes a **column layout** and applies generous default padding (`2rem 4rem`).
 *
 * This component is **responsive**, reducing the padding to `1rem 2rem` on small screens (`< sm`)
 * to conserve space on mobile viewports.
 *
 * @param {object} props - Styled component props automatically injected by Emotion/Styled-components.
 * @param {object} props.theme - The active Oxygen UI theme object, used for breakpoint definition.
 */
export const ContentOuterContainer = styled(Grid)(({theme}) => ({
    padding: '2rem 4rem',
    display: 'flex',
    flexDirection: 'column',

    [theme.breakpoints.down('sm')]: {
        padding: '1rem 2rem',
    }
}))

/**
 * A styled component derived from the Oxygen UI `Grid` component, designed
 * to structure a **section of content that requires flexible horizontal/vertical grouping**.
 *
 * It defaults to a **row layout** (elements side-by-side) with a fixed gap (`1rem`).
 *
 * This component is **responsive**, switching to a **column layout** (stacking elements vertically)
 * on medium screens and below (`< md`), making it ideal for adapting two-column layouts
 * for tablets and mobile devices.
 *
 * @param {object} props - Styled component props automatically injected by Emotion/Styled-components.
 * @param {object} props.theme - The active Oxygen UI theme object, used for breakpoint definition.
 */
export const ContentInnerSectionContainer = styled(Grid)(({theme}) => ({
    display: 'flex',
    flexDirection: 'row',
    alignItems: "center",
    gap: '1rem',

    [theme.breakpoints.down('md')]: {
        flexDirection: 'column',
    }
}))

/**
 * A styled component derived from the Oxygen UI `Card` component, designed
 * to act as an **inner container for displaying individual infographic elements**
 * (like total balance or a chart).
 *
 * It applies a **fixed height of `20rem`** on large screens and uses a **column flex layout**
 * to center its content. It features a visible **dark box shadow**.
 *
 * This component is **responsive**, switching its height to **`fit-content`** on medium screens and below (`< md`),
 * allowing the content to determine the final height on smaller viewports.
 *
 * @param {object} props - Styled component props automatically injected by Emotion/Styled-components.
 * @param {object} props.theme - The active Oxygen UI theme object, used for breakpoint definition.
 */
export const InfographicsInnerContainer = styled(Card)(({theme}) => ({
    width: '100%',
    height: '20rem',
    display: 'flex',
    alignItems: "center",
    justifyContent: "center",
    flexDirection: 'column',
    boxShadow:'4.5px 4.5px 5.625px -2.25px var(--oxygen-palette-shadows-dark)',


    [theme.breakpoints.down('md')]: {
        height: 'fit-content',
    }
}))

export const BankCardContainer = styled(Card)(({theme}) => ({
    width: '100%',

    padding: '1rem',
    height: '16rem',
    display: 'flex',
    alignItems: "center",
    justifyContent: "center",
    boxShadow: '4.5px 4.5px 5.625px -2.25px var(--oxygen-palette-shadows-dark)',

    [theme.breakpoints.down('md')]: {
        flex: 1,
    },

    [theme.breakpoints.down('lg')]: {
        flex: 1,

    },

    [theme.breakpoints.down('xl')]: {
        flex: 1,

    },
}))

export const BankCardLogoContainer = styled(Box)(({ theme }) => ({
    width: '12rem',
    height: '12rem',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: theme.spacing(2),
    flexShrink: 0,
    boxShadow: theme.shadows[3],
    backgroundColor: theme.palette.grey[100],


    [theme.breakpoints.down('xl')]: {
        width: '6rem',
        height: '6rem',
    },
    [theme.breakpoints.down('md')]: {
        width: '6rem',
        height: '6rem',
    },
    [theme.breakpoints.down('sm')]: {
        width: '8rem',
        height: '8rem',
        borderRadius: theme.spacing(1),
    }
}));

export const BankCardInfoContainer = styled(Box)(({ theme }) => ({

    width: '100%',
    flex: 1,
    marginLeft: theme.spacing(3),
    minWidth: 0,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-between',
    padding: theme.spacing(1),
    fontSize: '2rem',

    [theme.breakpoints.down('md')]: {
        marginLeft: theme.spacing(2),
        fontSize: '1rem',
    },
}));
