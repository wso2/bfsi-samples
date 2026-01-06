# Banking MCP Server

Python implementation of a **Model Context Protocol (MCP)** server for banking use-cases.

This service exposes a set of banking "tools" (accounts, products, payees, payments) over MCP so an AI agent can safely call structured operations instead of scraping text. It also provides a small HTTP surface for health checks and retrieving generated payment receipts.

## What it does

- Hosts an MCP server (`FastMCP`) over HTTP (stateless)
- Registers banking tools:
	- `get_user_accounts` (optionally includes `transactions` as a sub-resource)
	- `get_bank_products`
	- `get_user_profile`, `get_user_payees`
	- Payment workflow tools: `payment_initiate`, `payment_authorize`, `payment_otp_verify`
- Generates and persists PDF receipts to the local filesystem and serves them via an HTTP endpoint

## Prerequisites

- Python **3.12+**
- A reachable Open Banking-style backend API (for this repo, the mock backend is typically used)

## Configuration

Configuration is loaded via environment variables (and optionally a local `.env` file). All variables use the prefix `OB_`.

Common variables (from `ServerConfigs`):

- `OB_SERVER_URL` — Base URL of the backing banking API (e.g., the mock bank backend)
- `OB_SERVER_API_KEY` — Optional API key forwarded to the backing API as `api-key`
- `OB_MCP_HOST` — MCP server bind host (default: `localhost`)
- `OB_MCP_PORT` — MCP server bind port (default: `8000`)
- `OB_MCP_SERVER_URL` — Public base URL of this MCP server (used when generating receipt URLs)

### Authentication header forwarding

When calling the backing banking API, this server forwards:

- `x-forwarded-authorization` (incoming) → `Authorization: Bearer <token>` (outgoing)
- `OB_SERVER_API_KEY` (if set) → `api-key: <value>` (outgoing)

## Run locally

From the `banking-mcp-server` directory:

```bash
pip install -r requirements.txt
python -m banking_mcp_server
```

The server listens on `http://localhost:8000` by default.

### Health check

```bash
curl -s http://localhost:8000/health
```

## Run with Docker

Build:

```bash
docker build -t banking-mcp-server:local .
```

Run (example):

```bash
docker run --rm -p 8000:8000 \
	-e OB_SERVER_URL="http://host.docker.internal:3001" \
	-e OB_MCP_SERVER_URL="http://localhost:8000" \
	banking-mcp-server:local
```

## Endpoints

- `GET /health` — returns service status and timestamp
- `GET /transactions/{transaction_id}/receipt` — returns a generated PDF receipt if present

Receipts are persisted under:

- `/tmp/ai-banking-agent/transactions/<transaction_id>.pdf`

## Using it with the Ballerina AI agent

The Ballerina agent in this repo expects the MCP endpoint URL in its config.

Example:

```toml
OB_MCP_SERVER_URL = "http://localhost:8000/mcp"
```

## License

Apache-2.0. See the repository root `LICENSE`.

