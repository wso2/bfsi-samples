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

import React from 'react';
import TableRow from './TableRow';
import './Table.scss';
import { TableFilters } from '../MessagesPage'; // Make sure this path is correct

interface TableProps {
    data: {
        id: string;
        mtMessageType: string;
        mxMessageType: string;
        currency: string;
        date: string;
        direction: string;
        amount: string;
        status: string;
        originalMessage: string;
        translatedMessage: string;
    }[];
    onRowClick: (id: string) => void;
    filters?: TableFilters;
    onFilterChange?: (filters: TableFilters) => void;
    disableFilters?: boolean;
    forceRender?: number; // Add this prop to force re-renders
}

const Table: React.FC<TableProps> = ({ 
    data, 
    onRowClick, 
    filters = {
        id: '',
        mtMessageType: '',
        mxMessageType: '',
        direction: '',
        amountMin: '',
        amountMax: '',
        currency: '',
        dateFrom: '',
        dateTo: '',
        status: ''
    }, 
    onFilterChange = () => {},
    disableFilters = false,
    forceRender 
}) => {
    
    const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        
        // Update filters through the callback
        onFilterChange({
            ...filters,
            [name]: value
        });
    };

    const filteredData = data;

    const handleRowClick = (id: string) => {
        // Ensure the ID exists in the filtered data before triggering the click
        const itemExists = filteredData.some(item => item.id === id);
        if (itemExists) {
            onRowClick(id);
        }
    };

    return (
        <div className='tableContainer'>
            <table className='table'>
                <thead>
                    <tr>
                        <th>
                            <div className='msgType'>Message ID</div>
                            {!disableFilters && (
                                <input 
                                    type="text" 
                                    name="id" 
                                    value={filters.id} 
                                    onChange={handleFilterChange} 
                                    placeholder="Search ID" 
                                    className={filters.id ? 'activeFilter' : ''}
                                />
                            )}
                        </th>
                        <th>
                            <div className='msgType'>Mt Message Type</div>
                            {!disableFilters && (
                                <input 
                                    type="text" 
                                    name="mtMessageType" 
                                    value={filters.mtMessageType} 
                                    onChange={handleFilterChange} 
                                    placeholder="Search Mt Type" 
                                    className={filters.mtMessageType ? 'activeFilter' : ''}
                                />
                            )}
                        </th>
                        <th>
                            <div className='msgType'>Mx Message Type</div>
                            {!disableFilters && (
                                <input 
                                    type="text" 
                                    name="mxMessageType" 
                                    value={filters.mxMessageType} 
                                    onChange={handleFilterChange} 
                                    placeholder="Search Mx Type" 
                                    className={filters.mxMessageType ? 'activeFilter' : ''}
                                />
                            )}
                        </th>
                        <th>
                            <div className='msgType'>Direction</div>
                            {!disableFilters && (
                                <select 
                                    name="direction" 
                                    value={filters.direction} 
                                    onChange={handleFilterChange}
                                    className={filters.direction ? 'activeFilter' : ''}
                                >
                                    <option value="">All</option>
                                    <option value="Inward">Inward</option>
                                    <option value="Outward">Outward</option>
                                </select>
                            )}
                        </th>
                        <th>
                            <div className='msgType'>Amount</div>
                            {!disableFilters && (
                                <div className='amountFilter'>
                                    <input 
                                        type="number" 
                                        name="amountMin" 
                                        value={filters.amountMin} 
                                        onChange={handleFilterChange} 
                                        placeholder="Min" 
                                        className={filters.amountMin ? 'activeFilter' : ''}
                                    />
                                    <input 
                                        type="number" 
                                        name="amountMax" 
                                        value={filters.amountMax} 
                                        onChange={handleFilterChange} 
                                        placeholder="Max" 
                                        className={filters.amountMax ? 'activeFilter' : ''}
                                    />
                                </div>
                            )}
                        </th>
                        <th>
                            <div className='msgType'>Currency</div>
                            {!disableFilters && (
                                <input 
                                    type="text" 
                                    name="currency" 
                                    value={filters.currency} 
                                    onChange={handleFilterChange} 
                                    placeholder="Search Currency" 
                                    className={filters.currency ? 'activeFilter' : ''}
                                />
                            )}
                        </th>
                        <th>
                            <div className='msgType'>DateTime</div>
                            <div>
                            {!disableFilters && (
                                <div className='dateRangeFilter'>
                                    <input 
                                        type="date" 
                                        name="dateFrom" 
                                        value={filters.dateFrom} 
                                        onChange={handleFilterChange}
                                        className={filters.dateFrom ? 'activeFilter' : ''}
                                        placeholder="From"
                                        title="From date"
                                        style={{ paddingBottom: '1px', paddingTop: '1px', marginTop: '3px'  }}
                                    />
                                    <input 
                                        type="date" 
                                        name="dateTo" 
                                        value={filters.dateTo} 
                                        onChange={handleFilterChange}
                                        className={filters.dateTo ? 'activeFilter' : ''}
                                        placeholder="To"
                                        title="To date"
                                        style={{ paddingBottom: '1px', paddingTop: '1px', marginTop: '0'  }}
                                    />
                                </div>
                            )}
                            </div>
                        </th>
                        <th>
                            <div className='msgType'>Status</div>
                            {!disableFilters && (
                                <select 
                                    name="status" 
                                    value={filters.status} 
                                    onChange={handleFilterChange}
                                    className={filters.status ? 'activeFilter' : ''}
                                >
                                    <option value="">All</option>
                                    <option value="Successful">Successful</option>
                                    <option value="Failed">Failed</option>
                                </select>
                            )}
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {filteredData.length > 0 ? (
                        filteredData.map((item) => (
                            <TableRow
                                key={item.id}
                                data={item}
                                onClick={() => handleRowClick(item.id)}
                            />
                        ))
                    ) : (
                        <tr className='noResults'>
                            <td colSpan={8}>No matching messages found</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

// Export as a memoized component to prevent unnecessary re-renders
export default React.memo(Table);
