# Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com/) All Rights Reserved.

# WSO2 LLC. licenses this file to you under the Apache License,
# Version 2.0 (the "License"); you may not use this file except
# in compliance with the License.
# You may obtain a copy of the License at

# http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.

import logging
from typing import Any, Optional

import requests
from requests import Response

from .config import ServerConfigs

logger = logging.getLogger(__name__)


class HTTPClient:

    def __init__(self, configs: ServerConfigs):
        self._configs = configs

    def get(
            self,
            url: str,
            *,
            headers: Optional[dict[str, str]] = None,
            params: Optional[dict[str, Any]] = None,
            timeout: float = 10.0,
    ) -> Any:
        """Perform an HTTP GET and return the parsed JSON payload.

        Args:
            url: The URL to fetch.
            headers: Optional additional headers to include in the request.
            params: Optional query parameters for the request.
            timeout: Socket timeout in seconds.

        Returns:
            Parsed JSON (could be dict, list, etc.).
        """
        # Base headers, can be overridden or extended by caller
        base_headers: dict[str, str] = {
            "Accept": "application/json",
        }
        if headers:
            base_headers.update(headers)

        request_kwargs: dict[str, Any] = {
            "headers": base_headers,
            "params": params,
            "timeout": timeout,
        }

        logger.debug(f"HTTP GET Request: url={url}, headers={base_headers}, params={params}, timeout={timeout}")
        try:
            response: Response = requests.get(url, **request_kwargs)
            logger.debug(f"HTTP GET Response: status_code={response.status_code}, content={response.text}")
            response.raise_for_status()  # Raises HTTPError for bad responses (4xx, 5xx)
            result: dict[str, Any] = response.json()
            data = result.get("data") or result.get("Data")
            if data is not None:
                return data
            else:
                logger.debug("No 'data' or 'Data' key found in response JSON.")
                return result
        except requests.RequestException as e:
            logger.error(f"HTTP GET request to {url} failed. Caused by: {e}")
            return {"error": str(e)}

    def post(
            self,
            url: str,
            *,
            json: Optional[dict[str, Any]] = None,
            headers: Optional[dict[str, str]] = None,
            timeout: float = 10.0,
    ) -> Any:
        """Perform an HTTP POST and return the parsed JSON payload.

        Args:
            url: The URL to post to.
            json: Optional JSON data to include in the request body.
            headers: Optional additional headers to include in the request.
            timeout: Socket timeout in seconds.

        Returns:
            Parsed JSON (could be dict, list, etc.).
        """
        # Base headers, can be overridden or extended by caller
        base_headers: dict[str, str] = {
            "Accept": "application/json",
            "Content-Type": "application/json",
        }
        if headers:
            base_headers.update(headers)

        request_kwargs: dict[str, Any] = {
            "headers": base_headers,
            "json": json,
            "timeout": timeout,
        }

        logger.debug(f"HTTP POST Request: url={url}, headers={base_headers}, json={json}, timeout={timeout}")
        try:
            response: Response = requests.post(url, **request_kwargs)
            logger.debug(f"HTTP POST Response: status_code={response.status_code}, content={response.text}")
            response.raise_for_status()  # Raises HTTPError for bad responses (4xx, 5xx)
            result: dict[str, Any] = response.json()
            data = result.get("data") or result.get("Data")
            if data is not None:
                return data
            else:
                logger.debug("No 'data' or 'Data' key found in response JSON.")
                return result
        except requests.RequestException as e:
            logger.error(f"HTTP POST request to {url} failed. Caused by: {e}")
            return {"error": str(e)}
