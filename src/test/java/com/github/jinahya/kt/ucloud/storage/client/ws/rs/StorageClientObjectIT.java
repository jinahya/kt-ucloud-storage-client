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

import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.headers;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.status;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import static java.util.Arrays.asList;
import java.util.Random;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Test(dependsOnGroups = {"container"}, groups = {"object"})
public class StorageClientObjectIT extends StorageClientIT {

    private static final Logger logger = getLogger(StorageClientObjectIT.class);

//    private static StorageClient client() {
//        final String authUrl = System.getProperty("authUrl");
//        if (authUrl == null) {
//            logger.error("missing property; authUrl; skipping...");
//            throw new SkipException("missing property; authUrl");
//        }
//        final String authUser = System.getProperty("authUser");
//        if (authUser == null) {
//            logger.error("missing property; authUser; skipping...");
//            throw new SkipException("missing property; authUser");
//        }
//        final String authPass = System.getProperty("authPass");
//        if (authPass == null) {
//            logger.error("missing proprety; authPass; skipping...");
//            throw new SkipException("missing property; authPass");
//        }
//        return new StorageClient(authUrl, authUser, authPass);
//    }
//
//    @BeforeClass
//    private void beforeClass() {
//        client = client();
//    }
//
//    private void status(final StatusType statusInfo, final Family expected) {
//        final Family family = statusInfo.getFamily();
//        final int statusCode = statusInfo.getStatusCode();
//        final String reasonPhrase = statusInfo.getReasonPhrase();
//        logger.debug("-> response.status: {} {}", statusCode, reasonPhrase);
//        assertEquals(family, expected);
//    }
//
//    private void status(final Response response, final Family expected) {
//        status(response.getStatusInfo(), expected);
//    }
//
//    private void headers(final Response response) {
//        response.getHeaders().entrySet().forEach(e -> {
//            e.getValue().forEach(value -> {
//                logger.debug("-> response.header: {}: {}", e.getKey(), value);
//            });
//        });
//    }
//
//    private void body(final Response response, final Charset charset)
//            throws IOException {
//        final StatusType statusInfo = response.getStatusInfo();
//        if (statusInfo.getStatusCode() != Status.OK.getStatusCode()) {
//            logger.debug("-> status code is not " + Status.OK.name()
//                         + ". skipping...");
//            return;
//        }
//        try (InputStream stream = response.readEntity(InputStream.class);
//             InputStreamReader reader = new InputStreamReader(stream, charset);
//             BufferedReader buffered = new BufferedReader(reader);) {
//            buffered.lines().forEach(System.out::println);
//        }
//    }
//
//    @Test
//    public void authenticateUser() {
//        logger.debug("-------------------------------- authenticating user...");
//        client.authenticateUser(r -> {
//            status(r, Family.SUCCESSFUL);
//            headers(r);
//        });
//        final String storageUrl = client.getStorageUrl();
//        logger.debug("client.storageUrl: {}", storageUrl);
//        assertNotNull(storageUrl);
//        final String authToken = client.getAuthToken();
//        logger.debug("client.authToken: {}", authToken);
//        assertNotNull(authToken);
//        final Date authTokenExpires = client.getAuthTokenExpires();
//        logger.debug("client.authTokenExpires: {}", authTokenExpires);
//        assertNotNull(authTokenExpires);
//        assertTrue(authTokenExpires.after(new Date(
//                System.currentTimeMillis() + TimeUnit.HOURS.toMillis(23L))));
//    }
//
//    @Test(dependsOnMethods = {"authenticateUser"})
//    public void peekAccount() {
//        logger.debug("------------------------------------ peeking account...");
//        final MultivaluedMap<String, Object> headers
//                = new MultivaluedHashMap<>();
//        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
//        client.peekAccount(
//                null,
//                headers,
//                r -> {
//                    status(r, Family.SUCCESSFUL);
//                    headers(r);
//                }
//        );
//    }
//
//    @Test(dependsOnMethods = {"peekAccount"})
//    public void readAccount() {
//        logger.debug("------------------------------------ reading account...");
//        final MultivaluedMap<String, Object> headers
//                = new MultivaluedHashMap<>();
//        asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML,
//               MediaType.APPLICATION_JSON)
//                .forEach(t -> {
//                    headers.putSingle(HttpHeaders.ACCEPT, t);
//                    client.readAccount(
//                            null,
//                            headers,
//                            r -> {
//                                status(r, Family.SUCCESSFUL);
//                                headers(r);
//                                try {
//                                    body(r, StandardCharsets.UTF_8);
//                                } catch (final IOException ioe) {
//                                    logger.error("failed to read body", ioe);
//                                }
//                            }
//                    );
//                });
//    }
//
//    @Test(dependsOnMethods = {"readAccount"})
//    public void updateAccount() {
//        logger.debug("----------------------------------- updating account...");
//        {
//            final String headerName = "X-Account-Meta-Test";
//            final String headerValue = "test";
//            {
//                final MultivaluedMap<String, Object> headers
//                        = new MultivaluedHashMap<>();
//                headers.putSingle(headerName, headerValue);
//                client.configureAccount(
//                        null,
//                        headers,
//                        r -> {
//                            status(r, Family.SUCCESSFUL);
//                            headers(r);
//                        }
//                );
//            }
//            {
//                final MultivaluedMap<String, Object> headers
//                        = new MultivaluedHashMap<>();
//                headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
//                client.peekAccount(
//                        null,
//                        headers,
//                        r -> {
//                            status(r, Family.SUCCESSFUL);
//                            headers(r);
//                            assertEquals(r.getHeaderString(headerName),
//                                         headerValue);
//                        }
//                );
//            }
//        }
//        {
//            final String headerName = "X-Remove-Account-Meta-Test";
//            final String headerValue = "any";
//            {
//                final MultivaluedMap<String, Object> headers
//                        = new MultivaluedHashMap<>();
//                headers.putSingle(headerName, headerValue);
//                client.configureAccount(
//                        null,
//                        headers,
//                        r -> {
//                            status(r, Family.SUCCESSFUL);
//                            headers(r);
//                        }
//                );
//            }
//            {
//                final MultivaluedMap<String, Object> headers
//                        = new MultivaluedHashMap<>();
//                headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
//                client.peekAccount(
//                        null,
//                        headers,
//                        r -> {
//                            status(r, Family.SUCCESSFUL);
//                            headers(r);
//                            assertNull(r.getHeaderString(headerName));
//                        }
//                );
//            }
//        }
//    }
//
    @Test
    public void all() {
        {
            logger.debug("----------------------------- creating container...");
            accept(c -> c.updateContainer(
                    containerName,
                    null, // params
                    null, // headers
                    r -> {
                        status(r, Family.SUCCESSFUL, Status.ACCEPTED,
                               Status.CREATED);
                        headers(r);
                    }
            ));
        }
        {
            logger.debug("------------------------------- creating objects...");
            final Random random = new SecureRandom();
            for (int i = 0; i < objectCount; i++) {
                final String objectName = Integer.toString(i);
                logger.debug("creating an object: " + objectName);
                final byte[] bytes = new byte[random.nextInt(1024)];
                random.nextBytes(bytes);
                final Entity<byte[]> entity = Entity.entity(
                        bytes, MediaType.APPLICATION_OCTET_STREAM);
                accept(c -> c.updateObject(
                        containerName,
                        objectName,
                        null, // params
                        null, // headers
                        entity,
                        r -> {
                            status(r, Family.SUCCESSFUL);
                        }
                ));
            }
        }
        {
            logger.debug("------------------------------ peeking container...");
            final MultivaluedMap<String, Object> headers
                    = new MultivaluedHashMap<>();
            headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
            accept(c -> c.peekContainer(
                    containerName,
                    null,
                    headers,
                    r -> {
                        status(r, Family.SUCCESSFUL, Status.NO_CONTENT);
                        headers(r);
                    }
            ));
        }
        {
            logger.debug("------------------------------ reading container...");
            final MultivaluedMap<String, Object> params
                    = new MultivaluedHashMap<>();
            final MultivaluedMap<String, Object> headers
                    = new MultivaluedHashMap<>();
            asList(TEXT_PLAIN, APPLICATION_XML, APPLICATION_JSON).forEach(t -> {
                logger.debug("accepting " + t);
                headers.putSingle(HttpHeaders.ACCEPT, t);
                accept(c -> c.readContainer(
                        containerName,
                        params,
                        headers,
                        r -> {
                            status(r, Family.SUCCESSFUL);
                            try {
                                body(r, StandardCharsets.UTF_8);
                            } catch (final IOException ioe) {
                                logger.error("failed to read", ioe);
                            }
                        }
                ));
            });
        }
        {
            logger.debug("-------------------------------- deleting object...");
            accept(c -> c.readContainerObjectNames(
                    containerName,
                    null,
                    null,
                    r -> r.getStatus() == Status.OK.getStatusCode(),
                    l -> {
                        logger.debug("deleting an object: " + l);
                        c.deleteObject(
                                containerName,
                                l,
                                null,
                                null,
                                r -> {
                                    status(r, SUCCESSFUL, NO_CONTENT);
                                }
                        );
                    }
            ));
        }
        {
            logger.debug("----------------------------- deleting container...");
            accept(c -> c.deleteContainer(
                    containerName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL, NO_CONTENT);
                    }
            ));
        }
    }
