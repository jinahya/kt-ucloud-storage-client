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

import com.github.jinahya.kt.ucloud.storage.client.StorageClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import static java.util.Collections.singletonList;
import java.util.List;
import java.util.Map.Entry;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.OK;
import javax.ws.rs.core.UriBuilder;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;

/**
 * A client for accessing kt ucloud storage using JAX-RS.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class WsRsClient extends StorageClient {

    private static final Logger logger = getLogger(WsRsClient.class.getName());

    public static Response authenticateUser(final Client client,
                                            final String authUrl,
                                            final String authUser,
                                            final String authKey,
                                            final boolean newToken) {
        Invocation.Builder builder = client
                .target(requireNonNull(authUrl, "null authUrl"))
                .request()
                .header(HEADER_X_AUTH_USER,
                        requireNonNull(authUser, "null authUser"))
                .header(HEADER_X_AUTH_PASS,
                        requireNonNull(authKey, "null authKey"));
        if (newToken) {
            builder = builder.header(HEADER_X_AUTH_NEW_TOKEN, Boolean.TRUE);
        }
        return builder.buildGet().invoke();
    }

    // ---------------------------------------------------------------- /account
    /**
     * Targets an account.
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
     * Builds for an account.
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
                .header(HEADER_X_AUTH_TOKEN,
                        requireNonNull(authToken, "null authToken"));
    }

    // ------------------------------------------------------ /account/container
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
                .header(HEADER_X_AUTH_TOKEN,
                        requireNonNull(authToken, "null authToken"));
    }

    // ----------------------------------------------- /account/container/object
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
                .header(HEADER_X_AUTH_TOKEN,
                        requireNonNull(authToken, "null authToken"));
    }

    // ---------------------------------------------------------------- reseller
    public static MultivaluedMap<String, Object> headers(
            MultivaluedMap<String, Object> headers, final String authAccount,
            final String authUser, final String authKey) {
        if (headers == null) {
            headers = new MultivaluedHashMap<>();
        }
        headers.putSingle(HEADER_X_AUTH_ADMIN_USER,
                          requireNonNull(authAccount, "null authAccount")
                          + ":"
                          + requireNonNull(authUser, "null authUser"));
        headers.putSingle(HEADER_X_AUTH_ADMIN_KEY,
                          requireNonNull(authKey, "null authKey"));
        return headers;
    }

    // ------------------------------------------------------- /reseller/account
    public static WebTarget targetResellerAccount(
            final Client client, final String storageUrl,
            final String authAccount,
            final MultivaluedMap<String, Object> params) {
        final URI uri;
        try {
            final URL url
                    = new URL(requireNonNull(storageUrl, "null storgeUrl"));
            final String protocol = url.getProtocol();
            final String authority = url.getAuthority();
            uri = UriBuilder.fromUri(protocol + "://" + authority)
                    .path("auth")
                    .path("v2")
                    .path(authAccount)
                    .build();
        } catch (final MalformedURLException murle) {
            throw new RuntimeException(murle);
        }
        WebTarget target
                = requireNonNull(client, "null client")
                .target(uri);
        if (params != null) {
            for (final Entry<String, List<Object>> entry : params.entrySet()) {
                final String name = entry.getKey();
                final Object[] values = entry.getValue().toArray();
                target = target.queryParam(name, values);
            }
        }
        return target;
    }

    public static Invocation.Builder buildResellerAccount(
            final Client client, final String storageUrl,
            final String authAccount,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final String authUser,
            final String authKey) {
        return targetResellerAccount(client, storageUrl, authAccount, params)
                .request()
                .headers(headers(headers, authAccount, authUser, authKey));
    }

    // -------------------------------------------------- /reseller/account/user
    public static WebTarget targetResellerUser(
            final Client client, final String storageUrl,
            final String authAccount, final String userName,
            final MultivaluedMap<String, Object> params) {
        WebTarget target = targetResellerAccount(client, storageUrl,
                                                 authAccount, params)
                .path(requireNonNull(userName, "null userName"));
        if (params != null) {
            for (final Entry<String, List<Object>> entry : params.entrySet()) {
                final String name = entry.getKey();
                final Object[] values = entry.getValue().toArray();
                target = target.queryParam(name, values);
            }
        }
        return target;
    }

    public static Invocation.Builder buildResellerUser(
            final Client client, final String storageUrl,
            final String authAccount, final String userName,
            final MultivaluedMap<String, Object> params,
            final String authUser, final String authKey) {
        return targetResellerUser(client, storageUrl, authAccount, userName,
                                  params)
                .request()
                .headers(headers(null, authAccount, authUser, authKey));
    }

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance.
     *
     * @param authUrl the authentication URL
     * @param authUser the authentication username
     * @param authKey the authentication password
     */
    public WsRsClient(final String authUrl, final String authUser,
                      final String authKey) {
        super(authUrl, authUser, authKey);
    }

    public WsRsClient(final StorageClient client) {
        this(client.getStorageUrl(), client.getAuthUser(),
             client.getAuthKey());
        storageUrl = client.getStorageUrl();
        authToken = client.getAuthToken();
        authTokenExpires = client.getAuthTokenExpires();
    }

    // -------------------------------------------------------------------------
    private Client registerFilters(final Client client) {
        ofNullable(clientRequestFilter).ifPresent(client::register);
        ofNullable(clientResponseFilter).ifPresent(client::register);
        return client;
    }

    private <R> R close(final Client client,
                        final Function<Client, R> function) {
        try {
            return function.apply(registerFilters(client));
        } finally {
            client.close();
        }
    }

    private <R> R apply(final Function<Client, R> function) {
        return close(ClientBuilder.newClient(), function);
    }

    // -------------------------------------------------------------------------
    @Override
    protected int authenticateUser(final boolean newToken) {
        return authenticateUser(
                newToken,
                Response::getStatus
        );
    }

    public <T> T authenticateUser(final boolean newToken,
                                  final Function<Response, T> function) {
        return apply(c -> {
            final Response response = authenticateUser(
                    c, authUrl, getXAuthUserHeaderValue(), authKey,
                    newToken);
            try {
                if (OK.getStatusCode() != response.getStatus()) {
                    throw new WebApplicationException(
                            "failed to authenticate user", response);
                }
                storageUrl = response.getHeaderString(HEADER_X_STORAGE_URL);
                authToken = response.getHeaderString(HEADER_X_AUTH_TOKEN);
                setAuthTokenExpires(response.getHeaderString(
                        HEADER_X_AUTH_TOKEN_EXPIRES));
                return function.apply(response);
            } finally {
                response.close();
            }
        });
    }

    public <T> T authenticateUser(
            final boolean newToken,
            final BiFunction<Response, WsRsClient, T> function) {
        return authenticateUser(
                newToken,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public WsRsClient authenticateUser(
            final boolean newToken,
            final Consumer<Response> consumer) {
        return authenticateUser(
                newToken,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public WsRsClient authenticateUser(
            final boolean newToken,
            final BiConsumer<Response, WsRsClient> consumer) {
        return authenticateUser(
                newToken,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

//    /**
//     * Invalidates this client by purging the authorization information.
//     *
//     * @return this client
//     */
//    public WsRsClient invalidate() {
//        storageUrl = null;
//        authToken = null;
//        authTokenExpires = null;
//        return this;
//    }
//
//    /**
//     * Checks if the authorization information is valid until given
//     * milliseconds.
//     *
//     * @param until the milliseconds
//     * @return {@code true} if the token is value until given milliseconds,
//     * {@code false} otherwise.
//     */
//    public boolean isValid(final long until) {
//        if (storageUrl == null || authToken == null
//            || authTokenExpires == null) {
//            invalidate();
//            return false;
//        }
//        return authTokenExpires.getTime() >= until;
//    }
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
                       final BiConsumer<String, WsRsClient> consumer)
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
                       final BiConsumer<String, WsRsClient> consumer)
            throws IOException {
        lines(response, l -> consumer.accept(l, this));
    }

    // ---------------------------------------------------------------- /account
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
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildAccount(
                    client, storageUrl, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder = builder.headers(headers);
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

    public <T> T peekAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, WsRsClient, T> function) {
        return peekAccount(
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public WsRsClient peekAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return peekAccount(
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public WsRsClient peekAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
        return peekAccount(
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    /**
     * Reads a storage using {@link javax.ws.rs.HttpMethod#GET}.
     *
     * @param <T> result type parameter
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value {@code function} results
     */
    public <T> T readAccount(final MultivaluedMap<String, Object> params,
                             final MultivaluedMap<String, Object> headers,
                             final Function<Response, T> function) {
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildAccount(
                    client, storageUrl, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder = builder.headers(headers);
            }
            final Response response = builder.get();
            try {
                return function.apply(response);
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
            final BiFunction<Response, WsRsClient, T> function) {
        return readAccount(
                params,
                headers,
                r -> {
                    return requireNonNull(function, "null function")
                    .apply(r, this);
                }
        );
    }

    public WsRsClient readAccount(
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

    public WsRsClient readAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
        return readAccount(
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r, this);
                }
        );
    }

    /**
     * Reads an account and consumes all container names.
     *
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param predicate a predicate for testing server response.
     * @param consumer the consumer to be accepted with each of the consumer
     * names and this client; may be {@code null}
     * @return this client
     */
    public WsRsClient readAccountContainerNames(
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
     * @param prediate a predicate for testing the server response before
     * accepting the {@code consumer}
     * @param consumer the consumer to be accepted with each of the consumer
     * names and this client; may be {@code null}
     * @return this client
     */
    public WsRsClient readAccountContainerNames(
            MultivaluedMap<String, Object> params,
            MultivaluedMap<String, Object> headers,
            final Predicate<Response> prediate,
            final BiConsumer<String, WsRsClient> consumer) {
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
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildAccount(
                    client, storageUrl, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder = builder.headers(headers);
            }
            final Response response = builder.post(null);
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
            final BiFunction<Response, WsRsClient, T> function) {
        return configureAccount(
                params,
                headers,
                r -> {
                    return requireNonNull(function, "null function")
                    .apply(r, this);
                }
        );
    }

    public WsRsClient configureAccount(
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

    public WsRsClient configureAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
        return configureAccount(
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r, this);
                }
        );
    }

    // ------------------------------------------------------- account/container
    public <T> T peekContainer(final String containerName,
                               final MultivaluedMap<String, Object> params,
                               final MultivaluedMap<String, Object> headers,
                               final Function<Response, T> function) {
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildContainer(
                    client, storageUrl, containerName, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder = builder.headers(headers);
            }
            final Response response = builder.head();
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
            final BiFunction<Response, WsRsClient, T> function) {
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

    public WsRsClient peekContainer(
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

    public WsRsClient peekContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
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
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildContainer(
                    client, storageUrl, containerName, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder = builder.headers(headers);
            }
            final Response response = builder.get();
            try {
                return function.apply(response);
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
            final BiFunction<Response, WsRsClient, T> function) {
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

    public WsRsClient readContainer(
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

    public WsRsClient readContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
        return readContainer(
                containerName,
                params,
                headers,
                r -> {
                    requireNonNull(consumer, "null consumer").accept(r, this);
                }
        );
    }

    public WsRsClient readContainerObjectNames(
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

    public WsRsClient readContainerObjectNames(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Predicate<Response> predicate,
            final BiConsumer<String, WsRsClient> consumer) {
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
        final Client client = registerFilters(ClientBuilder.newClient());
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
            final BiFunction<Response, WsRsClient, T> function) {
        return updateContainer(
                containerName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public WsRsClient updateContainer(
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

    public WsRsClient updateContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
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
        final Client client = registerFilters(ClientBuilder.newClient());
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
            final BiFunction<Response, WsRsClient, T> function) {
        return configureContainer(
                containerName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public WsRsClient configureContainer(
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

    public WsRsClient configureContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
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
        final Client client = registerFilters(ClientBuilder.newClient());
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
            final BiFunction<Response, WsRsClient, T> function) {
        return deleteContainer(
                containerName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public WsRsClient deleteContainer(
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

    public WsRsClient deleteContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
        return deleteContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    // ----------------------------------------------- /account/container/object
    public <T> T peekObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Function<Response, T> function) {
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildObject(
                    client, storageUrl, containerName, objectName, params,
                    authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder = builder.headers(headers);
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
            final BiFunction<Response, WsRsClient, T> function) {
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

    public WsRsClient peekObject(
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

    public WsRsClient peekObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
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
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildObject(
                    client, storageUrl, containerName, objectName, params,
                    authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder = builder.headers(headers);
            }
            final Response response = builder.get();
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
            final BiFunction<Response, WsRsClient, T> function) {
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

    public WsRsClient readObject(
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

    public WsRsClient readObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
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
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildObject(
                    client, storageUrl, containerName, objectName, params,
                    authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder = builder.headers(headers);
            }
            final Response response = builder.put(entity);
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
            final BiFunction<Response, WsRsClient, T> function) {
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

    public WsRsClient updateObject(
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

    public WsRsClient updateObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Entity<?> entity,
            final BiConsumer<Response, WsRsClient> consumer) {
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

    public <T> T configureObject(final String containerName,
                                 final String objectName,
                                 final MultivaluedMap<String, Object> params,
                                 final MultivaluedMap<String, Object> headers,
                                 final Function<Response, T> function) {
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildObject(
                    client, storageUrl, containerName, objectName, params,
                    authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder = builder.headers(headers);
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

    public <T> T configureObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, WsRsClient, T> function) {
        return configureObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public WsRsClient configureObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return configureObject(
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

    public WsRsClient configureObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
        return configureObject(
                containerName,
                objectName,
                params,
                headers,
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
//        ensureValid();
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildObject(
                    client, storageUrl, containerName, objectName, params,
                    authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder = builder.headers(headers);
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

    public <T> T deleteObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, WsRsClient, T> function) {
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

    public WsRsClient deleteObject(
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

    public WsRsClient deleteObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
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

    // ---------------------------------------------------------------- reseller
    // -------------------------------------------------------- reseller/account
    public <R> R readResellerAccount(
            final MultivaluedMap<String, Object> params,
            MultivaluedMap<String, Object> headers,
            final Function<Response, R> function) {
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            registerFilters(client);
//            WebTarget target = targetResellerAccount(client, storageUrl, authAdmin, params).path(".groups");
            final Invocation.Builder builder = buildResellerAccount(client, storageUrl, authAccount, params, headers,
                    authUser, authKey);
            if (headers != null) {
                headers = headers(headers, authAccount, authUser, authKey);
                builder.headers(headers);
            }
            final Response response = builder.get();
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <R> R readResellerAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, WsRsClient, R> function) {
        return readResellerAccount(
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public WsRsClient readResellerAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return readResellerAccount(
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public WsRsClient readResellerAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
        return readResellerAccount(
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    // --------------------------------------------------- reseller/account/user
    public <R> R readResellerUser(final String userName,
                                  final MultivaluedMap<String, Object> params,
                                  MultivaluedMap<String, Object> headers,
                                  final Function<Response, R> function) {
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            final Invocation.Builder builder = buildResellerUser(client, storageUrl, authAccount, userName, params, authUser,
                                                                 authKey);
            if (headers != null) {
                headers = headers(headers, authAccount, authUser, authKey);
                builder.headers(headers);
            }
            final Response response = builder.get();
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <R> R readResellerUser(
            final String userName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, WsRsClient, R> function) {
        return readResellerUser(
                userName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public WsRsClient readResellerUser(
            final String userName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return readResellerUser(
                userName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public WsRsClient readResellerUser(
            final String userName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
        return readResellerUser(
                userName,
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    public <R> R updateResellerUser(final String userName, final String userKey,
                                    final MultivaluedMap<String, Object> params,
                                    MultivaluedMap<String, Object> headers,
                                    final Function<Response, R> function) {
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            Invocation.Builder builder = buildResellerUser(client, storageUrl, authAccount, userName, params, authUser,
                                                           authKey);
            headers = headers(headers, authAccount, authUser, authKey);
            headers.putSingle(HEADER_X_AUTH_USER_KEY, userKey);
            headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
            builder = builder.headers(headers);
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

    public <R> R updateResellerUser(
            final String userName, final String userKey,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, WsRsClient, R> function) {
        return updateResellerUser(
                userName,
                userKey,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public WsRsClient updateResellerUser(
            final String userName, final String userKey,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return updateResellerUser(
                userName,
                userKey,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public WsRsClient updateResellerUser(
            final String userName, final String userKey,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
        return updateResellerUser(
                userName,
                userKey,
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    public <R> R deleteResellerUser(final String userName,
                                    final MultivaluedMap<String, Object> params,
                                    MultivaluedMap<String, Object> headers,
                                    final Function<Response, R> function) {
        final Client client = registerFilters(ClientBuilder.newClient());
        try {
            final Invocation.Builder builder = buildResellerUser(client, storageUrl, authAccount, userName, params, authUser,
                                                                 authKey);
            if (headers != null) {
                headers = headers(headers, authAccount, authUser, authKey);
                builder.headers(headers);
            }
            final Response response = builder.delete();
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();;
        }
    }

    public <R> R deleteResellerUser(
            final String userName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, WsRsClient, R> function) {
        return deleteResellerUser(
                userName,
                params,
                headers,
                r -> {
                    return function.apply(r, this);
                }
        );
    }

    public WsRsClient deleteResellerUser(
            final String userName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return deleteResellerUser(
                userName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return this;
                }
        );
    }

    public WsRsClient deleteResellerUser(
            final String userName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, WsRsClient> consumer) {
        return deleteResellerUser(
                userName,
                params,
                headers,
                r -> {
                    consumer.accept(r, this);
                }
        );
    }

    // ------------------------------------------------------------- authAccount
    @Override
    public WsRsClient authAccount(final String authAccount) {
        return (WsRsClient) super.authAccount(authAccount);
    }

    // ----------------------------------------------------- clientRequestFilter
    public ClientRequestFilter getClientRequestFilter() {
        return clientRequestFilter;
    }

    public void setClientRequestFilter(
            final ClientRequestFilter clientRequestFilter) {
        this.clientRequestFilter = clientRequestFilter;
    }

    // ---------------------------------------------------- clientResponseFilter
    public ClientResponseFilter getClientResponseFilter() {
        return clientResponseFilter;
    }

    public void setClientResponseFilter(
            final ClientResponseFilter clientResponseFilter) {
        this.clientResponseFilter = clientResponseFilter;
    }

    private transient ClientRequestFilter clientRequestFilter;

    private transient ClientResponseFilter clientResponseFilter;
}
