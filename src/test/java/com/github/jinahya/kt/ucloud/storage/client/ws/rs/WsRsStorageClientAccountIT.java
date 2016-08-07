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
import static java.util.Arrays.asList;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Test(groups = {"account"})
public class WsRsStorageClientAccountIT extends WsRsStorageClientIT {

    private static final Logger logger
            = getLogger(WsRsStorageClientAccountIT.class);

    @Test
    public void peekAccount() {
        logger.debug("------------------------------------ peeking account...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        headers.putSingle(ACCEPT, TEXT_PLAIN);
        accept(c -> c.peekAccount(
                null,
                headers,
                r -> {
                    status(r, SUCCESSFUL, NO_CONTENT);
                    headers(r);
                    assertNotNull(r.getHeaderString(
                            WsRsStorageClient.HEADER_X_ACCOUNT_CONTAINER_COUNT));
                    assertNotNull(r.getHeaderString(
                            WsRsStorageClient.HEADER_X_ACCOUNT_OBJECT_COUNT));
                    assertNotNull(r.getHeaderString(
                            WsRsStorageClient.HEADER_X_ACCOUNT_BYTES_USED));
                }
        ));
    }

    @Test
    public void readAccount() {
        logger.debug("------------------------------------ reading account...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        asList(TEXT_PLAIN, APPLICATION_XML, APPLICATION_JSON)
                .forEach(t -> {
                    headers.putSingle(ACCEPT, t);
                    accept(c -> c.readAccount(
                            null,
                            headers,
                            r -> {
                                status(r, SUCCESSFUL, OK);
                                headers(r);
                                try {
                                    body(r);
                                } catch (final IOException ioe) {
                                    logger.error("failed to read body", ioe);
                                }
                            }
                    ));
                });
    }

    @Test
    public void readAccountCotainerNames() {
        logger.debug("-------------------- reading account container names...");
        accept(c -> c.readAccountContainerNames(
                null,
                null,
                r -> r.getStatus() == OK.getStatusCode(),
                l -> logger.debug("container name: {}", l))
        );
    }

    @Test
    public void updateAccount() {
        logger.debug("----------------------------------- updating account...");
        {
            final String headerName = "X-Account-Meta-Test";
            final String headerValue = "test";
            {
                final MultivaluedMap<String, Object> headers
                        = new MultivaluedHashMap<>();
                headers.putSingle(headerName, headerValue);
                accept(c -> c.configureAccount(
                        null,
                        headers,
                        r -> {
                            status(r, SUCCESSFUL, NO_CONTENT);
                            headers(r);
                        }
                ));
            }
            {
                final MultivaluedMap<String, Object> headers
                        = new MultivaluedHashMap<>();
                headers.putSingle(ACCEPT, TEXT_PLAIN);
                accept(c -> c.peekAccount(
                        null,
                        headers,
                        r -> {
                            status(r, SUCCESSFUL, NO_CONTENT);
                            headers(r);
                            assertEquals(r.getHeaderString(headerName),
                                         headerValue);
                        }
                ));
            }
        }
        {
            final String headerName = "X-Remove-Account-Meta-Test";
            final String headerValue = "any";
            {
                final MultivaluedMap<String, Object> headers
                        = new MultivaluedHashMap<>();
                headers.putSingle(headerName, headerValue);
                accept(c -> c.configureAccount(
                        null,
                        headers,
                        r -> {
                            status(r, SUCCESSFUL, NO_CONTENT);
                            headers(r);
                        }
                ));
            }
            {
                final MultivaluedMap<String, Object> headers
                        = new MultivaluedHashMap<>();
                headers.putSingle(ACCEPT, TEXT_PLAIN);
                accept(c -> c.peekAccount(
                        null,
                        headers,
                        r -> {
                            status(r, SUCCESSFUL, NO_CONTENT);
                            headers(r);
                            assertNull(r.getHeaderString(headerName));
                        }
                ));
            }
        }
    }
}
