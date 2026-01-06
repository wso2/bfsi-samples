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
import uuid
from datetime import datetime
from io import BytesIO
from typing import Optional, Dict

from mcp.server import FastMCP
from mcp.server.fastmcp import Context
from pydantic import BaseModel, Field
from reportlab.lib import colors
from reportlab.lib.pagesizes import letter
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import inch
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle, Paragraph, Spacer
from typing_extensions import Annotated

from ..client import HTTPClient
from ..config import ServerConfigs
from ..utils import save, build_request_headers

logger = logging.getLogger(__name__)


class Party(BaseModel):
    """Represents sender or beneficiary details."""

    account_id: str = Field(default="", description="Account ID of the party")
    name: str = Field(default="", description="Name of the party")
    bank_name: str = Field(default="", description="Name of the bank where the party holds the account")


class PaymentRequest(BaseModel):
    """Represents initial payment request details."""

    currency: str = Field(description="Currency of the payment amount (e.g., USD, EUR)")
    amount: float = Field(description="Payment amount")
    sender: Party = Field(description="Sender party details")
    beneficiary: Party = Field(description="Beneficiary party details")
    remarks: str = Field(description="Payment remarks or reference")


class ConsentRequest(BaseModel):
    """Represents user's consent for a payment."""

    transaction_id: str = Field(
        description="Transaction identifier from payment_initiate tool"
    )
    consent: bool = Field(
        default=None, description="User's consent to authorize this payment."
    )


class OTPRequest(BaseModel):
    """Represents OTP verification request."""

    transaction_id: str = Field(
        description="Transaction identifier from payment_authorize tool"
    )
    otp: str = Field(description="OTP provided by user")


class PaymentContext(PaymentRequest):
    """Represents the payment context stored in cache."""

    transaction_id: Optional[str] = None
    otp: Optional[str] = None


# Payment cache storage
payment_cache: Dict[str, PaymentContext] = {}


