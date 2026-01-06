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

from datetime import datetime, timezone
from pathlib import Path

from starlette.responses import JSONResponse, FileResponse
from starlette.exceptions import HTTPException


async def health_endpoint(request):
    """Health check endpoint that returns server status and current timestamp."""
    return JSONResponse(
        {
            "status": "ACTIVE",
            "service": "Banking MCP Server",
            "timestamp": datetime.now(timezone.utc).isoformat(),
        }
    )


async def get_receipt_endpoint(request):
    """Retrieve a transaction receipt PDF by transaction ID."""
    transaction_id: str = request.path_params.get("transaction_id")

    if not transaction_id:
        raise HTTPException(status_code=400, detail="transaction_id is required")

    # Construct file path
    pdf_path: Path = (
        Path("/tmp/ai-banking-agent/transactions") / f"{transaction_id}.pdf"
    )

    if not pdf_path.exists():
        raise HTTPException(
            status_code=404, detail=f"PDF for transaction {transaction_id} not found"
        )

    return FileResponse(
        pdf_path, filename=f"{transaction_id}.pdf", media_type="application/pdf"
    )
