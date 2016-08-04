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
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import static java.util.Optional.ofNullable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import javax.ws.rs.core.Response.Status;

/**
 * A client using JAX-RS.
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

    /**
     * Authenticates with given arguments.
     *
     * @param client the client
     * @param authUrl authentication URL
     * @param authUser authentication username
     * @param authPass authentication password
     * @return a response
     */
    public static Response authenticateUser(final Client client,
                                            final String authUrl,
                                            final String authUser,
                                            final String authPass) {
        return client
                .target(authUrl)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HEADER_X_AUTH_USER, authUser) // X-Auth-User -> 500
                .header(HEADER_X_AUTH_PASS, authPass) // X-Auth-Pass -> 500
                .header(HEADER_X_AUTH_NEW_TOKEN, true)
                .buildGet()
                .invoke();
    }

    public static WebTarget targetStorage(
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

    public static Invocation.Builder buildStorage(
            final Client client, final String storageUrl,
            final MultivaluedMap<String, Object> params,
            final String authToken) {
        return targetStorage(client, storageUrl, params)
                .request()
                .header(HEADER_X_AUTH_TOKEN, authToken);
    }

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
            return targetStorage(client, storageUrl, params)
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
     * @param client a client
     * @param storageUrl storage URL
     * @param containerName container name
     * @param params query parameters
     * @param authToken authorization token
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
     * @param function the function to be applied with an response.
     * @return the value applied or {@code null} if the {@code function} is
     * {@code null}
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
     * Invalidates this client by purging the authorization token.
     *
     * @return this client
     */
    public StorageClient invalidate() {
        storageUrl = null;
        authToken = null;
        authTokenExpires = null;
        return this;
    }

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

    // ----------------------------------------------------------------- storage
    public <T> T peekStorage(final MultivaluedMap<String, Object> params,
                             final MultivaluedMap<String, Object> headers,
                             final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildStorage(
                    client, storageUrl, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.head();
            try {
                return function == null ? null : function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T peekStorage(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return peekStorage(
                params,
                headers,
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null)
        );
    }

    public StorageClient peekStorage(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return peekStorage(
                params,
                headers,
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(r -> this)
        );
    }

    public StorageClient peekStorage(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return peekStorage(
                params,
                headers,
                ofNullable(consumer)
                .map(f -> (Consumer<Response>) r -> f.accept(r, this))
                .orElse(null)
        );
    }

    public <T> T readStorage(final MultivaluedMap<String, Object> params,
                             final MultivaluedMap<String, Object> headers,
                             final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildStorage(
                    client, storageUrl, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.get();
            try {
                return function == null ? null : function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T readStorage(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return readStorage(
                params,
                headers,
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null)
        );
    }

    public StorageClient readStorage(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return readStorage(
                params,
                headers,
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(null)
        );
    }

    public StorageClient readStorage(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return readStorage(
                params,
                headers,
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null)
        );
    }

    public <T> T updateStorage(final MultivaluedMap<String, Object> params,
                               final MultivaluedMap<String, Object> headers,
                               final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildStorage(
                    client, storageUrl, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
//            final Response response = builder.post(Entity.text(""));
            final Response response = builder.post(null);
            try {
                return function == null ? null : function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T updateStorage(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return updateStorage(
                params,
                headers,
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null)
        );
    }

    public StorageClient updateStorage(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return updateStorage(
                params,
                headers,
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(null)
        );
    }

    public StorageClient updateStorage(
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return updateStorage(
                params,
                headers,
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null)
        );
    }

    // --------------------------------------------------------------- container
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
    public <T> T createContainer(final String containerName,
                                 final MultivaluedMap<String, Object> params,
                                 final MultivaluedMap<String, Object> headers,
                                 final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildContainer(
                    client, storageUrl,
                    requireNonNull(containerName, "null containerName"),
                    params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.put(Entity.text(""));
            try {
                return function == null ? null : function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T createContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        return createContainer(
                containerName,
                params,
                headers,
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null)
        );
    }

    public StorageClient createContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Consumer<Response> consumer) {
        return createContainer(
                containerName,
                params,
                headers,
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(r -> this));
    }

    public StorageClient createContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<Response, StorageClient> consumer) {
        return createContainer(
                containerName,
                params,
                headers,
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null)
        );
    }

    public <T> T peekContainer(final String containerName,
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
            final Response response = builder.head();
            try {
                return function == null ? null : function.apply(response);
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
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null)
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
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(r -> this)
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
                ofNullable(consumer)
                .map(v -> (Consumer<Response>) r -> v.accept(r, this))
                .orElse(null)
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
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildContainer(
                    client, storageUrl, containerName, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.get();
            try {
                return function == null ? null : function.apply(response);
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
                containerName, params, headers,
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null)
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
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(r -> this)
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
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null)
        );
    }

    private void lines(final Response response, final Consumer<String> consumer)
            throws IOException {
        try (InputStream stream = response.readEntity(InputStream.class);
             InputStreamReader reader = new InputStreamReader(
                     stream, StandardCharsets.UTF_8);
             BufferedReader buffered = new BufferedReader(reader);) {
            buffered.lines().forEach(consumer::accept);
        }
    }

    private void lines(final Response response,
                       final BiConsumer<String, StorageClient> consumer)
            throws IOException {
        lines(response, l -> {
          consumer.accept(l, this);
      });
    }

    public StorageClient readContainerObjectNames(
            final String containerName,
            MultivaluedMap<String, Object> params,
            MultivaluedMap<String, Object> headers,
            final Consumer<String> consumer) {
        if (headers == null) {
            headers = new MultivaluedHashMap<>();
        }
        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
        return readContainer(
                containerName,
                params,
                headers,
                r -> {
                    final StatusType statusInfo = r.getStatusInfo();
                    final Family family = statusInfo.getFamily();
                    if (family != Family.SUCCESSFUL) {
                        throw new WebApplicationException(
                                "failed to read object names", r);
                    }
                    if (r.getStatus() != Status.OK.getStatusCode()) {
                        return;
                    }
                    if (consumer != null) {
                        try {
                            lines(r, consumer);
                        } catch (final IOException ioe) {
                            logger.log(Level.SEVERE, "failed to read container",
                                       ioe);
                        }
                    }
                }
        );
    }

    public StorageClient readContainerObjectNames(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            MultivaluedMap<String, Object> headers,
            final BiConsumer<String, StorageClient> consumer) {
        return StorageClient.this.readContainerObjectNames(
                containerName,
                params,
                headers,
                ofNullable(consumer)
                .map(c -> (Consumer<String>) r -> c.accept(r, this))
                .orElse(null)
        );
    }

    public StorageClient readContainerObjectNamesAll(
            final String containerName,
            MultivaluedMap<String, Object> params,
            MultivaluedMap<String, Object> headers,
            final Consumer<String> consumer) {
        final String[] marker = new String[1];
        marker[0] = null;
        if (headers == null) {
            headers = new MultivaluedHashMap<>();
        }
        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
        while (true) {
            final String marker_ = marker[0];
            if (marker_ != null) {
                params.putSingle("marker", marker_);
            }
            marker[0] = null;
            readContainerObjectNames(
                    containerName,
                    params,
                    headers,
                    ofNullable(consumer)
                    .map(c -> ((Consumer<String>) on -> marker[0] = on)
                            .andThen(c))
                    .orElse(null));
            if (marker[0] == null) {
                break;
            }
        }
        return this;
    }

    public StorageClient readContainerObjectNamesAll(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            MultivaluedMap<String, Object> headers,
            final BiConsumer<String, StorageClient> consumer) {
        return StorageClient.this.readContainerObjectNamesAll(
                containerName,
                params,
                headers,
                ofNullable(consumer)
                .map(c -> (Consumer<String>) r -> c.accept(r, this))
                .orElse(null)
        );
    }

    public <T> T updateContainer(final String containerName,
                                 final MultivaluedMap<String, Object> params,
                                 final MultivaluedMap<String, Object> headers,
                                 final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder = buildContainer(
                    client, storageUrl,
                    requireNonNull(containerName, "null containerName"),
                    params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.post(null);
            try {
                return function == null ? null : function.apply(response);
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
        return updateContainer(
                containerName,
                params,
                headers,
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null)
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
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(r -> this));
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
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null)
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
                return function == null ? null : function.apply(response);
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
                containerName, params, headers,
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null));
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
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(r -> this));
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
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null));
    }

    // ------------------------------------------------------------------ object
    public <T> T createObject(final String containerName,
                              final String objectName,
                              final MultivaluedMap<String, Object> params,
                              final MultivaluedMap<String, Object> headers,
                              final Entity<?> entity,
                              final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        updateContainer(containerName, null, null, (Consumer<Response>) null);
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
                return function == null ? null : function.apply(response);
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
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null));
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
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                }).orElse(r -> this)
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
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null));
    }

    public <T> T peekObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
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
                return function == null ? null : function.apply(response);
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
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null)
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
                ofNullable(consumer)
                .map(f -> (Function<Response, StorageClient>) r -> {
                    f.accept(r);
                    return this;
                })
                .orElse(r -> this)
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
                ofNullable(consumer)
                .map(f -> (Consumer<Response>) r -> f.accept(r, this))
                .orElse(null)
        );
    }

    public <T> T readObject(final String containerName, final String objectName,
                            final MultivaluedMap<String, Object> params,
                            final MultivaluedMap<String, Object> headers,
                            final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
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
                return function == null ? null : function.apply(response);
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
                containerName, objectName, params, headers,
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null));
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
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(r -> this));
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
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null)
        );
    }

    public <T> T updateObject(final String containerName,
                              final String objectName,
                              final MultivaluedMap<String, Object> params,
                              final MultivaluedMap<String, Object> headers,
                              final Entity<?> entity,
                              final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
        updateContainer(containerName, null, null, (Consumer<Response>) null);
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
                return function == null ? null : function.apply(response);
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
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null));
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
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                }).orElse(r -> this)
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
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null));
    }

    /**
     * Deletes an object.
     *
     * @param <T> return value type parameter
     * @param containerName the container name
     * @param objectName the object name
     * @param params query parameters
     * @param headers additional headers; may be {@code null}.
     * @param function a function applies with the response.
     * @return a value the function results
     */
    public <T> T deleteObject(final String containerName,
                              final String objectName,
                              final MultivaluedMap<String, Object> params,
                              final MultivaluedMap<String, Object> headers,
                              final Function<Response, T> function) {
        ensureValid(TimeUnit.MINUTES, 10L);
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
                return function == null ? null : function.apply(response);
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
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null));
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
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(r -> this)
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
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null)
        );
    }

    // -------------------------------------------------------------- storageUrl
    public String getStorageUrl() {
        return storageUrl;
    }

    // ---------------------------------------------------------------- authToken
    public String getAuthToken() {
        return authToken;
    }

    // ------------------------------------------------------------ tokenExpires
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

    private String storageUrl;

    private String authToken;

    private Date authTokenExpires;
}
