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

import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import './RecentActivity.scss';
import './commonStyles.scss';
import apiService from '../../../services';

interface RecentActivityProps {
  title: string;
  direction: string;
  period: string;
}

interface RecentMessage {
  id: string;
  time: string;
  mtMessageType: string;
  status: string;
  direction: string;
}

const RecentActivity: React.FC<RecentActivityProps> = ({ title, direction, period }) => {
  const history = useHistory();
  const [activities, setActivities] = useState<RecentMessage[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchRecentMessages = async () => {
      try {
        setLoading(true);
        const response = await apiService.getRecentMessages(period, direction);
        setActivities(response.recentMessages);
        setError(null);
      } catch (err) {
        setError('Failed to load recent activity. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchRecentMessages();
  }, [direction, period]);

  const handleRowClick = (id: string) => {
    // Navigate to messages page with the selected message ID and a flag to show details
    history.push('/messages', { 
      selectedMessageId: id,
      showDetails: true
    });
  };

  // Handle button click to view all messages
  const handleViewAllMessages = () => {
    history.push('/messages');
  };
  
  if (loading) {
    return (
      <div className="summary-component">
        <h3>{title}</h3>
        <div className='loading-indicator-activity'>
          <div className='spinner-activity'></div>
          <span className='loading-text'>Loading recent activity...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="summary-component">
        <div className="chart-header">
          <h3>{title}</h3>
          <div className='viewAllButtonContainer'>
            <button 
              className='viewAllButton' 
              onClick={handleViewAllMessages}
            >
              All Messages
            </button>
          </div>
          <div className="chart-info-icon activity" data-tooltip="Shows the most recent messages based on your selected time period and direction filters. Click on a message ID to view its details.">
              <span className="info-icon">i</span>
          </div>
        </div>
        <div className='error-message-activity'>
              <span>{error}</span>
              <button onClick={() => {
                window.location.reload();
              }}>Retry</button>
        </div>
      </div>
    );
  }

  return (
    <div className="summary-component">
      <div className="chart-header">
        <h3>{title}</h3>
        <div className='viewAllButtonContainer'>
          <button 
            className='viewAllButton' 
            onClick={handleViewAllMessages}
          >
            All Messages
          </button>
        </div>
        <div className="chart-info-icon activity" data-tooltip="Shows the most recent messages based on your selected time period and direction filters. Click on a message ID to view its details.">
            <span className="info-icon">i</span>
        </div>
      </div>
      {activities.length === 0 ? (
        <div className='no-data-message-activity'>
          <span>No recent messages found for selected filters</span>
        </div>
      ) : (
        <div className="recent-activity-table-container"> {/* A container div for overall styling */}
          <table className="recent-activity-actual-table"> {/* This will be your actual table */}
            <thead>
              <tr>
                {/* Column Headers - Essential for semantic structure and setting widths */}
                <th className="col-id">ID</th>
                <th className="col-time">Time</th>
                <th className="col-type">Type</th>
                <th className="col-direction">Direction</th>
                <th className="col-status">Status</th>
              </tr>
            </thead>
            <tbody>
            {activities.length > 0 && activities.map((activity, index) => (
              <tr key={index} className="recent-activity-row" onClick={() => handleRowClick(activity.id)}>
                <td className="recent-activity-id">{activity.id}</td>
                <td className="recent-activity-time">{activity.time}</td>
                <td className="recent-activity-type">{activity.mtMessageType}</td>
                <td className={`recent-activity-direction ${activity.direction.toLowerCase()}`}>{activity.direction}</td>
                <td className={`recent-activity-status ${activity.status.toLowerCase()}`}>
                  {activity.status.charAt(0).toUpperCase() + activity.status.slice(1)}
                </td>
              </tr>
            ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default RecentActivity;
