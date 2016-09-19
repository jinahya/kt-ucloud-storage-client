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
package com.github.jinahya.kt.ucloud.storage.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import static java.lang.System.currentTimeMillis;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @param <T> implementation type parameter
 * @param <RequestType> request type parameter
 * @param <ResponseType> response type parameter
 */
public abstract class StorageClient<T extends StorageClient<T, RequestType, ResponseType>, RequestType, ResponseType> {

    private static final Logger logger
            = getLogger(StorageClient.class.getName());

    /**
     * A constant for {@code authUrl} whose value is
     * {@value #AUTH_URL_STANDARD_KOR_CENTER}.
     */
    public static final String AUTH_URL_STANDARD_KOR_CENTER
            = "https://api.ucloudbiz.olleh.com/storage/v1/auth";

    /**
     * A constant for {@code authUrl} whose value is
     * {@value #AUTH_URL_STANDARD_JPN}.
     */
    public static final String AUTH_URL_STANDARD_JPN
            = "https://api.ucloudbiz.olleh.com/storage/v1/authjp";

    /**
     * A constant for {@code authUrl} whose value is
     * {@value #AUTH_URL_LITE_KOR_HA}.
     */
    public static final String AUTH_URL_LITE_KOR_HA
            = "https://api.ucloudbiz.olleh.com/storage/v1/authlite";

    /**
     * A constant for a query parameter whose value is
     * {@value #QUERY_PARAM_LIMIT}.
     */
    public static final String QUERY_PARAM_LIMIT = "limit";

    /**
     * A constant for a query parameter whose value is
     * {@value #QUERY_PARAM_MARKER}.
     */
    public static final String QUERY_PARAM_MARKER = "marker";

    /**
     * A constant for a query parameter whose value is
     * {@value #QUERY_PARAM_FORMAT}.
     */
    public static final String QUERY_PARAM_FORMAT = "format";

//    public static final String HEADER_X_AUTH_USER = "X-Storage-User";
    /**
     * A constant for a header whose value is {@value #HEADER_X_AUTH_USER}.
     */
    public static final String HEADER_X_AUTH_USER = "X-Auth-User";

//    public static final String HEADER_X_AUTH_PASS = "X-Storage-Pass";
    /**
     * A constant for a header whose value is {@value #HEADER_X_AUTH_KEY}.
     */
    public static final String HEADER_X_AUTH_KEY = "X-Auth-Key";

    /**
     * A constant for a header name whose value is
     * {@value #HEADER_X_AUTH_NEW_TOKEN}.
     */
    public static final String HEADER_X_AUTH_NEW_TOKEN = "X-Auth-New-Token";

    /**
     * A constant for a header name whose value is
     * {@value #HEADER_X_AUTH_TOKEN}.
     */
    public static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";

    /**
     * Constant for a header name whose value is
     * {@value #HEADER_X_AUTH_TOKEN_EXPIRES}.
     */
    public static final String HEADER_X_AUTH_TOKEN_EXPIRES
            = "X-Auth-Token-Expires";

    /**
     * Header name whose value is {@value #HEADER_X_STORAGE_URL}.
     */
    public static final String HEADER_X_STORAGE_URL = "X-Storage-Url";

    /**
     * Header name whose value is {@value #HEADER_X_ACCOUNT_OBJECT_COUNT}.
     */
    public static final String HEADER_X_ACCOUNT_OBJECT_COUNT
            = "X-Account-Object-Count";

    public static final String HEADER_X_ACCOUNT_BYTES_USED
            = "X-Account-Bytes-Used";

    public static final String HEADER_X_ACCOUNT_CONTAINER_COUNT
            = "X-Account-Container-Count";

    public static final String HEADER_X_CONTAINER_OBJECT_COUNT
            = "X-Container-Object-Count";

    public static final String HEADER_X_CONTAINER_BYTES_USED
            = "X-Container-Bytes-Used";

    public static final String HEADER_X_CONTAINER_READ = "X-Container-Read";

    public static final String HEADER_X_CONTAINER_WRITE = "X-Container-Write";

    public static final String HEADER_X_REMOVE_CONTAINER_READ
            = "X-Remove-Container-Read";

    public static final String HEADER_X_REMOVE_CONTAINER_WRITE
            = "X-Remove-Container-Write";

    public static final String HEADER_X_COPY_FROM = "X-Copy-From";

