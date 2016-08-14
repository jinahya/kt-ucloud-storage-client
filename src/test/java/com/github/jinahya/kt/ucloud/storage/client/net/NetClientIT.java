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

import com.github.jinahya.kt.ucloud.storage.client.StorageClientIT;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import static java.util.Arrays.stream;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.OK;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class NetClientIT
        extends StorageClientIT<NetClient, InputStream, URLConnection> {

    private static final Logger logger = getLogger(NetClientIT.class);

    static <R> R status(final HttpURLConnection connection,
                        final BiFunction<Integer, String, R> function) {
        try {
            final int statusCode = connection.getResponseCode();
            final String reasonPhrase = connection.getResponseMessage();
            return function.apply(statusCode, reasonPhrase);
        } catch (final IOException ioe) {
            fail("failed to read status", ioe);
            throw new RuntimeException("failed to read status", ioe);
        }
    }

    static int statusCode(final HttpURLConnection connection) {
        return status(connection, (c, p) -> c);
    }

    static String reasonPhrase(final HttpURLConnection connection) {
        return status(connection, (c, p) -> p);
    }

    static void status(final HttpURLConnection connection,
                       final Response.Status... expectedStatuses) {
        try {
            final int statusCode = connection.getResponseCode();
            final String reasonPhrase = connection.getResponseMessage();
            logger.debug("-> status: {} {}", statusCode, reasonPhrase);
            if (expectedStatuses != null) {
                if (stream(expectedStatuses)
                        .map(Response.Status::getStatusCode)
                        .filter(v -> v == statusCode).findAny()
                        .orElse(null) == null) {
                    fail("status is non of expec†ed: " + statusCode
                         + " \u2288 " + Arrays.toString(expectedStatuses));
                }
            }
        } catch (final IOException ioe) {
            fail("failed to read status", ioe);
        }
    }

    static void status(final URLConnection connection,
                       final Response.Status... allowedStatuses) {
        status((HttpURLConnection) connection, allowedStatuses);
    }

    static void headers(final URLConnection connection) {
        connection.getHeaderFields().forEach((n, vs) -> {
            vs.forEach(v -> {
                logger.debug("-> header: {}: {}", n, v);
            });
        });
    }

    static void body(final URLConnection connection, final Charset charset) {
        try {
            final int statusCode
                    = ((HttpURLConnection) connection).getResponseCode();
            if (statusCode != OK.getStatusCode()) {
                logger.debug("status is not ok. skipping...");
                return;
            }
            try (InputStream stream = connection.getInputStream();
                 Reader reader = new InputStreamReader(stream, charset);
                 BufferedReader buffered = new BufferedReader(reader)) {
                buffered.lines().forEach(l -> logger.debug("-> body: {}", l));
            }
        } catch (final IOException ioe) {
            fail("failed to read body", ioe);
        }
    }

    public NetClientIT() {
        super(NetClient.class);
    }

    @Override
    protected void assertSuccesfulAuthentication(final URLConnection response) {
        try {
            final int statusCode
                    = ((HttpURLConnection) response).getResponseCode();
            final String reasonPhrase
                    = ((HttpURLConnection) response).getResponseMessage();
            assertTrue(statusCode / 100 == 2,
                       "status: " + statusCode + " " + reasonPhrase);
        } catch (final IOException ioe) {
            fail("failed to get status info", ioe);
        }
    }

    @Override
    protected int statusCode(final URLConnection response) {
        return NetClient.statusCode((HttpURLConnection) response);
    }

    @Override
    protected String reasonPhrase(URLConnection response) {
        return NetClient.reasonPhrase((HttpURLConnection) response);
    }

    @Override
    protected InputStream entity() {
        final byte[] bytes
                = new byte[ThreadLocalRandom.current().nextInt(1024)];
        ThreadLocalRandom.current().nextBytes(bytes);
        return new ByteArrayInputStream(bytes);
    }
}
