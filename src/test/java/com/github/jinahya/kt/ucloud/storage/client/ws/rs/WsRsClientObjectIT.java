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

import com.github.jinahya.kt.ucloud.storage.client.net.NetClient;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.headers;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.SecureRandom;
import static java.util.Arrays.asList;
import java.util.Random;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_LENGTH;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Test(dependsOnGroups = {"ws.rs.container"}, groups = {"ws.rs.object"})
public class WsRsClientObjectIT extends WsRsClientIT {

    private static final Logger logger = getLogger(WsRsClientObjectIT.class);

    @Test
    public void all() {
        {
            logger.debug("----------------------------- creating container...");
            accept(c -> c.updateContainer(
                    containerName,
                    null, // params
                    null, // headers
                    r -> {
                        status(r, SUCCESSFUL, ACCEPTED, CREATED);
                        headers(r);
                    }
            ));
        }
        {
            logger.debug("------------------------------- creating objects...");
            final Random random = new SecureRandom();
            for (int i = 0; i < objectCount; i++) {
                final String objectName = Integer.toString(i);
                final byte[] entity = new byte[random.nextInt(1024)];
                random.nextBytes(entity);
                logger.debug("creating an object: {} with {} bytes",
                             objectName, entity.length);
                accept(c -> c.updateObject(
                        containerName,
                        objectName,
                        null, // params
                        null, // headers
                        entity(entity, APPLICATION_OCTET_STREAM),
                        r -> {
                            status(r, SUCCESSFUL);
                        }
                ));
            }
        }
        {
            logger.debug("-------------------------------- copying objects...");
            final MultivaluedMap<String, Object> headers
                    = new MultivaluedHashMap<>();
            for (int i = objectCount - 1; i >= 0; i--) {
                final String objectName = Integer.toString(i);
                logger.debug("copying an object: " + objectName);
                headers.putSingle(WsRsClient.HEADER_X_COPY_FROM,
                                  "/" + containerName + "/" + objectName);
                accept(c -> {
                    c.peekObject(containerName,
                                 objectName,
                                 null,
                                 null,
                                 r -> {
                                     final String contentLength = r.getHeaderString(CONTENT_LENGTH);
                                     headers.putSingle(CONTENT_LENGTH, contentLength);
                                     final NetClient client = new NetClient(c);
                                     try {
                                         client.updateObject(
                                                 containerName,
                                                 objectName + "_copied",
                                                 null,
                                                 headers,
                                                 n -> {
                                                     try {
                                                         n.getOutputStream().close();
                                                         final int statusCode = ((HttpURLConnection) n).getResponseCode();
                                                         logger.debug("status code: " + statusCode);
                                                     } catch (final IOException ioe) {
                                                         fail("failed to copy object", ioe);
                                                     }
                                                 });
                                     } catch (final IOException ioe) {
                                         fail("failed to copy object", ioe);
                                     }
                                 }
                    );
                });
            }
        }
        {
            logger.debug("------------------------------ peeking container...");
            final MultivaluedMap<String, Object> headers
                    = new MultivaluedHashMap<>();
            headers.putSingle(ACCEPT, TEXT_PLAIN);
            accept(c -> c.peekContainer(
                    containerName,
                    null,
                    headers,
                    r -> {
                        status(r, SUCCESSFUL, NO_CONTENT);
                        headers(r);
                    }
            ));
        }
        {
            logger.debug("------------------------------ reading container...");
            final MultivaluedMap<String, Object> headers
                    = new MultivaluedHashMap<>();
            asList(TEXT_PLAIN, APPLICATION_XML, APPLICATION_JSON).forEach(t -> {
                logger.debug("accepting " + t);
                headers.putSingle(ACCEPT, t);
                accept(c -> c.readContainer(
                        containerName,
                        null,
                        headers,
                        r -> {
                            status(r, SUCCESSFUL);
                            try {
                                body(r);
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
                    r -> r.getStatus() == OK.getStatusCode(),
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

    private final String containerName = getClass().getName();

    private final int objectCount = 2;
}
