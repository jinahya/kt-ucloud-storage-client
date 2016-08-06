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

import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.headers;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.status;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static java.util.Arrays.asList;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.status;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.status;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.StorageClientIT.status;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class StorageClientContainerIT extends StorageClientIT {

    private static final Logger logger
            = getLogger(StorageClientContainerIT.class);

    @Test
    public void updateContainer() {
        logger.debug("--------------------------------- updating container...");
        accept(c -> c.updateContainer(
                containerName,
                null, // params
                null, // headers
                r -> {
                    status(r, Family.SUCCESSFUL, Status.CREATED,
                           Status.ACCEPTED);
                    headers(r);
                }
        ));
    }

    @Test
    public void peekContainer() {
        logger.debug("---------------------------------- peeking container...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        headers.putSingle(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
        accept(c -> c.peekContainer(
                containerName,
                null,
                headers,
                r -> {
                    status(r, null, Status.NOT_FOUND, Status.NO_CONTENT);
                    headers(r);
                }
        ));
    }

    @Test
    public void readContainer() {
        logger.debug("---------------------------------- reading container...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML,
               MediaType.APPLICATION_JSON)
                .forEach(mediaType -> {
                    logger.debug("accepting " + mediaType);
                    headers.putSingle(HttpHeaders.ACCEPT, mediaType);
                    accept(c -> c.readContainer(
                            containerName,
                            null,
                            headers,
                            r -> {
                                status(r, null, Status.NOT_FOUND, Status.OK,
                                       Status.NO_CONTENT);
                                try {
                                    body(r);
                                } catch (final IOException ioe) {
                                    fail("failed to read container", ioe);
                                }
                            }
                    ));
                });
    }

    @Test
    public void readContainerObjectNames() {
        logger.debug("--------------------- reading container object names...");
        accept(c -> c.readContainerObjectNames(
                containerName,
                null,
                null,
                r -> r.getStatus() == Status.OK.getStatusCode(),
                l -> {
                    logger.debug("object name: {}", l);
                }
        ));
    }

    @Test(dependsOnMethods = {"readContainerObjectNames"})
    public void deleteContainer() {
        logger.debug("--------------------------------- deleting container...");
        accept(c -> c.deleteContainer(
                containerName,
                null,
                null,
                r -> {
                    status(r, Family.SUCCESSFUL, Status.NO_CONTENT);
                }
        ));
    }

    @Test
    public void all() {
        {
            logger.debug("----------------------------- creating container...");
            accept(c -> c.updateContainer(
                    containerName,
                    null, // params
                    null, // headers
                    r -> {
                        status(r, Family.SUCCESSFUL, Status.CREATED,
                               Status.ACCEPTED);
                        headers(r);
                    }
            ));
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
                        assertNotNull(r.getHeaderString(
                                StorageClient.HEADER_X_CONTAINER_OBJECT_COUNT));
                        assertNotNull(r.getHeaderString(
                                StorageClient.HEADER_X_CONTAINER_BYTES_USED));
                    }
            ));
        }
        {
            logger.debug("------------------------------ reading container...");
            final MultivaluedMap<String, Object> headers
                    = new MultivaluedHashMap<>();
            asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML,
                   MediaType.APPLICATION_JSON)
                    .forEach(mediaType -> {
                        logger.debug("accepting " + mediaType);
                        headers.putSingle(HttpHeaders.ACCEPT, mediaType);
                        accept(c -> c.readContainer(
                                containerName,
                                null,
                                headers,
                                r -> {
                                    status(r, Family.SUCCESSFUL, Status.OK,
                                           Status.NO_CONTENT);
                                    try {
                                        body(r, StandardCharsets.UTF_8);
                                    } catch (final IOException ioe) {
                                        fail("failed to read container", ioe);
                                    }
                                }
                        ));
                    });
        }
        {
            logger.debug("----------------- reading container object names...");
            accept(c -> c.readContainerObjectNames(
                    containerName,
                    null,
                    null,
                    r -> r.getStatus() == Status.OK.getStatusCode(),
                    l -> {
                        logger.debug("object name: {}", l);
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
                        status(r, Family.SUCCESSFUL, Status.NO_CONTENT);
                    }
            ));
        }
    }

    private final String containerName = getClass().getName();
}
