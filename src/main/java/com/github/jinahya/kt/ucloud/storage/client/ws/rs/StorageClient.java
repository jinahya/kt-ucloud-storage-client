/*
 * Copyright 2016 Jin Kwon &lt;onacit_at_gmail.com&gt;.
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
package com.github.jinahya.kt.ucloud.storage.client.ws.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import static java.util.Collections.singletonList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

/**
 * A client for accessing kt ucloud storage using JAX-RS.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class StorageClient {

    private static final Logger logger
            = getLogger(StorageClient.class.getName());

    public static final String AUTH_URL_STANDARD_KOR_CENTER
            = "https://api.ucloudbiz.olleh.com/storage/v1/auth";

    public static final String AUTH_URL_STANDARD_JPN
            = "https://api.ucloudbiz.olleh.com/storage/v1/authjp";

    public static final String AUTH_URL_LITE_KOR_HA
            = "https://api.ucloudbiz.olleh.com/storage/v1/authlite";

    public static final String QUERY_PARAM_LIMIT = "limit";

    public static final String QUERY_PARAM_MARKER = "marker";

    public static final String QUERY_PARAM_FORMAT = "format";

    public static final String HEADER_X_AUTH_USER = "X-Storage-User";

    public static final String HEADER_X_AUTH_PASS = "X-Storage-Pass";

    public static final String HEADER_X_AUTH_NEW_TOKEN = "X-Auth-New-Token";

    public static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";

    public static final String HEADER_X_AUTH_TOKEN_EXPIRES
            = "X-Auth-Token-Expires";

    public static final String HEADER_X_STORAGE_URL = "X-Storage-Url";

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

    public static Response authenticateUser(final Client client,
                                            final String authUrl,
                                            final String authUser,
                                            final String authPass,
                                            final boolean newToken) {
        Invocation.Builder builder = client
                .target(requireNonNull(authUrl, "null authUrl"))
                .request()
                .header(HEADER_X_AUTH_USER,
                        requireNonNull(authUser, "null authUser"))
                .header(HEADER_X_AUTH_PASS,
                        requireNonNull(authPass, "null authPass"));
        if (newToken) {
            builder = builder.header(HEADER_X_AUTH_NEW_TOKEN, Boolean.TRUE);
        }
        return builder.buildGet().invoke();
    }

    /**
     * Authenticates with given arguments. This method invokes
     * {@link #authenticateUser(javax.ws.rs.client.Client, java.lang.String, java.lang.String, java.lang.String, boolean)}
     * with given arguments and a {@code true} and returns the result.
     *
     * @param client the client
     * @param authUrl authentication URL
     * @param authUser authentication username
     * @param authPass authentication password
     * @return a response
     * @see #authenticateUser(javax.ws.rs.client.Client, java.lang.String,
     * java.lang.String, java.lang.String, boolean)
     */
    public static Response authenticateUser(final Client client,
                                            final String authUrl,
                                            final String authUser,
                                            final String authPass) {
        return authenticateUser(client, authUrl, authUser, authPass, true);
//        return client
//                .target(authUrl)
//                .request(MediaType.APPLICATION_JSON_TYPE)
//                .header(HEADER_X_AUTH_USER, authUser) // X-Auth-User -> 500
//                .header(HEADER_X_AUTH_PASS, authPass) // X-Auth-Pass -> 500
//                .header(HEADER_X_AUTH_NEW_TOKEN, true)
//                .buildGet()
//                .invoke();
    }

    // ----------------------------------------------------------------- storage
    /**
     * Targets a account.
     *
     * @param client a client to use
     * @param storageUrl storage URL
     * @param params query parameters; may be {@code null}
     * @return a web target
     */
    public static WebTarget targetAccount(
            final Client client, final String storageUrl,
            final MultivaluedMap<String, Object> params) {
        WebTarget target = requireNonNull(client, "null client")
                .target(requireNonNull(storageUrl, "null storageUrl"));
        if (params != null) {
            for (final Entry<String, List<Object>> entry : params.entrySet()) {
                final String name = entry.getKey();
                final Object[] values = entry.getValue().toArray();
                target = target.queryParam(name, values);
            }
        }
        return target;
    }

    /**
     * Builds for a storage.
     *
     * @param client a client to use
     * @param storageUrl a storage URL
     * @param params query parameters; may be {@code null}
     * @param authToken an authorization token
     * @return a builder.
     */
    public static Invocation.Builder buildAccount(
            final Client client, final String storageUrl,
            final MultivaluedMap<String, Object> params,
            final String authToken) {
        return targetAccount(client, storageUrl, params)
                .request()
                .header(HEADER_X_AUTH_TOKEN, authToken);
    }

    /**
     * Peeks an account using {@link HttpMethod#HEAD}.
     *
     * @param client a client to use
     * @param storageUrl a storage URL
     * @param params query parameters; may be {@code null}
     * @param authToken an authorization token
     * @param headers request headers; may be {@code null}
     * @return a server response
     */
    public static Response peekAccount(
            final Client client, final String storageUrl,
            final MultivaluedMap<String, Object> params,
            final String authToken,
            final MultivaluedMap<String, Object> headers) {
        Invocation.Builder builder = buildAccount(
                client, storageUrl, params, authToken);
        if (headers != null) {
            headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
            builder = builder.headers(headers);
        }
        return builder.head();
    }

    /**
     * Reads an account using {@link HttpMethod#GET}.
     *
     * @param client a client to use
     * @param storageUrl a storage URL
     * @param params query parameters; may be {@code null}
     * @param authToken an authorization token
     * @param headers request headers; may be {@code null}
     * @return a server response
     */
    public static Response readAccount(
            final Client client, final String storageUrl,
            final MultivaluedMap<String, Object> params,
            final String authToken,
            final MultivaluedMap<String, Object> headers) {
        Invocation.Builder builder = buildAccount(
                client, storageUrl, params, authToken);
        if (headers != null) {
            headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
            builder = builder.headers(headers);
        }
        return builder.get();
    }

    /**
     * Updates an account using {@link HttpMethod#POST}.
     *
     * @param client a client to use
     * @param storageUrl a storage URL
     * @param params query parameters; may be {@code null}
     * @param authToken an authorization token.
     * @param headers request headers; may be {@code null}
     * @return a server response
     */
    public static Response configureAccount(
            final Client client, final String storageUrl,
            final MultivaluedMap<String, Object> params,
            final String authToken,
            final MultivaluedMap<String, Object> headers) {
        Invocation.Builder builder = buildAccount(
                client, storageUrl, params, authToken);
        if (headers != null) {
            headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
            builder = builder.headers(headers);
        }
        return builder.post(null);
    }

    // --------------------------------------------------------------- container
    /**
     * Targets a container.
     *
     * @param client a client
     * @param storageUrl storage URL
     * @param containerName container name
     * @param params query parameters
     * @return a target
     */
    public static WebTarget targetContainer(
            final Client client, final String storageUrl,
            final String containerName,
            final MultivaluedMap<String, Object> params) {
        if (ThreadLocalRandom.current().nextBoolean()) {
            return targetAccount(client, storageUrl, params)
                    .path(requireNonNull(containerName, "null containerName"));
        }
        WebTarget target = requireNonNull(client, "null client")
                .target(requireNonNull(storageUrl, "null storageUrl"))
                .path(requireNonNull(containerName, "null containerName"));
        if (params != null) {
            for (final Entry<String, List<Object>> entry : params.entrySet()) {
                final String name = entry.getKey();
                final Object[] values = entry.getValue().toArray();
                target = target.queryParam(name, values);
            }
        }
        return target;
    }

    /**
     * Builds for a container.
     *
     * @param client a client to use
     * @param storageUrl storage URL
     * @param containerName container name
     * @param params query parameters; may be {@code null}
     * @param authToken an authorization token
     * @return a builder.
     */
    public static Invocation.Builder buildContainer(
            final Client client, final String storageUrl,
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final String authToken) {
        return targetContainer(client, storageUrl, containerName, params)
                .request()
                .header(HEADER_X_AUTH_TOKEN, authToken);
    }

    public static Response peekContainer(
            final Client client, final String storageUrl,
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final String authToken,
            final MultivaluedMap<String, Object> headers) {
        Invocation.Builder builder = buildContainer(
                client, storageUrl, containerName, params, authToken);
        if (headers != null) {
            headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
            builder = builder.headers(headers);
        }
        return builder.head();
    }

    public static Response readContainer(
            final Client client, final String storageUrl,
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final String authToken,
            final MultivaluedMap<String, Object> headers) {
        Invocation.Builder builder = buildContainer(
                client, storageUrl, containerName, params, authToken);
        if (headers != null) {
            headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
            builder = builder.headers(headers);
        }
        return builder.get();
    }

    // ------------------------------------------------------------------ object
    /**
     * Targets an object.
     *
     * @param client a client
     * @param storageUrl the storage URL
     * @param containerName container name
     * @param objectName object name
     * @param params query parameters
     * @return a target
     */
    public static WebTarget targetObject(
            final Client client, final String storageUrl,
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params) {
        if (ThreadLocalRandom.current().nextBoolean()) {
            return targetContainer(client, storageUrl, containerName, params)
                    .path(objectName);
        }
        WebTarget target
                = requireNonNull(client, "null client")
                .target(requireNonNull(storageUrl, "null storageUrl"))
                .path(requireNonNull(containerName, "containerName"))
                .path(requireNonNull(objectName, "null objectName"));
        if (params != null) {
            for (final Entry<String, List<Object>> entry : params.entrySet()) {
                final String name = entry.getKey();
                final Object[] values = entry.getValue().toArray();
                target = target.queryParam(name, values);
            }
        }
        return target;
    }

    /**
     * Builds for an object.
     *
     * @param client a client
     * @param storageUrl storage URL
     * @param containerName container name
     * @param objectName object name
     * @param params query parameters
     * @param authToken authorization token
     * @return a builder
     */
    public static Invocation.Builder buildObject(
            final Client client, final String storageUrl,
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final String authToken) {
        return targetObject(client, storageUrl, containerName, objectName,
                            params)
                .request()
                .header(HEADER_X_AUTH_TOKEN, authToken);
    }

    /**
     * Creates a new instance.
     *
     * @param authUrl the authentication URL
     * @param authUser the authentication username
     * @param authPass the authentication password
     */
    public StorageClient(final String authUrl, final String authUser,
                         final String authPass) {
        super();
        this.authUrl = requireNonNull(authUrl, "null authUrl");
        this.authUser = requireNonNull(authUser, "null authUser");
        this.authPass = requireNonNull(authPass, "null authPass");
    }

    /**
     * Authenticates user and applies given function with the response.
     *
     * @param <T> return value type parameter.
     * @param function a function to be applied with the server response.
     * @return the value {@code function} results or {@code null} if the
     * {@code function} is {@code null}
     * @see #authenticateUser(javax.ws.rs.client.Client, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public <T> T authenticateUser(final Function<Response, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = authenticateUser(
                    client, authUrl, authUser, authPass);
            try {
                final StatusType statusInfo = response.getStatusInfo();
                final Family family = statusInfo.getFamily();
                if (family == Family.SUCCESSFUL) {
                    storageUrl = response.getHeaderString(HEADER_X_STORAGE_URL);
                    if (storageUrl == null) {
                        throw new WebApplicationException(
                                "response header not found"
                                + HEADER_X_STORAGE_URL);
                    }
                    authToken = response.getHeaderString(HEADER_X_AUTH_TOKEN);
                    if (storageUrl == null) {
                        throw new WebApplicationException(
                                "response header not found"
                                + HEADER_X_AUTH_TOKEN);
                    }
                    final String authTokenExpires_ = response.getHeaderString(
                            HEADER_X_AUTH_TOKEN_EXPIRES);
                    if (storageUrl == null) {
                        throw new WebApplicationException(
                                "response header not found"
                                + HEADER_X_AUTH_TOKEN_EXPIRES);
                    }
                    authTokenExpires = new Date(
                            System.currentTimeMillis()
                            + (Long.parseLong(authTokenExpires_)
                               * TimeUnit.SECONDS.toMillis(1L)));
                } else {
                    invalidate();
                    throw new WebApplicationException(
                            "failed to authenticate user", response);
                }
                return function == null ? null : function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    /**
     * Authenticates user and applies given function with the response.
     *
     * @param <T> return value type parameter.
     * @param function a function to be applied with the server response.
     * @return the value {@code function} results or {@code null} if the
     * {@code function} is {@code null}
     * @see #authenticateUser(javax.ws.rs.client.Client, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public <T> T authenticateUser(
            final BiFunction<Response, StorageClient, T> function) {
        return authenticateUser(
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null));
    }

    public StorageClient authenticateUser(final Consumer<Response> consumer) {
        return authenticateUser(
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(r -> this)
        );
    }

    public StorageClient authenticateUser(
            final BiConsumer<Response, StorageClient> consumer) {
        return authenticateUser(
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null));
    }

    /**
     * Invalidates this client by purging the authorization information.
     *
     * @return this client
     */
    public StorageClient invalidate() {
        storageUrl = null;
        authToken = null;
        authTokenExpires = null;
        return this;
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
        if (storageUrl == null || authToken == null
            || authTokenExpires == null) {
            invalidate();
            return false;
        }
        return authTokenExpires.getTime() >= until;
    }

    /**
     * Checks if the authentication token is valid in specified time unit and
     * duration.
     *
     * @param unit time unit
     * @param duration time unit duration.
     * @return {@code true} if the token is valid; {@code false} otherwise.
     */
    public boolean isValid(final TimeUnit unit, final long duration) {
        return isValid(System.currentTimeMillis() + unit.toMillis(duration));
    }

    private void ensureValid(final TimeUnit unit, final long duration) {
        if (!isValid(unit, duration)) {
            authenticateUser(r -> {
            });
        }
    }

    private void ensureValid() {
        ensureValid(TimeUnit.MINUTES, 10L);
    }

    private void lines(final Response response, final Charset charset,
                       final Consumer<String> consumer)
            throws IOException {
        try (InputStream stream = response.readEntity(InputStream.class);
             Reader reader = new InputStreamReader(stream, charset);
             BufferedReader buffered = new BufferedReader(reader);) {
            buffered.lines().forEach(consumer::accept);
        }
    }

    private void lines(final Response response, final Charset charset,
                       final BiConsumer<String, StorageClient> consumer)
            throws IOException {
        lines(response, charset, l -> consumer.accept(l, this));
    }

    private void lines(final Response response, final Consumer<String> consumer)
            throws IOException {
        try (Reader reader = response.readEntity(Reader.class);
             BufferedReader buffered = new BufferedReader(reader);) {
            buffered.lines().forEach(consumer::accept);
        }
    }

    private void lines(final Response response,
                       final BiConsumer<String, StorageClient> consumer)
            throws IOException {
        lines(response, l -> consumer.accept(l, this));
    }

    // ----------------------------------------------------------------- account
    /**
     * Peeks a storage using {@link javax.ws.rs.HttpMethod#HEAD}.
     *
     * @param <T> result type parameter
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function the function to be applied with the response
     * @return the value the {@code function} results
     */
    public <T> T peekAccount(final MultivaluedMap<String, Object> params,
                             final MultivaluedMap<String, Object> headers,
                             final Function<Response, T> function) {
        ensureValid();
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = peekAccount(
                    client, storageUrl, params, authToken, headers);
            try {
                return requireNonNull(function, "null function")
                        .apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    /**
     * Peeks a storage using {@link javax.ws.rs.HttpMethod#HEAD}.
     *
     * @param <T> result type parameter
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function the function to be applied with the response and this
     * client
     * @return the value {@code function} results
     * @see #peekAccount(javax.ws.rs.core.MultivaluedMap,
     * javax.ws.rs.core.MultivaluedMap, java.util.function.Function)
     */
    public <T> T peekAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return peekAccount(
                params,
                headers,
                r -> {
                    return requireNonNull(function, "null function")
                    .apply(r, this);
                }
        );
    }

    /**
     * Peeks a storage using {@link javax.ws.rs.HttpMethod#HEAD}.
     *
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param consumer the consumer to be accepted with the response
     * @return this client
     * @see #peekAccount(javax.ws.rs.core.MultivaluedMap,
     * javax.ws.rs.core.MultivaluedMap, java.util.function.Function)
     */
    public StorageClient peekAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return peekAccount(
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r);
                    return this;
                }
        );
    }

    /**
     * Peeks a storage using {@link javax.ws.rs.HttpMethod#HEAD}.
     *
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param consumer the consumer to be accepted with the response and this
     * client
     * @return this client
     * @see #peekAccount(javax.ws.rs.core.MultivaluedMap,
     * javax.ws.rs.core.MultivaluedMap, java.util.function.Consumer)
     */
    public StorageClient peekAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return peekAccount(
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r, this);
                }
        );
    }

    /**
     * Reads a storage using {@link javax.ws.rs.HttpMethod#GET}.
     *
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value {@code function} results
     */
    public <T> T readAccount(final MultivaluedMap<String, Object> params,
                             final MultivaluedMap<String, Object> headers,
                             final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = readAccount(
                    client, storageUrl, params, authToken, headers);
            try {
                return requireNonNull(function, "null function")
                        .apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T readAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return readAccount(
                params,
                headers,
                r -> {
                    return requireNonNull(function, "null function")
                    .apply(r, this);
                }
        );
    }

    public StorageClient readAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return readAccount(
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r);
                    return this;
                }
        );
    }

    public StorageClient readAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return readAccount(
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r, this);
                }
        );
    }

