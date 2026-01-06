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


def register_bank_tools(mcp: FastMCP) -> None:
    """Register bank tools to the MCP server."""
    logger.info("Registering bank tools...")

    configs: ServerConfigs = ServerConfigs()
    http_client: HTTPClient = HTTPClient(configs=configs)

    @mcp.tool(
        description=(
                "Retrieves banking product information including loans, mortgages, and interest rates from the bank. "
                "Use this tool when you need to find loan products, compare interest rates, check loan eligibility criteria, or get details about banking products. "
                "Essential for answering questions about loan affordability, product features, interest rates, loan terms, and eligibility requirements. "
                "Pass 'product_id' to get specific product details, or leave blank to retrieve all available products for comparison."
        )
    )
    def get_bank_products(
            ctx: Context,
            product_id: Annotated[
                str,
                Field(
                    description=(
                            "The unique identifier of the banking product to fetch (e.g., 'loan-001', 'loan-002'). "
                            "Leave blank to retrieve all available products. Use specific product ID when you need "
                            "detailed information about a particular loan or banking product."
                    )
                ),
            ] = "",
    ) -> Annotated[
        Dict | List,
        Field(
            description=(
                    "Banking product information: "
                    "- Product details (name, type, description) "
                    "- Interest rates (fixed/variable, min/max rates, representative APR) "
                    "- Loan terms (min/max amounts, repayment periods) "
            )
        ),
    ]:
        """Fetch banking products and loan information from the Open Banking API."""
        products_url: str = configs.server_url.rstrip("/") + "/products"
        if product_id:
            products_url += f"/{product_id}"

        headers = build_request_headers(ctx, configs)

        logger.info(
            f"Fetching products from URL: {products_url} with headers: {headers}"
        )
        return http_client.get(url=products_url, headers=headers)

    logger.info("Bank tools registered successfully: get_bank_products. ")
