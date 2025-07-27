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

import React, { useState, useEffect, useMemo } from "react";
import { withRouter } from "react-router-dom";
import {
  useReactTable,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  flexRender,
} from "@tanstack/react-table";
import "./LogsPage.scss";
import apiService from "../../../services";
import { getDayRange, getMonthRange } from "./utils/dateUtils";

const month = getMonthRange();
const today = getDayRange();

function DateRangeFilter({ column, onFilterChange }) {
  // Get the current filter value from the table state
  const filterValue = column.getFilterValue() || {};
  const [filters, setFilters] = useState({
    dateFrom: filterValue.dateFrom || month.startDateStr,
    dateTo: filterValue.dateTo || today.endDateStr,
  });

  // Handle changes to the date inputs
  const handleFilterChange = (e) => {
    const { name, value } = e.target;

    // Update the local state
    const newFilters = { ...filters, [name]: value };
    setFilters(newFilters);

    // Call the parent's handler with the new range
    // We pass the entire object as the filter value
    const filterToSet =
      newFilters.dateFrom || newFilters.dateTo ? newFilters : undefined;
    onFilterChange(column.id, filterToSet);
  };

  return (
    <div className="date-range-filter"
    >
      <label>
        From
        <input
          type="date"
          name="dateFrom"
          value={filters.dateFrom}
          onChange={handleFilterChange}
          className={filters.dateFrom ? "activeFilter" : ""}
          title="From date"
        />
      </label>
      <label>
        To
        <input
          type="date"
          name="dateTo"
          value={filters.dateTo}
          onChange={handleFilterChange}
          className={filters.dateTo ? "activeFilter" : ""}
          title="To date"
        />
      </label>
    </div>
  );
}

// Default column filter component
function DefaultColumnFilter({ column, onFilterChange }) {
  const filterValue = column.getFilterValue();
  return (
    <input
      value={(filterValue || "") as string}
      onChange={(e) => onFilterChange(column.id, e.target.value)}
      placeholder={`Filter ${column.columnDef.header}...`}
      className="column-filter-input"
    />
  );
}

// Dropdown Filter Component for 'level'
function LevelFilter({ column, levels, onFilterChange }) {
  const filterValue = column.getFilterValue();
  const selectedValue =
    filterValue === undefined || filterValue === null ? "ALL" : String(filterValue);  

  return (
    <select
      value={selectedValue}
      onChange={(e) => {
        const newValue = e.target.value;
        const filterToSet = newValue === "ALL" ? undefined : newValue;
        onFilterChange(column.id, filterToSet);
      }}
      className="column-filter-select"
    >
      <option value="ALL">All Levels</option>
      {levels.map((level) => (
        <option key={level} value={level}>
          {level}
        </option>
      ))}
    </select>
  );
}