def register_payment_tools(mcp: FastMCP) -> None:
    """Register payment-related tools to the MCP server."""
    logger.info("Registering payment tools...")

    configs: ServerConfigs = ServerConfigs()
    http_client: HTTPClient = HTTPClient(configs=configs)

    @mcp.tool(
        description=(
            "Initiate a payment transaction by providing payment details. "
            "This is the second step of four steps in processing a payment. "
            "Only call this tool after selecting accounts with sufficient balance. "
            "The response includes the transaction_id needed for the payment_authorize tool."
        )
    )
    async def payment_initiate(
        request: Annotated[
            PaymentRequest,
            Field(
                description="Complete payment transaction details including currency, amount, "
                "sender account details (account_id, name), beneficiary account details "
                "(account_id, name, bank_name), and remarks for the transaction."
            ),
        ],
    ) -> Annotated[
        str,
        Field(description="Instructions with next steps and transaction details"),
    ]:
        """Initiate a payment transaction and return transaction ID for authorization."""

        transaction_id: str = str(uuid.uuid4())
        logger.info(f"[TID: {transaction_id}] Payment initiated with details.")

        # Store payment context in cache with transaction ID as key for payment_authorize tool
        request.sender.bank_name = "Finthesis Bank"
        payment_cache[transaction_id] = PaymentContext(
            **request.model_dump(), transaction_id=transaction_id
        )
        logger.info(
            f"[TID: {transaction_id}] Payment context stored. Ready for authorization."
        )

        return (
            f"Payment initiated successfully. Transaction ID: {transaction_id}\n"
            f"From: name={request.sender.name} account ID={request.sender.account_id}\n"
            f"To: name={request.beneficiary.name}, bank={request.beneficiary.bank_name}, "
            f"account ID={request.beneficiary.account_id}\n"
            f"Amount: {request.amount} {request.currency}\n"
            f"Remarks: {request.remarks}\n\n"
            f"NEXT STEP: Show user ALL transaction details above. Request user confirmation (yes/no) to proceed with payment authorization.\n"
            f"Then call payment_authorize tool with transaction ID and consent=true if confirmed or consent=false if declined"
        )

    @mcp.tool(
        description=(
            "Authorize a payment transaction and generate an OTP. "
            "This is the third step of four steps in processing a payment. "
            "Use the transaction_id from payment_initiate and the user's consent decision (true to authorize, "
            "false to decline). When user authorized, generates an OTP required for final payment confirmation."
        )
    )
    async def payment_authorize(
        request: Annotated[
            ConsentRequest,
            Field(
                description="Transaction authorization request containing the transaction_id from payment_initiate "
                "and the user's consent decision (boolean: true to authorize payment, false to decline)"
            ),
        ],
    ) -> Annotated[
        str,
        Field(description="Message confirming the payment authorization outcome. "),
    ]:
        """Authorize payment and generate OTP if consented."""
        logger.info(
            f"[TID: {request.transaction_id}] Authorization request received. Consent: {request.consent}"
        )

        # Validate transaction
        error_message, payment_context = _validate_transaction_id(
            request.transaction_id
        )
        if error_message:
            return error_message

        # At this point, payment_context is guaranteed to be non-None
        assert payment_context is not None

        if request.consent is None:
            logger.info(
                f"[TID: {request.transaction_id}] User has not yet provided authorization."
            )
            response: str = (
                f"Payment authorization pending. Transaction ID {request.transaction_id} is waiting for user consent. "
                f"Please ask the user to confirm or decline the payment."
            )
        elif request.consent is False:
            logger.info(
                f"[TID: {request.transaction_id}] User has declined the payment authorization."
            )
            response: str = (
                f"Payment declined by user. Transaction ID {request.transaction_id} has been cancelled. "
                f"No funds have been transferred."
            )
        else:
            logger.info(
                f"[TID: {request.transaction_id}] User has authorized the payment. Processing transaction."
            )
            # Generate OTP using current month and date (DDMM format)
            otp: str = datetime.now().strftime("%d%m")

            logger.info(
                f"[TID: {request.transaction_id}] OTP generated for payment verification: {otp}"
            )

            # Store OTP in payment context
            payment_context.otp = otp
            payment_cache[request.transaction_id] = payment_context

            response: str = (
                f"Payment authorized! An OTP sent to the user's email and phone for two-factor verification.\n"
                f"Transaction ID: {request.transaction_id}\n"
                f"To complete the payment, ask the user for the OTP and use the payment_otp_verify tool next. "
            )

        logger.info(f"[TID: {request.transaction_id}] Authorization request processed.")
        return response

    @mcp.tool(
        description=(
            "Verify the OTP provided by the user to complete the payment. "
            "This is the final step of four steps in processing a payment. "
            "Use this tool after the user provides the OTP from payment_authorize tool. "
        )
    )
    async def payment_otp_verify(
        ctx: Context,
        request: Annotated[
            OTPRequest,
            Field(
                description="OTP verification request containing the transaction_id from payment_authorize "
                "and the OTP provided by the user."
            ),
        ],
    ) -> Annotated[
        str,
        Field(description="Transaction receipt with PDF or error message"),
    ]:
        """Confirm payment by verifying OTP and complete the transaction."""
        logger.info(
            f"[TID: {request.transaction_id}] OTP verification request received. OTP: {request.otp}"
        )

        # Validate transaction
        error_message, payment_context = _validate_transaction_id(
            request.transaction_id
        )
        if error_message:
            return error_message

        # At this point, payment_context is guaranteed to be non-None
        assert payment_context is not None

        # Verify OTP
        if request.otp != payment_context.otp:
            logger.info(
                f"[TID: {request.transaction_id}] OTP verification failed. "
                f"Expected: {payment_context.otp}, Got: {request.otp}"
            )
            return (
                f"OTP verification failed. The OTP you entered is incorrect. "
                f"Please ask the user for the correct OTP and try again."
            )
        else:
            logger.info(
                f"[TID: {request.transaction_id}] OTP verified successfully. Payment completed."
            )

            transactions_url: str = (
                f"{configs.server_url.rstrip('/')}/accounts/{payment_context.sender.account_id}/transactions"
            )

            headers: dict = build_request_headers(ctx, configs)

            logger.info(
                f"[TID: {request.transaction_id}] Recording transaction at URL: {transactions_url} with headers: {headers}"
            )
            http_client.post(
                url=transactions_url, json=payment_context.model_dump(), headers=headers
            )

            # Generate PDF receipt and persist to disk
            try:
                logger.info(
                    f"[TID: {request.transaction_id}] Receipt PDF generation started."
                )
                pdf_buffer: BytesIO = await _generate_receipt_pdf(payment_context)
                receipt_file_path: str = await save(payment_context.transaction_id, pdf_buffer)
                logger.info(
                    f"[TID: {request.transaction_id}] Receipt PDF persisted to: {receipt_file_path}"
                )
            except Exception as e:
                logger.error(
                    f"[TID: {request.transaction_id}] Failed to generate/persist PDF: {str(e)}"
                )

            return (
                f"Payment completed successfully!\n"
                f"From: {payment_context.sender.name} ({payment_context.sender.account_id})\n"
                f"To: {payment_context.beneficiary.name} ({payment_context.beneficiary.account_id}) - {payment_context.beneficiary.bank_name}\n"
                f"Amount: {payment_context.amount} {payment_context.currency}\n\n"
                f"Transaction receipt is ready to download. Ask the user to click and download it as an attachment:\n"
                f"{configs.mcp_server_url.rstrip('/')}/transactions/{payment_context.transaction_id}/receipt"
            )

    logger.info(
        "Payment tools registered successfully: payment_initiate, payment_authorize, payment_otp_verify."
    )


