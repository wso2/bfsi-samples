/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * Calculates a future date by adding a specified number of days to the current date
 *
 * @param {number} daysToAdd - The number of days to add to the current date
 * @returns {string} The calculated date in ISO format (YYYY-MM-DD)
 *
 * @example
 * // If today is 2026-02-03
 * calculateNextDate(30) // Returns "2026-03-05"
 * calculateNextDate(7)  // Returns "2026-02-10"
 */
export const calculateNextDate = (daysToAdd: number): string => {
    const currentDate = new Date();
    const futureDate = new Date(currentDate);
    futureDate.setDate(currentDate.getDate() + daysToAdd);

    // Format as YYYY-MM-DD
    const year = futureDate.getFullYear();
    const month = String(futureDate.getMonth() + 1).padStart(2, '0');
    const day = String(futureDate.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
};

/**
 * Processes standing orders by converting relative day numbers to actual dates
 *
 * @param {Array} standingOrders - Array of standing order objects with nextDate as number
 * @returns {Array} Array of standing orders with nextDate converted to date string
 *
 * @example
 * const orders = [
 *   { id: "SO-001", reference: "RENT", nextDate: 30, amount: "1000" }
 * ];
 * const processed = processStandingOrders(orders);
 * // Returns: [{ id: "SO-001", reference: "RENT", nextDate: "2026-03-05", amount: "1000" }]
 */
export const processStandingOrders = (standingOrders: any[]): any[] => {
    return standingOrders.map(order => ({
        ...order,
        nextDate: typeof order.nextDate === 'number'
            ? calculateNextDate(order.nextDate)
            : order.nextDate
    }));
};
/**
 * Calculates a past date by subtracting a specified number of days from the current date
 *
 * @param {number} daysAgo - The number of days to subtract from the current date
 * @returns {string} The calculated date in ISO format (YYYY-MM-DD)
 *
 * @example
 * // If today is 2026-02-03
 * calculatePastDate(5)  // Returns "2026-01-29"
 * calculatePastDate(10) // Returns "2026-01-24"
 */
export const calculatePastDate = (daysAgo: number): string => {
    const currentDate = new Date();
    const pastDate = new Date(currentDate);
    pastDate.setDate(currentDate.getDate() - daysAgo);
    const year = pastDate.getFullYear();
    const month = String(pastDate.getMonth() + 1).padStart(2, '0');
    const day = String(pastDate.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
};

/**
 * Processes transactions by converting relative day numbers to actual past dates
 *
 * @param {Array} transactions - Array of transaction objects with date as number
 * @returns {Array} Array of transactions with date converted to date string
 *
 * @example
 * const txns = [
 *   { id: "T001", date: 5, amount: "100.00" }
 * ];
 * const processed = processTransactions(txns);
 * // Returns: [{ id: "T001", date: "2026-01-29", amount: "100.00" }]
 */
export const processTransactions = (transactions: any[]): any[] => {
    return transactions.map(txn => ({
        ...txn,
        date: typeof txn.date === 'number'
            ? calculatePastDate(txn.date)
            : txn.date
    }));
};

/**
 * Processes both standing orders and transactions for all banks
 *
 * @param {Array} banks - Array of bank objects
 * @returns {Array} Array of banks with both standing orders and transactions processed
 *
 * @example
 * const config = { banks: [...] };
 * const fullyProcessed = processAllBankDates(config.banks);
 */
export const processAllBankDates = (banks: any[]): any[] => {
    return banks.map(bank => {
        const processedAccounts = bank.accounts?.map((account: any) => ({
            ...account,
            transactions: account.transactions
                ? processTransactions(account.transactions)
                : []
        })) || [];

        const processedStandingOrders = bank.standingOrders
            ? processStandingOrders(bank.standingOrders)
            : [];

        return {
            ...bank,
            accounts: processedAccounts,
            standingOrders: processedStandingOrders
        };
    });
};
