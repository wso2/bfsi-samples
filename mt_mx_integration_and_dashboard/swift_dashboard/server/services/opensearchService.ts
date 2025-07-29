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

import { OpenSearchClient } from ".../../../src/core/server";
import { env } from "process";
import { PluginContext, MessageDocument, LogDocument } from "../types";

class OpenSearchService {
  private context: PluginContext;
  private index: string | undefined;
  // Todo - Cache for messages to avoid frequent queries
  private cachedMessages: MessageDocument[] | null = null;
  private cachedLogs: LogDocument[] | null = null;
  private lastCacheTime: number = 0;
  private cacheTTL: number = 5 * 60 * 1000; // 5 minutes cache TTL

  constructor(context: PluginContext) {
    this.context = context;
    this.index = context.config?.index || env.OPENSEARCH_INDEX;
    this.cacheTTL = context.config?.cacheTTL || 5 * 60 * 1000; // Default 5 minutes
  }

  /**
   * Get the total count of documents in the messages index
   * @returns Total number of documents in the index
   */
  public async getDocumentCount(client: OpenSearchClient): Promise<number> {
    try {
      const response = await client.count({
        index: this.index,
      });

      const count = response.body.count;

      return count;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Get messages within a specified date range with optional direction filtering
   * @param fromDate Start date in ISO format (YYYY-MM-DD)
   * @param toDate End date in ISO format (YYYY-MM-DD)
   * @param direction Optional - 'inward', 'outward', or undefined for all directions
   * @returns Array of messages within the specified date range
   */
  public async getMessagesInDateRange(
    client: OpenSearchClient,
    fromDate?: string,
    toDate?: string,
    direction?: string
  ): Promise<MessageDocument[]> {
    try {
      // If no date range is provided, return all messages (with optional direction filter)
      if (!fromDate && !toDate) {
        return this.getAllMessages(client, direction);
      }

      const count = await this.getDocumentCount(client);
      let query: { bool: any };

      // Build the query with date range filter
      if (!fromDate) {
        query = {
          bool: {
            must: [
              {
                range: {
                  date: {
                    lte: toDate
                      ? toDate
                      : new Date().toISOString().split("T")[0],
                  },
                },
              },
            ],
          },
        };
      } else {
        query = {
          bool: {
            must: [
              {
                range: {
                  date: {
                    gte: fromDate,
                    lte: toDate
                      ? toDate
                      : new Date().toISOString().split("T")[0],
                  },
                },
              },
            ],
          },
        };
      }

      // Add direction filter if specified
      if (direction && direction.toLowerCase() !== "all") {
        (query.bool.must as any[]).push({
          term: {
            "direction.keyword": direction.toLowerCase(),
          },
        });
      }

      const response = await client.search({
        index: this.index,
        body: {
          size: count,
          query: query,
          sort: [
            {
              date: {
                order: "desc",
              },
            },
          ],
        },
      });

      const messages = response.body.hits.hits.map((hit: any) => {
        const source = hit._source || ({} as any);
        return {
          id: source.id,
          mtMessageType: source.mtMessageType || "",
          mxMessageType: source.mxMessageType || "",
          direction: source.direction || "",
          amount: source.amount || "",
          currency: source.currency || "",
          date: source.date || "",
          status: source.status || "",
          originalMessage: source.originalMessage || "",
          translatedMessage: source.translatedMessage || "",
          fieldError: source.fieldError || "",
          notSupportedError: source.notSupportedError || "",
          invalidError: source.invalidError || "",
          otherError: source.otherError || "",
        };
      });

      return messages;
    } catch (error) {
      return [];
    }
  }

  /**
   * Get logs within a specified date range
   * @param dateFrom Start date in ISO format (YYYY-MM-DD)
   * @param dateTo End date in ISO format (YYYY-MM-DD)
   * @returns Array of logs within the specified date range
   */
  public async getLogData(
    client: OpenSearchClient,
    dateFrom?: string,
    dateTo?: string
  ): Promise<LogDocument[]> {
    const now = Date.now();

    const count = await this.getDocumentCount(client);

    // Build the range query dynamically
    const rangeQuery: any = {};
    if (dateFrom) {
      // gte: start of the selected 'dateFrom' day
      rangeQuery.gte = `${dateFrom}T00:00:00.000Z`; // Force UTC midnight for start
    }
    if (dateTo) {
      // lt: start of the day *after* the selected 'dateTo' day
      // This ensures all logs within the 'dateTo' day are included.
      const nextDay = new Date(dateTo);
      nextDay.setDate(nextDay.getDate() + 1);
      const nextDayISO = nextDay.toISOString().split("T")[0]; // Get YYYY-MM-DD for the next day
      rangeQuery.lt = `${nextDayISO}T00:00:00.000Z`; // Force UTC midnight for next day
    }

    // Build the query with date range filter
    const query = (!dateFrom || !dateTo)
      ? { match_all: {} }
      : {
          bool: {
            must: [
              {
                range: {
                  time: {
                    gte: dateFrom,
                    lte: dateTo,
                  },
                },
              },
            ],
          },
        };
    const response = await client.search({
      index: "ballerina_log",
      body: {
        size: count,
        query: query,
        sort: [
          {
            time: {
              order: "desc"
            }
          }
        ],
      },
    });

    const logs = response.body.hits.hits.map((hit: any) => {
      const source = hit._source || {};
      return {
        timestamp: source.time || "",
        level: source.level || "",
        module: source.module || "",
        message: source.message || "",
      };
    });

    // Cache the results
    this.cachedLogs = logs;
    this.lastCacheTime = now;

    return logs;
  }

  /**
   * Get a single message by ID
   */
  public async getMessageById(client: OpenSearchClient, id: string) {
    try {
      const response = await client.search({
        index: this.index,
        body: {
          query: {
            term: {
              "id.keyword": id,
            },
          },
        },
      });

      const total =
        typeof response.body.hits.total === "number"
          ? response.body.hits.total
          : response.body.hits.total.value;

      if (total === 0) {
        throw new Error("Message not found");
      }

      const source = response.body.hits.hits[0]._source || ({} as any);

      return {
        id: source.id || "",
        mtMessageType: source.mtMessageType || "",
        mxMessageType: source.mxMessageType || "",
        direction: source.direction || "",
        amount: source.amount || "",
        currency: source.currency || "",
        date: source.date || "",
        status: source.status || "",
        originalMessage: source.originalMessage || "",
        translatedMessage: source.translatedMessage || "",
        fieldError: source.fieldError || "",
        notSupportedError: source.notSupportedError || "",
        invalidError: source.invalidError || "",
        otherError: source.otherError || "",
      };
    } catch (error) {
      // If we get here, rethrow the error
      throw error;
    }
  }

  /**
   * Get chart data for messages by direction and time period
   * @param timeframe 'daily', 'weekly', or 'monthly'
   * @param direction optional - 'inward', 'outward', or undefined for both
   */
  public async getMessageChartData(
    client: OpenSearchClient,
    timeframe: "daily" | "weekly" | "monthly",
    direction?: "inward" | "outward"
  ) {
    // Get all messages regardless of year
    const allMessages = await this.getAllMessages(client, direction); //

    // Determine date range based on timeframe
    let now = new Date();
    let startDate: Date;

    if (timeframe === "daily") {
      // For daily view, show past 7 days including today
      startDate = new Date(now);
      startDate.setDate(now.getDate() - 6); // Go back 6 days to include today
      startDate.setHours(0, 0, 0, 0);
    } else if (timeframe === "weekly") {
      // For weekly view, show past 52 weeks including current week
      startDate = new Date(now);
      startDate.setDate(now.getDate() - 364); // Go back ~52 weeks (364 days)
      startDate.setHours(0, 0, 0, 0);
    } else {
      // For monthly view, show past 12 months including current month
      startDate = new Date(now);
      startDate.setMonth(now.getMonth() - 11); // Go back 11 months to include current month
      startDate.setDate(1); // Start at the 1st of the month
      startDate.setHours(0, 0, 0, 0);
    }

    // Filter by date
    const filteredByDate = allMessages.filter((msg) => {
      const msgDate = new Date(msg.date);
      now.setHours(23, 59, 59, 999); // Set to end of the day
      return msgDate >= startDate && msgDate <= now;
    });

    // Group by time period
    const chartData = new Map();

    // First, create empty data points for all periods to ensure continuous data
    if (timeframe === "daily") {
      // Create data points for past 7 days including today
      for (let i = 6; i >= 0; i--) {
        const date = new Date(now);
        date.setDate(now.getDate() - i);

        // Format date as YYYY-MM-DD consistently
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const key = `${year}-${month}-${day}`;

        const dayOfWeek = date.getDay(); // 0 = Sunday, 1 = Monday, etc.
        const dayNames = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

        chartData.set(key, {
          date: key,
          displayDate: `${dayNames[dayOfWeek]} ${month}/${day}`,
          dayOfWeek: dayOfWeek,
          inward: { success: 0, fail: 0 },
          outward: { success: 0, fail: 0 },
        });
      }
    } else if (timeframe === "weekly") {
      // Create data points for past 52 weeks including current week
      for (let weekOffset = 51; weekOffset >= 0; weekOffset--) {
        // Calculate the date for each week
        const weekDate = new Date(now);
        weekDate.setDate(now.getDate() - weekOffset * 7);

        // Get the week number and year for this date
        const weekYear = weekDate.getFullYear();
        const weekNum = this.getISOWeek(weekDate);

        const key = `${weekYear}-W${String(weekNum).padStart(2, "0")}`;

        // Create a readable display format (Week N, YYYY)
        const displayDate = `Week ${weekNum}, ${weekYear}`;

        chartData.set(key, {
          date: key,
          displayDate,
          weekNumber: weekNum,
          year: weekYear,
          inward: { success: 0, fail: 0 },
          outward: { success: 0, fail: 0 },
        });
      }
    } else {
      // Create data points for past 12 months including current month
      for (let monthOffset = 11; monthOffset >= 0; monthOffset--) {
        const monthDate = new Date(now);
        monthDate.setMonth(now.getMonth() - monthOffset);

        const monthYear = monthDate.getFullYear();
        const month = monthDate.getMonth() + 1;

        const key = `${monthYear}-${String(month).padStart(2, "0")}`;

        // Get month name for display
        const monthNames = monthDate.toLocaleString('default', { month: 'long' });

        // Create a readable display format (Mon YYYY)
        const displayDate = `${monthNames[month - 1]} ${monthYear}`;

        chartData.set(key, {
          date: key,
          displayDate,
          monthName: monthNames[month - 1],
          year: monthYear,
          inward: { success: 0, fail: 0 },
          outward: { success: 0, fail: 0 },
        });
      }
    }

    // Now populate with actual data
    filteredByDate.forEach((msg) => {
      const msgDate = new Date(msg.date);
      let key: string;

      if (timeframe === "daily") {
        // Format as YYYY-MM-DD consistently
        const year = msgDate.getFullYear();
        const month = String(msgDate.getMonth() + 1).padStart(2, "0");
        const day = String(msgDate.getDate()).padStart(2, "0");
        key = `${year}-${month}-${day}`;
      } else if (timeframe === "weekly") {
        // Calculate ISO week number
        const weekNum = this.getISOWeek(msgDate);
        const year = msgDate.getFullYear();
        key = `${year}-W${String(weekNum).padStart(2, "0")}`;
      } else {
        // Format as YYYY-MM
        const year = msgDate.getFullYear();
        const month = String(msgDate.getMonth() + 1).padStart(2, "0");
        key = `${year}-${month}`;
      }

      // Skip if key is not in our pre-defined range
      if (!chartData.has(key)) return;

      // Update counts
      const dataPoint = chartData.get(key);
      const dir = msg.direction.toLowerCase();

      if (msg.status.toLowerCase() === "successful") {
        dataPoint[dir].success++;
      } else if (msg.status.toLowerCase() === "failed") {
        dataPoint[dir].fail++;
      }
    });

    // Convert to array and sort by date
    return Array.from(chartData.values()).sort((a, b) =>
      a.date.localeCompare(b.date)
    );
  }

  /**
   * Get all messages from the database or mock data, regardless of year
   */
  public async getAllMessages(
    client: OpenSearchClient,
    direction?: string
  ): Promise<MessageDocument[]> {
    const now = Date.now();

    try {
      const count = await this.getDocumentCount(client);
      const query =
        direction && direction.toLowerCase() !== "all"
          ? {
              bool: {
                must: [
                  {
                    term: {
                      "direction.keyword":
                        direction.charAt(0).toUpperCase() +
                        direction.slice(1).toLowerCase(),
                    },
                  },
                ],
              },
            }
          : {
              match_all: {},
            };

      const response = await client.search({
        index: this.index,
        body: {
          size: count,
          query: query,
          sort: [
            {
              date: {
                order: "desc", // Sort by date in descending order (newest first)
              },
            },
          ],
        },
      });

      const messages = response.body.hits.hits.map((hit: any) => {
        const source = hit._source || {};
        return {
          id: source.id,
          mtMessageType: source.mtMessageType || "",
          mxMessageType: source.mxMessageType || "",
          direction: source.direction || "",
          amount: source.amount || "",
          currency: source.currency || "",
          date: source.date || "",
          status: source.status || "",
          originalMessage: source.originalMessage || "",
          translatedMessage: source.translatedMessage || "",
          fieldError: source.fieldError || "",
          notSupportedError: source.notSupportedError || "",
          invalidError: source.invalidError || "",
          otherError: source.otherError || "",
        };
      });

      // Cache the results
      this.cachedMessages = messages;
      this.lastCacheTime = now;

      return messages;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Calculate ISO week number
   * @param date The date to get the week number for
   * @returns Week number (1-53)
   */
  private getISOWeek(date: Date): number {
    const d = new Date(
      Date.UTC(date.getFullYear(), date.getMonth(), date.getDate())
    );
    d.setUTCDate(d.getUTCDate() + 4 - (d.getUTCDay() || 7));
    const yearStart = new Date(Date.UTC(d.getUTCFullYear(), 0, 1));
    return Math.ceil(((d.getTime() - yearStart.getTime()) / 86400000 + 1) / 7);
  }

  /**
   * Get top message types for the specified time period and direction
   * @param timeFilter 'daily', 'weekly', or 'monthly'
   * @param direction optional - 'inward', 'outward', or undefined for both
   * @param limit Number of top message types to return (default: 7)
   * @returns Array of top message types with counts, sorted by frequency
   */
  public async getTopMessageTypes(
    client: OpenSearchClient,
    timeFilter: "daily" | "weekly" | "monthly",
    direction?: "inward" | "outward",
    limit: number = 7
  ): Promise<{ type: string; count: number }[]> {
    // Get messages for the specified time period
    let messages: MessageDocument[];

    switch (timeFilter) {
      case "daily":
        messages = await this.getDailyMessages(client, direction);
        break;
      case "weekly":
        messages = await this.getWeeklyMessages(client, direction);
        break;
      case "monthly":
        messages = await this.getMonthlyMessages(client, direction);
        break;
      default:
        throw new Error(`Invalid timeFilter: ${timeFilter}`);
    }

    // Apply direction filter if specified
    if (direction) {
      messages = messages.filter(
        (msg) => msg.direction.toLowerCase() === direction.toLowerCase()
      );
    }

    // Count occurrences of each message type
    const typeCounts = new Map<string, number>();

    messages.forEach((msg) => {
      const msgType = msg.mtMessageType || "Unknown";
      typeCounts.set(msgType, (typeCounts.get(msgType) || 0) + 1);
    });

    // Convert to array and sort by count (descending)
    const sortedTypes = Array.from(typeCounts.entries())
      .map(([type, count]) => ({ type, count }))
      .sort((a, b) => b.count - a.count);

    // Log information about the results
    const directionText = direction ? `${direction} ` : "";

    // Return top N results
    return sortedTypes.slice(0, limit);
  }

  /**
   * Get top message types with more detailed statistics (including success/failure rates)
   * @param timeFilter 'daily', 'weekly', or 'monthly'
   * @param direction optional - 'inward', 'outward', or undefined for both
   * @param limit Number of top message types to return (default: 7)
   * @returns Array of top message types with detailed stats
   */
  public async getTopMessageTypesWithStats(
    client: OpenSearchClient,
    timeFilter: "daily" | "weekly" | "monthly",
    direction?: "inward" | "outward",
    limit: number = 7
  ): Promise<
    {
      type: string;
      count: number;
      successful: number;
      failed: number;
      successRate: number;
    }[]
  > {
    // Get messages for the specified time period
    let messages: MessageDocument[];

    switch (timeFilter) {
      case "daily":
        messages = await this.getDailyMessages(client, direction);
        break;
      case "weekly":
        messages = await this.getWeeklyMessages(client, direction);
        break;
      case "monthly":
        messages = await this.getMonthlyMessages(client, direction);
        break;
      default:
        throw new Error(`Invalid timeFilter: ${timeFilter}`);
    }

    // Track statistics for each message type
    const typeStats = new Map<
      string,
      {
        count: number;
        successful: number;
        failed: number;
      }
    >();

    messages.forEach((msg) => {
      const msgType = msg.mtMessageType || "Unknown";

      // Initialize if this is the first time seeing this message type
      if (!typeStats.has(msgType)) {
        typeStats.set(msgType, {
          count: 0,
          successful: 0,
          failed: 0,
        });
      }

      // Update counts
      const stats = typeStats.get(msgType)!;
      stats.count++;

      if (msg.status.toLowerCase() === "successful") {
        stats.successful++;
      } else if (msg.status.toLowerCase() === "failed") {
        stats.failed++;
      }
    });

    // Convert to array and compute success rate
    const resultsWithStats = Array.from(typeStats.entries())
      .map(([type, stats]) => ({
        type,
        count: stats.count,
        successful: stats.successful,
        failed: stats.failed,
        successRate:
          stats.count > 0
            ? Math.round((stats.successful / stats.count) * 100)
            : 0,
      }))
      .sort((a, b) => b.count - a.count);

    // Log information about the results
    const directionText = direction ? `${direction} ` : "";

    // Return top N results
    return resultsWithStats.slice(0, limit);
  }

  /**
   * Force refresh of cached data
   */
  public invalidateCache() {
    this.cachedMessages = null;
    this.lastCacheTime = 0;
  }

  /**
   * Get messages for the current day with optional direction filter
   * @param direction Optional - 'inward', 'outward', or undefined for all directions
   * @returns Array of messages from the current day matching the direction criteria
   */
  public async getDailyMessages(
    client: OpenSearchClient,
    direction?: string
  ): Promise<MessageDocument[]> {
    // Get today's date range
    const today = new Date(); // Set to the start of the day
    today.setUTCHours(0, 0, 0, 0);

    const endOfToday = new Date(today);
    endOfToday.setUTCHours(23, 59, 59, 999);

    // Format dates for query
    const startDateStr = today.toISOString().split("T")[0];
    const endDateStr = endOfToday.toISOString().split("T")[0];

    // Otherwise, query OpenSearch directly
    try {
      const count = await this.getDocumentCount(client);

      // Build the query with date range filter
      let query = {
        bool: {
          must: [
            {
              range: {
                date: {
                  gte: startDateStr,
                  lte: endDateStr,
                },
              },
            },
          ],
        },
      };

      // Add direction filter if specified
      if (direction && direction.toLowerCase() !== "all") {
        (query.bool.must as any[]).push({
          term: {
            "direction.keyword":
              direction.charAt(0).toUpperCase() +
              direction.slice(1).toLowerCase(),
          },
        });
      }

      const response = await client.search({
        index: this.index,
        body: {
          size: count,
          query: query,
          sort: [
            {
              date: {
                order: "desc",
              },
            },
          ],
        },
      });

      const messages = response.body.hits.hits.map((hit: any) => {
        const source = hit._source || {};
        return {
          id: source.id,
          mtMessageType: source.mtMessageType || "",
          mxMessageType: source.mxMessageType || "",
          direction: source.direction || "",
          amount: source.amount || "",
          currency: source.currency || "",
          date: source.date || "",
          status: source.status || "",
          originalMessage: source.originalMessage || "",
          translatedMessage: source.translatedMessage || "",
          fieldError: source.fieldError || "",
          notSupportedError: source.notSupportedError || "",
          invalidError: source.invalidError || "",
          otherError: source.otherError || "",
        };
      });

      return messages;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Get messages for the current week (Sunday to Saturday) with optional direction filter
   * @param direction Optional - 'inward', 'outward', or undefined for all directions
   * @returns Array of messages from the current week matching the direction criteria
   */
  public async getWeeklyMessages(
    client: OpenSearchClient,
    direction?: string
  ): Promise<MessageDocument[]> {
    // Get current week date range
    const today = new Date();

    // Start of week (Monday)
    const startOfWeek = new Date(today);
    const day = today.getDay();
    // Adjust to Monday (1) if today is Sunday (0)
    if (day === 0) {
      startOfWeek.setDate(today.getDate() - 6);
    } else {
      startOfWeek.setDate(today.getDate() - day + 1);
    }
    startOfWeek.setUTCHours(0, 0, 0, 0);

    // End of week (Sunday)
    const endOfWeek = new Date(startOfWeek);
    endOfWeek.setDate(startOfWeek.getDate() + 6);
    endOfWeek.setUTCHours(23, 59, 59, 999);

    // Format dates for query
    const startDateStr = startOfWeek.toISOString().split("T")[0];
    const endDateStr = endOfWeek.toISOString().split("T")[0];

    // Otherwise, query OpenSearch directly
    try {
      const count = await this.getDocumentCount(client);

      // Build the query with date range filter
      let query = {
        bool: {
          must: [
            {
              range: {
                date: {
                  gte: startDateStr,
                  lte: endDateStr,
                },
              },
            },
          ],
        },
      };

      // Add direction filter if specified
      if (direction && direction.toLowerCase() !== "all") {
        (query.bool.must as any[]).push({
          term: {
            "direction.keyword":
              direction.charAt(0).toUpperCase() +
              direction.slice(1).toLowerCase(),
          },
        });
      }

      const response = await client.search({
        index: this.index,
        body: {
          size: count,
          query: query,
          sort: [
            {
              date: {
                order: "desc",
              },
            },
          ],
        },
      });

      const messages = response.body.hits.hits.map((hit: any) => {
        const source = hit._source || {};
        return {
          id: source.id,
          mtMessageType: source.mtMessageType || "",
          mxMessageType: source.mxMessageType || "",
          direction: source.direction || "",
          amount: source.amount || "",
          currency: source.currency || "",
          date: source.date || "",
          status: source.status || "",
          originalMessage: source.originalMessage || "",
          translatedMessage: source.translatedMessage || "",
          fieldError: source.fieldError || "",
          notSupportedError: source.notSupportedError || "",
          invalidError: source.invalidError || "",
          otherError: source.otherError || "",
        };
      });

      return messages;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Get messages for the current month with optional direction filter
   * @param direction Optional - 'inward', 'outward', or undefined for all directions
   * @returns Array of messages from the current month matching the direction criteria
   */
  public async getMonthlyMessages(
    client: OpenSearchClient,
    direction?: string
  ): Promise<MessageDocument[]> {
    // Get current month date range
    const today = new Date();

    // Start of month
    const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 2); // Changed from 2 to 1 to include the 1st day
    startOfMonth.setHours(0, 0, 0, 0);

    // End of month
    const endOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);
    endOfMonth.setHours(23, 59, 59, 999);

    // Format dates for query
    const startDateStr = startOfMonth.toISOString().split("T")[0];
    const endDateStr = endOfMonth.toISOString().split("T")[0];

    // Otherwise, query OpenSearch directly
    try {
      const count = await this.getDocumentCount(client);

      // Build the query with date range filter
      let query = {
        bool: {
          must: [
            {
              range: {
                date: {
                  gte: startDateStr,
                  lte: endDateStr,
                },
              },
            },
          ],
        },
      };

      // Add direction filter if specified
      if (direction && direction.toLowerCase() !== "all") {
        (query.bool.must as any[]).push({
          term: {
            "direction.keyword":
              direction.charAt(0).toUpperCase() +
              direction.slice(1).toLowerCase(),
          },
        });
      }

      const response = await client.search({
        index: this.index,
        body: {
          size: count,
          query: query,
          sort: [
            {
              date: {
                order: "desc",
              },
            },
          ],
        },
      });

      const messages = response.body.hits.hits.map((hit: any) => {
        const source = hit._source || {};
        return {
          id: source.id,
          mtMessageType: source.mtMessageType || "",
          mxMessageType: source.mxMessageType || "",
          direction: source.direction || "",
          amount: source.amount || "",
          currency: source.currency || "",
          date: source.date || "",
          status: source.status || "",
          originalMessage: source.originalMessage || "",
          translatedMessage: source.translatedMessage || "",
          fieldError: source.fieldError || "",
          notSupportedError: source.notSupportedError || "",
          invalidError: source.invalidError || "",
          otherError: source.otherError || "",
        };
      });

      return messages;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Get chart data for daily messages
   * @param direction optional - 'inward', 'outward', or undefined for both
   */
  public async getDailyChartData(
    client: OpenSearchClient,
    direction?: "inward" | "outward"
  ) {
    // Get messages for the current day
    const messages = await this.getDailyMessages(client, direction);

    // Create data points for each hour of the day (0-23)
    const hourlyData = new Map();

    // Pre-populate hours
    for (let hour = 0; hour < 24; hour++) {
      const key = hour.toString().padStart(2, "0");
      hourlyData.set(key, {
        hour: key,
        inward: { success: 0, fail: 0 },
        outward: { success: 0, fail: 0 },
      });
    }

    // Populate with message data
    messages.forEach((msg) => {
      const msgDate = new Date(msg.date);
      const hour = msgDate.getHours().toString().padStart(2, "0");

      if (!hourlyData.has(hour)) return;

      const dataPoint = hourlyData.get(hour);
      const dir = msg.direction.toLowerCase();

      if (msg.status.toLowerCase() === "successful") {
        dataPoint[dir].success++;
      } else if (msg.status.toLowerCase() === "failed") {
        dataPoint[dir].fail++;
      }
    });

    // Convert to array and sort by hour
    return Array.from(hourlyData.values());
  }

  /**
   * Get chart data for weekly messages
   * @param direction optional - 'inward', 'outward', or undefined for both
   */
  public async getWeeklyChartData(
    client: OpenSearchClient,
    direction?: "inward" | "outward"
  ) {
    // Get messages for the current week
    const messages = await this.getWeeklyMessages(client, direction);

    // Create data points for each day of the week (Sunday to Saturday)
    const dailyData = new Map();
    const today = new Date();

    // Pre-populate days of the week
    const startOfWeek = new Date(today);
    startOfWeek.setDate(today.getDate() - today.getDay()); // Sunday
    startOfWeek.setHours(0, 0, 0, 0);

    for (let i = 0; i < 7; i++) {
      const date = new Date(startOfWeek);
      date.setDate(startOfWeek.getDate() + i);

      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const key = `${year}-${month}-${day}`;

      dailyData.set(key, {
        date: key,
        dayName: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"][i],
        dayOfWeek: i,
        inward: { success: 0, fail: 0 },
        outward: { success: 0, fail: 0 },
      });
    }

    // Populate with message data
    messages.forEach((msg) => {
      const msgDate = new Date(msg.date);
      const year = msgDate.getFullYear();
      const month = String(msgDate.getMonth() + 1).padStart(2, "0");
      const day = String(msgDate.getDate()).padStart(2, "0");
      const key = `${year}-${month}-${day}`;

      if (!dailyData.has(key)) return;

      const dataPoint = dailyData.get(key);
      const dir = msg.direction.toLowerCase();

      if (msg.status.toLowerCase() === "successful") {
        dataPoint[dir].success++;
      } else if (msg.status.toLowerCase() === "failed") {
        dataPoint[dir].fail++;
      }
    });

    // Convert to array and sort by day of week
    return Array.from(dailyData.values()).sort(
      (a, b) => a.dayOfWeek - b.dayOfWeek
    );
  }

  /**
   * Get chart data for monthly messages
   * @param direction optional - 'inward', 'outward', or undefined for both
   */
  public async getMonthlyChartData(
    client: OpenSearchClient,
    direction?: "inward" | "outward"
  ) {
    // Get messages for the current month
    const messages = await this.getMonthlyMessages(client, direction);

    // Create data points for each day of the month
    const dailyData = new Map();
    const today = new Date();

    // Pre-populate days of the month
    const daysInMonth = new Date(
      today.getFullYear(),
      today.getMonth() + 1,
      0
    ).getDate();
    const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);

    for (let i = 0; i < daysInMonth; i++) {
      const date = new Date(startOfMonth);
      date.setDate(startOfMonth.getDate() + i);

      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const key = `${year}-${month}-${day}`;

      dailyData.set(key, {
        date: key,
        day: date.getDate(),
        inward: { success: 0, fail: 0 },
        outward: { success: 0, fail: 0 },
      });
    }

    // Populate with message data
    messages.forEach((msg) => {
      const msgDate = new Date(msg.date);
      const year = msgDate.getFullYear();
      const month = String(msgDate.getMonth() + 1).padStart(2, "0");
      const day = String(msgDate.getDate()).padStart(2, "0");
      const key = `${year}-${month}-${day}`;

      if (!dailyData.has(key)) return;

      const dataPoint = dailyData.get(key);
      const dir = msg.direction.toLowerCase();

      if (msg.status.toLowerCase() === "successful") {
        dataPoint[dir].success++;
      } else if (msg.status.toLowerCase() === "failed") {
        dataPoint[dir].fail++;
      }
    });

    // Convert to array and sort by date
    return Array.from(dailyData.values()).sort((a, b) =>
      a.date.localeCompare(b.date)
    );
  }

  /**
   * Get the most recent messages with essential details
   * @param limit Number of messages to return (default: 5)
   * @param direction Filter by message direction ('All', 'Inward', or 'Outward')
   * @param period Filter by time period ('Daily', 'Weekly', 'Monthly')
   * @returns Array of recent messages with id, time, message type and status
   */
  public async getRecentMessages(
    client: OpenSearchClient,
    limit: number = 5,
    direction: string = "All",
    period: string = "All"
  ): Promise<
    {
      id: string;
      time: string;
      mtMessageType: string;
      status: string;
      direction: string;
    }[]
  > {
    try {
      let timeFilteredMessages: MessageDocument[];
      if (period === "Daily") {
        // Filter to messages from today only
        timeFilteredMessages = await this.getDailyMessages(client);
      } else if (period === "Weekly") {
        // Filter to messages from the current week
        timeFilteredMessages = await this.getWeeklyMessages(client);
      } else {
        timeFilteredMessages = await this.getMonthlyMessages(client);
      }

      // Filter by direction if not 'All'
      let filteredMessages = timeFilteredMessages;
      if (direction && direction !== "All") {
        filteredMessages = timeFilteredMessages.filter(
          (msg) => msg.direction.toLowerCase() === direction.toLowerCase()
        );
      }

      // Sort by date (most recent first)
      const sortedMessages = [...filteredMessages].sort((a, b) => {
        return new Date(b.date).getTime() - new Date(a.date).getTime();
      });

      // Take the top N messages and format the response
      const recentMessages = sortedMessages.slice(0, limit).map((msg) => {
        // Parse the date to format a datetime string
        const messageDate = new Date(msg.date);

        // Format the date as YYYY-MM-DD HH:MM
        const year = messageDate.getFullYear();
        const month = String(messageDate.getMonth() + 1).padStart(2, "0");
        const day = String(messageDate.getDate()).padStart(2, "0");
        const hours = messageDate.getHours().toString().padStart(2, "0");
        const minutes = messageDate.getMinutes().toString().padStart(2, "0");

        // Create datetime string in format: "YYYY-MM-DD HH:MM"
        const time = `${year}-${month}-${day}T${hours}:${minutes}`;

        return {
          id: msg.id,
          time,
          mtMessageType: msg.mtMessageType,
          status: msg.status,
          direction: msg.direction,
        };
      });

      // Log summary of applied filters
      let filterDesc = "";
      if (period !== "All") filterDesc += ` for ${period.toLowerCase()} period`;
      if (direction !== "All") filterDesc += ` with ${direction} direction`;
      return recentMessages;
    } catch (error) {
      throw error;
    }
  }

  /**
   * Get error statistics categorized by error type
   * @param timeFilter 'daily', 'weekly', 'monthly', or 'all'
   * @param direction 'inward', 'outward', or 'all'
   * @returns Object containing counts of different error types
   */
  public async getErrorStatistics(
    client: OpenSearchClient,
    timeFilter: string,
    direction: string
  ): Promise<{
    fieldErrors: number;
    notSupportedErrors: number;
    invalidErrors: number;
    otherErrors: number;
    totalErrors: number;
  }> {
    try {
      // Get messages for the specified time period
      let messages: MessageDocument[];

      switch (timeFilter) {
        case "Daily":
          messages = await this.getDailyMessages(client);
          break;
        case "Weekly":
          messages = await this.getWeeklyMessages(client);
          break;
        default:
          messages = await this.getMonthlyMessages(client);
          break;
      }

      // Apply direction filter if specified and not 'all'
      if (direction !== "All") {
        messages = messages.filter(
          (msg) => msg.direction.toLowerCase() === direction.toLowerCase()
        );
      }

      // Filter to only failed messages
      const failedMessages = messages.filter(
        (msg) => msg.status.toLowerCase() === "failed"
      );

      // Count different error types
      const fieldErrors = failedMessages.filter(
        (msg) => msg.fieldError && msg.fieldError.trim() !== ""
      ).length;
      const notSupportedErrors = failedMessages.filter(
        (msg) => msg.notSupportedError && msg.notSupportedError.trim() !== ""
      ).length;
      const invalidErrors = failedMessages.filter(
        (msg) => msg.invalidError && msg.invalidError.trim() !== ""
      ).length;
      const otherErrors = failedMessages.filter(
        (msg) =>
          (msg.otherError && msg.otherError.trim() !== "") ||
          ((!msg.fieldError || msg.fieldError.trim() === "") &&
            (!msg.notSupportedError || msg.notSupportedError.trim() === "") &&
            (!msg.invalidError || msg.invalidError.trim() === ""))
      ).length;

      // Build result
      const result = {
        fieldErrors,
        notSupportedErrors,
        invalidErrors,
        otherErrors,
        totalErrors: failedMessages.length,
      };

      // Log summary
      let filterDesc = "";
      if (timeFilter !== "All") filterDesc += ` for ${timeFilter} period`;
      if (direction !== "All") filterDesc += ` with ${direction} direction`;

      return result;
    } catch (error) {
      throw error;
    }
  }
}

export default OpenSearchService;