    /**
     * A constant for a header name whose value is
     * {@value #HEADER_X_AUTH_ADMIN_USER}.
     */
    public static final String HEADER_X_AUTH_ADMIN_USER = "X-Auth-Admin-User";

    /**
     * A constant for a header name whose value is
     * {@value #HEADER_X_AUTH_ADMIN_KEY}.
     */
    public static final String HEADER_X_AUTH_ADMIN_KEY = "X-Auth-Admin-Key";

    /**
     * A constant for a header name whose value is
     * {@value #HEADER_X_AUTH_USER_KEY}.
     */
    public static final String HEADER_X_AUTH_USER_KEY = "X-Auth-User-Key";

    /**
     * A constant for a header name whose value is
     * {@value #HEADER_X_AUTH_USER_ADMIN}.
     */
    public static final String HEADER_X_AUTH_USER_ADMIN = "X-Auth-User-Admin";

    /**
     * Capitalizes given string. This method does nothing but returning the
     * string if the string is {@code null} or empty.
     *
     * @param token the string to capitalize
     * @return a capitalized value of given string
     */
    public static String capitalize(final String token) {
        if (token == null || token.isEmpty()) {
            return token;
        }
        return token.substring(0, 1).toUpperCase()
               + token.substring(1).toLowerCase();
    }

    /**
     * Capitalizes each token and joins them with '{@code -}'. Note that each
     * token split by '{@code -}' first.
     *
     * @param tokens the tokens
     * @return a string
     */
    public static String capitalizeAndJoin(final String... tokens) {
        return stream(tokens)
                .flatMap(t -> Stream.of(t.split("-")))
                .map(v -> capitalize(v)).collect(joining("-"));
    }

    public static String metaHeader(final boolean remove, final String scope,
                                    final String... tokens) {
        return "X" + (remove ? "-Remove" : "") + "-" + scope + "-Meta" + "-"
               + capitalizeAndJoin(tokens);
    }

    public static String storageMetaHeader(final boolean remove,
                                           final String... tokens) {
        return metaHeader(remove, "Account", tokens);
    }

    public static String containerMetaHeader(final boolean remove,
                                             final String... tokens) {
        return metaHeader(remove, "Container", tokens);
    }

    public static String objectMetaHeader(final boolean remove,
                                          final String... tokens) {
        return metaHeader(remove, "Object", tokens);
    }

    /**
     * Creates a URL for an account from given storage URL and account name.
     *
     * @param storageUrl the storage URL
     * @param accountName the account name
     * @return a URL for an account
     */
    public static String accountUrl(final String storageUrl,
                                    final String accountName) {
        try {
            final URL url = new URL(storageUrl);
            final String protocol = url.getProtocol();
            final String authority = url.getAuthority();
            return protocol + "://" + authority + "/auth/v2/" + accountName;
        } catch (final MalformedURLException murle) {
            throw new StorageClientException(murle);
        }
    }

    /**
     * Accepts each lines of given {@code reader} to specified {@code consumer}.
     *
     * @param reader the reader
     * @param consumer the consumer
     */
    public static void lines(final Reader reader,
                             final Consumer<String> consumer) {
        new BufferedReader(reader).lines().forEach(consumer::accept);
    }

    /**
     * Accepts each lines of given input stream to specified consumer.
     *
     * @param stream the stream
     * @param charset a character set
     * @param consumer the consumer
     */
    public static void lines(final InputStream stream, final Charset charset,
                             final Consumer<String> consumer) {
        lines(new InputStreamReader(stream, charset), consumer);
    }

    /**
     * Accepts each lines of given {@code reader} and given {@code client} to
     * specified {@code consumer}.
     *
     * @param <ClientType> client type parameter
     * @param reader the reader
     * @param consumer the consumer
     * @param client the client
     * @return given {@code client}
     */
    public static <ClientType extends StorageClient> ClientType lines(
            final Reader reader, final BiConsumer<String, ClientType> consumer,
            final ClientType client) {
        lines(reader, l -> consumer.accept(l, client));
        return client;
    }

