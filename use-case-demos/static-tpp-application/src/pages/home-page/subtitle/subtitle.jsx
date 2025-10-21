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

import './subtitle.scss'
import {Button} from "@oxygen-ui/react";

/**
 * A simple presentational component designed to render a section subtitle or heading.
 *
 * It wraps the provided **`title`** string in a `p` tag within a `div` that uses
 * the class name 'title-outer' for styling.
 *
 * @param {object} props - The component props.
 * @param {string} props.title - The text string to be displayed as the subtitle.
 * @returns {JSX.Element} The rendered subtitle component.
 */
const Subtitle = ({title, name, onAction})=>{


    const display = !(onAction && name)? "none": "flex"

    return (
        <>
            <div className="title-outer">
                <p>{title}</p>
                <Button color="primary" variant="contained" sx={{display:display}} onClick={onAction}>{name}</Button>
            </div>
        </>
    );
}

export default Subtitle;
