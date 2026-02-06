# Bank AI Agent (Ballerina)

AI-powered banking chat service implemented in Ballerina. This module exposes a small HTTP API that accepts a user message and returns an agent-generated response.

The agent uses:
- An LLM via `ballerinax/ai.anthropic` (Anthropic Claude)
- Banking tools via MCP (`ai:McpToolKit`) pointed at the local `banking-mcp-server`

## What this service provides

- **POST** `/api/chat` — send a message + session id, receive a response
- **GET** `/api/health` — basic health check

## High-level architecture

1. Client calls `/api/chat` with `{ message, sessionId }`.
2. The agent runs the query using the configured LLM.
3. When needed, the agent calls banking tools through the MCP server (`OB_MCP_SERVER_URL`).
4. The service returns a single response message.

## Prerequisites

- Ballerina Swan Lake (the Docker image uses `ballerina/ballerina:2201.8.6`)
- An Anthropic API key
- The MCP server running and reachable at `OB_MCP_SERVER_URL` (defaults to `http://localhost:8000/mcp` in this repo)

## Configuration

This module uses Ballerina `configurable` variables.

Configure values in [ai-agent/Config.toml](Config.toml):

```toml
OB_MCP_SERVER_URL = "http://localhost:8000/mcp"
ANTHROPIC_API_KEY = "<your-api-key>"
```

## Run locally

From the `ai-agent` directory:

```bash
bal run
```

The service listens on Ballerina’s default HTTP port (typically `9090`).

### Health check

```bash
curl -s http://localhost:9090/api/health | jq
```

### Chat request

```bash
curl -s http://localhost:9090/api/chat \
	-H 'content-type: application/json' \
	-d '{"message":"What is my account balance?","sessionId":"session_123"}' | jq
```

## Run with Docker

Build the image:

```bash
docker build -t ai-agent:local .
```

Run the container (make sure `Config.toml` has a valid `ANTHROPIC_API_KEY` and a reachable `OB_MCP_SERVER_URL`):

```bash
docker run --rm -p 9090:9090 ai-agent:local
```

## Example prompts

- “I want to buy a car. Can you check what loan I can afford based on my spending and your loan rates?”
- “Show my recent transactions and summarize my spending.”
- “What are the available loan products and interest rates?”

## Troubleshooting

- **Agent can’t access banking data**: verify the MCP server is running and `OB_MCP_SERVER_URL` is correct.
- **LLM errors**: verify `ANTHROPIC_API_KEY` is set and valid.
- **Port already in use**: stop the process using `9090` or run Ballerina on a different listener/port.

## License

Apache-2.0. See the repository root [LICENSE](../LICENSE).
