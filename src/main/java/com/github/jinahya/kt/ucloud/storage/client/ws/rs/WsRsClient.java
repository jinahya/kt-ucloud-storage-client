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
import com.github.jinahya.kt.ucloud.storage.client.StorageClientException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import static javax.ws.rs.core.Response.Status.OK;
import javax.ws.rs.core.Response.StatusType;

/**
 * A client for accessing kt ucloud storage using JAX-RS.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class WsRsClient extends StorageClient<WsRsClient, Entity<?>, Response> {

    private static final Logger logger = getLogger(WsRsClient.class.getName());

    public static <R> R statusInfo(final Response response,
                                   final Function<StatusType, R> function) {
        return function.apply(response.getStatusInfo());
    }

    public static Family statusFamily(final Response response) {
        return statusInfo(response, StatusType::getFamily);
    }

    public static int statusCode(final Response response) {
        return statusInfo(response, StatusType::getStatusCode);
    }

    @Deprecated
    public static String reasonPhrase(final Response response) {
        return statusInfo(response, StatusType::getReasonPhrase);
    }

    /**
     * Returns a newly created instance of {@link MultivaluedMap} containing
     * given map's entries.
     *
     * @param <K> key type parameter
     * @param <V> value type parameter
     * @param map the map
     * @return a {@link MultivaluedMap} containing given map's entries or
     * {@code null} if the {@code map} is {@code null}
     */
    public static <K, V> MultivaluedMap<K, V> multivalued(
            final Map<K, List<V>> map) {
        if (map == null) {
            return null;
        }
        if (map instanceof MultivaluedMap) {
            return (MultivaluedMap<K, V>) map;
        }
        final MultivaluedMap<K, V> multi = new MultivaluedHashMap<>(map.size());
        multi.putAll(map);
        return multi;
    }

    // -------------------------------------------------------------------------
    public static Response authenticateUser(final Client client,
                                            final String authUrl,
                                            final String authUser,
                                            final String authKey,
                                            final boolean newToken) {
        Invocation.Builder builder = client
                .target(authUrl)
                .request()
                .header(HEADER_X_AUTH_USER, authUser)
                .header(HEADER_X_AUTH_PASS, authKey);
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
        WebTarget target = client.target(storageUrl);
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
                .header(HEADER_X_AUTH_TOKEN, authToken);
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
                    .path(containerName);
        }
        WebTarget target = client.target(storageUrl).path(containerName);
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
        WebTarget target
                = targetContainer(client, storageUrl, containerName, params)
                .path(objectName);
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

    // ------------------------------------------------------- /reseller/account
    public static WebTarget targetResellerAccount(
            final Client client, final String resellerUrl,
            final MultivaluedMap<String, Object> params) {
        WebTarget target = client.target(resellerUrl);
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
            final Client client, final String resellerUrl,
            final MultivaluedMap<String, Object> params,
            final String authAdminUser, final String authAdminKey) {
        return targetResellerAccount(client, resellerUrl, params)
                .request()
                .header(HEADER_X_AUTH_ADMIN_USER, authAdminUser)
                .header(HEADER_X_AUTH_ADMIN_KEY, authAdminKey);
    }

    // -------------------------------------------------- /reseller/account/user
    public static WebTarget targetResellerUser(
            final Client client, final String resellerStorageUrl,
            final String userName,
            final MultivaluedMap<String, Object> params) {
        return targetResellerAccount(client, resellerStorageUrl, params)
                .path(userName);
    }

    public static Invocation.Builder buildResellerUser(
            final Client client, final String resellerAccountUrl,
            final String userName,
            final MultivaluedMap<String, Object> params,
            final String authAdminUser, final String authAdminKey) {
        return targetResellerUser(client, resellerAccountUrl, userName, params)
                .request()
                .header(HEADER_X_AUTH_ADMIN_USER, authAdminUser)
                .header(HEADER_X_AUTH_ADMIN_KEY, authAdminKey);
    }

    // -------------------------------------------------------------------------
    public static void lines(final Response response, final Charset charset,
                             final Consumer<String> consumer) {
        try {
            try (InputStream stream = response.readEntity(InputStream.class)) {
                lines(stream, charset, consumer);
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    public static WsRsClient lines(
            final Response response, final Charset charset,
            final BiConsumer<String, WsRsClient> consumer,
            final WsRsClient client) {
        try {
            try (InputStream stream = response.readEntity(InputStream.class)) {
                return lines(stream, charset, consumer, client);
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    public static void lines(final Response response,
                             final Consumer<String> consumer) {
        try {
            try (Reader reader = response.readEntity(Reader.class)) {
                lines(reader, consumer);
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
    }

    public static WsRsClient lines(
            final Response response,
            final BiConsumer<String, WsRsClient> consumer,
            final WsRsClient client) {
        try {
            try (Reader reader = response.readEntity(Reader.class)) {
                return lines(reader, consumer, client);
            }
        } catch (final IOException ioe) {
            throw new StorageClientException(ioe);
        }
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

    @Deprecated
    public WsRsClient(final StorageClient client) {
        this(client.getStorageUrl(), client.getAuthUser(),
             client.getAuthKey());
        setStorageUrl(client.getStorageUrl());
        setAuthToken(client.getAuthToken());
        setAuthTokenExpires(client.getAuthTokenExpires());
    }

    // -------------------------------------------------------------------------
    @Override
    public <R> R authenticateUser(final boolean newToken,
                                  final Function<Response, R> function) {
        return applyClient(c -> {
            final Response response = authenticateUser(
                    c, authUrl, authUser, authKey, newToken);
            try {
                if (OK.getStatusCode() != response.getStatus()) {
                    throw new WebApplicationException(
                            "failed to authenticate user", response);
                }
                setStorageUrl(response.getHeaderString(HEADER_X_STORAGE_URL));
                setAuthToken(response.getHeaderString(HEADER_X_AUTH_TOKEN));
                setAuthTokenExpires(response.getHeaderString(
                        HEADER_X_AUTH_TOKEN_EXPIRES));
                return function.apply(response);
            } finally {
                response.close();
            }
        });
    }

    // ---------------------------------------------------------------- /account
    public <R> R peekAccount(final MultivaluedMap<String, Object> params,
                             final MultivaluedMap<String, Object> headers,
                             final Function<Response, R> function) {
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildAccount(
                    client, getStorageUrl(), params, getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R peekStorage(final Map<String, List<Object>> params,
                             final Map<String, List<Object>> headers,
                             final Function<Response, R> function) {
        return peekAccount(multivalued(params), multivalued(headers), function);
    }

    /**
     * Reads a storage using {@link javax.ws.rs.HttpMethod#GET}.
     *
     * @param <R> result type parameter
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function a function to be applied with the server response
     * @return the value {@code function} results
     */
    public <R> R readAccount(final MultivaluedMap<String, Object> params,
                             final MultivaluedMap<String, Object> headers,
                             final Function<Response, R> function) {
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildAccount(
                    client, getStorageUrl(), params, getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R readStorage(final Map<String, List<Object>> params,
                             final Map<String, List<Object>> headers,
                             final Function<Response, R> function) {
        return readAccount(multivalued(params), multivalued(headers), function);
    }

    public <R> R configureStorage(final MultivaluedMap<String, Object> params,
                                  final MultivaluedMap<String, Object> headers,
                                  final Function<Response, R> function) {
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildAccount(
                    client, getStorageUrl(), params, getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R configureStorage(final Map<String, List<Object>> params,
                                  final Map<String, List<Object>> headers,
                                  final Function<Response, R> function) {
        return configureStorage(
                multivalued(params), multivalued(headers), function);
    }

    // ------------------------------------------------------ /account/container
    public <R> R peekContainer(final String containerName,
                               final MultivaluedMap<String, Object> params,
                               final MultivaluedMap<String, Object> headers,
                               final Function<Response, R> function) {
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildContainer(
                    client, getStorageUrl(), containerName, params, getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R peekContainer(final String containerName,
                               final Map<String, List<Object>> params,
                               final Map<String, List<Object>> headers,
                               final Function<Response, R> function) {
        return peekContainer(containerName, multivalued(params),
                             multivalued(headers), function);
    }

    public <R> R readContainer(final String containerName,
                               final MultivaluedMap<String, Object> params,
                               final MultivaluedMap<String, Object> headers,
                               final Function<Response, R> function) {
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildContainer(
                    client, getStorageUrl(), containerName, params, getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R readContainer(final String containerName,
                               final Map<String, List<Object>> params,
                               final Map<String, List<Object>> headers,
                               final Function<Response, R> function) {
        return readContainer(containerName, multivalued(params),
                             multivalued(headers), function);
    }

    /**
     * Creates or updates a container.
     *
     * @param <R> result type parameter
     * @param containerName container name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}
     * @param function the function to be applied with the response; may be
     * {@code null}
     * @return the value the function results; {@code null} if the
     * {@code function} is {@code null}
     */
    public <R> R updateContainer(final String containerName,
                                 final MultivaluedMap<String, Object> params,
                                 final MultivaluedMap<String, Object> headers,
                                 final Function<Response, R> function) {
        final Client client = getClient();
        try {
            final Invocation.Builder builder = buildContainer(
                    client, getStorageUrl(), containerName, params, getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R updateContainer(final String containerName,
                                 final Map<String, List<Object>> params,
                                 final Map<String, List<Object>> headers,
                                 final Function<Response, R> function) {
        return updateContainer(containerName, multivalued(params),
                               multivalued(headers), function);
    }

    public <R> R configureContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Function<Response, R> function) {
        final Client client = getClient();
        try {
            final Invocation.Builder builder = buildContainer(
                    client, getStorageUrl(), containerName, params,
                    getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R configureContainer(final String containerName,
                                    final Map<String, List<Object>> params,
                                    final Map<String, List<Object>> headers,
                                    final Function<Response, R> function) {
        return configureContainer(containerName, multivalued(params),
                                  multivalued(headers), function);
    }

    /**
     * Deletes a container identified by given name and returns the result .
     *
     * @param <R> return value type parameter
     * @param containerName the container name
     * @param params query parameters
     * @param headers additional request headers
     * @param function the function to be applied with the response.
     * @return the value function results or {@code null} if the
     * {@code function} is {@code null}.
     */
    public <R> R deleteContainer(final String containerName,
                                 final MultivaluedMap<String, Object> params,
                                 final MultivaluedMap<String, Object> headers,
                                 final Function<Response, R> function) {
        final Client client = getClient();
        try {
            final Invocation.Builder builder = buildContainer(
                    client, getStorageUrl(), containerName, params, getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R deleteContainer(final String containerName,
                                 final Map<String, List<Object>> params,
                                 final Map<String, List<Object>> headers,
                                 final Function<Response, R> function) {
        return deleteContainer(containerName, multivalued(params),
                               multivalued(headers), function);
    }

    // ----------------------------------------------- /account/container/object
    public <R> R peekObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Function<Response, R> function) {
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildObject(
                    client, getStorageUrl(), containerName, objectName, params,
                    getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R peekObject(final String containerName, final String objectName,
                            final Map<String, List<Object>> params,
                            final Map<String, List<Object>> headers,
                            final Function<Response, R> function) {
        return peekObject(containerName, objectName, multivalued(params),
                          multivalued(headers), function);
    }

    public <R> R readObject(final String containerName, final String objectName,
                            final MultivaluedMap<String, Object> params,
                            final MultivaluedMap<String, Object> headers,
                            final Function<Response, R> function) {
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildObject(
                    client, getStorageUrl(), containerName, objectName, params,
                    getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R readObject(final String containerName, final String objectName,
                            final Map<String, List<Object>> params,
                            final Map<String, List<Object>> headers,
                            final Function<Response, R> function) {
        return readObject(containerName, objectName, multivalued(params),
                          multivalued(headers), function);
    }

    public <R> R updateObject(final String containerName,
                              final String objectName,
                              final MultivaluedMap<String, Object> params,
                              final MultivaluedMap<String, Object> headers,
                              final Supplier<Entity<?>> entity,
                              final Function<Response, R> function) {
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildObject(
                    client, getStorageUrl(), containerName, objectName, params,
                    getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
                builder = builder.headers(headers);
            }
            final Response response = builder.put(entity.get());
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    @Override
    public <R> R updateObject(final String containerName,
                              final String objectName,
                              final Map<String, List<Object>> params,
                              final Map<String, List<Object>> headers,
                              final Supplier<Entity<?>> entity,
                              final Function<Response, R> function) {
        return updateObject(containerName, objectName, multivalued(params),
                            multivalued(headers), entity, function);
    }

    public <R> R configureObject(final String containerName,
                                 final String objectName,
                                 final MultivaluedMap<String, Object> params,
                                 final MultivaluedMap<String, Object> headers,
                                 final Function<Response, R> function) {
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildObject(
                    client, getStorageUrl(), containerName, objectName, params,
                    getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R configureObject(final String containerName,
                                 final String objectName,
                                 final Map<String, List<Object>> params,
                                 final Map<String, List<Object>> headers,
                                 final Function<Response, R> function) {
        return configureObject(containerName, objectName, multivalued(params),
                               multivalued(headers), function);
    }

    /**
     * Deletes an object using {@link javax.ws.rs.HttpMethod#DELETE}.
     *
     * @param <R> return value type parameter
     * @param containerName the container name
     * @param objectName the object name
     * @param params query parameters; may be {@code null}
     * @param headers request headers; may be {@code null}.
     * @param function a function to be applied with the server response; may be
     * {@code null}
     * @return a value the {@code function} results or {@code null} if the
     * {@code function} is {@code null}
     */
    public <R> R deleteObject(final String containerName,
                              final String objectName,
                              final MultivaluedMap<String, Object> params,
                              final MultivaluedMap<String, Object> headers,
                              final Function<Response, R> function) {
//        ensureValid();
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildObject(
                    client, getStorageUrl(), containerName, objectName, params,
                    getAuthToken());
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, getAuthToken());
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

    @Override
    public <R> R deleteObject(final String containerName,
                              final String objectName,
                              final Map<String, List<Object>> params,
                              final Map<String, List<Object>> headers,
                              final Function<Response, R> function) {
        return deleteObject(containerName, objectName, multivalued(params),
                            multivalued(headers), function);
    }

    // ------------------------------------------------------- /reseller/account
    public <R> R readResellerAccount(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Function<Response, R> function) {
        final Client client = getClient();
        try {
            final Invocation.Builder builder = buildResellerAccount(client, accountUrl(), params, authUser, authKey);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_ADMIN_USER, authUser);
                headers.putSingle(HEADER_X_AUTH_ADMIN_KEY, authKey);
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

    @Override
    public <R> R readAccount(final Map<String, List<Object>> params,
                             final Map<String, List<Object>> headers,
                             final Function<Response, R> function) {
        return readResellerAccount(multivalued(params), multivalued(headers),
                                   function);
    }

    // -------------------------------------------------- /reseller/account/user
    public <R> R readUer(final String userName,
                         final MultivaluedMap<String, Object> params,
                         final MultivaluedMap<String, Object> headers,
                         final Function<Response, R> function) {
        final Client client = getClient();
        try {
            final Invocation.Builder builder = buildResellerUser(client, accountUrl(), userName, params, authUser, authKey);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_ADMIN_USER, authUser);
                headers.putSingle(HEADER_X_AUTH_ADMIN_KEY, authKey);
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

    @Override
    public <R> R readUser(final String userName,
                          final Map<String, List<Object>> params,
                          final Map<String, List<Object>> headers,
                          final Function<Response, R> function) {
        return readUser(userName, multivalued(params),
                        multivalued(headers), function);
    }

    public <R> R updateUser(final String userName, final String userKey,
                            final Boolean userAdmin,
                            final MultivaluedMap<String, Object> params,
                            MultivaluedMap<String, Object> headers,
                            final Function<Response, R> function) {
        final Client client = getClient();
        try {
            Invocation.Builder builder = buildResellerUser(client, accountUrl(), userName, params, authUser,
                                                           authKey);
            if (headers == null) {
                headers = new MultivaluedHashMap<>();
            }
            headers.putSingle(HEADER_X_AUTH_ADMIN_USER, authUser);
            headers.putSingle(HEADER_X_AUTH_ADMIN_KEY, authKey);
            headers.putSingle(HEADER_X_AUTH_USER_KEY, userKey);
            if (userAdmin != null && userAdmin) {
                headers.putSingle(HEADER_X_AUTH_USER_ADMIN, userAdmin);
            }
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

    @Override
    public <R> R updateUser(final String userName, final String userKey,
                            final Boolean admin,
                            final Map<String, List<Object>> params,
                            final Map<String, List<Object>> headers,
                            final Function<Response, R> function) {
        return updateUser(userName, userKey, admin, multivalued(params),
                          multivalued(headers), function);
    }

    public <R> R deleteUser(
            final String userName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Function<Response, R> function) {
        final Client client = getClient();
        try {
            final Invocation.Builder builder = buildResellerUser(
                    client, accountUrl(), userName, params, authUser, authKey);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_ADMIN_USER, authUser);
                headers.putSingle(HEADER_X_AUTH_ADMIN_KEY, authKey);
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

    @Override
    public <R> R deleteUser(final String userName,
                            final Map<String, List<Object>> params,
                            final Map<String, List<Object>> headers,
                            final Function<Response, R> function) {
        return deleteUser(userName, multivalued(params), multivalued(headers),
                          function);
    }

    // ---------------------------------------------------------- clientSupplier
    public Supplier<Client> getClientSupplier() {
        return clientSupplier;
    }

    public void setClientSupplier(final Supplier<Client> clientSupplier) {
        this.clientSupplier = clientSupplier;
    }

    public WsRsClient clientSupplier(final Supplier<Client> clientSupplier) {
        setClientSupplier(clientSupplier);
        return this;
    }

    // ------------------------------------------------------------------ client
    protected Client getClient() {
        return getClientSupplier().get();
    }

    protected <R> R applyClient(final Function<Client, R> function) {
        final Client client = getClient();
        try {
            return function.apply(client);
        } finally {
            client.close();
        }
    }

    protected <U, R> R applyClient(final BiFunction<Client, U, R> function,
                                   final Supplier<U> supplier) {
        return applyClient(c -> {
            return function.apply(c, supplier.get());
        });
    }

    protected void acceptClient(final Consumer<Client> consumer) {
        applyClient(c -> {
            consumer.accept(c);
            return null;
        });
    }

    protected <U> void acceptClient(final BiConsumer<Client, U> consumer,
                                    final Supplier<U> supplier) {
        acceptClient(c -> {
            consumer.accept(c, supplier.get());
        });
    }

    // -------------------------------------------------------------------------
    private transient Supplier<Client> clientSupplier
            = () -> ClientBuilder.newClient();
}