const LogsPage: React.FC<{ location?: any }> = ({ location }) => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [columnFilters, setColumnFilters] = useState<any[]>([
    { id: "level", value: "INFO" },
  ]);

  // Function to handle filter changes and update the state
  const onFilterChange = React.useCallback((id, value) => {
    setColumnFilters((old) => {
    // Filter out the old filter for this ID
    const newFilters = old.filter((f) => f.id !== id);
    // Add the new filter only if its value is not undefined (or null)
    if (value !== undefined && value !== null) {
      newFilters.push({ id, value });
    }
    return newFilters;
  });
  }, []);

  // Fetch all logs when component mounts
  useEffect(() => {
    const fetchLogs = async () => {
      try {
        setLoading(true);
        const dateFilter = columnFilters.find((f) => f.id === 'timestamp');
        const dateFrom = dateFilter ? dateFilter.value?.dateFrom : month.startDateStr;
        const dateTo = dateFilter ? dateFilter.value?.dateTo : today.endDateStr;
        const logData = await apiService.getAllLogs(dateFrom, dateTo);

        setData(logData);
        setError(null);
      } catch (err) {
        setError("Failed to fetch logs. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchLogs();
  }, [columnFilters]);

  const uniqueLevels = useMemo(() => {
    const levels = new Set<string>();
    data.forEach((log) => {
      if (log.level) levels.add(log.level);
    });
    return Array.from(levels).sort();
  }, [data]);

  const dateRangeFilter = (row, columnId, filterValue) => {
    // filterValue is an object: { dateFrom: 'YYYY-MM-DD', dateTo: 'YYYY-MM-DD' }
    if (!filterValue || (!filterValue.dateFrom && !filterValue.dateTo)) {
      return true; // No filter applied
    }

    const rowDate = new Date(row.original.timestamp);
    const dateFrom = filterValue.dateFrom
      ? new Date(filterValue.dateFrom)
      : null;
    const dateTo = filterValue.dateTo ? new Date(filterValue.dateTo) : null;

    // Convert to UTC to prevent timezone issues with comparisons
    const rowDateUTC = new Date(
      rowDate.getTime() + rowDate.getTimezoneOffset() * 60000
    );

    if (dateFrom && dateTo) {
      // Check if the row date is between the start and end dates (inclusive)
      // We add a day to the end date to include all logs from that day.
      const endPlusOneDay = new Date(dateTo);
      endPlusOneDay.setDate(endPlusOneDay.getDate() + 1);
      return rowDateUTC >= dateFrom && rowDateUTC < endPlusOneDay;
    } else if (dateFrom) {
      return rowDateUTC >= dateFrom;
    } else if (dateTo) {
      const endPlusOneDay = new Date(dateTo);
      endPlusOneDay.setDate(endPlusOneDay.getDate() + 1);
      return rowDateUTC < endPlusOneDay;
    }

    return true;
  };

  const columns = useMemo(
    () => [
      {
        accessorKey: "timestamp",
        header: "Timestamp",
        cell: (info) => new Date(info.getValue()).toLocaleString(),
        Filter: ({ column }) => (
          <DateRangeFilter column={column} onFilterChange={onFilterChange} />
        ),
        filterFn: dateRangeFilter,
      },
      {
        accessorKey: "level",
        header: "Level",
        cell: (info) => info.getValue(),
        filterFn: "equals",
        Filter: ({ column }) => (
          <LevelFilter
            column={column}
            levels={uniqueLevels}
            onFilterChange={onFilterChange}
          />
        ),
      },
      {
        accessorKey: "module",
        header: "Module",
        cell: (info) => info.getValue(),
        filterFn: "includesString",
        Filter: ({ column }) => (
          <DefaultColumnFilter
            column={column}
            onFilterChange={onFilterChange}
          />
        ),
      },
      {
        accessorKey: "message",
        header: "Message",
        cell: (info) => info.getValue(),
        filterFn: "includesString",
        Filter: ({ column }) => (
          <DefaultColumnFilter
            column={column}
            onFilterChange={onFilterChange}
          />
        ),
      },
    ],
    [uniqueLevels, onFilterChange]
  );

  const table = useReactTable({
    data,
    columns,
    state: {
      columnFilters,
    },
    filterFns: {
      dateRange: dateRangeFilter,
    },
    onColumnFiltersChange: setColumnFilters,
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
  });

  return (
    <div className="container">
      <h2>Log Viewer</h2>
      {loading && <p style={{ textAlign: "center" }}>Loading logs...</p>}
      {error && (
        <p style={{ textAlign: "center", color: "red" }}>Error: {error}</p>
      )}
      {!loading && !error && (
        <div className="log-table-container">
          <div className="table-wrapper">
            <table>
              <thead>
                {table.getHeaderGroups().map((headerGroup) => (
                  <tr key={headerGroup.id}>
                    {headerGroup.headers.map((header) => (
                      <th
                        key={header.id}
                        colSpan={header.colSpan}
                        style={header.style}
                      >
                        {header.isPlaceholder ? null : (
                          <>
                            {flexRender(
                              header.column.columnDef.header,
                              header.getContext()
                            )}
                            {header.column.getCanFilter() ? (
                              <div className={`column-filter ${header.id}`}>
                                {flexRender(header.column.columnDef.Filter, {
                                  ...header.getContext(),
                                  onFilterChange,
                                })}
                              </div>
                            ) : null}
                          </>
                        )}
                      </th>
                    ))}
                  </tr>
                ))}
              </thead>
              <tbody>
                {table.getRowModel().rows.length ? (
                  table.getRowModel().rows.map((row) => (
                    <tr key={row.id}>
                      {row.getVisibleCells().map((cell) => (
                        <td key={cell.id}>
                          {flexRender(
                            cell.column.columnDef.cell,
                            cell.getContext()
                          )}
                        </td>
                      ))}
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td
                      colSpan={columns.length}
                      style={{ textAlign: "center" }}
                    >
                      No logs found.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
          <div className="pagination-controls">
            <button
              onClick={() => table.previousPage()}
              disabled={!table.getCanPreviousPage()}
            >
              {"<"}
            </button>
            <button
              onClick={() => table.nextPage()}
              disabled={!table.getCanNextPage()}
            >
              {">"}
            </button>
            <span>
              Page{" "}
              <strong>
                {table.getState().pagination.pageIndex + 1} of{" "}
                {table.getPageCount()}
              </strong>
            </span>
            <select
              value={table.getState().pagination.pageSize}
              onChange={(e) => table.setPageSize(Number(e.target.value))}
            >
              {[10, 20, 30, 40, 50].map((pageSize) => (
                <option key={pageSize} value={pageSize}>
                  Show {pageSize}
                </option>
              ))}
            </select>
          </div>
        </div>
      )}
    </div>
  );
};

export default withRouter(LogsPage);
