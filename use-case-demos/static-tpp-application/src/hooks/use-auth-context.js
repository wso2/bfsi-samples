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

import {useEffect, useRef, useState} from "react";
import {api} from "../api.js";

/*
 * useAuthContext hook handles fetching user information (name, image)
 * for the current session, providing fallback data if necessary.
 *
 * Returns:
 * @returns {object} The user information object.
 */
// TODO: This hook is temporary and will be replaced by the Asgardeo SDK for production authentication.
const useAuthContext = () => {

    const [user, setUser] = useState({});
    const fetchedRef = useRef(false);

    useEffect(() => {

        if (fetchedRef.current) {
            return;
        }
        const fetchData = async () => {

            try {
                const response = await api.get("config.json");
                setUser(response.user);
            } catch (e) {
                console.error(e.message);
            }
            fetchedRef.current = true;
        }
        fetchData()

    }, [])
    return user ? user : {
        "name": "John Smith",
        "image": "/resources/assets/images/profile/dp_image.webp"
    };
};

export default useAuthContext;

