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
import ballerina/http;
import ballerina/log;
import ballerina/time;

listener ai:Listener 'listener = new (listenOn = check http:getDefaultListener());

service /api on 'listener {
    resource function post chat(@http:Payload ai:ChatReqMessage chatReqMessage) returns ai:ChatRespMessage|error {
        log:printInfo(`Chat request received: ${chatReqMessage.message}`);
        final string stringResult = check agent.run(query = chatReqMessage.message, sessionId = chatReqMessage.sessionId);
        log:printInfo(`Agent response: ${stringResult}`);
        return {message: stringResult};
    }

    resource function get health() returns record {|string status; string timestamp;|} {
        return {
            status: "OK",
            timestamp: time:utcToString(time:utcNow())
        };
    }
}
