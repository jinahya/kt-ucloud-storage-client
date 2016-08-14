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
import java.util.List;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import static java.util.stream.Collectors.joining;
import javax.ws.rs.core.MultivaluedHashMap;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @param <ClientType> storage client implementation type parameter
 * @param <EntityType> request entity type parameter
 * @param <ResponseType> server response type parameter
 */
public abstract class StorageClient<ClientType extends StorageClient, EntityType, ResponseType> {

    public static final String AUTH_URL_STANDARD_KOR_CENTER
            = "https://api.ucloudbiz.olleh.com/storage/v1/auth";

    public static final String AUTH_URL_STANDARD_JPN
            = "https://api.ucloudbiz.olleh.com/storage/v1/authjp";

    public static final String AUTH_URL_LITE_KOR_HA
            = "https://api.ucloudbiz.olleh.com/storage/v1/authlite";

    public static final String QUERY_PARAM_LIMIT = "limit";

    public static final String QUERY_PARAM_MARKER = "marker";

    public static final String QUERY_PARAM_FORMAT = "format";

//    public static final String HEADER_X_AUTH_USER = "X-Storage-User";
    public static final String HEADER_X_AUTH_USER = "X-Auth-User";

//    public static final String HEADER_X_AUTH_PASS = "X-Storage-Pass";
    public static final String HEADER_X_AUTH_PASS = "X-Auth-Key";

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

    public static final String HEADER_X_CONTAINER_READ = "X-Container-Read";

    public static final String HEADER_X_CONTAINER_WRITE = "X-Container-Write";

    public static final String HEADER_X_COPY_FROM = "X-Copy-From";

    public static final String HEADER_X_AUTH_ADMIN_USER = "X-Auth-Admin-User";

    public static final String HEADER_X_AUTH_ADMIN_KEY = "X-Auth-Admin-Key";

    public static final String HEADER_X_AUTH_USER_KEY = "X-Auth-User-Key";

    public static final String HEADER_X_AUTH_USER_ADMIN = "X-Auth-User-Admin";

    public static String capitalize(final String token) {
        return token.substring(0, 1).toUpperCase()
               + token.substring(1).toLowerCase();
    }

    public static String capitalizeAndJoin(final String... tokens) {
        return stream(tokens).map(v -> capitalize(v)).collect(joining("-"));
    }

    public static String metaHeader(final boolean remove, final String scope,
                                    final String... tokens) {
        return "X" + (remove ? "-Remove" : "") + "-" + scope + "-Meta" + "-"
               + capitalizeAndJoin(tokens);
    }

