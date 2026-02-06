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

import type { Step } from 'react-joyride';

export const DEMO_STEPS: Step[] = [
    {
        target: '.add-account-btn',
        content: 'You can experience the Accounts initiation process here.',
        title: 'Add New Account  (1/4)',
        disableBeacon: true,
    },
    {
        target: '.pay-bills-button',
        content: 'You can experience the payment initiation process here. ',
        title: 'Pay Your Bills  (2/4)',
        disableBeacon: true,
    },
    {
        target: '.view-all-transactions',
        content: 'You can view all transactions here. ',
        title: 'View All Transactions (3/4)',
        disableBeacon: true,  // ✅ Added this
    },
    {
        target: '.view-all-standing-orders',
        content: 'You can view all standing orders. ',
        title: 'View All Standing Orders  (4/4)',
        disableBeacon: true,  // ✅ Added this
    },
];
