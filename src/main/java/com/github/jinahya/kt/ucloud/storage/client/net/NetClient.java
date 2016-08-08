/*
 * Copyright 2016 Jin Kwon &lt;onacit at gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.kt.ucloud.storage.client.net;

import com.github.jinahya.kt.ucloud.storage.client.StorageClient;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import static java.util.Collections.singletonList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.concurrent.TimeUnit.MINUTES;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class NetClient extends StorageClient {

    private static final Logger logger = getLogger(NetClient.class.getName());

    private static StringBuilder queryParameters(
            final StringBuilder builder,
            final Map<String, List<Object>> params) {
        logger.log(Level.FINE, "queryParameters({0}, {1})",
                   new Object[]{builder, params});
        if (params != null && !params.isEmpty()) {
            builder.append('?').append(
                    params.entrySet().stream().flatMap(
                            e -> e.getValue().stream()
                            .map(v -> e.getKey() + "=" + v))
                    .collect(joining("&")));
        }
        return builder;
    }

    private static <T extends URLConnection> T requestProperties(
            final T connection,
            final Map<String, List<Object>> requestProperties) {
        if (requestProperties != null) {
            requestProperties.forEach((n, vs) -> {
                vs.forEach(v -> {
                    connection.setRequestProperty(n, String.valueOf(v));
                });
            });
        }
        return connection;
    }

    // ----------------------------------------------------------------- account
    public static StringBuilder buildAccount(
            final String storageUrl,
            final Map<String, List<Object>> params) {
        return queryParameters(new StringBuilder(storageUrl), params);
    }

    public static URL locateAccount(
            final String storageUrl,
            final Map<String, List<Object>> params)
            throws MalformedURLException {
        return new URL(buildAccount(storageUrl, params).toString());
    }

    public static URLConnection openAccount(
            final String storageUrl, final Map<String, List<Object>> params)
            throws IOException {
        return locateAccount(storageUrl, params).openConnection();
    }

    // --------------------------------------------------------------- container
    public static StringBuilder buildContainer(
            final String storageUrl, final String containerName,
            final Map<String, List<Object>> params) {
        return queryParameters(
                new StringBuilder(storageUrl)
                .append('/')
                .append(containerName),
                params);
    }

    public static URL locateContainer(
            final String storageUrl, final String containerName,
            final Map<String, List<Object>> params)
            throws MalformedURLException {
        return new URL(buildContainer(storageUrl, containerName, params)
                .toString());
    }

    public static URLConnection openContainer(
            final String storageUrl, final String containerName,
            final Map<String, List<Object>> params)
            throws IOException {
        return locateContainer(storageUrl, containerName, params)
                .openConnection();
    }

    // ------------------------------------------------------------------ object
    public static StringBuilder buildObject(
            final String storageUrl, final String containerName,
            final String objectName, final Map<String, List<Object>> params) {
        return queryParameters(
                new StringBuilder(storageUrl)
                .append('/')
                .append(containerName)
                .append('/')
                .append(objectName),
                params);
    }

    public static URL locateObject(
            final String storageUrl, final String containerName,
            final String objectName, final Map<String, List<Object>> params)
            throws MalformedURLException {
        return new URL(buildObject(storageUrl, containerName, objectName,
                                   params).toString());
    }

    public static URLConnection openObject(
            final String storageUrl, final String containerName,
            final String objectName, final Map<String, List<Object>> params)
            throws IOException {
        return locateObject(storageUrl, containerName, objectName, params)
                .openConnection();
    }

    // -------------------------------------------------------------------------
    public NetClient(final String storageUrl, final String authUser,
                     final String authPass) {
        super(storageUrl, authUser, authPass);
    }

    public NetClient(final StorageClient client) {
        this(client.getStorageUrl(), client.getAuthUser(),
             client.getAuthPass());
        storageUrl = client.getStorageUrl();
        authToken = client.getAuthToken();
        authTokenExpires = client.getAuthTokenExpires();
    }

    // -------------------------------------------------------------------------
    @Override
    protected void authenticateUser() {
        try {
            authenticateUser(
                    n -> {
                        return null;
                    });
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public <T> T authenticateUser(final Function<URLConnection, T> function)
            throws IOException {
        final HttpURLConnection connection
                = (HttpURLConnection) new URL(authUrl).openConnection();
        connection.setRequestMethod("GET");
        final Map<String, List<Object>> headers = new HashMap<>();
        headers.put(HEADER_X_AUTH_USER, singletonList(authUser));
        headers.put(HEADER_X_AUTH_PASS, singletonList(authPass));
        headers.put(HEADER_X_AUTH_NEW_TOKEN, singletonList(Boolean.TRUE));
        requestProperties(connection, headers);
        System.out.println("properties set");
        connection.setDoOutput(false);
        connection.setDoInput(true);
        if (connectTimeout != null) {
            connection.setConnectTimeout(connectTimeout);
        }
        connection.connect();
        try {
            System.out.println("connected");
            final int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                throw new IOException(
                        "failed to authenticate user; statusCode="
                        + statusCode);
            }
            storageUrl = connection.getHeaderField(HEADER_X_STORAGE_URL);
            authToken = connection.getHeaderField(HEADER_X_AUTH_TOKEN);
            setAuthTokenExpires(
                    connection.getHeaderField(HEADER_X_AUTH_TOKEN_EXPIRES));
            return function.apply(connection);
        } finally {
            connection.disconnect();
        }
    }

    public <T> T authenticateUser(
            final BiFunction<URLConnection, NetClient, T> function)
            throws IOException {
        return authenticateUser(
                n -> {
                    return function.apply(n, this);
                }
        );
    }

    public NetClient authenticateUser(final Consumer<URLConnection> consumer)
            throws IOException {
        return authenticateUser(
                n -> {
                    consumer.accept(n);
                    return this;
                }
        );
    }

    public NetClient authenticateUser(
            final BiConsumer<URLConnection, NetClient> consumer)
            throws IOException {
        return authenticateUser(
                n -> {
                    consumer.accept(n, this);
                }
        );
    }

    protected void ensureValid() throws IOException {
        if (!isValid(MINUTES, 10L)) {
            authenticateUser(
                    n -> {
                    }
            );
        }
    }

    // ------------------------------------------------------------------ object
    public <T> T peekObject(final String containerName, final String objectName,
                            final Map<String, List<Object>> params,
                            Map<String, List<Object>> headers,
                            final Function<URLConnection, T> function)
            throws IOException {
        ensureValid();
        final HttpURLConnection connection = (HttpURLConnection) openObject(
                storageUrl, containerName, objectName, params);
        connection.setRequestMethod("HEAD");
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        requestProperties(connection, headers);
        connection.setDoOutput(false);
        connection.setDoInput(false); // @@?
        if (connectTimeout != null) {
            connection.setConnectTimeout(connectTimeout);
        }
        connection.connect();
        try {
            return function.apply(connection);
        } finally {
            connection.disconnect();
        }
    }

    public <T> T peekObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<URLConnection, NetClient, T> function)
            throws IOException {
        return peekObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    return function.apply(n, this);
                }
        );
    }

    public NetClient peekObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<URLConnection> consumer)
            throws IOException {
        return peekObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return this;
                }
        );
    }

    public NetClient peekObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<URLConnection, NetClient> consumer)
            throws IOException {
        return peekObject(
                containerName,
                objectName,
                params,
                headers,
                (n, c) -> {
                    consumer.accept(n, c);
                }
        );
    }

    public <T> T updateObject(final String containerName,
                              final String objectName,
                              final Map<String, List<Object>> params,
                              Map<String, List<Object>> headers,
                              final Function<URLConnection, T> function)
            throws IOException {
        ensureValid();
        final HttpURLConnection connection = (HttpURLConnection) openObject(
                storageUrl, containerName, objectName, params);
        connection.setRequestMethod("PUT");
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        requestProperties(connection, headers);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        if (connectTimeout != null) {
            connection.setConnectTimeout(connectTimeout);
        }
        connection.connect();
        try {
            return function.apply(connection);
        } finally {
            connection.disconnect();
        }
    }

    public <T> T updateObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<URLConnection, NetClient, T> function)
            throws IOException {
        return updateObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    return function.apply(n, this);
                }
        );
    }

    public NetClient updateObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<URLConnection> consumer)
            throws IOException {
        return updateObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return this;
                }
        );
    }

    public NetClient updateObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<URLConnection, NetClient> consumer)
            throws IOException {
        return updateObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    consumer.accept(n, this);
                }
        );
    }
}
