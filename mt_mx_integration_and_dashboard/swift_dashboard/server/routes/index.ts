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

import { IRouter, RequestHandlerContext } from '../../../../src/core/server';
import { PluginContext } from '../types';
import { schema } from '@osd/config-schema';
import { DashboardController } from '../controllers/dashboardController';

export function registerRoutes(router: IRouter, context: PluginContext) {
  const { logger } = context;
  const dashboardController = new DashboardController(context);

  // Base path for all API routes
  const API_BASE = '/api/swift-dashboard';

  // Dashboard data - messages list
  router.get(
    {
      path: `${API_BASE}/messages-list`,
      validate: {
        query: schema.object({
          fromDate: schema.maybe(schema.string()),
          toDate: schema.maybe(schema.string()),
          direction: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchDashboardData(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching messages list: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch messages list'
          }
        });
      }
    }
  );

  // Log list
  router.get(
    {
      path: `${API_BASE}/log-list`,
      validate: {
        query: schema.object({
          dateFrom: schema.maybe(schema.string()),
          dateTo: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchLogData(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching logs: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch logs'
          }
        });
      }
    }
  );


  // Message specific endpoint
  router.get(
    {
      path: `${API_BASE}/message/{id}`,
      validate: {
        params: schema.object({
          id: schema.string()
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchMessageById(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching message ${request.params.id}: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: `Failed to fetch message ${request.params.id}`
          }
        });
      }
    }
  );

  // Chart data endpoint
  router.get(
    {
      path: `${API_BASE}/chart`,
      validate: {
        query: schema.object({
          range: schema.maybe(schema.string()),
          timeframe: schema.maybe(schema.string()),
          direction: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchChartData(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching chart data: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch chart data'
          }
        });
      }
    }
  );

  // Time-specific message endpoints
  router.get(
    {
      path: `${API_BASE}/messages/daily`,
      validate: {
        query: schema.object({
          date: schema.maybe(schema.string()),
          direction: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchDailyMessages(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching daily messages: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch daily messages'
          }
        });
      }
    }
  );

  router.get(
    {
      path: `${API_BASE}/messages/weekly`,
      validate: {
        query: schema.object({
          week: schema.maybe(schema.string()),
          direction: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchWeeklyMessages(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching weekly messages: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch weekly messages'
          }
        });
      }
    }
  );

  router.get(
    {
      path: `${API_BASE}/messages/monthly`,
      validate: {
        query: schema.object({
          month: schema.maybe(schema.string()),
          direction: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchMonthlyMessages(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching monthly messages: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch monthly messages'
          }
        });
      }
    }
  );

  // Time-specific chart data endpoints
  router.get(
    {
      path: `${API_BASE}/chart/daily`,
      validate: {
        query: schema.object({
          date: schema.maybe(schema.string()),
          direction: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchDailyChartData(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching daily chart data: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch daily chart data'
          }
        });
      }
    }
  );

  router.get(
    {
      path: `${API_BASE}/chart/weekly`,
      validate: {
        query: schema.object({
          week: schema.maybe(schema.string()),
          direction: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchWeeklyChartData(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching weekly chart data: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch weekly chart data'
          }
        });
      }
    }
  );

  router.get(
    {
      path: `${API_BASE}/chart/monthly`,
      validate: {
        query: schema.object({
          month: schema.maybe(schema.string()),
          direction: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchMonthlyChartData(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching monthly chart data: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch monthly chart data'
          }
        });
      }
    }
  );

  // Stats for top message types
  router.get(
    {
      path: `${API_BASE}/stats/top-message-types`,
      validate: {
        query: schema.object({
          limit: schema.maybe(schema.number()),
          timeframe: schema.maybe(schema.string()),
          timeFilter: schema.maybe(schema.string()), // Add this to match what frontend sends
          includeStats: schema.maybe(schema.boolean()),
          direction: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchTopMessageTypes(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching top message types: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch top message types'
          }
        });
      }
    }
  );

  // Recent messages
router.get(
    {
      path: `${API_BASE}/messages/recent`,
      validate: {
        query: schema.object({
          limit: schema.maybe(schema.number()),
          direction: schema.maybe(schema.string()),
          timeframe: schema.maybe(schema.string()),
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        // Log the incoming request
        logger.debug(`Recent messages request received: ${JSON.stringify(request.query)}`);
        
        const result = await dashboardController.getRecentMessages(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching recent messages: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch recent messages'
          }
        });
      }
    }
  );

  // Error statistics
  router.get(
    {
      path: `${API_BASE}/error-statistics`,
      validate: {
        query: schema.object({
            timeframe: schema.maybe(schema.string()),
            timeFilter: schema.maybe(schema.string()), // Add this parameter
            direction: schema.maybe(schema.string())
        })
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.fetchErrorStatistics(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error fetching error statistics: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to fetch error statistics'
          }
        });
      }
    }
  );

  // Cache management
  router.post(
    {
      path: `${API_BASE}/cache/refresh`,
      validate: {
        body: schema.nullable(schema.object({
          target: schema.maybe(schema.string())
        }))
      }
    },
    async (context: RequestHandlerContext, request, response) => {
      try {
        const result = await dashboardController.refreshCache(context, request);
        return response.ok({ body: result });
      } catch (error) {
        logger.error(`Error refreshing cache: ${error}`);
        return response.customError({
          statusCode: 500,
          body: {
            message: 'Failed to refresh cache'
          }
        });
      }
    }
  );
}
