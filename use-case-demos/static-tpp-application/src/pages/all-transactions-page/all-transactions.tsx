/**
 * Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
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


import ApplicationLayout from "../../layouts/application-layout/application-layout.tsx";
import PaymentAccountPageLayout from "../../layouts/payment-account-page-layout/payment-account-page-layout.tsx";
import {Box, Button} from "@oxygen-ui/react";
import type {TableConfigs, TransactionData} from "../../hooks/config-interfaces.ts";
import {useState, useEffect} from "react";
import TableComponent from "../../components/table-component.tsx";

interface AllTransactionsProps {
    name: string;
    transactions: TransactionData[];
    transactionTableHeaderData?: TableConfigs[];
}

/**
 * @function AllTransactionsPage
 * @description A page component dedicated to displaying a paginated list of all
 * transaction data within the application layout. It handles state for pagination
 * and renders transaction details in a table, including credit/debit status.
 */
const AllTransactionsPage = ({name, transactions,
                                 transactionTableHeaderData}: AllTransactionsProps) => {

    const itemsPerPage = 10;

    const getInitialPage = () => {
        const savedPage = sessionStorage.getItem('allTransactionsCurrentPage');
        return savedPage ? parseInt(savedPage, 10) : 1;
    };
    const [currentPage, setCurrentPage] = useState(getInitialPage);
    const totalPages = Math.ceil(transactions.length / itemsPerPage);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const transactionsToDisplay = transactions.slice(startIndex, endIndex);
    useEffect(() => {
        sessionStorage.setItem('allTransactionsCurrentPage', currentPage.toString());
    }, [currentPage]);
    useEffect(() => {
        if (currentPage > totalPages && totalPages > 0) {
            setCurrentPage(totalPages);
        }
    }, [transactions, currentPage, totalPages]);
    const handlePageChange = (newPage: number) => {
        if (newPage >= 1 && newPage <= totalPages) {
            setCurrentPage(newPage);
            // Scroll to top when page changes
            window.scrollTo({top: 0, behavior: 'smooth'});
        }
    }
    const handlePrevious = () => {
        if (currentPage > 1) {
            handlePageChange(currentPage - 1);
        }
    }
    const handleNext = () => {
        if (currentPage < totalPages) {
            handlePageChange(currentPage + 1);
        }
    }
    const showPrevButton = currentPage > 1;
    const showNextButton = currentPage < totalPages;
    return (
        <>
            <ApplicationLayout name={name}>
                <PaymentAccountPageLayout title={"Transactions"}>
                    <Box className={'table-container'}>
                        <TableComponent
                            dataLimit={9}
                            tableData={transactionsToDisplay}
                            tableType={"transaction"}
                            dataConfigs={transactionTableHeaderData}
                        />
                        {/* Pagination Controls */}
                        {totalPages > 1 && (
                            <Box className="pagination-container" sx={{
                                display: 'flex',
                                justifyContent: 'center',
                                alignItems: 'center',
                                gap: '1rem',
                                marginTop: '2rem',
                                flexWrap: 'wrap'
                            }}>
                                {/* Previous Button */}
                                {showPrevButton && (
                                    <Button
                                        onClick={handlePrevious}
                                        variant="outlined"
                                        sx={{
                                            minWidth: '100px',
                                            height: '40px',
                                        }}
                                    >
                                        Previous
                                    </Button>
                                )}

                                {/* Page Numbers */}
                                <Box sx={{
                                    display: 'flex',
                                    gap: '0.5rem',
                                    alignItems: 'center',
                                    flexWrap: 'wrap',
                                    justifyContent: 'center'
                                }}>
                                    {Array.from({length: totalPages}, (_, i) => i + 1)
                                        .map((page) => (
                                        <Button
                                            key={page}
                                            onClick={() => handlePageChange(page)}
                                            variant={currentPage === page ? "contained" : "outlined"}
                                            sx={{
                                                minWidth: '40px',
                                                height: '40px',
                                                fontWeight: currentPage === page ? 'bold' : 'normal'
                                            }}
                                        >
                                            {page}
                                        </Button>
                                    ))}
                                </Box>

                                {/* Next Button */}
                                {showNextButton && (
                                    <Button
                                        onClick={handleNext}
                                        variant="outlined"
                                        sx={{
                                            minWidth: '100px',
                                            height: '40px',
                                        }}
                                    >
                                        Next
                                    </Button>
                                )}
                            </Box>
                        )}

                        {/* Page Info */}
                        {totalPages > 1 && (
                            <Box sx={{
                                textAlign: 'center',
                                marginTop: '1rem',
                                color: 'text.secondary',
                                fontSize: '0.875rem'
                            }}>
                                Page {currentPage} of {totalPages} | Showing {startIndex + 1}-
                                {Math.min(endIndex, transactions.length)} of {transactions.length} transactions
                            </Box>
                        )}
                    </Box>
                </PaymentAccountPageLayout>
            </ApplicationLayout>
        </>
    )
}

export default AllTransactionsPage;
