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
import java.nio.charset.Charset;
import java.util.Date;
import static java.util.Optional.ofNullable;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public abstract class StorageClient {

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

    /**
     * Accepts each lines of given {@code reader} to specified {@code consumer}.
     *
     * @param reader the reader
     * @param consumer the consumer
     * @throws IOException if an I/O error occurs.
     */
    public static void lines(final Reader reader,
                             final Consumer<String> consumer)
            throws IOException {
        try (BufferedReader buffered = new BufferedReader(reader)) {
            buffered.lines().forEach(consumer::accept);
        }
    }

    /**
     * Accepts each lines of given {@code reader} and given {@code client} to
     * specified {@code consumer}.
     *
     * @param <T> client type parameter
     * @param reader the reader
     * @param consumer the consumer
     * @param client the client
     * @return given {@code client}
     * @throws IOException if an I/O error occurs.
     */
    public static <T extends StorageClient> T lines(
            final Reader reader, final BiConsumer<String, T> consumer,
            final T client)
            throws IOException {
        lines(reader, l -> consumer.accept(l, client));
        return client;
    }

    public static void lines(final InputStream stream, final Charset charset,
                             final Consumer<String> consumer)
            throws IOException {
        try (InputStreamReader reader
                = new InputStreamReader(stream, charset)) {
            lines(reader, consumer);
        }
    }

    public static <T extends StorageClient> T lines(
            final InputStream stream, final Charset charset,
            final BiConsumer<String, T> consumer, final T client)
            throws IOException {
        try (InputStreamReader reader
                = new InputStreamReader(stream, charset)) {
            return lines(reader, consumer, client);
        }
    }

    // -------------------------------------------------------------------------
    public StorageClient(final String authUrl, final String authUser,
                         final String authKey) {
        this.authUrl = requireNonNull(authUrl, "null authUrl");
        this.authUser = requireNonNull(authUser, "null authUser");
        this.authKey = requireNonNull(authKey, "null authKey");
    }

    protected abstract int authenticateUser(boolean newToken);

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
        return isValid(currentTimeMillis() + unit.toMillis(duration));
    }

    // ----------------------------------------------------------------- authUrl
    public String getAuthUrl() {
        return authUrl;
    }

    // ------------------------------------------------------------- authAccount
    public String getAuthAccount() {
        return authAccount;
    }

    public void setAuthAccount(final String authAccount) {
        this.authAccount = authAccount;
    }

    public StorageClient authAccount(final String authAccount) {
        setAuthAccount(authAccount);
        return this;
    }

    // ---------------------------------------------------------------- authUser
    public String getAuthUser() {
        return authUser;
    }

    // ---------------------------------------------------- authAccount/authUser
    protected String getXAuthUserHeaderValue() {
        return (authAccount != null ? authAccount + ":" : "") + authUser;
    }

    // ----------------------------------------------------------------- authKey
    public String getAuthKey() {
        return authKey;
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

    // --------------------------------------------------------------- authToken
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

    protected String authAccount;

    protected final String authUser;

    protected final String authKey;

    protected transient String storageUrl;

    protected transient String authToken;

    protected transient Date authTokenExpires;
}
