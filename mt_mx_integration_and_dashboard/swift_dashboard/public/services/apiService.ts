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

import axios from 'axios';
import { MessageData, MessageChartData, LogData} from '../applications/swift_dashboard_app/components/types';

// Use environment variable if available, otherwise fallback to hardcoded URL
const API_BASE_URL = process.env.REACT_APP_API_URL || '/api/swift-dashboard';

// Define interfaces for message type distribution
interface MessageTypeData {
  type: string;
  count: number;
  successful?: number;
  failed?: number;
  successRate?: number;
}

interface MessageTypeResponse {
  timeFilter: string;
  direction: string;
  period: string;
  date?: string;
  startDate?: string;
  endDate?: string;
  monthName?: string;
  messageTypes: MessageTypeData[];
}

// Configure Axios with defaults
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 seconds timeout
});

// Add request/response interceptors for debugging
apiClient.interceptors.request.use(
  config => {
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    return Promise.reject(error);
  }
);

/**
 * API Service for Swift Dashboard
 */
const apiService = {
  /**
 * Fetch messages with optional date range and direction filters
 * @param fromDate Optional - Start date in YYYY-MM-DD format
 * @param toDate Optional - End date in YYYY-MM-DD format
 * @param direction Optional - 'inward', 'outward', or undefined for all directions
 * @returns Array of messages matching the criteria
 */
getAllMessages: async (
    fromDate?: string,
    toDate?: string,
    direction?: string
  ): Promise<MessageData[]> => {
    try {
      
      // Build query parameters
      const params: Record<string, string> = {};
      if (fromDate) params.fromDate = fromDate;
      if (toDate) params.toDate = toDate;
      if (direction) params.direction = direction;
      
      // Make API request with query parameters
      const response = await apiClient.get('/messages-list', { params });
      
      if (response.data && response.data.messages) {
        return response.data.messages;
      }
      
      return Array.isArray(response.data) ? response.data : [];
    } catch (error) {
      throw error;
    }
  },

/**
 * Fetch logs
 * @returns Array of logs matching the criteria
 */
getAllLogs: async (
  dateFrom?: string,
  dateTo?: string
  ): Promise<LogData[]> => {
    try {

      // Build query parameters
      const params: Record<string, string> = {};
      if (dateFrom) params.dateFrom = dateFrom;
      if (dateTo) params.dateTo = dateTo;
      
      // Make API request with query parameters
      const response = await apiClient.get('/log-list', { params });
      
      if (response.data && response.data.logs) {
        return response.data.logs;
      }
      
      return Array.isArray(response.data) ? response.data : [];
    } catch (error) {
      throw error;
    }
  },



  /**
   * Fetch message details by ID
   */
  getMessageById: async (id: string): Promise<MessageData> => {
    try {
      const response = await apiClient.get(`/message/${id}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  /**
   * Fetch chart data with optional filters
   */
  getChartData: async (
    timeframe: string,
    direction?: string
  ): Promise<MessageChartData[]> => {
    try {
      const response = await apiClient.get('/chart', {
        params: {
          timeframe,
          direction
        }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  /**
   * Fetch time-specific message data
   */
  getTimeSpecificData: async (
    timeframe: string,
    direction?: string
  ): Promise<{
    messages: MessageData[];
    period: string;
    date?: string;
    startDate?: string;
    endDate?: string;
  }> => {
    try {
      const response = await apiClient.get(`/messages/${timeframe}`, {
        params: { direction }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  },
  
  /**
 * Get recent messages with proper parameter passing
 * @param timeframe - Time period filter (daily, weekly, monthly)
 * @param direction - Optional direction filter (inward, outward, all)
 * @param limit - Optional limit for number of messages to return
 * @returns Recent messages
 */
getRecentMessages: async (
    timeframe: string,
    direction: string,
    limit: number = 5
  ): Promise<any> => {
    try {
      
      // Use timeframe consistently (not period)
      const response = await apiClient.get('/messages/recent', {
        params: {
          timeframe,  
          direction,
          limit
        }
      });
      return response.data;

    } catch (error) {
      throw error;
    }
  },

  /**
 * Get error statistics for messages
 * @param timeFilter - Time period filter (daily, weekly, monthly)
 * @param direction - Optional direction filter (inward, outward, all)
 * @returns Error statistics response
 */
getErrorStatistics: async (
    timeFilter: string,
    direction: string
  ): Promise<any> => {
    try {
      
      const response = await apiClient.get('/error-statistics', {
        params: {
          timeFilter,
          direction
        }
      });
      
      return response.data;
    } catch (error) {
      // Return a default empty structure instead of throwing
      return {
        timeFilter,
        direction: direction || 'all',
        errors: [],
        totalCount: 0,
        errorRate: 0
      };
    }
  },

  /**
   * Get top message types distribution
   * @param timeFilter - Time period filter (daily, weekly, monthly)
   * @param direction - Optional direction filter (inward, outward)
   * @param limit - Optional limit for number of message types to return
   * @param includeStats - Whether to include success/fail statistics
   * @returns Response with message type distribution data
   */
  getTopMessageTypes: async (
    timeFilter: string,
    direction?: string,
    limit: number = 7,
    includeStats: boolean = true
  ): Promise<MessageTypeResponse> => {
    try {
      const response = await apiClient.get('/stats/top-message-types', {
        params: {
          timeFilter,
          direction,
          limit,
          includeStats
        }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default apiService;
