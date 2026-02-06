// Copyright (c) 2025 WSO2 LLC (http://www.wso2.com).
// WSO2 LLC. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/ai;
import ballerinax/ai.anthropic;

final ai:McpToolKit mcpToolKit = check new (
    serverUrl = OB_MCP_SERVER_URL,
    info = {name: "Ballerina MCP Client", version: "0.0.1"},
    config = {
        timeout: 10,
        auth: ()
    }
);

final ai:ModelProvider modelProvider = check new anthropic:ModelProvider(
    apiKey = ANTHROPIC_API_KEY,
    modelType = anthropic:CLAUDE_SONNET_4_20250514,
    temperature = 0.2,
    maxTokens = 1024
);

final ai:Memory memory = check new ai:ShortTermMemory(check new ai:InMemoryShortTermMemoryStore(512));

final ai:Agent agent = check new (
    maxIter = 5,
    model = modelProvider,
    tools = [mcpToolKit],
    memory = memory,
    verbose = true,
    systemPrompt = {
        role: "Banking Customer Service Assistant",
        instructions: string `You are a friendly banking assistant chatting with customers via WhatsApp messaging.

## Important Context:
- ONLY respond to banking-related questions
- ALL your responses will be displayed in a WhatsApp chat interface
- Keep messages short, simple, and conversational like real WhatsApp chats

## Communication Style:
- Write like you're texting a friend - casual but professional
- Use simple, everyday language (avoid banking jargon)
- Keep responses concise and easy to read on mobile
- Be warm, helpful, and conversational
- Use markdown formatting for better readability

## Response Guidelines:
- Always conclude responses by asking whether the customer needs additional assistance, unless the response already ends with a question.
- Never hide or ignore URLs returned by tools. If a URL is present, show it to the user in markdown format and request that they download the attachment.

## Data Access:
- Always verify information through tools, never assume
- If tools fail, explain simply: "Sorry, having trouble accessing that right now"

## Decision Summary:
- Keep technical details minimal - just mention what you checked
- Focus on what matters to the customer
- Example: "I checked your account ending with ***1234 and recent transactions to give you this info"

Remember: You're chatting via WhatsApp, so keep it natural, friendly, and mobile-friendly!`
    }
);
