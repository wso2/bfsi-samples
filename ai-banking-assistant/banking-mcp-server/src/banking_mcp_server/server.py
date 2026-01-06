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

import contextlib
import logging
from pathlib import Path

from starlette.applications import Starlette
from starlette.routing import Mount, Route
from mcp.server.fastmcp import FastMCP

from .config import ServerConfigs
from .endpoints import health_endpoint, get_receipt_endpoint
from .tools import (
    register_account_tools,
    register_bank_tools,
    register_payment_tools,
    register_user_tools,
)

logger = logging.getLogger(__name__)

configs: ServerConfigs = ServerConfigs()

# Create an MCP server
mcp = FastMCP(
    name="Banking MCP Server",
    host=configs.mcp_host,
    port=configs.mcp_port,
    json_response=True,
    stateless_http=True,
)
register_account_tools(mcp)
register_bank_tools(mcp)
register_payment_tools(mcp)
register_user_tools(mcp)


# Create lifespan context manager for Starlette
@contextlib.asynccontextmanager
async def lifespan(app: Starlette):
    async with mcp.session_manager.run():
        yield


# Create Starlette app with custom routes
app = Starlette(
    routes=[
        Route("/health", health_endpoint, methods=["GET"]),
        Route(
            "/transactions/{transaction_id}/receipt", get_receipt_endpoint, methods=["GET"]
        ),
        Mount("", app=mcp.streamable_http_app()),
    ],
    lifespan=lifespan,
)