async def _generate_receipt_pdf(payment_context: PaymentContext) -> BytesIO:
    """
    Generate a transaction receipt PDF in memory.

    Args:
        payment_context: Payment context with transaction details

    Returns:
        BytesIO buffer containing the PDF data
    """
    buffer: BytesIO = BytesIO()
    doc: SimpleDocTemplate = SimpleDocTemplate(
        buffer, 
        pagesize=letter, 
        topMargin=0.5 * inch, 
        bottomMargin=0.5 * inch,
        title=f"Transaction Receipt - {payment_context.transaction_id}",
        author="Banking MCP Server",
        subject=f"Transaction Receipt for {payment_context.transaction_id}",
        creator="AI Banking Assistant",
        producer="ReportLab"
    )

    # Styles
    styles: getSampleStyleSheet = getSampleStyleSheet()
    title_style: ParagraphStyle = ParagraphStyle(
        "CustomTitle",
        parent=styles["Heading1"],
        fontSize=18,
        textColor=colors.HexColor("#1f4788"),
        spaceAfter=12,
        alignment=1,  # Center
    )

    footer_style: ParagraphStyle = ParagraphStyle(
        "CustomFooter",
        parent=styles["Normal"],
        fontSize=9,
        textColor=colors.HexColor("#cccccc"),  # Light grey
        alignment=1,  # Center
    )

    # Content
    elements: list = [
        Paragraph("TRANSACTION RECEIPT", title_style),
        Spacer(1, 0.2 * inch),
    ]

    # Transaction details table
    data: list = [
        ["Transaction ID:", payment_context.transaction_id],
        ["Date & Time:", datetime.now().strftime("%Y-%m-%d %H:%M:%S")],
        ["Status:", "Completed"],
        ["", ""],
        ["From:", payment_context.sender.name],
        ["From Bank:", payment_context.sender.bank_name],
        ["From Account:", payment_context.sender.account_id],
        ["", ""],
        ["To:", payment_context.beneficiary.name],
        ["To Bank:", payment_context.beneficiary.bank_name],
        ["To Account:", payment_context.beneficiary.account_id],
        ["", ""],
        ["Amount:", f"{payment_context.amount} {payment_context.currency}"],
        ["Remarks:", payment_context.remarks],
    ]

    table: Table = Table(data, colWidths=[2 * inch, 4 * inch])
    table.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (0, -1), colors.HexColor("#f0f0f0")),
                ("TEXTCOLOR", (0, 0), (-1, -1), colors.black),
                ("ALIGN", (0, 0), (-1, -1), "LEFT"),
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
                ("FONTNAME", (0, 0), (0, -1), "Helvetica-Bold"),
                ("FONTSIZE", (0, 0), (-1, -1), 10),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 6),
                ("TOPPADDING", (0, 0), (-1, -1), 6),
                ("GRID", (0, 0), (-1, -1), 1, colors.grey),
            ]
        )
    )

    elements.append(table)
    elements.append(Spacer(1, 0.5 * inch))
    elements.append(
        Paragraph("This is an electronically generated receipt.", footer_style)
    )

    # Build PDF
    doc.build(elements)
    buffer.seek(0)

    return buffer


def _validate_transaction_id(
    transaction_id: str,
) -> tuple[Optional[str], Optional[PaymentContext]]:
    """
    Validate transaction ID exists in cache.

    Returns:
        tuple: (error_message, payment_context) where error_message is None if valid,
               payment_context is None if invalid.
    """
    if not transaction_id:
        error_msg = (
            "Error: No transaction ID provided. "
            "Please ensure you use the transaction_id from the payment_authorize tool."
        )
        logger.info(error_msg)
        return error_msg, None

    payment_context: Optional[PaymentContext] = payment_cache.get(transaction_id)
    if not payment_context:
        error_msg = (
            f"Error: Invalid transaction ID {transaction_id}. "
            f"Please ensure you use the correct transaction_id from the payment_authorize tool."
        )
        logger.info(error_msg)
        return error_msg, None

    return None, payment_context
