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

export interface MessageData {

    id: string,
    mtMessageType: string,
    mxMessageType: string,
    direction: string,
    amount: string,
    currency: string,
    date: string,
    status: string,
    originalMessage: string,
    translatedMessage: string,
    fieldError: string,
    notSupportedError: string,
    invalidError: string,
    otherError: string
  
  }

  export interface LogData {
    time: string;
    level: string;
    module: string;
    message: string;
  }

// Message chart data interface
export interface MessageChartData {
    date: string;
    displayDate?: string;
    inward: {
      success: number;
      fail: number;
    };
    outward: {
      success: number;
      fail: number;
    };
  }

// Direction stats interface
export interface DirectionStats {
  inward: {
    successful: number;
    failed: number;
    pending: number;
    total: number;
  };
  outward: {
    successful: number;
    failed: number;
    pending: number;
    total: number;
  };
  total: number;
}

export interface TimeSpecificData {
    successCount: number;
    failCount: number;
    inwardCount: number;
    outwardCount: number;
    totalCount: number;
    successPercentage: number;
    failPercentage: number;
    period: string;
    date?: string; // For daily
    startDate?: string; // For weekly/monthly
    endDate?: string; // For weekly/monthly
  }
  
  // Add interface for MessageCounts props
  export interface MessageCountsProps {
    successCount: number;
    failCount: number;
    timeSpecificData: TimeSpecificData | null;
    direction: string;
  }
