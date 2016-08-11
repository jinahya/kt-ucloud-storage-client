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
package com.github.jinahya.kt.ucloud.storage.client.net;

import com.github.jinahya.kt.ucloud.storage.client.AccountInfo;
import com.github.jinahya.kt.ucloud.storage.client.JaxbTest;
import static com.github.jinahya.kt.ucloud.storage.client.StorageClient.accountMetaHeader;
import static com.github.jinahya.kt.ucloud.storage.client.net.NetClientIT.headers;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import static java.util.Arrays.asList;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.MediaType.WILDCARD;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Test(dependsOnGroups = {"net.user"}, groups = {"net.account"})
public class NetClientAccountIT extends NetClientIT {

    private static final Logger logger = getLogger(NetClientAccountIT.class);

    @Test
    public void peekAccount() {
        logger.debug("------------------------------------ peeking account...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
//        headers.putSingle(ACCEPT, TEXT_PLAIN);
        headers.putSingle(ACCEPT, WILDCARD);
        accept(c -> {
            try {
                c.peekAccount(
                        null,
                        headers,
                        n -> {
                            status((HttpURLConnection) n, NO_CONTENT);
                            headers((HttpURLConnection) n);
                            final AccountInfo info = AccountInfo.newInstance(n);
                            logger.debug("info: {}", info);
                            JaxbTest.printXml(AccountInfo.class, info);
                        });
            } catch (final IOException ioe) {
                fail("failed to peek account", ioe);
            }
        });
    }

    @Test(dependsOnMethods = {"peekAccount"})
    public void readAccount() {
        logger.debug("------------------------------------ reading account...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        asList(TEXT_PLAIN, APPLICATION_XML, APPLICATION_JSON).forEach(t -> {
            logger.debug("accepting {}", t);
            headers.putSingle(ACCEPT, t);
            accept(c -> {
                try {
                    c.readAccount(
                            null,
                            headers,
                            n -> {
                                status(n, OK);
                                headers(n);
                                body(n, StandardCharsets.UTF_8);
                            });
                } catch (final IOException ioe) {
                    fail("failed to peek account", ioe);
                }
            });
        });
    }

    @Test(dependsOnMethods = {"readAccount"}, invocationCount = 1)
    public void configureAccount() {
        logger.debug("-------------------------------- configuring account...");
        final long sleep = 2000L;
        final String[] tokens = getClass().getName().split("\\.");
        {
            final String key = accountMetaHeader(false, tokens);
            final String value = "test";
            accept(c -> {
                final MultivaluedMap<String, Object> headers
                        = new MultivaluedHashMap<>();
                headers.putSingle(key, value);
                logger.debug("adding meta...");
                logger.debug("request headers: {}", headers);
                try {
                    c.configureAccount(
                            null,
                            headers,
                            n -> {
                                status(n, NO_CONTENT);
                                headers(n);
                            });
                } catch (final IOException ioe) {
                    fail("failed to configure account", ioe);
                }
            });
            logger.debug("sleeping for {} millis...", sleep);
            try {
                Thread.sleep(sleep);
            } catch (final InterruptedException ie) {
                fail("failed to sleep", ie);
            }
            accept(c -> {
                try {
                    final MultivaluedMap<String, Object> headers
                            = new MultivaluedHashMap<>();
//                    headers.putSingle(ACCEPT, TEXT_PLAIN);
                    headers.putSingle(ACCEPT, WILDCARD);
                    logger.debug("checking meta...");
                    c.peekAccount(
                            null,
                            headers,
                            n -> {
                                status(n, NO_CONTENT);
                                headers(n);
                                assertEquals(n.getHeaderField(key), value);
                            });
                } catch (final IOException ioe) {
                    fail("failed to peek account", ioe);
                }
            });
        }
        {
            accept(c -> {
                final String key = accountMetaHeader(true, tokens);
                final String value = "any";
                final MultivaluedMap<String, Object> headers
                        = new MultivaluedHashMap<>();
                headers.putSingle(key, value);
                logger.debug("removing meta...");
                logger.debug("request headers: {}", headers);
                try {
                    c.configureAccount(
                            null,
                            headers,
                            n -> {
                                status(n, NO_CONTENT);
                                headers(n);
                            });
                } catch (final IOException ioe) {
                    fail("failed to configure account", ioe);
                }
            });
            logger.debug("sleeping for {} millis...", sleep);
            try {
                Thread.sleep(sleep);
            } catch (final InterruptedException ie) {
                fail("failed to sleep", ie);
            }
            accept(c -> {
                try {
                    final MultivaluedMap<String, Object> headers
                            = new MultivaluedHashMap<>();
//                    headers.putSingle(ACCEPT, TEXT_PLAIN);
                    headers.putSingle(ACCEPT, WILDCARD);
                    logger.debug("checking meta...");
                    c.peekAccount(
                            null,
                            headers,
                            n -> {
                                status(n, NO_CONTENT);
                                headers(n);
                                final String key
                                = accountMetaHeader(false, tokens);
                                assertNull(n.getHeaderField(key));
                            });
                } catch (final IOException ioe) {
                    fail("failed to peek account", ioe);
                }
            });
        }
    }
}
