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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
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
@Test(groups = {"net.account"})
public class NetClientAccountIT extends NetClientIT {

    private static final Logger logger = getLogger(NetClientAccountIT.class);

    @Test
    public void peekAccount() {
        logger.debug("------------------------------------ peeking account...");
        accept(c -> {
            try {
                c.peekAccount(
                        null,
                        null,
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
        final Map<String, List<Object>> headers = new HashMap<>();
        asList(TEXT_PLAIN, APPLICATION_XML, APPLICATION_JSON).forEach(t -> {
            logger.debug("accepting {}", t);
            headers.put(ACCEPT, singletonList(t));
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

    @Test(dependsOnMethods = {"readAccount"})
    public void configureAccount() {
        logger.debug("-------------------------------- configuring account...");
        final Map<String, List<Object>> headers = new HashMap<>();
        final String name = "X-Account-Meta-Test";
        final String value = "test";
        headers.put(name, singletonList(value));
        accept(c -> {
            try {
                c.configureAccount(
                        null,
                        headers,
                        n -> {
                            status(n, NO_CONTENT);
                            headers(n);
                        });
            } catch (final IOException ioe) {
                fail("failed to peek account", ioe);
            }
        });
        accept(c -> {
            try {
                c.peekAccount(
                        null,
                        headers,
                        n -> {
                            status(n, NO_CONTENT);
                            headers(n);
                            assertEquals(n.getHeaderField(name), value);
                        });
            } catch (final IOException ioe) {
                fail("failed to peek account", ioe);
            }
        });
        headers.remove(name);
        headers.put("X-Remove-Account-Meta-Test", singletonList(value));
        accept(c -> {
            try {
                c.configureAccount(
                        null,
                        headers,
                        n -> {
                            status(n, NO_CONTENT);
                            headers(n);
                        });
            } catch (final IOException ioe) {
                fail("failed to peek account", ioe);
            }
        });
        accept(c -> {
            try {
                c.peekAccount(
                        null,
                        headers,
                        n -> {
                            status(n, NO_CONTENT);
                            headers(n);
                            assertNull(n.getHeaderField(name));
                        });
            } catch (final IOException ioe) {
                fail("failed to peek account", ioe);
            }
        });
    }
}
