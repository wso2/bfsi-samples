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

package com.wso2.openbanking.demo.http;

import com.wso2.openbanking.demo.devconsole.FlowEntry;
import com.wso2.openbanking.demo.devconsole.FlowRecorder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/** Builds and executes HTTPS requests with configurable method, headers, and body. */
public class HttpConnection {

    private final String url;
    private final SSLContext sslContext;
    private final String method;
    private final Map<String, String> headers;
    private String body;
    private boolean followRedirects = true;

    /**
     * Creates an HttpConnection with the given URL, SSL context, and HTTP method.
     *
     * @param url        target URL for the request
     * @param sslContext SSL context used for the secure connection
     * @param method     HTTP method (e.g. GET, POST, DELETE)
     */
    private HttpConnection(String url, SSLContext sslContext, String method) {
        this.url = url;
        this.sslContext = sslContext;
        this.method = method;
        this.headers = new HashMap<>();
        this.body = null;
    }

    /**
     * Creates a POST request to the given URL.
     *
     * @param url        target URL
     * @param sslContext SSL context for the connection
     * @return new HttpConnection configured for POST
     */
    public static HttpConnection post(String url, SSLContext sslContext) {
        return new HttpConnection(url, sslContext, "POST");
    }

    /**
     * Creates a GET request to the given URL.
     *
     * @param url        target URL
     * @param sslContext SSL context for the connection
     * @return new HttpConnection configured for GET
     */
    public static HttpConnection get(String url, SSLContext sslContext) {
        return new HttpConnection(url, sslContext, "GET");
    }

    /**
     * Creates a DELETE request to the given URL.
     *
     * @param url        target URL
     * @param sslContext SSL context for the connection
     * @return new HttpConnection configured for DELETE
     */
    public static HttpConnection delete(String url, SSLContext sslContext) {
        return new HttpConnection(url, sslContext, "DELETE");
    }

    /**
     * Adds a request header to the connection.
     *
     * @param key   header name
     * @param value header value
     * @return this HttpConnection instance for chaining
     */
    public HttpConnection addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    /**
     * Sets the request body.
     *
     * @param body request body as a string
     * @return this HttpConnection instance for chaining
     */
    public HttpConnection withBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * Executes the request and returns the response body.
     *
     * @return response body as a string
     * @throws IOException if the request fails
     */
    public String execute() throws IOException {
        FlowEntry entry = FlowRecorder.start(method, url, headers, body);
        long startedAt = System.currentTimeMillis();
        try {
            HttpsURLConnection connection = createConnection();
            if (body != null) {
                writeBody(connection);
            }
            String response = readResponse(connection);
            int status = connection.getResponseCode();
            FlowRecorder.finish(entry, status, response,
                    System.currentTimeMillis() - startedAt, status < 200 || status >= 300);
            return response;
        } catch (IOException e) {
            FlowRecorder.finish(entry, 0, String.valueOf(e),
                    System.currentTimeMillis() - startedAt, true);
            throw e;
        }
    }

    /**
     * Executes the request and returns the HTTP status code.
     *
     * @return HTTP response status code
     * @throws IOException if the request fails
     */
    public int executeAndGetStatus() throws IOException {
        FlowEntry entry = FlowRecorder.start(method, url, headers, body);
        long startedAt = System.currentTimeMillis();
        try {
            HttpsURLConnection connection = createConnection();
            if (body != null) {
                writeBody(connection);
            }
            int status = connection.getResponseCode();
            FlowRecorder.finish(entry, status, "(status only, body not read)",
                    System.currentTimeMillis() - startedAt, status < 200 || status >= 300);
            return status;
        } catch (IOException e) {
            FlowRecorder.finish(entry, 0, String.valueOf(e),
                    System.currentTimeMillis() - startedAt, true);
            throw e;
        }
    }

    /**
     * Creates and configures the underlying HTTPS connection.
     *
     * @return configured HttpsURLConnection ready for use
     * @throws IOException if the connection cannot be opened
     */
    private HttpsURLConnection createConnection() throws IOException {
        URL urlObj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
        connection.setSSLSocketFactory(sslContext.getSocketFactory());
        connection.setRequestMethod(method);
        connection.setInstanceFollowRedirects(followRedirects);
        connection.setDoInput(true);
        if (body != null || "POST".equals(method)) {
            connection.setDoOutput(true);
        }
        for (Map.Entry<String, String> header : headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
        return connection;
    }

    /**
     * Writes the request body to the connection output stream.
     *
     * @param connection the open HTTPS connection to write to
     * @throws IOException if writing the body fails
     */
    private void writeBody(HttpsURLConnection connection) throws IOException {
        if (body == null) {
            return;
        }
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
    }

    /**
     * Reads the response body from the connection.
     *
     * @param connection the open HTTPS connection to read from
     * @return response body as a string
     * @throws IOException if reading the response fails
     */
    private String readResponse(HttpsURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        InputStream is = (responseCode >= 200 && responseCode < 300)
                ? connection.getInputStream()
                : connection.getErrorStream();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }
}