    public static <ClientType extends StorageClient> ClientType lines(
            final InputStream stream, final Charset charset,
            final BiConsumer<String, ClientType> consumer,
            final ClientType client) {
        return lines(new InputStreamReader(stream, charset), consumer, client);
    }

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance.
     *
     * @param authUrl a URL for authentication
     * @param authUser username
     * @param authKey password
     */
    public StorageClient(final String authUrl, final String authUser,
                         final String authKey) {
        this.authUrl = requireNonNull(authUrl, "null authUrl");
        this.authUser = requireNonNull(authUser, "null authUser");
        {
            final int i = this.authUser.indexOf(':');
            accountName = i == -1 ? null : this.authUser.substring(0, i);
        }
        this.authKey = requireNonNull(authKey, "null authKey");
    }

    // -------------------------------------------------------------------------
    /**
     * Returns the value of {@code Status-Code} of given response.
     *
     * @param response the response
     * @return the value of {@code Status-Code} of given response
     */
    public abstract int getStatusCode(ResponseType response);

    // -------------------------------------------------------------------------
    /**
     * Authenticates user.
     *
     * @param <R> result type parameter
     * @param newToken a flag for refreshing the token.
     * @param function a function results the result
     * @return the value the function results
     */
    public abstract <R> R authenticateUser(boolean newToken,
                                           Function<ResponseType, R> function);

    /**
     * Authenticates user and returns the status code.
     *
     * @param newToken a flag for refreshing the token.
     * @return status code of user authentication
     */
    public int authenticateUser(final boolean newToken) {
        return authenticateUser(newToken, this::getStatusCode);
    }

    /**
     * Purges authentication information.
     *
     * @return this client
     */
    public T invalidate() {
        storageUrl = null;
        authToken = null;
        authTokenExpires = null;
        return (T) this;
    }

    /**
     * Checks if the authorization information is valid until given
     * milliseconds.
     *
     * @param until the milliseconds
     * @return {@code true} if the token is value until given milliseconds,
     * {@code false} otherwise.
     */
    public boolean isValid(final long until) {
        return storageUrl != null && authToken != null
               && authTokenExpires != null
               && authTokenExpires.getTime() >= until;
    }

    /**
     * Checks if the authentication token is valid in specified unit and
     * duration.
     *
     * @param unit the unit of time
     * @param duration the duration of the time unit.
     * @return {@code true} if the token is valid until specified time;
     * {@code false} otherwise.
     */
    public boolean isValid(final TimeUnit unit, final long duration) {
        return isValid(currentTimeMillis() + unit.toMillis(duration));
    }

    // ---------------------------------------------------------------- /storage
    /**
     * Peeks the storage using the {@code HEAD} method. Note that the
     * {@code Accept} header (with any value; e.g. *\/*} might be required.
     *
     * @param <R> result type parameter
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value the {@code function} results
     */
    public abstract <R> R peekStorage(Map<String, List<Object>> params,
                                      Map<String, List<Object>> headers,
                                      Function<ResponseType, R> function);

    /**
     * Reads the storage using {@code GET} method.
     *
     * @param <R> result type parameter
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value the {@code function} results
     */
    public abstract <R> R readStorage(final Map<String, List<Object>> params,
                                      final Map<String, List<Object>> headers,
                                      final Function<ResponseType, R> function);