//    public StorageClient readAccountContainerNames(
//            MultivaluedMap<String, Object> params,
//            MultivaluedMap<String, Object> headers,
//            final Consumer<String> consumer) {
//        if (params == null) {
//            params = new MultivaluedHashMap<>();
//        }
//        params.putIfAbsent(QUERY_PARAM_LIMIT, singletonList(512));
//        if (headers == null) {
//            headers = new MultivaluedHashMap<>();
//        }
//        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
//        return readAccount(
//                params,
//                headers,
//                r -> {
//                    final StatusType statusInfo = r.getStatusInfo();
//                    final Family family = statusInfo.getFamily();
//                    if (family != Family.SUCCESSFUL) {
//                        throw new WebApplicationException(
//                                "failed to read container names; family="
//                                + family);
//                    }
//                    final int statusCode = statusInfo.getStatusCode();
//                    if (statusCode == Status.OK.getStatusCode()) {
//                        try {
////                            lines(r, StandardCharsets.UTF_8, consumer);
//                            lines(r, consumer);
//                        } catch (final IOException ioe) {
//                            throw new WebApplicationException(ioe);
//                        }
//                    }
//                }
//        );
//    }
//
//    public StorageClient readAccountContainerNames(
//            MultivaluedMap<String, Object> params,
//            MultivaluedMap<String, Object> headers,
//            final BiConsumer<String, StorageClient> consumer) {
//        return readAccountContainerNames(
//                params,
//                headers,
//                l -> consumer.accept(l, this)
//        );
//    }
    /**
     * Reads an account and consumes all container names.
     *
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param consumer the consumer to be accepted with each of the consumer
     * names and this client; may be {@code null}
     * @return this client
     */
    public StorageClient readAccountContainerNames(
            MultivaluedMap<String, Object> params,
            MultivaluedMap<String, Object> headers,
            final Predicate<Response> predicate,
            final Consumer<String> consumer) {
        if (params == null) {
            params = new MultivaluedHashMap<>();
        }
        params.putIfAbsent(QUERY_PARAM_LIMIT, singletonList(512));
        if (headers == null) {
            headers = new MultivaluedHashMap<>();
        }
        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
        for (final String[] marker = new String[1];;) {
            if (marker[0] != null) {
                params.putSingle(QUERY_PARAM_MARKER, marker[0]);
            }
            marker[0] = null;
            readAccount(
                    params,
                    headers,
                    r -> {
                        if (!predicate.test(r)) {
                            return;
                        }
                        try {
//                            lines(r, StandardCharsets.UTF_8, consumer);
                            lines(r, consumer);
                        } catch (final IOException ioe) {
                            throw new WebApplicationException(ioe);
                        }
                    }
            );
            if (marker[0] == null) {
                break;
            }
        }
        return this;
    }

    /**
     * Reads an account and consumes all container names.
     *
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param consumer the consumer to be accepted with each of the consumer
     * names and this client; may be {@code null}
     * @return this client
     */
    public StorageClient readAccountContainerNames(
            MultivaluedMap<String, Object> params,
            MultivaluedMap<String, Object> headers,
            final Predicate<Response> prediate,
            final BiConsumer<String, StorageClient> consumer) {
        return readAccountContainerNames(
                params,
                headers,
                prediate,
                l -> consumer.accept(l, this)
        );
    }

    public <T> T configureAccount(final MultivaluedMap<String, Object> params,
                                  final MultivaluedMap<String, Object> headers,
                                  final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = configureAccount(
                    client, storageUrl, params, authToken, headers);
            try {
                return requireNonNull(function, "null function")
                        .apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T configureAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return configureAccount(
                params,
                headers,
                r -> {
                    return requireNonNull(function, "null function")
                    .apply(r, this);
                }
        );
    }

    public StorageClient configureAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return configureAccount(
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r);
                    return this;
                }
        );
    }

    public StorageClient configureAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return configureAccount(
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r, this);
                }
        );
    }

    // --------------------------------------------------------------- container
    public <T> T peekContainer(final String containerName,
                               final MultivaluedMap<String, Object> params,
                               final MultivaluedMap<String, Object> headers,
                               final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = peekContainer(
                    client, storageUrl, containerName, params, authToken,
                    headers);
            try {
                return requireNonNull(function, "null function")
                        .apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    /**
     * Requests {@value javax.ws.rs.HttpMethod#HEAD} for a container. Currently,
     * the server responds
     * {@link javax.ws.rs.core.Response.Status#NOT_ACCEPTABLE} when requested
     * without {@value javax.ws.rs.core.HttpHeaders#ACCEPT} with
     * {@value javax.ws.rs.core.MediaType#TEXT_HTML}.
     *
     * @param <T> result type parameter
     * @param containerName the name of the container
     * @param params query parameters
     * @param headers request headers.
     * @param function the function to be applied with the response and this
     * client.
     * @return the value the function results.
     */
    public <T> T peekContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return peekContainer(
                containerName,
                params,
                headers,
                r -> {
                    return requireNonNull(function, "null function")
                    .apply(r, this);
                }
        );
    }

    public StorageClient peekContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return peekContainer(
                containerName,
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r);
                    return this;
                }
        );
    }

    public StorageClient peekContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return peekContainer(
                containerName,
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r, this);
                }
        );
    }

    /**
     * Reads container.
     *
     * @param <T> result type parameter
     * @param containerName container name
     * @param params query parameters
     * @param headers request headers.
     * @param function the function to be applied with the response
     * @return the value the function results or else if {@code function} is
     * {@code null}
     */
    public <T> T readContainer(final String containerName,
                               final MultivaluedMap<String, Object> params,
                               final MultivaluedMap<String, Object> headers,
                               final Function<Response, T> function) {
        ensureValid();
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = readContainer(
                    client, storageUrl, containerName, params, authToken,
                    headers);
            try {
                return requireNonNull(function, "null function")
                        .apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T readContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return readContainer(
                containerName,
                params,
                headers,
                r -> {
                    return requireNonNull(function, "null function")
                    .apply(r, this);
                }
        );
    }

    public StorageClient readContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return readContainer(
                containerName,
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r);
                    return this;
                }
        );
    }

    public StorageClient readContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return readContainer(
                containerName,
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r, this);
                }
        );
    }

    public StorageClient readContainerObjectNames(
            final String containerName,
            MultivaluedMap<String, Object> params,
            MultivaluedMap<String, Object> headers,
            final Predicate<Response> predicate,
            final Consumer<String> consumer) {
        requireNonNull(predicate, "null predicate");
        if (params == null) {
            params = new MultivaluedHashMap<>();
        }
        params.putIfAbsent(QUERY_PARAM_LIMIT, singletonList(512));
        if (headers == null) {
            headers = new MultivaluedHashMap<>();
        }
        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
        for (final String[] marker = new String[1];;) {
            if (marker[0] != null) {
                params.putSingle(QUERY_PARAM_MARKER, marker[0]);
            }
            marker[0] = null;
            readContainer(
                    containerName,
                    params,
                    headers,
                    r -> {
                        if (!predicate.test(r)) {
                            return;
                        }
                        try {
                            lines(r, requireNonNull(consumer, "null consumer"));
                        } catch (final IOException ioe) {
                            throw new WebApplicationException(
                                    "failed to read object names", ioe);
                        }
                    }
            );
            if (marker[0] == null) {
                break;
            }
        }
        return this;
    }

    public StorageClient readContainerObjectNames(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Predicate<Response> predicate,
            final BiConsumer<String, StorageClient> consumer) {
        requireNonNull(consumer, "null consumer");
        return readContainerObjectNames(
                containerName,
                params,
                headers,
                predicate,
                l -> consumer.accept(l, this)
        );
    }

    /**
     * Creates or updates a container.
     *
     * @param <T> result type parameter
     * @param containerName container name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function the function to be applied with the response; may be
     * {@code null}
     * @return the value the function results; {@code null} if the
     * {@code function} is {@code null}
     */
    public <T> T updateContainer(final String containerName,
                                 final MultivaluedMap<String, Object> params,
                                 final MultivaluedMap<String, Object> headers,
                                 final Function<Response, T> function) {
        requireNonNull(function, "null function");
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildContainer(
                    client, storageUrl, containerName, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.put(Entity.text(""));
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T updateContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        requireNonNull(function, "null function");
        return updateContainer(
                containerName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public StorageClient updateContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return updateContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public StorageClient updateContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return updateContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    public <T> T configureContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildContainer(
                    client, storageUrl, containerName, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.post(null);
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T configureContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return configureContainer(
                containerName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public StorageClient configureContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return configureContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public StorageClient configureContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return configureContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    /**
     * Deletes a container identified by given name and returns the result .
     *
     * @param <T> return value type parameter
     * @param containerName the container name
     * @param params query parameters
     * @param headers additional request headers
     * @param function the function to be applied with the response.
     * @return the value function results or {@code null} if the
     * {@code function} is {@code null}.
     */
    public <T> T deleteContainer(final String containerName,
                                 final MultivaluedMap<String, Object> params,
                                 final MultivaluedMap<String, Object> headers,
                                 final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildContainer(
                    client, storageUrl, containerName, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.delete();
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T deleteContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return deleteContainer(
                containerName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public StorageClient deleteContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return deleteContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public StorageClient deleteContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return deleteContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    // ------------------------------------------------------------------ object
    public <T> T createObject(final String containerName,
                              final String objectName,
                              final MultivaluedMap<String, Object> params,
                              final MultivaluedMap<String, Object> headers,
                              final Entity<?> entity,
                              final Function<Response, T> function) {
        ensureValid();
        updateContainer(
                containerName,
                null,
                null,
                r -> {
                }
        );
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildObject(
                    client, storageUrl, containerName, objectName, params,
                    authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Invocation invocation = builder.buildPut(entity);
            final Response response = invocation.invoke();
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T createObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Entity<?> entity,
            final BiFunction<Response, StorageClient, T> function) {
        return createObject(
                containerName,
                objectName,
                params,
                headers,
                entity,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public StorageClient createObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Entity<?> entity, final Consumer<Response> consumer) {
        return createObject(
                containerName,
                objectName,
                params,
                headers,
                entity,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public StorageClient createObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Entity<?> entity,
            final BiConsumer<Response, StorageClient> consumer) {
        return createObject(
                containerName,
                objectName,
                params,
                headers,
                entity,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    public <T> T peekObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Function<Response, T> function) {
        ensureValid();
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildObject(
                    client, storageUrl, containerName, objectName, params,
                    authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.head();
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T peekObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return peekObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public StorageClient peekObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return peekObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public StorageClient peekObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return peekObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    public <T> T readObject(final String containerName, final String objectName,
                            final MultivaluedMap<String, Object> params,
                            final MultivaluedMap<String, Object> headers,
                            final Function<Response, T> function) {
        ensureValid();
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildObject(
                    client, storageUrl, containerName, objectName, params,
                    authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Invocation invocation = builder.buildGet();
            final Response response = invocation.invoke();
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T readObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return readObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public StorageClient readObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return readObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public StorageClient readObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return readObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    public <T> T updateObject(final String containerName,
                              final String objectName,
                              final MultivaluedMap<String, Object> params,
                              final MultivaluedMap<String, Object> headers,
                              final Entity<?> entity,
                              final Function<Response, T> function) {
        ensureValid();
//        configureContainer(containerName, null, null, (Consumer<Response>) null);
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildObject(
                    client, storageUrl, containerName, objectName, params,
                    authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.post(null);
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T updateObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Entity<?> entity,
            final BiFunction<Response, StorageClient, T> function) {
        return updateObject(
                containerName,
                objectName,
                params,
                headers,
                entity,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public StorageClient updateObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Entity<?> entity, final Consumer<Response> consumer) {
        return updateObject(
                containerName,
                objectName,
                params,
                headers,
                entity,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public StorageClient updateObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Entity<?> entity,
            final BiConsumer<Response, StorageClient> consumer) {
        return updateObject(
                containerName,
                objectName,
                params,
                headers,
                entity,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    /**
     * Deletes an object using {@link javax.ws.rs.HttpMethod#DELETE}.
     *
     * @param <T> return value type parameter
     * @param containerName the container name
     * @param objectName the object name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}.
     * @param function a function to be applied with the server response; may be
     * {@code null}
     * @return a value the {@code function} results or {@code null} if the
     * {@code function} is {@code null}
     */
    public <T> T deleteObject(final String containerName,
                              final String objectName,
                              final MultivaluedMap<String, Object> params,
                              final MultivaluedMap<String, Object> headers,
                              final Function<Response, T> function) {
        ensureValid();
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildObject(
                    client, storageUrl, containerName, objectName, params,
                    authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Invocation invocation = builder.buildDelete();
            final Response response = invocation.invoke();
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T deleteObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return deleteObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public StorageClient deleteObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return deleteObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public StorageClient deleteObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return deleteObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    // -------------------------------------------------------------- storageUrl
    /**
     * Returns the storage URL.
     *
     * @return the storage URL
     */
    public String getStorageUrl() {
        return storageUrl;
    }

    // ---------------------------------------------------------------- authToken
    /**
     * Returns the authorization token.
     *
     * @return the authorization token.
     */
    public String getAuthToken() {
        return authToken;
    }

    // ------------------------------------------------------------ tokenExpires
    /**
     * Return the date the authorization token expires.
     *
     * @return the date the authorization token expires.
     */
    public Date getAuthTokenExpires() {
        if (authTokenExpires == null) {
            return null;
        }
        return new Date(authTokenExpires.getTime());
    }

    // -------------------------------------------------------------------------
    private final String authUrl;

    private final String authUser;

    private final String authPass;

    private transient String storageUrl;

    private transient String authToken;

    private transient Date authTokenExpires;
}
