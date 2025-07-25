// Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
//
// WSO2 LLC. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied. See the License for the
// specific language governing permissions and limitations
// under the License.

import { 
  CoreSetup, 
  CoreStart, 
  Plugin, 
  Logger, 
  PluginInitializerContext 
} from '../../../src/core/server';

// Setup is when the plugin is initialized
export interface SwiftDashboardServerPluginSetup {
  // Define setup contracts the plugin provides to others
}

// Start is when all plugins are ready and the server is running
export interface SwiftDashboardServerPluginStart {
  // Define start contracts the plugin provides to others
}

// Dependencies from other plugins that the server-side plugin needs during setup
export interface SwiftDashboardServerPluginSetupDependencies {
  // Add any plugin dependencies needed during setup phase
}

// Dependencies from other plugins that the server-side plugin needs during start
export interface SwiftDashboardServerPluginStartDependencies {
  // Add any plugin dependencies needed during start phase
}

// Common context that can be used throughout the plugin
export interface PluginContext {
  logger: Logger;
  config?: {
    enabled: boolean;
    index: string;
    cacheTTL?: number;
    [key: string]: any; // For any additional config properties
  };
}

// Type for message documents from OpenSearch
export interface MessageDocument {
  id: string;
  mtMessageType: string;
  mxMessageType: string;
  direction: string;
  amount: string;
  currency: string;
  date: string;
  status: string;
  originalMessage: string;
  translatedMessage: string;
  fieldError: string;
  notSupportedError: string;
  invalidError: string;
  otherError: string;
}

export interface LogDocument {
  time: string;
  level: string;
  module: string;
  message: string;
}

// For services that use the OpenSearch client
export interface OpenSearchServiceOptions {
  index?: string;
  cacheTTL?: number;
}