//
//    @Test(dependsOnMethods = {"peekContainer"}, enabled = true)
//    public void readContainer() {

//        logger.debug("reading object names one by one...");
//        params.putSingle("limit", Integer.toString(1));
//        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
//        client.readContainerObjectNames(
//                containerName,
//                params,
//                headers,
//                r -> r.getStatus() == Status.OK.getStatusCode(),
//                on -> logger.debug("objectName: {}", on)
//        );
//    }
//
//    @Test(dependsOnMethods = {"readContainer"}, enabled = true)
//    public void createObjects() {
//    }
//
//    @Test(dependsOnMethods = {"createObjects"}, enabled = true)
//    public void peekObjects() {
//        logger.debug("------------------------------------ peeking objects...");
//        for (int i = 0; i < objectCount; i++) {
//            final String objectName = Integer.toString(i);
//            logger.debug("peeking object named: " + objectName);
//            client.peekObject(
//                    containerName,
//                    objectName,
//                    null,
//                    null,
//                    (r, c) -> {
//                        status(r, Family.SUCCESSFUL);
//                        headers(r);
//                        return null;
//                    });
//        }
//    }
//
//    @Test(dependsOnMethods = {"peekObjects"}, enabled = true)
//    public void updateObjects() {
//        logger.debug("----------------------------------- updating objects...");
//    }
//
//    @Test(dependsOnMethods = {"updateObjects"}, enabled = true)
//    public void deleteObjects() {
//        logger.debug("----------------------------------- deleting objects...");
//        for (int i = 0; i < objectCount; i++) {
//            final String objectName = Integer.toString(i);
//            logger.debug("deleting object: " + objectName);
//            client.deleteObject(
//                    containerName,
//                    objectName,
//                    null,
//                    null,
//                    r -> {
//                        status(r, Family.SUCCESSFUL);
//                        headers(r);
//                    }
//            );
//        }
//    }
//
//    @Test(dependsOnMethods = {"deleteObjects"}, enabled = true)
//    public void deleteContainer() {
//    }
//
//    private StorageClient client;
//
    private final String containerName = getClass().getName();

    private final int objectCount = 2;
}
