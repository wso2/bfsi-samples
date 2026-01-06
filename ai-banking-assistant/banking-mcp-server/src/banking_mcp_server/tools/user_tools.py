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


def register_user_tools(mcp: FastMCP) -> None:
    """Register user-related tools to the MCP server."""
    logger.info("Registering user tools...")

    configs: ServerConfigs = ServerConfigs()
    http_client: HTTPClient = HTTPClient(configs=configs)

    @mcp.tool(
        description=(
                "Gets the current authenticated user's complete profile and personal information. "
                "Use this tool when you need the user's name, contact details, financial status, "
                "employment information, or banking preferences to personalize responses or provide tailored advice. "
                "DO NOT use this tool for account balances, transactions, or for general banking product information."
        )
    )
    def get_user_profile(
            ctx: Context,
    ) -> Annotated[
        Dict,
        Field(
            description=(
                    "Complete user profile information including: "
                    "- Personal details (name, date of birth, contact information) "
                    "- Employment and financial status (income, credit score, assets) "
                    "- Banking relationship details (customer segment, preferences) "
                    "- Account summary (total balances, available credit) "
                    "- Verification and compliance status "
                    "- Communication preferences and settings"
            )
        ),
    ]:
        """Fetch user profile information from the /me endpoint."""
        user_url: str = configs.server_url.rstrip("/") + "/me"

        headers = build_request_headers(ctx, configs)

        logger.info(f"Fetching user profile from URL: {user_url} with headers: {headers}")
        return http_client.get(url=user_url, headers=headers)

    @mcp.tool(
        description=(
                "Retrieves account information of user's payees. Use this tool when you need to find a contact's "
                "account details for making payments, transfers, or bill payments. "
        )
    )
    async def get_user_payees(
            ctx: Context,
    ) -> Annotated[
        Dict | List,
        Field(
            description=(
                    "Contact account information of the user's payees including account numbers, bank details, and payee names."
            )
        ),
    ]:
        """Fetch contact accounts from the API."""

        # Fetch all contact accounts from the API
        contact_accounts_url: str = configs.server_url.rstrip("/") + "/payees"
        headers = build_request_headers(ctx, configs)

        logger.info(f"Fetching payees from URL: {contact_accounts_url}")
        return http_client.get(url=contact_accounts_url, headers=headers)

    logger.info("User tools registered successfully: get_user_profile, get_user_payees. ")