    public static String accountMetaHeader(final boolean remove,
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

    public static String resellerAccountUrl(final String storageUrl,
                                            final String resellerAccountName) {
        try {
            final URL url
                    = new URL(requireNonNull(storageUrl, "null storageUrl"));
            final String protocol = url.getProtocol();
            final String authority = url.getAuthority();
            return protocol + "://" + authority + "/auth/v2/"
                   + requireNonNull(resellerAccountName,
                                    "null resellerAccountName");
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
    public StorageClient(final String authUrl, final String authUser,
                         final String authKey) {
        this.authUrl = requireNonNull(authUrl, "null authUrl");
        this.authUser = requireNonNull(authUser, "null authUser");
        {
            final int i = this.authUser.indexOf(':');
            resellerAccountName
                    = i == -1 ? null : this.authUser.substring(0, i);
        }
        this.authKey = requireNonNull(authKey, "null authKey");
    }

//    protected abstract int statusCode(ResponseType response);
    public abstract <R> R authenticateUser(boolean newToken,
                                           Function<ResponseType, R> function);

    public <R> R authenticateUser(
            final boolean newToken,
            final BiFunction<ResponseType, ClientType, R> function) {
        return authenticateUser(
                newToken,
                response -> {
                    return function.apply(response, (ClientType) this);
                }
        );
    }

    public ClientType authenticateUser(
            final boolean newToken,
            final Consumer<ResponseType> consumer) {
        return authenticateUser(
                newToken,
                response -> {
                    consumer.accept(response);
                    return (ClientType) this;
                }
        );
    }

    public ClientType authenticateUser(
            final boolean newToken,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return authenticateUser(
                newToken,
                response -> {
                    consumer.accept(response, (ClientType) this);
                }
        );
    }

    /**
     * Purges authorization information.
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
        return storageUrl != null && authToken != null
               && authTokenExpires != null
               && authTokenExpires.getTime() >= until;
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
        return isValid(currentTimeMillis() + unit.toMillis(duration));
    }

    // ---------------------------------------------------------------- /account
    public abstract <R> R peekAccount(Map<String, List<Object>> params,
                                      Map<String, List<Object>> headers,
                                      Function<ResponseType, R> function);

    public <R> R peekAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return peekAccount(
                params,
                headers,
                n -> {
                    return function.apply(n, (ClientType) this);
                }
        );
    }

    public ClientType peekAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return peekAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return (ClientType) this;
                }
        );
    }

    public ClientType peekAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return peekAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n, (ClientType) this);
                }
        );
    }

    public abstract <R> R readAccount(final Map<String, List<Object>> params,
                                      final Map<String, List<Object>> headers,
                                      final Function<ResponseType, R> function);

    public <R> R readAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return readAccount(
                params,
                headers,
                n -> {
                    return function.apply(n, (ClientType) this);
                }
        );
    }

    public ClientType readAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return readAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return (ClientType) this;
                }
        );
    }

    public ClientType readAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return readAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n, (ClientType) this);
                }
        );
    }

    public ClientType readAccountContainerNames(
            Map<String, List<Object>> params,
            Map<String, List<Object>> headers,
            final Function<ResponseType, Reader> function,
            final Consumer<String> consumer) {
        if (params == null) {
            params = new MultivaluedHashMap<>();
        }
        params.putIfAbsent(QUERY_PARAM_LIMIT, singletonList(512));
        if (headers == null) {
            headers = new MultivaluedHashMap<>();
        }
        headers.put("Accept", singletonList("text/plain"));
        for (final String[] marker = new String[1];;) {
            if (marker[0] != null) {
                params.put(QUERY_PARAM_MARKER, singletonList(marker[0]));
            }
            marker[0] = null;
            readAccount(
                    params,
                    headers,
                    r -> {
                        try {
                            try (Reader reader = function.apply(r)) {
                                lines(reader, consumer);
                            }
                        } catch (final IOException ioe) {
                            throw new StorageClientException(ioe);
                        }
                    }
            );
            if (marker[0] == null) {
                break;
            }
        }
        return (ClientType) this;
    }

    public ClientType readAccountContainerNames(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Function<ResponseType, Reader> function,
            final BiConsumer<String, ClientType> consumer) {
        return readAccountContainerNames(
                params,
                headers,
                function,
                l -> consumer.accept(l, (ClientType) this)
        );
    }

    public abstract <R> R configureAccount(Map<String, List<Object>> params,
                                           Map<String, List<Object>> headers,
                                           Function<ResponseType, R> function);

    public <R> R configureAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return configureAccount(
                params,
                headers,
                response -> {
                    return function.apply(response, (ClientType) this);
                }
        );
    }

    public ClientType configureAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return configureAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return (ClientType) this;
                }
        );
    }

    public ClientType configureAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return configureAccount(
                params,
                headers,
                n -> {
                    consumer.accept(n, (ClientType) this);
                }
        );
    }

    // ------------------------------------------------------ /account/container
    public abstract <R> R peekContainer(String containerName,
                                        Map<String, List<Object>> params,
                                        Map<String, List<Object>> headers,
                                        Function<ResponseType, R> function);

    public <R> R peekContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return peekContainer(
                containerName,
                params,
                headers,
                n -> {
                    return function.apply(n, (ClientType) this);
                }
        );
    }

    public ClientType peekContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return peekContainer(
                containerName,
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return (ClientType) this;
                }
        );
    }

    public ClientType peekContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return peekContainer(
                containerName,
                params,
                headers,
                n -> {
                    consumer.accept(n, (ClientType) this);
                }
        );
    }

    public abstract <R> R readContainer(String containerName,
                                        Map<String, List<Object>> params,
                                        Map<String, List<Object>> headers,
                                        Function<ResponseType, R> function);

    public <R> R readContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return readContainer(
                containerName,
                params,
                headers,
                n -> {
                    return function.apply(n, (ClientType) this);
                }
        );
    }

    public ClientType readContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return readContainer(
                containerName,
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return (ClientType) this;
                }
        );
    }

    public ClientType readContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return readContainer(
                containerName,
                params,
                headers,
                n -> {
                    consumer.accept(n, (ClientType) this);
                }
        );
    }

    public ClientType readContainerObjectNames(
            final String containerName,
            Map<String, List<Object>> params,
            Map<String, List<Object>> headers,
            final Function<ResponseType, Reader> function,
            final Consumer<String> consumer) {
        if (params == null) {
            params = new MultivaluedHashMap<>();
        }
        params.putIfAbsent(QUERY_PARAM_LIMIT, singletonList(512));
        if (headers == null) {
            headers = new MultivaluedHashMap<>();
        }
        headers.put("Accept", singletonList("text/plain"));
        for (final String[] marker = new String[1];;) {
            if (marker[0] != null) {
                params.put(QUERY_PARAM_MARKER, singletonList(marker[0]));
            }
            marker[0] = null;
            readContainer(
                    containerName,
                    params,
                    headers,
                    r -> {
                        try {
                            try (Reader reader = function.apply(r)) {
                                lines(reader, consumer);
                            }
                        } catch (final IOException ioe) {
                            throw new StorageClientException(ioe);
                        }
                    }
            );
            if (marker[0] == null) {
                break;
            }
        }
        return (ClientType) this;
    }

    public ClientType readContainerObjectNames(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Function<ResponseType, Reader> function,
            final BiConsumer<String, ClientType> consumer) {
        return readContainerObjectNames(
                containerName,
                params,
                headers,
                function,
                l -> consumer.accept(l, (ClientType) this)
        );
    }

    public abstract <R> R updateContainer(String containerName,
                                          Map<String, List<Object>> params,
                                          Map<String, List<Object>> headers,
                                          Function<ResponseType, R> function);

    public <R> R updateContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return updateContainer(
                containerName,
                params,
                headers,
                r -> {
                    return function.apply(r, (ClientType) this);
                }
        );
    }

    public ClientType updateContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return updateContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return (ClientType) this;
                }
        );
    }

    public ClientType updateContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return updateContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r, (ClientType) this);
                }
        );
    }

    public abstract <R> R configureContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Function<ResponseType, R> function);

    public <R> R configureContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return configureContainer(
                containerName,
                params,
                headers,
                r -> {
                    return function.apply(r, (ClientType) this);
                }
        );
    }

    public ClientType configureContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return configureContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return (ClientType) this;
                }
        );
    }

    public ClientType configureContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return configureContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r, (ClientType) this);
                }
        );
    }

    public abstract <R> R deleteContainer(String containerName,
                                          Map<String, List<Object>> params,
                                          Map<String, List<Object>> headers,
                                          Function<ResponseType, R> function);

    public <R> R deleteContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return deleteContainer(
                containerName,
                params,
                headers,
                r -> {
                    return function.apply(r, (ClientType) this);
                }
        );
    }

    public ClientType deleteContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return deleteContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return (ClientType) this;
                }
        );
    }

    public ClientType deleteContainer(
            final String containerName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return deleteContainer(
                containerName,
                params,
                headers,
                r -> {
                    consumer.accept(r, (ClientType) this);
                }
        );
    }

    // ----------------------------------------------- /account/container/object
    public abstract <R> R peekObject(String containerName, String objectName,
                                     Map<String, List<Object>> params,
                                     Map<String, List<Object>> headers,
                                     Function<ResponseType, R> function);

    public <R> R peekObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return peekObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    return function.apply(n, (ClientType) this);
                }
        );
    }

    public ClientType peekObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return peekObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return (ClientType) this;
                }
        );
    }

    public ClientType peekObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return peekObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    consumer.accept(n, (ClientType) this);
                }
        );
    }

    public abstract <R> R readObject(String containerName, String objectName,
                                     Map<String, List<Object>> params,
                                     Map<String, List<Object>> headers,
                                     Function<ResponseType, R> function);

    public <R> R readObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return readObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    return function.apply(n, (ClientType) this);
                }
        );
    }

    public ClientType readObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return readObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    consumer.accept(n);
                    return (ClientType) this;
                }
        );
    }

    public ClientType readObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return readObject(
                containerName,
                objectName,
                params,
                headers,
                n -> {
                    consumer.accept(n, (ClientType) this);
                }
        );
    }

    public abstract <R> R updateObject(String containerName, String objectName,
                                       Map<String, List<Object>> params,
                                       Map<String, List<Object>> headers,
                                       Supplier<EntityType> supplier,
                                       Function<ResponseType, R> function);

    public <R> R updateObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Supplier<EntityType> entity,
            final BiFunction<ResponseType, ClientType, R> function) {
        return updateObject(
                containerName,
                objectName,
                params,
                headers,
                entity,
                n -> {
                    return function.apply(n, (ClientType) this);
                }
        );
    }

    public ClientType updateObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Supplier<EntityType> supplier,
            final Consumer<ResponseType> consumer) {
        return updateObject(
                containerName,
                objectName,
                params,
                headers,
                supplier,
                n -> {
                    consumer.accept(n);
                    return (ClientType) this;
                }
        );
    }

    public ClientType updateObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Supplier<EntityType> supplier,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return updateObject(
                containerName,
                objectName,
                params,
                headers,
                supplier,
                n -> {
                    consumer.accept(n, (ClientType) this);
                }
        );
    }

    public abstract <R> R configureObject(String containerName,
                                          String objectName,
                                          Map<String, List<Object>> params,
                                          Map<String, List<Object>> headers,
                                          Function<ResponseType, R> function);

    public <R> R configureObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return configureObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    return function.apply(r, (ClientType) this);
                }
        );
    }

    public ClientType configureObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return configureObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return (ClientType) this;
                }
        );
    }

    public ClientType configureObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return configureObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    consumer.accept(r, (ClientType) this);
                }
        );
    }

    public abstract <R> R deleteObject(String containerName,
                                       String objectName,
                                       Map<String, List<Object>> params,
                                       Map<String, List<Object>> headers,
                                       Function<ResponseType, R> function);

    public <R> R deleteObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return deleteObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    return function.apply(r, (ClientType) this);
                }
        );
    }

    public ClientType deleteObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return deleteObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return (ClientType) this;
                }
        );
    }

    public ClientType deleteObject(
            final String containerName, final String objectName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return deleteObject(
                containerName,
                objectName,
                params,
                headers,
                r -> {
                    consumer.accept(r, (ClientType) this);
                }
        );
    }

    // ------------------------------------------------------- /reseller/account
    public abstract <R> R readResellerAccount(
            Map<String, List<Object>> params, Map<String, List<Object>> headers,
            Function<ResponseType, R> function);

    public <R> R readResellerAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return readResellerAccount(
                params,
                headers,
                r -> {
                    return function.apply(r, (ClientType) this);
                }
        );
    }

    public ClientType readResellerAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return readResellerAccount(
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return (ClientType) this;
                }
        );
    }

    public ClientType readResellerAccount(
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return readResellerAccount(
                params,
                headers,
                r -> {
                    consumer.accept(r, (ClientType) this);
                }
        );
    }

    // -------------------------------------------------- /reseller/account/user
    public abstract <R> R readResellerUser(String userName,
                                           Map<String, List<Object>> params,
                                           Map<String, List<Object>> headers,
                                           Function<ResponseType, R> function);

    public <R> R readResellerUser(
            final String userName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return readResellerUser(
                userName,
                params,
                headers,
                r -> {
                    return function.apply(r, (ClientType) this);
                }
        );
    }

    public ClientType readResellerUser(
            final String userName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return readResellerUser(
                userName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return (ClientType) this;
                }
        );
    }

    public ClientType readResellerUser(
            final String userName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return readResellerUser(
                userName,
                params,
                headers,
                r -> {
                    consumer.accept(r, (ClientType) this);
                }
        );
    }

    public abstract <R> R updateResellerUser(
            String userName, String userKey, boolean admin,
            Map<String, List<Object>> params,
            Map<String, List<Object>> headers,
            Function<ResponseType, R> function);

    public <R> R updateResellerUser(
            final String userName, final String userKey, final boolean admin,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return updateResellerUser(
                userName,
                userKey,
                admin,
                params,
                headers,
                r -> {
                    return function.apply(r, (ClientType) this);
                }
        );
    }

    public ClientType updateResellerUser(
            final String userName, final String userKey, final boolean admin,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return updateResellerUser(
                userName,
                userKey,
                admin,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return (ClientType) this;
                }
        );
    }

    public ClientType updateResellerUser(
            final String userName, final String userKey, final boolean admin,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return updateResellerUser(
                userName,
                userKey,
                admin,
                params,
                headers,
                r -> {
                    consumer.accept(r, (ClientType) this);
                }
        );
    }

    public abstract <R> R deleteResellerUser(
            String userName, Map<String, List<Object>> params,
            Map<String, List<Object>> headers,
            Function<ResponseType, R> function);

    public <R> R deleteResellerUser(
            final String userName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiFunction<ResponseType, ClientType, R> function) {
        return deleteResellerUser(
                userName,
                params,
                headers,
                r -> {
                    return function.apply(r, (ClientType) this);
                }
        );
    }

    public ClientType deleteResellerUser(
            final String userName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final Consumer<ResponseType> consumer) {
        return deleteResellerUser(
                userName,
                params,
                headers,
                r -> {
                    consumer.accept(r);
                    return (ClientType) this;
                }
        );
    }

    public ClientType deleteResellerUser(
            final String userName,
            final Map<String, List<Object>> params,
            final Map<String, List<Object>> headers,
            final BiConsumer<ResponseType, ClientType> consumer) {
        return deleteResellerUser(
                userName,
                params,
                headers,
                r -> {
                    consumer.accept(r, (ClientType) this);
                }
        );
    }

    // ----------------------------------------------------------------- authUrl
    public String getAuthUrl() {
        return authUrl;
    }

    // ---------------------------------------------------------------- authUser
    public String getAuthUser() {
        return authUser;
    }

    // ----------------------------------------------------------------- authKey
    public String getAuthKey() {
        return authKey;
    }

    // ----------------------------------------------------- resellerAccountName
    public String getResellerAccountName() {
        return resellerAccountName;
    }

    public String resellerAccountName() {
        return getResellerAccountName();
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

    public String storageUrl() {
        return getStorageUrl();
    }

    protected void setStorageUrl(final String storageUrl) {
        this.storageUrl = requireNonNull(storageUrl, "null storageUrl");
        if (resellerAccountName != null) {
            resellerAccountUrl
                    = resellerAccountUrl(storageUrl, resellerAccountName);
        }
    }

    protected StorageClient storegeUrl(final String storageUrl) {
        setStorageUrl(storageUrl);
        return this;
    }

    // ------------------------------------------------------ resellerAccountUrl
    public String getResllerAccountUrl() {
        return resellerAccountUrl;
    }

    public String resellerAccountUrl() {
        return getResellerAccountName();
    }

    // --------------------------------------------------------------- authToken
    /**
     * Returns the authorization token.
     *
     * @return the authorization token.
     */
    public String getAuthToken() {
        return authToken;
    }

    protected void setAuthToken(final String authToken) {
        this.authToken = authToken;
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

    protected void setAuthTokenExpires(final Date authTokenExpires) {
        this.authTokenExpires
                = ofNullable(authTokenExpires)
                .map(Date::getTime)
                .map(Date::new)
                .orElse(null);
    }

    protected void setAuthTokenExpires(final String authTokenExpires) {
        setAuthTokenExpires(
                ofNullable(authTokenExpires)
                .map(Integer::parseInt)
                .map(SECONDS::toMillis)
                .map(v -> new Date(currentTimeMillis() + v))
                .orElse(null)
        );
    }

    // -------------------------------------------------------------------------
    protected final String authUrl;

    protected final String authUser;

    protected final String authKey;

    private final String resellerAccountName;

    private transient String storageUrl;

    private transient String resellerAccountUrl;

    private transient String authToken;

    private transient Date authTokenExpires;
}
