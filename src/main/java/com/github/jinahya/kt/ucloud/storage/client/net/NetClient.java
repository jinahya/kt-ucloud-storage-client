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
import static java.lang.System.currentTimeMillis;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import static java.util.Collections.singletonList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.MINUTES;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
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
            final Map<String, List<Object>> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            builder.append('?').append(
                    parameters.entrySet().stream().flatMap(
                            e -> e.getValue().stream()
                            .map(v -> e.getKey() + "=" + v))
                    .collect(joining("&")));
        }
        return builder;
    }

    private static <T extends URLConnection> T requestProperties(
            final T connection, final Map<String, List<Object>> properties) {
        if (properties != null) {
            properties.forEach((n, vs) -> {
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

    public <R> R authenticateUser(final Function<URLConnection, R> function)
            throws IOException {
        final HttpURLConnection connection
                = (HttpURLConnection) new URL(authUrl).openConnection();
        connection.setRequestMethod("GET");
        final Map<String, List<Object>> headers = new HashMap<>();
        headers.put(HEADER_X_AUTH_USER, singletonList(authUser));
        headers.put(HEADER_X_AUTH_PASS, singletonList(authPass));
        headers.put(HEADER_X_AUTH_NEW_TOKEN, singletonList(Boolean.TRUE));
        requestProperties(connection, headers);
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.connect();
        try {
            final int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                final String reasonPhrase = connection.getResponseMessage();
                throw new IOException(
                        "failed to authenticate user; " + statusCode + " "
                        + reasonPhrase);
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

    public <R> R authenticateUser(
            final BiFunction<URLConnection, NetClient, R> function)
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

    protected void ensureValid(final TimeUnit unit, final long duration)
            throws IOException {
        if (!isValid(currentTimeMillis() + unit.toMillis(duration))) {
            authenticateUser(
                    n -> {
                    }
            );
        }
    }

    protected void ensureValid() throws IOException {
        ensureValid(MINUTES, 10L);
    }

    // ----------------------------------------------------------------- account
    public <R> R peekAccount(final Map<String, List<Object>> params,
                             Map<String, List<Object>> headers,
                             final Function<URLConnection, R> function)
            throws IOException {
        ensureValid();
        final HttpURLConnection connection = (HttpURLConnection) openAccount(
                storageUrl, params);
        connection.setRequestMethod("HEAD");
        if (headers == null) {
            headers = new HashMap<>();
        }
//        headers.putIfAbsent(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        headers.put(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        requestProperties(connection, headers);
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.connect();
        try {
            return function.apply(connection);
        } finally {
            connection.disconnect();
        }
    }

    public <R> R peekAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<URLConnection, NetClient, R> function)
            throws IOException {
        return peekAccount(
                params,
                headers,
                n -> {
                    return function.apply(n, this);
                }
        );
    }

    public NetClient peekAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<URLConnection> consumer)
            throws IOException {
        return peekAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return this;
                }
        );
    }

    public NetClient peekAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<URLConnection, NetClient> consumer)
            throws IOException {
        return peekAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n, this);
                }
        );
    }

    public <R> R readAccount(final Map<String, List<Object>> params,
                             Map<String, List<Object>> headers,
                             final Function<URLConnection, R> function)
            throws IOException {
        ensureValid();
        final HttpURLConnection connection = (HttpURLConnection) openAccount(
                storageUrl, params);
        connection.setRequestMethod("GET");
        if (headers == null) {
            headers = new HashMap<>();
        }
        //headers.putIfAbsent(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        headers.put(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        requestProperties(connection, headers);
        connection.setDoOutput(false);
        connection.setDoInput(true); // @@?
        connection.connect();
        try {
            return function.apply(connection);
        } finally {
            connection.disconnect();
        }
    }

    public <R> R readAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<URLConnection, NetClient, R> function)
            throws IOException {
        return readAccount(
                params,
                headers,
                n -> {
                    return function.apply(n, this);
                }
        );
    }

    public NetClient readAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<URLConnection> consumer)
            throws IOException {
        return readAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return this;
                }
        );
    }

    public NetClient readAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<URLConnection, NetClient> consumer)
            throws IOException {
        return readAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n, this);
                }
        );
    }

    public <R> R configureAccount(final Map<String, List<Object>> params,
                                  Map<String, List<Object>> headers,
                                  final Function<URLConnection, R> function)
            throws IOException {
        ensureValid();
        final HttpURLConnection connection = (HttpURLConnection) openAccount(
                storageUrl, params);
        connection.setRequestMethod("POST");
        if (headers == null) {
            headers = new HashMap<>();
        }
        //headers.put(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        // for testing...
        headers.putIfAbsent(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        requestProperties(connection, headers);
        connection.setDoOutput(false);
        connection.setDoInput(true); // @@?
        connection.connect();
        try {
            return function.apply(connection);
        } finally {
            connection.disconnect();
        }
    }

    public <R> R configureAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<URLConnection, NetClient, R> function)
            throws IOException {
        return configureAccount(
                params,
                headers,
                n -> {
                    return function.apply(n, this);
                }
        );
    }

    public NetClient configureAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<URLConnection> consumer)
            throws IOException {
        return configureAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return this;
                }
        );
    }

    public NetClient configureAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<URLConnection, NetClient> consumer)
            throws IOException {
        return configureAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n, this);
                }
        );
    }

    // --------------------------------------------------------------- container
    public <R> R peekContainer(final String containerName,
                               final Map<String, List<Object>> params,
                               Map<String, List<Object>> headers,
                               final Function<URLConnection, R> function)
            throws IOException {
        ensureValid();
        final HttpURLConnection connection = (HttpURLConnection) openContainer(
                storageUrl, containerName, params);
        connection.setRequestMethod("HEAD");
        if (headers == null) {
            headers = new HashMap<>();
        }
//        headers.putIfAbsent(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        headers.put(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        requestProperties(connection, headers);
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.connect();
        try {
            return function.apply(connection);
        } finally {
            connection.disconnect();
        }
    }

    public <R> R peekContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<URLConnection, NetClient, R> function)
            throws IOException {
        return peekContainer(
                containerName,
                params,
                headers,
                n -> {
                    return function.apply(n, this);
                }
        );
    }

    public NetClient peekContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<URLConnection> consumer)
            throws IOException {
        return peekContainer(
                containerName,
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return this;
                }
        );
    }

    public NetClient peekContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<URLConnection, NetClient> consumer)
            throws IOException {
        return peekContainer(
                containerName,
                params,
                headers,
                n -> {
                    consumer.accept(n, this);
                }
        );
    }

    public <R> R readContainer(final String containerName,
                               final Map<String, List<Object>> params,
                               Map<String, List<Object>> headers,
                               final Function<URLConnection, R> function)
            throws IOException {
        ensureValid();
        final HttpURLConnection connection = (HttpURLConnection) openContainer(
                storageUrl, containerName, params);
        connection.setRequestMethod("GET");
        if (headers == null) {
            headers = new HashMap<>();
        }
//        headers.putIfAbsent(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        headers.put(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        requestProperties(connection, headers);
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.connect();
        try {
            return function.apply(connection);
        } finally {
            connection.disconnect();
        }
    }

    public <R> R readContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<URLConnection, NetClient, R> function)
            throws IOException {
        return readContainer(
                containerName,
                params,
                headers,
                n -> {
                    return function.apply(n, this);
                }
        );
    }

    public NetClient readContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<URLConnection> consumer)
            throws IOException {
        return readContainer(
                containerName,
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return this;
                }
        );
    }

    public NetClient readContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<URLConnection, NetClient> consumer)
            throws IOException {
        return readContainer(
                containerName,
                params,
                headers,
                n -> {
                    consumer.accept(n, this);
                }
        );
    }

    // ------------------------------------------------------------------ object
    public <R> R peekObject(final String containerName, final String objectName,
                            final Map<String, List<Object>> params,
                            Map<String, List<Object>> headers,
                            final Function<URLConnection, R> function)
            throws IOException {
        ensureValid();
        final HttpURLConnection connection = (HttpURLConnection) openObject(
                storageUrl, containerName, objectName, params);
        connection.setRequestMethod("HEAD");
        if (headers == null) {
            headers = new HashMap<>();
        }
//        headers.putIfAbsent(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        headers.put(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        requestProperties(connection, headers);
        connection.setDoOutput(false);
        connection.setDoInput(false); // @@?
        connection.connect();
        try {
            return function.apply(connection);
        } finally {
            connection.disconnect();
        }
    }

    public <R> R peekObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<URLConnection, NetClient, R> function)
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
                n -> {
                    consumer.accept(n, this);
                }
        );
    }

    public <R> R updateObject(final String containerName,
                              final String objectName,
                              final Map<String, List<Object>> params,
                              Map<String, List<Object>> headers,
                              final Function<URLConnection, R> function)
            throws IOException {
        ensureValid();
        final HttpURLConnection connection = (HttpURLConnection) openObject(
                storageUrl, containerName, objectName, params);
        connection.setRequestMethod("PUT");
        if (headers == null) {
            headers = new HashMap<>();
        }
//        headers.putIfAbsent(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        headers.put(HEADER_X_AUTH_TOKEN, singletonList(authToken));
        requestProperties(connection, headers);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();
        try {
            return function.apply(connection);
        } finally {
            connection.disconnect();
        }
    }

    public <R> R updateObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<URLConnection, NetClient, R> function)
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
