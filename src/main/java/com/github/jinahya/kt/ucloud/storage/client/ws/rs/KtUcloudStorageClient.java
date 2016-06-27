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

import java.io.IOException;
import java.util.Date;
import java.util.function.Function;
import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

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

    public static Response authenticateUser(final String xAuthUrl,
                                            final String xAuthUser,
                                            final String xAuthPass) {
        final Client client = ClientBuilder.newClient();
        try {
            return client
                    .target(xAuthUrl).path("storage").path("v1").path("auth")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("X-Storage-User", xAuthUser) // X-Auth-User -> 500
                    .header("X-Storage-Pass", xAuthPass) // X-Auth-Pass -> 500
                    .header("X-Auth-New-Token", "true")
                    .buildGet()
                    .invoke();
        } finally {
            client.close();
        }
    }

    public static Response updateContainer(final String xStorageUrl,
                                           final String xAuthToken,
                                           final String containerName) {
        final Client client = ClientBuilder.newClient();
        try {
            return client
                    .target(xStorageUrl).path(containerName)
                    .request()
                    .header("X-Auth-Token", xAuthToken)
                    .buildPut(null)
                    .invoke();
        } finally {
            client.close();
        }
    }

    public static Response deleteContainer(final String xStorageUrl,
                                           final String xAuthToken,
                                           final String containerName) {
        final Client client = ClientBuilder.newClient();
        try {
            return client
                    .target(xStorageUrl).path(containerName)
                    .request()
                    .header("X-Auth-Token", xAuthToken)
                    .buildDelete()
                    .invoke();
        } finally {
            client.close();
        }
    }

    // -------------------------------------------------------------- readObject
    public static WebTarget targetObject(
            final Client client, final String url, final String container,
            final String object) {
        return client.target(url).path(container).path(object);
    }

    public static Invocation.Builder buildObject(
            final Client client, final String url, final String container,
            final String object, final String token) {
        return targetObject(client, url, container, object)
                .request()
                .header("X-Auth-Token", token);
    }

    public static <T> T invokeObject(final Client client, final String url,
                                     final String container, final String object,
                                     final String token,
                                     final Function<Invocation, T> invoker) {
        final Invocation.Builder builder = buildObject(
                client, url, container, object, token);
        .
        buildGet();
        return invoker.apply(invocation);
    }

    public static <T> T readObjectAsync(
            final String url, final String container, final String object,
            final String token, final Function<AsyncInvoker, T> getter) {
        final Client client = ClientBuilder.newClient();
        try {
            final AsyncInvoker invoker = buildObject(
                    client, url, container, object, token)
                    .async();
            invoker.
            return getter.apply(invoker);
        } finally {
            client.close();
        }
    }

//    public static Future<Response> readObjectAsync(
//            final String storageUrl, final String containerName,
//            final String objectName, final String authToken,
//            final InvocationCallback invocationCallback) {
//        return readObjectAsync(storageUrl, containerName, objectName, authToken, invoker -> invoker.get(invocationCallback));
//    }
    // -------------------------------------------------------------- updateObject
    public static Invocation.Builder updateObjectBuilder(
            final Client client, final String url, final String container,
            final String object, final String token) {
        return client
                .target(url).path(container).path(object)
                .request()
                .header("X-Auth-Token", token);
    }

    public static <T> T updateObject(final String url, final String container,
                                     final String object, final String token,
                                     final Entity<?> entity,
                                     final Function<Invocation, T> invoker) {
        final Client client = ClientBuilder.newClient();
        try {
            final Invocation invocation = buildObject(
                    client, url, container, object, token)
                    .buildPut(entity);
            return invoker.apply(invocation);
        } finally {
            client.close();
        }
    }

    public static <T> T updateObjectAsync(
            final String url, final String container, final String object,
            final String token, final Function<AsyncInvoker, T> getter) {
        final Client client = ClientBuilder.newClient();
        try {
            final AsyncInvoker invoker = buildObject(
                    client, url, container, object, token)
                    .async();
            return getter.apply(invoker);
        } finally {
            client.close();
        }
    }

    public KtUcloudStorageClient(final String xAuthUrl, final String xAuthUser,
                                 final String xAuthPass) {
        super();
        this.authUrl = xAuthUrl;
        this.authUser = xAuthUser;
        this.authPass = xAuthPass;
    }

    public void authenticateUser() throws IOException {
        if (token != null && date != null
            && date.before(
                        new Date(System.currentTimeMillis() + 600000L))) {
            return;
        }
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = authenticateUser(
                    authUrl, authUser, authPass);
            final int statusCode = response.getStatus();
            if (statusCode != 200) {
                throw new IOException(
                        "failed to authenticate user; status code = "
                        + statusCode);
            }
            url = response.getHeaderString("X-Storage-Url");
            token = response.getHeaderString("X-Auth-Token");
            expires = response.getHeaderString("X-Auth-Token-Expires");
            date = new Date(System.currentTimeMillis()
                            + (Long.parseLong(expires) * 1000L) - 600000L);
        } finally {
            client.close();
        }
    }

    public void updateContainer(final String containerName) throws IOException {
        authenticateUser();
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = updateContainer(
                    url, token, containerName);
            final StatusType status = response.getStatusInfo();
            if (status.getFamily() != Response.Status.Family.SUCCESSFUL) {
                final int statusCode = status.getStatusCode();
                throw new IOException(
                        "failed to update container; status code; "
                        + statusCode);
            }
        } finally {
            client.close();
        }
    }

    public int deleteContainer(final String containerName) throws IOException {
        authenticateUser();
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = deleteContainer(
                    url, token, containerName);
            final StatusType status = response.getStatusInfo();
            return status.getStatusCode();
        } finally {
            client.close();
        }
    }

    // -------------------------------------------------------------- readObject
    public <T> T readObject(final String container, final String object,
                            final Function<Invocation, T> invoker) {
        final Client client = ClientBuilder.newClient();
        try {
            final WebTarget target = client.target(url).path(container).path(object);
            target.request().header("X-Auth-Token", token).async().get
            final Invocation invocation = buildObject(
                    client, url, container, object, token)
                    .buildGet();
            return invoker.apply(invocation);
        } finally {
            client.close();
        }
    }

    public <T> T readObjectAsync(final String container, final String object,
                                 final Function<AsyncInvoker, T> getter) {
        final Client client = ClientBuilder.newClient();
        try {
            final AsyncInvoker invoker = buildObject(
                    client, url, container, object, token)
                    .async();
            return getter.apply(invoker);
        } finally {
            client.close();
        }
    }

    private String authUrl;

    private String authUser;

    private String authPass;

    private String url;

    private String token;

    private String expires;

    private Date date;
}