    /**
     * Reads container names and accepts each of them to given consumer. Put
     * parameters such as {@link #QUERY_PARAM_LIMIT} or
     * {@link #QUERY_PARAM_MARKER} into {@code params} if required.
     *
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function for yielding a {@code Reader} from the server
     * response. Make the function to throw a {@link StorageClientException} to
     * stop the iteration.
     * @param consumer a consumer accepts each container names
     * @return this client
     */
    public T readStorageContainerNames(
            Map<String, List<Object>> params,
            Map<String, List<Object>> headers,
            final Function<ResponseType, Reader> function,
            final Consumer<String> consumer) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.putIfAbsent(QUERY_PARAM_LIMIT, singletonList(512));
        if (headers == null) {
            headers = new HashMap<>();
        }
        for (Iterator<String> i = headers.keySet().iterator(); i.hasNext();) {
            if ("accept".equalsIgnoreCase(i.next())) {
                i.remove();
            }
        }
        headers.put("Accept", singletonList("text/plain"));
        final String marker_;
        {
            String marker__;
            try {
                marker__ = params.get(QUERY_PARAM_MARKER).get(0).toString();
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                marker__ = null;
            }
            marker_ = marker__;
        }
        for (final String[] marker = new String[]{marker_};;) {
            if (marker[0] != null) {
                params.put(QUERY_PARAM_MARKER, singletonList(marker[0]));
            }
            marker[0] = null;
            final Object result = readStorage(
                    params,
                    headers,
                    r -> {
                        try {
                            try {
                                try (Reader reader = function.apply(r)) {
                                    lines(reader, consumer);
                                }
                                return null;
                            } catch (final StorageClientException sce) {
                                return sce;
                            }
                        } catch (final IOException ioe) {
                            throw new StorageClientException(ioe);
                        }
                    }
            );
            if (result != null) {
                break;
            }
            if (marker[0] == null) {
                break;
            }
        }
        return (T) this;
    }

    public abstract <R> R configureStorage(Map<String, List<Object>> params,
                                           Map<String, List<Object>> headers,
                                           Function<ResponseType, R> function);

    // ------------------------------------------------------ /storage/container
    /**
     * Peeks a container using {@code HEAD} method.
     *
     * @param <R> result type parameter
     * @param containerName the name of the container
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value the {@code function} results
     */
    public abstract <R> R peekContainer(String containerName,
                                        Map<String, List<Object>> params,
                                        Map<String, List<Object>> headers,
                                        Function<ResponseType, R> function);

    /**
     * Reads a container using {@code GET} method.
     *
     * @param <R> result type parameter
     * @param containerName the name of the container
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value the {@code funtion} results
     */
    public abstract <R> R readContainer(String containerName,
                                        Map<String, List<Object>> params,
                                        Map<String, List<Object>> headers,
                                        Function<ResponseType, R> function);

    /**
     * Reads object names in a container and accepts each of them to specified
     * consumer.
     *
     * @param containerName the name of the container
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function for creating a reader from the server
     * response; make this function to throw a {@link StorageClientException}
     * for stopping the iteration.
     * @param consumer the consumer
     * @return this client.
     */
    public T readContainerObjectNames(
            final String containerName, Map<String, List<Object>> params,
            Map<String, List<Object>> headers,
            final Function<ResponseType, Reader> function,
            final Consumer<String> consumer) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.putIfAbsent(QUERY_PARAM_LIMIT, singletonList(512));
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Accept", singletonList("text/plain"));
        final String marker_;
        {
            String marker__;
            try {
                marker__ = params.get(QUERY_PARAM_MARKER).get(0).toString();
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                marker__ = null;
            }
            marker_ = marker__;
        }
        for (final String[] marker = new String[]{marker_};;) {
            if (marker[0] != null) {
                params.put(QUERY_PARAM_MARKER, singletonList(marker[0]));
            }
            marker[0] = null;
            final Object result = readContainer(
                    containerName,
                    params,
                    headers,
                    r -> {
                        try {
                            try {
                                try (Reader reader = function.apply(r)) {
                                    lines(reader, consumer);
                                }
                                return null;
                            } catch (final StorageClientException sce) {
                                return sce;
                            }
                        } catch (final IOException ioe) {
                            throw new StorageClientException(ioe);
                        }
                    }
            );
            if (result != null) {
                break;
            }
            if (marker[0] == null) {
                break;
            }
        }
        return (T) this;
    }

    /**
     * Creates or updates a container using {@code PUT} method.
     *
     * @param <R> result type parameter
     * @param containerName container name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value the {@code function} results
     */
    public abstract <R> R updateContainer(String containerName,
                                          Map<String, List<Object>> params,
                                          Map<String, List<Object>> headers,
                                          Function<ResponseType, R> function);

    /**
     * Configures a container using the {@code POST} method.
     *
     * @param <R> result type parameter
     * @param containerName container name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value the {@code function} results
     */
    public abstract <R> R configureContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Function<ResponseType, R> function);

    /**
     * Deletes a container using {@code DELETE} method.
     *
     * @param <R> result type parameter
     * @param containerName container name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value the {@code function} results
     */
    public abstract <R> R deleteContainer(String containerName,
                                          Map<String, List<Object>> params,
                                          Map<String, List<Object>> headers,
                                          Function<ResponseType, R> function);

    /**
     * Deletes a container.
     *
     * @param <R> result type parameter
     * @param containerName container name
     * @param function a function to be applied with the server response
     * @return the result the {@code function} results
     */
    public <R> R deleteContainer(final String containerName,
                                 final Function<ResponseType, R> function) {
        return deleteContainer(containerName, null, null, function);
    }

    /**
     * Deletes a container and returns the status code of the server response.
     *
     * @param containerName container name
     * @return the status code of the server response
     */
    public int deleteContainer(final String containerName) {
        return deleteContainer(containerName, this::getStatusCode);
    }

    // ----------------------------------------------- /storage/container/object
    /**
     * Peeks an object using {@code HEAD} method.
     *
     * @param <R> result type parameter
     * @param containerName container name
     * @param objectName object name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response.
     * @return the result the {@code function} results
     */
    public abstract <R> R peekObject(String containerName, String objectName,
                                     Map<String, List<Object>> params,
                                     Map<String, List<Object>> headers,
                                     Function<ResponseType, R> function);

    public abstract <R> R readObject(String containerName, String objectName,
                                     Map<String, List<Object>> params,
                                     Map<String, List<Object>> headers,
                                     Function<ResponseType, R> function);

    /**
     * Updates an object using the {@code PUT} method.
     *
     * @param <R> result type parameter
     * @param containerName a container name
     * @param objectName an object name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function1 a function for writing request entity on the request
     * @param function2 a function to be applied with the server response
     * @return the value the {@code function2} results.
     */
    public abstract <R> R updateObject(
            String containerName, String objectName,
            Map<String, List<Object>> params,
            Map<String, List<Object>> headers,
            Function<RequestType, ResponseType> function1,
            Function<ResponseType, R> function2);

    // ------------------------------------- /storage/container/object/configure
    public abstract <R> R configureObject(String containerName,
                                          String objectName,
                                          Map<String, List<Object>> params,
                                          Map<String, List<Object>> headers,
                                          Function<ResponseType, R> function);

    /**
     * Deletes an object using {@code DELETE} method.
     *
     * @param <R> result type parameter
     * @param containerName container name
     * @param objectName object name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response.
     * @return the value the {@code function} results
     */
    public abstract <R> R deleteObject(String containerName,
                                       String objectName,
                                       Map<String, List<Object>> params,
                                       Map<String, List<Object>> headers,
                                       Function<ResponseType, R> function);

    /**
     * Deletes an object without any query parameters and request headers.
     *
     * @param <R> result type parameter
     * @param containerName container name
     * @param objectName object name
     * @param function a function to be applied with the server response
     * @return the value the {@code function} results
     */
    public <R> R deleteObject(final String containerName,
                              final String objectName,
                              final Function<ResponseType, R> function) {
        return deleteObject(containerName, objectName, null, null, function);
    }

    /**
     * Deletes an object and returns the status code of the server response.
     *
     * @param containerName container name
     * @param objectName object name
     * @return the status code of the server response
     */
    public int deleteObject(final String containerName,
                            final String objectName) {
        return deleteObject(containerName, objectName, this::getStatusCode);
    }

    // ---------------------------------------------------------------- /account
    /**
     * Reads account information.
     *
     * @param <R> result type parameter
     * @param params query parameters; may be {@code null}
     * @param headers additional request headers; may be {@code null}
     * @param function the function to be applied with the server response
     * @return the value the {@code function} results
     */
    public abstract <R> R readAccount(
            Map<String, List<Object>> params, Map<String, List<Object>> headers,
            Function<ResponseType, R> function);

    /**
     * Reads account information without any query parameters and request
     * headers.
     *
     * @param <R> result type parameter
     * @param function the function to be applied with the server response
     * @return the value the {@code function} results
     * @see #readAccount(java.util.Map, java.util.Map,
     * java.util.function.Function)
     */
    public <R> R readAccount(final Function<ResponseType, R> function) {
        return readAccount(null, null, function);
    }

    // ----------------------------------------------------------- /account/user
    /**
     * Reads user information.
     *
     * @param <R> result type parameter
     * @param userName username
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function the function to be applied with server response
     * @return the value the {@code function} results
     */
    public abstract <R> R readUser(String userName,
                                   Map<String, List<Object>> params,
                                   Map<String, List<Object>> headers,
                                   Function<ResponseType, R> function);

    /**
     * Reads user information with any query parameter and additional request
     * headers.
     *
     * @param <R> result type parameter
     * @param userName user name
     * @param function the function to be applied with the server response
     * @return the value the {@code funtion} results
     * @see #readUser(java.lang.String, java.util.Map, java.util.Map,
     * java.util.function.Function)
     */
    public <R> R readUser(final String userName,
                          final Function<ResponseType, R> function) {
        return readUser(userName, null, null, function);
    }

    public abstract <R> R updateUser(
            final String userName, final String userKey,
            final boolean userAdmin, final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Function<ResponseType, R> function);

    /**
     * Deletes a user using the {@code DELETE} method.
     *
     * @param <R> result type parameter
     * @param userName user name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value the {@code function} results
     */
    public abstract <R> R deleteUser(
            String userName, Map<String, List<Object>> params,
            Map<String, List<Object>> headers,
            Function<ResponseType, R> function);

    /**
     * Deletes a user without any query parameters and request headers.
     *
     * @param <R> result type parameter
     * @param userName user name
     * @param function a function to be applied with the server response
     * @return the result the {@code function} results
     */
    public <R> R deleteUser(final String userName,
                            final Function<ResponseType, R> function) {
        return deleteUser(userName, null, null, function);
    }

    /**
     * Deletes a user and returns the status code of the server response.
     *
     * @param userName user name
     * @return the status code of the server response
     */
    public int deleteUser(final String userName) {
        return deleteUser(userName, this::getStatusCode);
    }

    // -------------------------------------------------------- /account/.groups
    public abstract <R> R readGroups(Map<String, List<Object>> params,
                                     Map<String, List<Object>> headers,
                                     Function<ResponseType, R> function);

    // ----------------------------------------------------------------- authUrl
    /**
     * Returns the {@code authUrl}.
     *
     * @return the {@code authUrl}. //@deprecated forRemoval = true
     */
