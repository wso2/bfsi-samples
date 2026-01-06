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
from typing import Dict, List

from mcp.server import FastMCP
from mcp.server.fastmcp import Context
from pydantic import Field
from typing_extensions import Annotated

from ..client import HTTPClient
from ..config import ServerConfigs
from ..utils import build_request_headers

logger = logging.getLogger(__name__)


def register_account_tools(mcp: FastMCP) -> None:
    """Register account-related tools to the MCP server."""
    logger.info("Registering accounts tools...")

    configs: ServerConfigs = ServerConfigs()
    http_client: HTTPClient = HTTPClient(configs=configs)

    @mcp.tool(
        description=(
                "Retrieves account information for a user from their bank via the Open Banking API. "
                "Call this tool to retrieve details for a specific account by its logical ID, or all accounts if no ID is provided. "
                "Use this tool when you need account balances, account numbers, or other account metadata of the user. "
                "Pass the 'account_id' parameter to fetch a specific account, or leave it blank to fetch all accounts. "
                "You can also apply optional filters to search for accounts matching specific criteria. "
                "Supported filter operations: 'eq' (equals), 'ne' (not equals), 'contains', 'startswith', 'endswith', "
                "'gt' (greater than), 'gte' (>=), 'lt' (<), 'lte' (<=)."
        )
    )
    async def get_user_accounts(
            ctx: Context,
            account_id: Annotated[
                str,
                Field(
                    description="The logical ID of the account to fetch. If blank, all accounts will be returned."
                ),
            ] = "",
            sub_resource: Annotated[
                str,
                Field(
                    description="Specifies the type of account sub-resource to retrieve for the given account. Currently supports 'transactions'."
                ),
            ] = "",
    ) -> Annotated[
        Dict | List,
        Field(
            description=(
                    "A dictionary containing account information as returned by the Open Banking API. "
                    "Includes account balances, account numbers, and other metadata. "
                    "If 'account_id' is provided, returns details for that account; otherwise, returns all accounts. "
                    "Results are filtered by the provided filter conditions if any."
            )
        ),
    ]:
        """Fetch accounts from the Open Banking API."""
        accounts_url: str = configs.server_url.rstrip("/") + "/accounts"
        if account_id:
            accounts_url += f"/{account_id}"

        if sub_resource:
            accounts_url += f"/{sub_resource}"

        headers = build_request_headers(ctx, configs)

        logger.info(f"Fetching accounts from URL: {accounts_url} with headers: {headers}")
        return http_client.get(url=accounts_url, headers=headers)

    logger.info("Account tools registered successfully. ")
