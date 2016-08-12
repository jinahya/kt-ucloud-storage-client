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

import static com.github.jinahya.kt.ucloud.storage.client.StorageClient.HEADER_X_AUTH_TOKEN;
import static com.github.jinahya.kt.ucloud.storage.client.StorageClient.HEADER_X_CONTAINER_READ;
import static com.github.jinahya.kt.ucloud.storage.client.StorageClient.HEADER_X_CONTAINER_WRITE;
import com.github.jinahya.kt.ucloud.storage.client.StorageClientResellerIT;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientITs.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientITs.status;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.Response.Status.Family.familyOf;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import javax.ws.rs.core.Response.StatusType;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class WsRsClientResellerIT
        extends StorageClientResellerIT<WsRsClient> {

    private static final Logger logger = getLogger(WsRsClientResellerIT.class);

    public WsRsClientResellerIT() {
        super(WsRsClient.class);
    }

    @BeforeClass
    @Override
    public void doBeforeClass() throws ReflectiveOperationException {
        super.doBeforeClass();
        accept(c -> {
            c.setClientRequestFilter(requestContext -> {
                logger.debug("requstContext.method: {}",
                             requestContext.getMethod());
                logger.debug("requstContext.uri: {}", requestContext.getUri());
                requestContext.getHeaders().forEach((k, vs) -> {
                    vs.forEach(v -> {
                        logger.debug("requestContext.header: {}: {}", k, v);
                    });
                });
            });
            c.setClientResponseFilter((requestContext, responseContext) -> {
                final StatusType statusInfo = responseContext.getStatusInfo();
                logger.debug("responseContext.status: {} {}",
                             statusInfo.getStatusCode(),
                             statusInfo.getReasonPhrase());
                responseContext.getHeaders().forEach((k, vs) -> {
                    vs.forEach(v -> {
                        logger.debug("responseContext.header: {}: {}", k, v);
                    });
                });
            });
        });
    }

    @Test
    public void readAccount() {
        logger.debug("------------------------ testing readResellerAccount...");
        accept(c -> {
            logger.debug("c.storageUrl: {}", c.getStorageUrl());
            logger.debug("c.authToken: {}", c.getAuthToken());
            logger.debug("c.authTokenExpires: {}", c.getAuthTokenExpires());
            c.readResellerAccount(
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                        body(r);
                    }
            );
        });
    }

    @Test
    public void user() {
        logger.debug("----------------------------------- testing user...");
        final String userName = UUID.randomUUID().toString();
        final String userKey = UUID.randomUUID().toString();
        accept(c -> {
            logger.debug("--------------------------------- updating user...1");
            c.updateResellerUser(
                    userName,
                    userKey,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("--------------------------------- updating user...2");
            c.updateResellerUser(
                    userName,
                    userKey,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("----------------------------------- reading user...");
            c.readResellerUser(
                    userName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("---------------------------------- deleting user...");
            c.deleteResellerUser(
                    userName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
    }

    @Test(invocationCount = 2)
    public void objectWithUser() throws IOException {
        logger.debug("--------------------------- testing object with user...");
        final String containerName = UUID.randomUUID().toString();
        final String objectName = UUID.randomUUID().toString();
        final String userName = UUID.randomUUID().toString();
        final String userKey = UUID.randomUUID().toString();
        final String authUrl[] = new String[1];
        accept(c -> {
            authUrl[0] = c.getAuthUrl();
        });
        final String authAccount[] = new String[1];
        accept(c -> {
            authAccount[0] = c.getAuthAccount();
        });
        final String storageUrl[] = new String[1];
        accept(c -> {
            storageUrl[0] = c.getStorageUrl();
        });
        final String resellerAuthUser = authAccount[0] + ":" + userName;
        accept(c -> {
            logger.debug("---------------------------------- updating user...");
            c.updateResellerUser(
                    userName,
                    userKey,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("-------------------------------- updating container");
            c.updateContainer(
                    containerName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("------------------------ updating container; acl...");
            final MultivaluedMap<String, Object> headers
                    = new MultivaluedHashMap<>();
            headers.putSingle(HEADER_X_CONTAINER_READ, resellerAuthUser);
            headers.putSingle(HEADER_X_CONTAINER_WRITE, resellerAuthUser);
            c.configureContainer(
                    containerName,
                    null,
                    headers,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        final URL objectUrl = new URL(
                storageUrl[0] + "/" + containerName + "/" + objectName);
        final String[] authToken = new String[1];
        {
            logger.debug("----------------------- authenticating with user...");
            final WsRsClient client = new WsRsClient(
                    authUrl[0], resellerAuthUser, userKey);
            client.authenticateUser(
                    false,
                    r -> {
                        authToken[0] = r.getHeaderString(HEADER_X_AUTH_TOKEN);
                        logger.debug("authToken: {}", authToken[0]);
                    }
            );
        }
        final byte[] expected
                = new byte[ThreadLocalRandom.current().nextInt(1024)];
        ThreadLocalRandom.current().nextBytes(expected);
        {
            logger.debug("---------------------- updating object with user...");
            try {
                final HttpURLConnection connection
                        = (HttpURLConnection) objectUrl.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty(
                        HEADER_X_AUTH_TOKEN, authToken[0]);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.connect();
                try {
                    try (OutputStream output = connection.getOutputStream()) {
                        output.write(expected);
                        output.flush();
                    }
                    final int statusCode = connection.getResponseCode();
                    final String reasonPhrase = connection.getResponseMessage();
                    logger.debug("status: {} {}", statusCode, reasonPhrase);
                    assertEquals(familyOf(statusCode), SUCCESSFUL);
                } finally {
                    connection.disconnect();
                }
            } catch (final IOException ioe) {
                fail("failed to update object with user...", ioe);
            }
        }
        {
            logger.debug("---------------------- reading object with user...");
            try {
                final HttpURLConnection connection
                        = (HttpURLConnection) objectUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty(
                        HEADER_X_AUTH_TOKEN, authToken[0]);
                connection.setDoOutput(false);
                connection.setDoInput(true);
                connection.connect();
                try {
                    final int statusCode = connection.getResponseCode();
                    final String reasonPhrase = connection.getResponseMessage();
                    logger.debug("status: {} {}", statusCode, reasonPhrase);
                    assertEquals(familyOf(statusCode), SUCCESSFUL);
                    final byte[] actual = new byte[expected.length];
                    try (DataInputStream stream = new DataInputStream(
                            connection.getInputStream())) {
                        stream.readFully(actual);
                    }
                    assertEquals(actual, expected);
                } finally {
                    connection.disconnect();
                }
            } catch (final IOException ioe) {
                fail("failed to update object with user...", ioe);
            }
        }
        {
            logger.debug("---------------------- deleting object with user...");
            try {
                final HttpURLConnection connection
                        = (HttpURLConnection) objectUrl.openConnection();
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty(
                        HEADER_X_AUTH_TOKEN, authToken[0]);
                connection.setDoOutput(false);
                connection.setDoInput(true);
                connection.connect();
                try {
                    final int statusCode = connection.getResponseCode();
                    final String reasonPhrase = connection.getResponseMessage();
                    logger.debug("status: {} {}", statusCode, reasonPhrase);
                    assertEquals(familyOf(statusCode), SUCCESSFUL);
                } finally {
                    connection.disconnect();
                }
            } catch (final IOException ioe) {
                fail("failed to update object with user...", ioe);
            }
        }
        accept(c -> {
            logger.debug("----------------------------- deleting container...");
            c.deleteContainer(
                    containerName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("---------------------------------- deleting user...");
            c.deleteResellerUser(
                    userName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL, NO_CONTENT);
                    }
            );
        });
    }

    @Test(invocationCount = 1)
    public void multipleUsers() throws IOException {
        logger.debug("----------------------------- testing multiple usres...");
        final String containerName = UUID.randomUUID().toString();
        final String objectName = UUID.randomUUID().toString();
        final String userName1 = UUID.randomUUID().toString();
        final String userKey1 = UUID.randomUUID().toString();
        final String userName2 = UUID.randomUUID().toString();
        final String userKey2 = UUID.randomUUID().toString();
        final String authUrl[] = new String[1];
        accept(c -> authUrl[0] = c.getAuthUrl());
        final String authAccount[] = new String[1];
        accept(c -> authAccount[0] = c.getAuthAccount());
        final String storageUrl[] = new String[1];
        accept(c -> storageUrl[0] = c.getStorageUrl());
        final String resellerAuthUser1 = authAccount[0] + ":" + userName1;
        final String resellerAuthUser2 = authAccount[0] + ":" + userName2;
        accept(c -> {
            logger.debug("--------------------------------- updating user1...");
            c.updateResellerUser(
                    userName1,
                    userKey1,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("--------------------------------- updating user2...");
            c.updateResellerUser(
                    userName2,
                    userKey2,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        final URL objectUrl = new URL(
                storageUrl[0] + "/" + containerName + "/" + objectName);
        final String[] authToken1 = new String[1];
        {
            logger.debug("---------------------- authenticating with user1...");
            final WsRsClient client = new WsRsClient(
                    authUrl[0], resellerAuthUser1, userKey1);
            client.authenticateUser(
                    false,
                    r -> {
                        authToken1[0] = r.getHeaderString(HEADER_X_AUTH_TOKEN);
                        logger.debug("authToken1: {}", authToken1[0]);
                    }
            );
        }
        final String[] authToken2 = new String[1];
        {
            logger.debug("---------------------- authenticating with user2...");
            final WsRsClient client = new WsRsClient(
                    authUrl[0], resellerAuthUser2, userKey2);
            client.authenticateUser(
                    false,
                    r -> {
                        authToken2[0] = r.getHeaderString(HEADER_X_AUTH_TOKEN);
                        logger.debug("authToken2: {}", authToken2[0]);
                    }
            );
        }
    }
}