//    @Deprecated//(forRemoval = true)
    protected final String getAuthUrl() {
        return authUrl;
    }

    // ---------------------------------------------------------------- authUser
    /**
     * Returns the {@code authUser}.
     *
     * @return the {@code authUser}. //@deprecated forRemoval = true
     */
//    @Deprecated//(forRemoval = true)
    protected final String getAuthUser() {
        return authUser;
    }

    // ----------------------------------------------------------------- authKey
    /**
     * Returns the {@code authKey}.
     *
     * @return the {@code authKey} //@deprecated forRemoval = true
     */
//    @Deprecated//(forRemoval = true)
    protected final String getAuthKey() {
        return authKey;
    }

    // ------------------------------------------------------------- accountName
    protected final String getAccountName() {
        return accountName;
    }

//    public final String accountName() {
//        return getAccountName();
//    }
    // -------------------------------------------------------------- storageUrl
    /**
     * Returns the storage URL.
     *
     * @return the storage URL
     */
    protected final String getStorageUrl() {
        return storageUrl;
    }

//    protected String storageUrl() {
//        return getStorageUrl();
//    }
    protected final void setStorageUrl(final String storageUrl) {
        this.storageUrl = requireNonNull(storageUrl, "null storageUrl");
        if (accountName != null) {
            accountUrl = accountUrl(storageUrl, accountName);
        }
    }

