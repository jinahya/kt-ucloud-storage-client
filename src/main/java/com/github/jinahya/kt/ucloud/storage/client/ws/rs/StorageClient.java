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
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
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
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

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
        this.authUrl = authUrl;
        this.authUser = authUser;
        this.authPass = authPass;
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
                    assert storageUrl != null;
                    authToken = response.getHeaderString(HEADER_X_AUTH_TOKEN);
                    assert authToken != null;
                    final String authTokenExpires_ = response.getHeaderString(
                            HEADER_X_AUTH_TOKEN_EXPIRES);
                    assert authTokenExpires_ != null;
                    this.authTokenExpires = new Date(
                            System.currentTimeMillis()
                            + (Long.parseLong(authTokenExpires_) * 1000L));
                } else {
                    final int statusCode = statusInfo.getStatusCode();
                    final String reasonPhrase = statusInfo.getReasonPhrase();
                    logger.log(Level.SEVERE,
                               "failed to authenticate user; status: {0} {1}",
                               new Object[]{statusCode, reasonPhrase});
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
        authToken = null;
        authTokenExpires = null;
        return this;
    }

    public boolean isValid(final long until) {
        return authToken != null && authTokenExpires != null
               && authTokenExpires.getTime() >= until;
    }

    /**
     * Checks if the authentication token is valid before specified
     * milliseconds.
     *
     * @param unit time unit
     * @param duration time unit duration.
     * @return {@code true} if the token is valid; {@code false} otherwise.
     */
    public boolean isValid(final TimeUnit unit, long duration) {
        return isValid(System.currentTimeMillis() + unit.toMillis(duration));
    }

    /**
     * Ensures the authorization token is valid for given time. This method
     * checks the value of {@link #isValid(java.util.concurrent.TimeUnit, long)}
     * with given {@code unit} and {@code duration} and invokes
     * {@link #authenticateUser(java.util.function.Function)} with given
     * {@code function}. Note that the {@code funtion} will never be applied if
     * {@link #isValid(java.util.concurrent.TimeUnit, long)} yields {@code true}
     * with given {@code unit} and {@code duration} and the return value of this
     * method is {@code null}.
     *
     * @param <T> result type parameter
     * @param unit time unit
     * @param duration time duration
     * @param function the function to be applied with the web response.
     * @return {@code null} if the authorization token is valid in given time or
     * the {@code funtion} is {@code null}. Or the value of
     * {@link #authenticateUser(java.util.function.Function)}.
     * @see #isValid(java.util.concurrent.TimeUnit, long)
     * @see #authenticateUser(java.util.function.Function)
     */
    public <T> T ensureValid(final TimeUnit unit, final long duration,
                             final Function<Response, T> function) {
        if (!isValid(unit, duration)) {
            return authenticateUser(function);
        }
        return null;
    }

    public <T> T ensureValid(
            final TimeUnit unit, long duration,
            final BiFunction<Response, StorageClient, T> function) {
        return ensureValid(
                unit,
                duration,
                ofNullable(function)
                .map(f -> (Function<Response, T>) r -> f.apply(r, this))
                .orElse(null));
    }

    public StorageClient ensureValid(final TimeUnit unit, final long duration,
                                     final Consumer<Response> consumer) {
        return ensureValid(
                unit,
                duration,
                ofNullable(consumer)
                .map(c -> (Function<Response, StorageClient>) r -> {
                    c.accept(r);
                    return this;
                })
                .orElse(r -> this)
        );
    }

    public StorageClient ensureValid(
            final TimeUnit unit,
            final long duration,
            final BiConsumer<Response, StorageClient> consumer) {
        return ensureValid(
                unit,
                duration,
                ofNullable(consumer)
                .map(c -> (Consumer<Response>) r -> c.accept(r, this))
                .orElse(null)
        );
    }

    public <T> T peekContainer(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            client.register((ClientRequestFilter) requestContext -> {
//                System.out.println("peekContainer.request.method: "
//                                   + requestContext.getMethod());
//                System.out.println("peekContainer.request.uri: "
//                                   + requestContext.getUri());
//                requestContext.getHeaders().entrySet().forEach(e -> {
//                    e.getValue().forEach(value -> {
//                        System.out.println("peekContainer.request.header: " + e.getKey()
//                                           + ": " + value);
//                    });
//                });
            });
            client.register((ClientResponseFilter) (requestContext, responseContext) -> {
//                final StatusType statusInfo = responseContext.getStatusInfo();
//                System.out.println("peekContainer.response.status: " + statusInfo.getStatusCode() + " " + statusInfo.getReasonPhrase());
//                responseContext.getHeaders().entrySet().forEach(e -> {
//                    e.getValue().forEach(value -> {
//                        System.out.println(
//                                "peekContainer.response.header: " + e.getKey()
//                                + ": " + value);
//                    });
//                });
            });
            final Invocation.Builder builder = buildContainer(
                    client, storageUrl, containerName, params, authToken);
            if (headers != null) {
                headers.putSingle(HEADER_X_AUTH_TOKEN, authToken);
                builder.headers(headers);
            }
            final Response response = builder.head();
            try {
                return function == null ? null : function.apply(response, this);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
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

    public StorageClient withObjectNames(
            final String containerName,
            MultivaluedMap<String, Object> params,
            MultivaluedMap<String, Object> headers,
            final Consumer<String> consumer) {
        if (false) {
            if (params == null) {
                params = new MultivaluedHashMap<>();
            }
            params.putSingle("format", "string");
        }
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
                        logger.log(Level.SEVERE,
                                   "failed to read object names; {0} {1}",
                                   new Object[]{statusInfo.getStatusCode(),
                                                statusInfo.getReasonPhrase()});
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
                });
    }

    public StorageClient withObjectNames(
            final String containerName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiConsumer<String, StorageClient> consumer) {
        return StorageClient.this.withObjectNames(
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
            final Response response = builder
                    .put(Entity.entity(
                            new byte[0], MediaType.APPLICATION_OCTET_STREAM));
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

    public <T> T peekObject(
            final String containerName, final String objectName,
            final MultivaluedMap<String, Object> params,
            final MultivaluedMap<String, Object> headers,
            final BiFunction<Response, StorageClient, T> function) {
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
                return function == null ? null : function.apply(response, this);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T readObject(final String containerName, final String objectName,
                            final MultivaluedMap<String, Object> params,
                            final MultivaluedMap<String, Object> headers,
                            final Function<Response, T> function) {
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

    // ------------------------------------------------------------ tokenExpires
    public Date getAuthTokenExpires() {
        if (authTokenExpires == null) {
            return null;
        }
        return new Date(authTokenExpires.getTime());
    }

    private final String authUrl;

    private final String authUser;

    private final String authPass;

    private String storageUrl;

    private String authToken;

    private Date authTokenExpires;
}
