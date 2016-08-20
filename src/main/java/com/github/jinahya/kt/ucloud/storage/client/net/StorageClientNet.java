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
import com.github.jinahya.kt.ucloud.storage.client.StorageClientException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Boolean.TRUE;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import static java.util.Collections.singletonList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class StorageClientNet
        extends StorageClient<StorageClientNet, InputStream, URLConnection> {

    private static final Logger logger
            = getLogger(StorageClientNet.class.getName());

    /**
     * Appends given query parameters to specified {@code builder}.
     *
     * @param builder the builder
     * @param params the query parameters; may be {@code null}
     * @return given builder
     */
    private static StringBuilder params(
            final StringBuilder builder,
            final Map<String, List<Object>> params) {
        if (params != null && !params.isEmpty()) {
            builder.append('?').append(
                    params.entrySet().stream().flatMap(
                            e -> e.getValue().stream()
                            .map(v -> e.getKey() + "=" + v))
                    .collect(joining("&")));
        }
        return builder;
    }

    /**
     * Put given request headers to specified {@code connection}.
     *
     * @param <T> connection type parameter
     * @param connection the connection
     * @param headers request headers; may be {@code null}
     * @return given connection
     * @see URLConnection#addRequestProperty(java.lang.String, java.lang.String)
     */
    private static <T extends URLConnection> T headers(
            final T connection, final Map<String, List<Object>> headers) {
        if (headers != null) {
            headers.forEach((n, vs) -> {
                vs.forEach(v -> {
                    connection.addRequestProperty(n, String.valueOf(v));
                });
            });
        }
        return connection;
    }

    /**
     * Parses {@code Status-Code} and {@code Reason-Phrase} from the given
     * {@code connection} and applies them the the specified {@code function}.
     *
     * @param <R> result type parameter
     * @param connection the connection
     * @param function the function
     * @return the value the {@code function} results
     */
    public static <R> R status(final HttpURLConnection connection,
                               final BiFunction<Integer, String, R> function) {
        try {
            final int statusCode = connection.getResponseCode();
            final String reasonPhrase = connection.getResponseMessage();
            return function.apply(statusCode, reasonPhrase);
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    /**
     * Returns the {@code Status-Code} of given {@code connection}
     *
     * @param connection the connection
     * @return the {@code Status-Code} of given {@code connection}
     */
    public static int statusCode(final HttpURLConnection connection) {
        return status(connection, (c, p) -> c);
    }

    /**
     * Returns the {@code Reason-Phrase} of given {@code connection}.
     *
     * @param connection the connection
     * @return the {@code Reason-Phrase} of given {@code connection}
     */
    public static String reasonPhrase(final HttpURLConnection connection) {
        return status(connection, (c, p) -> p);
    }

    // ---------------------------------------------------------------- /storage
    public static StringBuilder buildStorage(
            final String storageUrl,
            final Map<String, List<Object>> params) {
        return params(new StringBuilder(storageUrl), params);
    }

    public static URL locateStorage(
            final String storageUrl,
            final Map<String, List<Object>> params)
            throws MalformedURLException {
        return new URL(buildStorage(storageUrl, params).toString());
    }

    public static URLConnection openStorage(
            final String storageUrl, final Map<String, List<Object>> params)
            throws IOException {
        return locateStorage(storageUrl, params).openConnection();
    }

    // ------------------------------------------------------ /storage/container
    public static StringBuilder buildContainer(
            final String storageUrl, final String containerName,
            final Map<String, List<Object>> params) {
        return params(
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

    // ----------------------------------------------- /storage/container/object
    public static StringBuilder buildObject(
            final String storageUrl, final String containerName,
            final String objectName, final Map<String, List<Object>> params) {
        return params(
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

    // ---------------------------------------------------------------- /account
    /**
     * Sets {@link #HEADER_X_AUTH_ADMIN_USER} and
     * {@link #HEADER_X_AUTH_ADMIN_KEY} as request properties on given
     * connection.
     *
     * @param <T> url connection type parameter
     * @param connection the connection
     * @param authUser the value of {@link #HEADER_X_AUTH_ADMIN_USER}
     * @param authKey the value of {@link #HEADER_X_AUTH_ADMIN_KEY}
     * @return given connection
     * @see URLConnection#setRequestProperty(java.lang.String, java.lang.String)
     */
    public static <T extends URLConnection> T setXAuthAdminCredential(
            final T connection, final String authUser, final String authKey) {
        connection.setRequestProperty(HEADER_X_AUTH_ADMIN_USER, authUser);
        connection.setRequestProperty(HEADER_X_AUTH_ADMIN_KEY, authKey);
        return connection;
    }

    public static StringBuilder buildAccount(
            final String accountUrl,
            final Map<String, List<Object>> params) {
        final StringBuilder builder = new StringBuilder(accountUrl);
        params(builder, params);
        return builder;
    }

    public static URL locateAccount(
            final String accountUrl,
            final Map<String, List<Object>> params)
            throws MalformedURLException {
        final StringBuilder builder = buildAccount(accountUrl, params);
        final URL locator = new URL(builder.toString());
        return locator;
    }

    public static URLConnection openAccount(
            final String accountUrl, final Map<String, List<Object>> params,
            final String authAdminUser, final String authAdminKey)
            throws IOException {
        final URL locator = locateAccount(accountUrl, params);
        final URLConnection connection = locator.openConnection();
        setXAuthAdminCredential(connection, authAdminUser, authAdminKey);
//        connection.setRequestProperty(HEADER_X_AUTH_ADMIN_USER, authAdminUser);
//        connection.setRequestProperty(HEADER_X_AUTH_ADMIN_KEY, authAdminKey);
        return connection;
    }

    // ----------------------------------------------------------- /account/user
    public static StringBuilder buildUser(
            final String accountUrl, final String userName,
            final Map<String, List<Object>> params) {
        final StringBuilder builder
                = new StringBuilder(accountUrl)
                .append("/")
                .append(userName);
        params(builder, params);
        return builder;
    }

    public static URL locateUser(
            final String accountUrl, final String userName,
            final Map<String, List<Object>> params)
            throws MalformedURLException {
        final StringBuilder builder
                = buildUser(accountUrl, userName, params);
        final URL locator = new URL(builder.toString());
        return locator;
    }

    /**
     * Opens a URLConnection for user.
     *
     * @param accountUrl base URL
     * @param userName username
     * @param params query parameters
     * @param authAdminUser the value for {@link #HEADER_X_AUTH_ADMIN_USER}
     * @param authAdminKey the value for {@link #HEADER_X_AUTH_ADMIN_KEY}
     * @return an opened URLConnection
     * @throws IOException if an I/O error occurs.
     */
    public static URLConnection openUser(
            final String accountUrl, final String userName,
            final Map<String, List<Object>> params, final String authAdminUser,
            final String authAdminKey)
            throws IOException {
        final URL locator = locateUser(accountUrl, userName, params);
        final URLConnection connection = locator.openConnection();
        setXAuthAdminCredential(connection, authAdminUser, authAdminKey);
//        connection.setRequestProperty(HEADER_X_AUTH_ADMIN_USER, authAdminUser);
//        connection.setRequestProperty(HEADER_X_AUTH_ADMIN_KEY, authAdminKey);
        return connection;
    }

    // -------------------------------------------------------------------------
    public StorageClientNet(final String authUrl, final String authUser,
                            final String authKey) {
        super(authUrl, authUser, authKey);
    }

    @Deprecated
    public StorageClientNet(final StorageClient client) {
        this(client.getAuthUrl(), client.getAuthUser(), client.getAuthKey());
        setStorageUrl(client.getStorageUrl());
        setAuthToken(client.getAuthToken());
        setAuthTokenExpires(client.getAuthTokenExpires());
    }

//    // -------------------------------------------------------------------------
//    @Override
//    protected int statusCode(final URLConnection connection) {
//        return statusCode(((HttpURLConnection) connection));
//    }
    // -------------------------------------------------------------------------
    @Override
    public <R> R authenticateUser(final boolean newToken,
                                  final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) new URL(authUrl).openConnection();
            connection.setRequestMethod("GET");
            final Map<String, List<Object>> headers = new HashMap<>();
            headers.put(HEADER_X_AUTH_USER, singletonList(authUser));
            headers.put(HEADER_X_AUTH_PASS, singletonList(authKey));
            if (newToken) {
                headers.put(HEADER_X_AUTH_NEW_TOKEN, singletonList(TRUE));
            }
            headers(connection, headers);
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
                setStorageUrl(connection.getHeaderField(HEADER_X_STORAGE_URL));
                setAuthToken(connection.getHeaderField(HEADER_X_AUTH_TOKEN));
                setAuthTokenExpires(
                        connection.getHeaderField(HEADER_X_AUTH_TOKEN_EXPIRES));
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    // ---------------------------------------------------------------- /account
    @Override
    public <R> R peekStorage(final Map<String, List<Object>> params,
                             Map<String, List<Object>> headers,
                             final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openStorage(getStorageUrl(), params);
            connection.setRequestMethod("HEAD");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R readStorage(final Map<String, List<Object>> params,
                             Map<String, List<Object>> headers,
                             final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) openStorage(
                    getStorageUrl(), params);
            connection.setRequestMethod("GET");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R configureStorage(final Map<String, List<Object>> params,
                                  Map<String, List<Object>> headers,
                                  final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openStorage(
                            getStorageUrl(), params);
            connection.setRequestMethod("POST");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.putIfAbsent(HEADER_X_AUTH_TOKEN,
                                singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    // ------------------------------------------------------ /account/container
    public <R> R peekContainer(final String containerName,
                               final Map<String, List<Object>> params,
                               Map<String, List<Object>> headers,
                               final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openContainer(
                            getStorageUrl(), containerName, params);
            connection.setRequestMethod("HEAD");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R readContainer(final String containerName,
                               final Map<String, List<Object>> params,
                               Map<String, List<Object>> headers,
                               final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openContainer(
                            getStorageUrl(), containerName, params);
            connection.setRequestMethod("GET");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R updateContainer(final String containerName,
                                 final Map<String, List<Object>> params,
                                 Map<String, List<Object>> headers,
                                 final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openContainer(
                            getStorageUrl(), containerName, params);
            connection.setRequestMethod("PUT");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R configureContainer(final String containerName,
                                    final Map<String, List<Object>> params,
                                    Map<String, List<Object>> headers,
                                    final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openContainer(
                            getStorageUrl(), containerName, params);
            connection.setRequestMethod("POST");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R deleteContainer(final String containerName,
                                 final Map<String, List<Object>> params,
                                 Map<String, List<Object>> headers,
                                 final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openContainer(
                            getStorageUrl(), containerName, params);
            connection.setRequestMethod("DELETE");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    // ----------------------------------------------- /account/container/object
    public <R> R peekObject(final String containerName, final String objectName,
                            final Map<String, List<Object>> params,
                            Map<String, List<Object>> headers,
                            final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) openObject(
                    getStorageUrl(), containerName, objectName, params);
            connection.setRequestMethod("HEAD");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(false); // @@?
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R readObject(final String containerName, final String objectName,
                            final Map<String, List<Object>> params,
                            Map<String, List<Object>> headers,
                            final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) openObject(
                    getStorageUrl(), containerName, objectName, params);
            connection.setRequestMethod("GET");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R updateObject(final String containerName,
                              final String objectName,
                              final Map<String, List<Object>> params,
                              Map<String, List<Object>> headers,
                              final Supplier<InputStream> entity,
                              final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) openObject(
                    getStorageUrl(), containerName, objectName, params);
            connection.setRequestMethod("PUT");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setChunkedStreamingMode(0);
            connection.connect();
            try {
                try (InputStream input = entity.get();
                     OutputStream output = connection.getOutputStream()) {
                    final byte[] buffer = new byte[8192];
                    for (int read; (read = input.read(buffer)) != -1;) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                }
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <T> T configureObject(final String containerName,
                                 final String objectName,
                                 final Map<String, List<Object>> params,
                                 Map<String, List<Object>> headers,
                                 final Function<URLConnection, T> function) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) openObject(
                    getStorageUrl(), containerName, objectName, params);
            connection.setRequestMethod("POST");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R deleteObject(final String containerName,
                              final String objectName,
                              final Map<String, List<Object>> params,
                              Map<String, List<Object>> headers,
                              final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) openObject(
                    getStorageUrl(), containerName, objectName, params);
            connection.setRequestMethod("DELETE");
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(HEADER_X_AUTH_TOKEN, singletonList(getAuthToken()));
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    // ---------------------------------------------------------------- /account
    @Override
    public <R> R readAccount(
            final Map<String, List<Object>> params,
            Map<String, List<Object>> headers,
            final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openAccount(
                            accountUrl(), params, authUser, authKey);
            connection.setRequestMethod("GET");
//            if (headers != null) {
//                headers.put(HEADER_X_AUTH_USER, singletonList(authUser));
//                headers.put(HEADER_X_AUTH_PASS, singletonList(authKey));
//            }
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    // ----------------------------------------------------------- /account/user
    @Override
    public <R> R readUser(final String userName,
                          final Map<String, List<Object>> params,
                          final Map<String, List<Object>> headers,
                          final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openUser(
                            accountUrl(), userName, params, authUser, authKey);
            connection.setRequestMethod("GET");
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R updateUser(final String userName, final String userKey,
                            final Boolean userAdmin,
                            final Map<String, List<Object>> params,
                            final Map<String, List<Object>> headers,
                            final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openUser(
                            accountUrl(), userName, params, authUser, authKey);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty(HEADER_X_AUTH_USER_KEY, userKey);
            if (userAdmin != null && userAdmin) {
                connection.setRequestProperty(
                        HEADER_X_AUTH_USER_ADMIN, userKey);
            }
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    @Override
    public <R> R deleteUser(final String userName,
                            final Map<String, List<Object>> params,
                            final Map<String, List<Object>> headers,
                            final Function<URLConnection, R> function) {
        try {
            final HttpURLConnection connection
                    = (HttpURLConnection) openUser(
                            accountUrl(), userName, params, authUser, authKey);
            connection.setRequestMethod("DELETE");
            headers(connection, headers);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    // -------------------------------------------------------- /account/.groups
    @Override
    public <R> R readGroups(final Map<String, List<Object>> params,
                            final Map<String, List<Object>> headers,
                            final Function<URLConnection, R> function) {
        final StringBuilder builder = params(
                buildAccount(accountUrl(), null).append('/').append(".groups"),
                params);
        try {
            final URL url = new URL(builder.toString());
            final HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();
            if (headers != null) {
                headers(connection, headers);
            }
            setXAuthAdminCredential(connection, authUser, authKey);
            connection.connect();
            try {
                return function.apply(connection);
            } finally {
                connection.disconnect();
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }
}
