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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import static java.util.Arrays.asList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class StorageClientIT {

    private static final Logger logger = getLogger(StorageClientIT.class);

    private static StorageClient client() {
        final String authUrl = System.getProperty("authUrl");
        if (authUrl == null) {
            logger.error("missing property; authUrl; skipping...");
            throw new SkipException("missing property; authUrl");
        }
        final String authUser = System.getProperty("authUser");
        if (authUser == null) {
            logger.error("missing property; authUser; skipping...");
            throw new SkipException("missing property; authUser");
        }
        final String authPass = System.getProperty("authPass");
        if (authPass == null) {
            logger.error("missing proprety; authPass; skipping...");
            throw new SkipException("missing property; authPass");
        }
        return new StorageClient(authUrl, authUser, authPass);
    }

    @BeforeClass
    private void beforeClass() {
        client = client();
    }

    private void status(final StatusType statusInfo,
                        final Family expected) {
        final Family family = statusInfo.getFamily();
        final int statusCode = statusInfo.getStatusCode();
        final String reasonPhrase = statusInfo.getReasonPhrase();
        logger.debug("-> response.status: {} {}", statusCode, reasonPhrase);
        assertEquals(family, expected);
    }

    private void response(final Response response, final Family expected) {
        status(response.getStatusInfo(), expected);
        response.getHeaders().entrySet().forEach(e -> {
            e.getValue().forEach(value -> {
                logger.debug("-> response.header: {}: {}", e.getKey(), value);
            });
        });
    }

    private void body(final Response response, final Charset charset)
            throws IOException {
        final StatusType statusInfo = response.getStatusInfo();
        if (statusInfo.getStatusCode() != Status.OK.getStatusCode()) {
            logger.debug("-> status code is not " + Status.OK.name()
                         + ". skipping...");
            return;
        }
        try (InputStream stream = response.readEntity(InputStream.class);
             InputStreamReader reader = new InputStreamReader(stream, charset);
             BufferedReader buffered = new BufferedReader(reader);) {
            buffered.lines().forEach(System.out::println);
        }
    }

    @Test(enabled = true)
    public void authenticateUser() {
        logger.debug("-------------------------------- authenticating user...");
        client.authenticateUser(r -> {
            response(r, Family.SUCCESSFUL);
        });
        final String storageUrl = client.getStorageUrl();
        logger.debug("client.storageUrl: {}", storageUrl);
        assertNotNull(storageUrl);
        final String authToken = client.getAuthToken();
        logger.debug("client.authToken: {}", authToken);
        assertNotNull(authToken);
        final Date authTokenExpires = client.getAuthTokenExpires();
        logger.debug("client.authTokenExpires: {}", authTokenExpires);
        assertNotNull(authTokenExpires);
        assertTrue(authTokenExpires.after(new Date(
                System.currentTimeMillis() + TimeUnit.HOURS.toMillis(23L))));
    }

    @Test(dependsOnMethods = {"authenticateUser"})
    public void peekStorage() {
        logger.debug("------------------------------------ peeking storage...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
        client.peekStorage(
                null,
                headers,
                r -> {
                    response(r, Family.SUCCESSFUL);
                }
        );
    }

    @Test(dependsOnMethods = {"peekStorage"})
    public void readStorage() {
        logger.debug("------------------------------------ reading storage...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML,
               MediaType.APPLICATION_JSON)
                .forEach(t -> {
                    headers.putSingle(HttpHeaders.ACCEPT, t);
                    client.readStorage(
                            null,
                            headers,
                            (r, c) -> {
                                response(r, Family.SUCCESSFUL);
                                try {
                                    body(r, StandardCharsets.UTF_8);
                                } catch (final IOException ioe) {
                                    logger.error("failed to read body", ioe);
                                }
                            }
                    );
                });
    }

    @Test(dependsOnMethods = {"readStorage"})
    public void updateStorage() {
        logger.debug("----------------------------------- updating storage...");
        {
            final String headerName = "X-Account-Meta-Test";
            final String headerValue = "test";
            {
                final MultivaluedMap<String, Object> headers
                        = new MultivaluedHashMap<>();
                headers.putSingle(headerName, headerValue);
                client.updateStorage(
                        null,
                        headers,
                        r -> {
                            response(r, Family.SUCCESSFUL);
                        });
            }
            {
                final MultivaluedMap<String, Object> headers
                        = new MultivaluedHashMap<>();
                headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
                client.peekStorage(
                        null,
                        headers,
                        r -> {
                            response(r, Family.SUCCESSFUL);
                            assertEquals(r.getHeaderString(headerName),
                                         headerValue);
                        });
            }
        }
        {
            final String headerName = "X-Remove-Account-Meta-Test";
            final String headerValue = "any";
            {
                final MultivaluedMap<String, Object> headers
                        = new MultivaluedHashMap<>();
                headers.putSingle(headerName, headerValue);
                client.updateStorage(
                        null,
                        headers,
                        r -> {
                            response(r, Family.SUCCESSFUL);
                        });
            }
            {
                final MultivaluedMap<String, Object> headers
                        = new MultivaluedHashMap<>();
                headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
                client.peekStorage(
                        null,
                        headers,
                        r -> {
                            response(r, Family.SUCCESSFUL);
                            assertNull(r.getHeaderString(headerName));
                        });
            }
        }
    }

    @Test(dependsOnMethods = {"updateStorage"}, enabled = true)
    public void createContainer() {
        logger.debug("--------------------------------- creating container...");
        client.createContainer(
                containerName,
                null, // params
                null, // headers
                r -> {
                    response(r, Family.SUCCESSFUL);
                }
        );
    }

    @Test(dependsOnMethods = {"createContainer"}, enabled = true)
    public void peekContainer() {
        logger.debug("---------------------------------- peeking container...");
        final MultivaluedMap<String, Object> params
                = new MultivaluedHashMap<>();
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
        client.peekContainer(
                containerName,
                null,
                headers,
                r -> {
                    response(r, Family.SUCCESSFUL);
                }
        );
    }

    @Test(dependsOnMethods = {"peekContainer"}, enabled = true)
    public void readContainer() {
        logger.debug("---------------------------------- reading container...");
        final MultivaluedMap<String, Object> params
                = new MultivaluedHashMap<>();
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML,
               MediaType.APPLICATION_JSON)
                .forEach(mediaType -> {
                    logger.debug("reading in " + mediaType);
                    headers.putSingle(HttpHeaders.ACCEPT, mediaType);
                    client.readContainer(
                            containerName,
                            params,
                            headers,
                            (r, c) -> {
                                status(r.getStatusInfo(), Family.SUCCESSFUL);
                                try {
                                    body(r, StandardCharsets.UTF_8);
                                } catch (final IOException ioe) {
                                    logger.error("failed to read", ioe);
                                }
                                return null;
                            }
                    );
                });
        logger.debug("reading object names one by one...");
        params.putSingle("limit", Integer.toString(1));
        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
        client.readContainerObjectNamesAll(
                containerName,
                params,
                headers,
                on -> logger.debug("objectName: {}", on)
        );
    }

    @Test(dependsOnMethods = {"readContainer"}, enabled = true)
    public void createObjects() {
        logger.debug("----------------------------------- creating objects...");
        final Random random = new SecureRandom();
        for (int i = 0; i < objectCount; i++) {
            final String objectName = Integer.toString(i);
            logger.debug("creating an object: " + objectName);
            final byte[] bytes = new byte[random.nextInt(1024)];
            random.nextBytes(bytes);
            final Entity<byte[]> entity = Entity.entity(
                    bytes, MediaType.APPLICATION_OCTET_STREAM);
            client.createObject(
                    containerName,
                    objectName,
                    null, // params
                    null, // headers
                    entity,
                    (r, c) -> {
                        status(r.getStatusInfo(), Family.SUCCESSFUL);
                        return null;
                    }
            );
        }
    }

    @Test(dependsOnMethods = {"createObjects"}, enabled = true)
    public void peekObjects() {
        logger.debug("------------------------------------ peeking objects...");
        for (int i = 0; i < objectCount; i++) {
            final String objectName = Integer.toString(i);
            logger.debug("peeking object named: " + objectName);
            client.peekObject(
                    containerName,
                    objectName,
                    null,
                    null,
                    (r, c) -> {
                        response(r, Family.SUCCESSFUL);
                        return null;
                    });
        }
    }

    @Test(dependsOnMethods = {"peekObjects"}, enabled = true)
    public void updateObjects() {
        logger.debug("----------------------------------- updating objects...");
    }

    @Test(dependsOnMethods = {"updateObjects"}, enabled = true)
    public void deleteObjects() {
        logger.debug("----------------------------------- deleting objects...");
        for (int i = 0; i < objectCount; i++) {
            final String objectName = Integer.toString(i);
            logger.debug("deleting object: " + objectName);
            client.deleteObject(
                    containerName,
                    objectName,
                    null,
                    null,
                    (Consumer<Response>) r -> response(r, Family.SUCCESSFUL)
            );
        }
    }

    @Test(dependsOnMethods = {"deleteObjects"}, enabled = true)
    public void deleteContainer() {
        logger.debug("--------------------------------- deleting container...");
        client.deleteContainer(
                containerName,
                null,
                null,
                (r, c) -> {
                    status(r.getStatusInfo(), Family.SUCCESSFUL);
                    return null;
                }
        );
    }

    private StorageClient client;

    private final String containerName = getClass().getName();

    private final int objectCount = 2;
}
