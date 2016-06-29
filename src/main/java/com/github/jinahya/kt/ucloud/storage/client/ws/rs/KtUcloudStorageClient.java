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

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class KtUcloudStorageClient {

    public static final String X_AUTH_URL_STANDARD_KOR_CENTER
            = "https://api.ucloudbiz.olleh.com/storage/v1/auth";

    public static final String X_AUTH_URL_STANDARD_JPN
            = "https://api.ucloudbiz.olleh.com/storage/v1/authjp";

    public static final String X_AUTH_URL_LITE_KOR_HA
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

//    /**
//     * Updates container.
//     *
//     * @param client the client
//     * @param storageUrl the storage URL
//     * @param containerName container name
//     * @param authToken authentication token
//     * @return a response
//     */
//    public static Response updateContainer(final Client client,
//                                           final String storageUrl,
//                                           final String containerName,
//                                           final String authToken) {
//        return buildContainer(client, storageUrl, containerName, authToken)
//                .buildPut(Entity.entity(
//                        new byte[0], MediaType.APPLICATION_OCTET_STREAM))
//                .invoke();
//    }
//
//    /**
//     * Deletes a container.
//     *
//     * @param client the client
//     * @param storageUrl the storage URL
//     * @param containerName the container name
//     * @param authToken the authentication token.
//     * @return a response
//     */
//    public static Response deleteContainer(final Client client,
//                                           final String storageUrl,
//                                           final String containerName,
//                                           final String authToken) {
//        return buildContainer(client, storageUrl, containerName, authToken)
//                .buildDelete()
//                .invoke();
//    }
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
     * @param accessToken the authentication token
     * @return an invocation builder.
     */
    public static Invocation.Builder buildObject(
            final Client client, final String storageUrl,
            final String containerName, final String objectName,
            final String accessToken) {
        return targetObject(client, storageUrl, containerName, objectName)
                .request()
                .header(HEADER_X_AUTH_TOKEN, accessToken);
    }

    /**
     * Creates a new instance.
     *
     * @param authUrl the authentication URL
     * @param authUser the authentication username
     * @param authPass the authentication password
     */
    public KtUcloudStorageClient(final String authUrl, final String authUser,
                                 final String authPass) {
        super();
        this.authUrl = authUrl;
        this.authUser = authUser;
        this.authPass = authPass;
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

    public WebTarget targetContainer(final Client client,
                                     final String containerName) {
        return targetContainer(client, storageUrl, containerName);
    }

    public Invocation.Builder buildContainer(final Client client,
                                             final String containerName) {
        return buildContainer(client, storageUrl, containerName, authToken);
    }

    /**
     * Updates container.
     *
     * @param <T> return value type parameter
     * @param containerName the container name
     * @param function the function to be applied with the response.
     * @return the value applied.
     */
    public <T> T updateContainer(final String containerName,
                                 final Function<Response, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            final Response response
                    = buildContainer(client, containerName)
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

    /**
     * Deletes container.
     *
     * @param <T> return value type parameter
     * @param containerName the container name
     * @param function the function to be applied with the response.
     * @return the value function results.
     */
    public <T> T deleteContainer(final String containerName,
                                 final Function<Response, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            final Response response
                    = buildContainer(client, containerName)
                    .delete();
            try {
                return function.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
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
                            final MultivaluedHashMap<String, Object> headers,
                            final Function<Response, T> invoker) {
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder
                    = buildObject(client, containerName, objectName);
            if (headers != null) {
                builder.headers(headers);
            }
            final Invocation invocation = builder.buildGet();
            final Response response = invocation.invoke();
            try {
                return invoker.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    public <T> T updateObject(final String containerName,
                              final String objectName,
                              final MultivaluedHashMap<String, Object> headers,
                              final Entity<?> entity,
                              final Function<Response, T> function) {
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder
                    = buildObject(client, containerName, objectName);
            if (headers != null) {
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

    public <T> T deleteObject(final String containerName,
                              final String objectName,
                              final MultivaluedHashMap<String, Object> headers,
                              final Function<Response, T> invoker) {
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation.Builder builder
                    = buildObject(client, containerName, objectName);
            if (headers != null) {
                builder.headers(headers);
            }
            final Invocation invocation = builder.buildDelete();
            final Response response = invocation.invoke();
            try {
                return invoker.apply(response);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    private String authUrl;

    private String authUser;

    private String authPass;

    private String storageUrl;

    private String authToken;

    private Date tokenExpires;
}
