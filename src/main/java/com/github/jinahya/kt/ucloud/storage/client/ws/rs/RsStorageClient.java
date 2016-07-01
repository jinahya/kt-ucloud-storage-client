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

import static java.util.Collections.singletonList;
import java.util.Date;
import java.util.function.Function;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * A client using JAX-RS.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class RsStorageClient {

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

    public static final String HEADER_X_STORAGE_URL = "X-Storage-Url";

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
     * Targets container.
     *
     * @param client the client to use.
     * @param storageUrl the storage URL
     * @param containerName container name
     * @return a target.
     */
    public static WebTarget targetContainer(final Client client,
                                            final String storageUrl,
                                            final String containerName) {
        return client.target(storageUrl).path(containerName);
    }

    /**
     * Builds an invocation for a container.
     *
     * @param client the client
     * @param storageUrl the storage URL
     * @param containerName container name
     * @param authToken authentication token.
     * @return an invocation builder.
     */
    public static Invocation.Builder buildContainer(final Client client,
                                                    final String storageUrl,
                                                    final String containerName,
                                                    final String authToken) {
        return targetContainer(client, storageUrl, containerName)
                .request()
                .header(HEADER_X_AUTH_TOKEN, authToken);
    }

    /**
     * Targets an object.
     *
     * @param client the client
     * @param storageUrl the storage URL
     * @param containerName the container name
     * @param objectName the object name
     * @return a target.
     */
    public static WebTarget targetObject(final Client client,
                                         final String storageUrl,
                                         final String containerName,
                                         final String objectName) {
        return client.target(storageUrl).path(containerName).path(objectName);
    }

    /**
     * Builds an invocation for an object.
     *
     * @param client the client
     * @param storageUrl the storage URL
     * @param containerName the container name
     * @param objectName the object name
     * @param authToken the authentication token
     * @return an invocation builder.
     */
    public static Invocation.Builder buildObject(
            final Client client, final String storageUrl,
            final String containerName, final String objectName,
            final String authToken) {
        return targetObject(client, storageUrl, containerName, objectName)
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
    public RsStorageClient(final String authUrl, final String authUser,
                           final String authPass) {
        super();
        this.authUrl = authUrl;
        this.authUser = authUser;
        this.authPass = authPass;
    }

    /**
     * Authenticates user.
     *
     * @param <T> return value type parameter.
     * @param function the function to be applied with an response.
     * @return the value applied
     */
    public <T> T authenticateUser(final Function<Response, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = authenticateUser(
                    client, authUrl, authUser, authPass);
            try {
                final int statusCode = response.getStatus();
                if (statusCode == 200) {
                    storageUrl = response.getHeaderString(HEADER_X_STORAGE_URL);
                    authToken = response.getHeaderString(HEADER_X_AUTH_TOKEN);
                    final String expires
                            = response.getHeaderString("X-Auth-Token-Expires");
                    tokenExpires = new Date(
                            System.currentTimeMillis()
                            + (Long.parseLong(expires) * 1000L));
                }
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    /**
     * Checks if the authentication token is valid before specified
     * milliseconds.
     *
     * @param millis the milliseconds.
     * @return {@code true} if the token is valid; {@code false} otherwise.
     */
    public boolean validBefore(final long millis) {
        return authToken != null && tokenExpires != null
               && tokenExpires.getTime() >= millis;
    }

    /**
     * Check if the token is valid and (if required) refreshes the token.
     *
     * @return this instance.
     */
    public RsStorageClient refreshToken() {
        if (!validBefore(System.currentTimeMillis() + 600000L)) {
            authenticateUser(response -> null);
        }
        return this;
    }

    /**
     * Refreshes the token if it expires before the specified milliseconds.
     *
     * @param millis the milliseconds
     * @return this instance.
     */
    public RsStorageClient refreshToken(final long millis) {
        if (!validBefore(millis)) {
            authenticateUser(response -> null);
        }
        return this;
    }

    /**
     * Targets a container identified by given name using specified client.
     *
     * @param client the client
     * @param containerName the container name
     * @return a web target
     */
    public WebTarget targetContainer(final Client client,
                                     final String containerName) {
        return targetContainer(client, storageUrl, containerName);
    }

    /**
     * Creates an invocation builder for a container identified by given name
     * using specified client.
     *
     * @param client the client
     * @param containerName the container name
     * @return an invocation builder.
     */
    public Invocation.Builder buildContainer(final Client client,
                                             final String containerName) {
        return buildContainer(client, storageUrl, containerName, authToken);
    }

    /**
     * Updates container.
     *
     * @param <T> return value type parameter
     * @param containerName the container name
     * @param headers additional request headers
     * @param function the function to be applied with the response.
     * @return the value applied.
     */
    public <T> T updateContainer(final String containerName,
                                 final MultivaluedMap<String, Object> headers,
                                 final Function<Response, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder
                    = buildContainer(client, containerName);
            if (headers != null) {
                headers.replace(HEADER_X_AUTH_TOKEN, singletonList(authToken));
                builder.headers(headers);
            }
            final Response response = builder
                    .put(Entity.entity(
                            new byte[0], MediaType.APPLICATION_OCTET_STREAM));
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T updateContainer(final String containerName,
                                 final Function<Response, T> function) {
        return updateContainer(containerName, null, function);
    }

    /**
     * Deletes a container identified by given name and returns the result .
     *
     * @param <T> return value type parameter
     * @param containerName the container name
     * @param headers additional request headers
     * @param function the function to be applied with the response.
     * @return the value function results.
     */
    public <T> T deleteContainer(final String containerName,
                                 final MultivaluedMap<String, Object> headers,
                                 final Function<Response, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder
                    = buildContainer(client, containerName);
            if (headers != null) {
                headers.replace(HEADER_X_AUTH_TOKEN, singletonList(authToken));
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

    public <T> T deleteContainer(final String containerName,
                                 final Function<Response, T> function) {
        return deleteContainer(containerName, null, function);
    }

    // ------------------------------------------------------------------ object
    public WebTarget targetObject(final Client client,
                                  final String containerName,
                                  final String objectName) {
        return targetObject(client, storageUrl, containerName, objectName);
    }

    public Invocation.Builder buildObject(final Client client,
                                          final String containerName,
                                          final String objectName) {
        return buildObject(client, storageUrl, containerName, objectName,
                           authToken);
    }

    public <T> T readObject(final String containerName, final String objectName,
                            final MultivaluedMap<String, Object> headers,
                            final Function<Response, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder
                    = buildObject(client, containerName, objectName);
            if (headers != null) {
                headers.replace(HEADER_X_AUTH_TOKEN, singletonList(authToken));
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

    public <T> T readObject(final String containerName, final String objectName,
                            final Function<Response, T> function) {
        return readObject(containerName, objectName, null, function);
    }

    /**
     * Updates an object.
     *
     * @param <T> return value type parameter
     * @param containerName the container name
     * @param objectName the object name
     * @param headers additional request headers; may be {@code null}
     * @param entity the entity
     * @param function a function applies with the response.
     * @return a value the function results.
     */
    public <T> T updateObject(final String containerName,
                              final String objectName,
                              final MultivaluedMap<String, Object> headers,
                              final Entity<?> entity,
                              final Function<Response, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder
                    = buildObject(client, containerName, objectName);
            if (headers != null) {
                headers.replace(HEADER_X_AUTH_TOKEN, singletonList(authToken));
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

    public <T> T updateObject(final String containerName,
                              final String objectName, final Entity<?> entity,
                              final Function<Response, T> function) {
        return updateObject(containerName, objectName, null, entity, function);
    }

    /**
     * Deletes an object.
     *
     * @param <T> return value type parameter
     * @param containerName the container name
     * @param objectName the object name
     * @param headers additional headers; may be {@code null}.
     * @param function a function applies with the response.
     * @return a value the function results
     */
    public <T> T deleteObject(final String containerName,
                              final String objectName,
                              final MultivaluedMap<String, Object> headers,
                              final Function<Response, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder
                    = buildObject(client, containerName, objectName);
            if (headers != null) {
                headers.replace(HEADER_X_AUTH_TOKEN, singletonList(authToken));
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

    /**
     * Deletes an object identified by given container name and object name.
     *
     * @param <T> return value type parameter
     * @param containerName the container name
     * @param objectName the object name
     * @param function the function to be applied with the response.
     * @return the value function results.
     */
    public <T> T deleteObject(final String containerName,
                              final String objectName,
                              final Function<Response, T> function) {
        return deleteObject(containerName, objectName, null, function);
    }

    private String authUrl;

    private String authUser;

    private String authPass;

    private String storageUrl;

    private String authToken;

    private Date tokenExpires;
}
