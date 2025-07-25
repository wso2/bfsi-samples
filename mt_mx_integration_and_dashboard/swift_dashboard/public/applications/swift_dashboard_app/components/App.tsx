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
import { Route, Switch } from 'react-router-dom';
import { CoreStart } from '../../../../../../src/core/public'; 
import { AppPluginStartDependencies } from '../../../types'; 
import Header from './Header';
import OverviewPage from './OverviewPage';
import MessagesPage from './MessagesPage';
import LogsPage from './LogsPage';

interface AppProps {
  coreStart: CoreStart;
  plugins: AppPluginStartDependencies;
}

const App: React.FC<AppProps> = ({ coreStart, plugins }) => {
  // You can now access OpenSearch Dashboards services like:
  // coreStart.http - for API requests
  // coreStart.notifications - for showing toasts
  // plugins.data - for data queries

  return (
    <div className="swift-dashboard-plugin">
      <Header />
      <div className="app-container">
        <Switch>
          <Route path="/messages" component={MessagesPage} />
          <Route path="/logs" component={LogsPage} />
          <Route path="/" component={OverviewPage} />
        </Switch>
      </div>
    </div>
  );
};

export default App;