//    protected StorageClient storegeUrl(final String storageUrl) {
//        setStorageUrl(storageUrl);
//        return this;
//    }
    // -------------------------------------------------------------- accountUrl
    protected final String getAccountUrl() {
        return accountUrl;
    }

//    protected String accountUrl() {
//        return getAccountUrl();
//    }
    // --------------------------------------------------------------- authToken
    /**
     * Returns the authorization token.
     *
     * @return the authorization token.
     */
    protected final String getAuthToken() {
        return authToken;
    }

    protected final void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }

    // ------------------------------------------------------------ tokenExpires
    /**
     * Return the date the authorization token expires.
     *
     * @return the date the authorization token expires.
     */
    public final Date getAuthTokenExpires() {
        if (authTokenExpires == null) {
            return null;
        }
        return new Date(authTokenExpires.getTime());
    }

    protected final void setAuthTokenExpires(final Date authTokenExpires) {
        this.authTokenExpires
                = ofNullable(authTokenExpires)
                .map(Date::getTime)
                .map(Date::new)
                .orElse(null);
    }

    protected final void setAuthTokenExpires(final String authTokenExpires) {
        setAuthTokenExpires(
                ofNullable(authTokenExpires)
                .map(Integer::parseInt)
                .map(SECONDS::toMillis)
                .map(v -> new Date(currentTimeMillis() + v))
                .orElse(null)
        );
    }

    // -------------------------------------------------------------------------
    private final String authUrl;

    private final String authUser;

    private final String authKey;

    private final String accountName;

    private transient String storageUrl;

    private transient String accountUrl;

    private transient String authToken;

    private transient Date authTokenExpires;
}
