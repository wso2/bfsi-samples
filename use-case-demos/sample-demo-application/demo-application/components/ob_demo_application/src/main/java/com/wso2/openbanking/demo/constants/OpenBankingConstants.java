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

package com.wso2.openbanking.demo.constants;

/**
 * Constants for the non-regulated Open Banking API: JSON field names, RAR authorization detail
 * types, code sets, permissions, and URL path segments.
 */
public final class OpenBankingConstants {

    private OpenBankingConstants() { }

    // JSON top-level field names
    public static final String FIELD_DATA = "Data";
    public static final String FIELD_RISK = "Risk";

    // Rich Authorization Request fields
    public static final String FIELD_AUTHORIZATION_DETAILS      = "authorization_details";
    public static final String FIELD_TYPE                       = "type";
    public static final String FIELD_PERMISSIONS                = "Permissions";
    public static final String FIELD_EXPIRATION_DATE_TIME       = "ExpirationDateTime";
    public static final String FIELD_TRANSACTION_FROM_DATE_TIME = "TransactionFromDateTime";
    public static final String FIELD_TRANSACTION_TO_DATE_TIME   = "TransactionToDateTime";

    /** Authorization detail type requesting account information access. */
    public static final String TYPE_ACCOUNT_INFORMATION = "account_information_v1.0";
    /** Authorization detail type requesting a single domestic payment. */
    public static final String TYPE_DOMESTIC_PAYMENT    = "domestic_payment_v1.0";

    // Account JSON fields
    public static final String FIELD_ACCOUNT    = "Account";
    public static final String FIELD_ACCOUNT_ID = "AccountId";
    public static final String FIELD_BALANCE    = "Balance";
    public static final String FIELD_AMOUNT     = "Amount";
    public static final String FIELD_CURRENCY   = "Currency";
    public static final String FIELD_NICKNAME   = "Nickname";
    public static final String FIELD_NAME       = "Name";
    public static final String FIELD_CONSENT_ID = "ConsentId";

    // Transaction JSON fields
    public static final String FIELD_TRANSACTION             = "Transaction";
    public static final String FIELD_TRANSACTION_ID          = "TransactionId";
    public static final String FIELD_BOOKING_DATE_TIME       = "BookingDateTime";
    public static final String FIELD_TRANSACTION_INFORMATION = "TransactionInformation";
    public static final String FIELD_CREDIT_DEBIT_INDICATOR  = "CreditDebitIndicator";

    // Payment initiation fields
    public static final String FIELD_INSTRUCTION_IDENTIFICATION = "InstructionIdentification";
    public static final String FIELD_END_TO_END_IDENTIFICATION  = "EndToEndIdentification";
    public static final String FIELD_LOCAL_INSTRUMENT           = "LocalInstrument";
    public static final String FIELD_INSTRUCTED_AMOUNT          = "InstructedAmount";
    public static final String FIELD_CREDITOR_ACCOUNT           = "CreditorAccount";
    public static final String FIELD_DEBTOR_ACCOUNT             = "DebtorAccount";
    public static final String FIELD_REMITTANCE_INFORMATION     = "RemittanceInformation";
    public static final String FIELD_UNSTRUCTURED               = "Unstructured";
    public static final String FIELD_REFERENCE                  = "Reference";
    public static final String FIELD_SCHEME_NAME                = "SchemeName";
    public static final String FIELD_IDENTIFICATION             = "Identification";
    public static final String FIELD_SECONDARY_IDENTIFICATION   = "SecondaryIdentification";
    public static final String FIELD_INITIATION                 = "Initiation";

    // ISO external code sets used by the non-regulated standard
    public static final String SCHEME_BBAN                            = "BBAN";
    public static final String LOCAL_INSTRUMENT_DOMESTIC_CREDIT_TRANSFER = "DomesticCreditTransfer";

    // Account information permissions
    public static final String PERM_READ_ACCOUNTS_BASIC       = "ReadAccountsBasic";
    public static final String PERM_READ_ACCOUNTS_DETAIL      = "ReadAccountsDetail";
    public static final String PERM_READ_BALANCES             = "ReadBalances";
    public static final String PERM_READ_TRANSACTIONS_BASIC   = "ReadTransactionsBasic";
    public static final String PERM_READ_TRANSACTIONS_DETAIL  = "ReadTransactionsDetail";
    public static final String PERM_READ_TRANSACTIONS_CREDITS = "ReadTransactionsCredits";
    public static final String PERM_READ_TRANSACTIONS_DEBITS  = "ReadTransactionsDebits";

    /** The only scope the RAR flow requests; the granted access is described by authorization_details. */
    public static final String SCOPE_OPENID = "openid";

    // URL path segments
    public static final String PATH_CONSENTS          = "/consents";
    public static final String PATH_ACCOUNTS          = "/accounts";
    public static final String PATH_BALANCES          = "/balances";
    public static final String PATH_TRANSACTIONS      = "/transactions";
    public static final String PATH_DOMESTIC_PAYMENTS = "/domestic-payments";

    // Timezone
    public static final String TIMEZONE_OFFSET = "+05:30";

    // Fallback display strings
    public static final String DEFAULT_ACCOUNT_NAME   = "Open Banking Account";
    public static final String DEFAULT_STANDARD_ACCOUNT = "Standard Account";

    // Payment ID prefixes
    public static final String PAYMENT_INSTRUCTION_PREFIX  = "INST-";
    public static final String PAYMENT_END_TO_END_PREFIX   = "E2E-";
    public static final String PAYMENT_SECONDARY_ID_SUFFIX = "001";
    public static final String PAYMENT_SECONDARY_ID_FIXED  = "0002";
}
