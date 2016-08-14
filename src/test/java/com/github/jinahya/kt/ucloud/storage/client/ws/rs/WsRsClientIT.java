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

import com.github.jinahya.kt.ucloud.storage.client.StorageClientIT;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import javax.ws.rs.client.Entity;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import javax.ws.rs.core.Response.StatusType;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class WsRsClientIT
        extends StorageClientIT<WsRsClient, Entity<?>, Response> {

    private static final Logger logger = getLogger(WsRsClientIT.class);

    @BeforeClass
    @Override
    public void doBeforeClass() throws ReflectiveOperationException {
        super.doBeforeClass();
//        accept(c -> {
//            c.setClientRequestFilter(requestContext -> {
//                logger.debug("requstContext.method: {}",
//                             requestContext.getMethod());
//                logger.debug("requstContext.uri: {}", requestContext.getUri());
//                requestContext.getHeaders().forEach((k, vs) -> {
//                    vs.forEach(v -> {
//                        logger.debug("requestContext.header: {}: {}", k, v);
//                    });
//                });
//            });
//            c.setClientResponseFilter((requestContext, responseContext) -> {
//                final StatusType statusInfo = responseContext.getStatusInfo();
//                logger.debug("responseContext.status: {} {}",
//                             statusInfo.getStatusCode(),
//                             statusInfo.getReasonPhrase());
//                responseContext.getHeaders().forEach((k, vs) -> {
//                    vs.forEach(v -> {
//                        logger.debug("responseContext.header: {}: {}", k, v);
//                    });
//                });
//            });
//        });
    }

    public WsRsClientIT() {
        super(WsRsClient.class);
    }

    static void status(final StatusType statusInfo, final Family expectedFamily,
                       final Status... expectedStatuses) {
        requireNonNull(statusInfo, "null statusInfo");
        final Family actualFamily = statusInfo.getFamily();
        final int statusCode = statusInfo.getStatusCode();
        final String reasonPhrase = statusInfo.getReasonPhrase();
        logger.debug("-> response.status: {} {}", statusCode, reasonPhrase);
        if (expectedFamily != null) {
            assertEquals(actualFamily, expectedFamily);
        }
        if (expectedStatuses != null && expectedStatuses.length > 0) {
            assertTrue(
                    Stream.of(expectedStatuses).map(Status::getStatusCode)
                    .filter(v -> v == statusCode)
                    .findAny()
                    .isPresent()
            );
        }
    }

//    static void status(final Response response, final Family expectedFamily,
//                       final Status... expectedStatuses) {
//        status(response.getStatusInfo(), expectedFamily, expectedStatuses);
//    }
    static void headers(final Response response) {
        response.getHeaders().entrySet().forEach(e -> {
            e.getValue().forEach(value -> {
                logger.debug("-> response.header: {}: {}", e.getKey(), value);
            });
        });
    }

    static void body(final Response response, final Charset charset)
            throws IOException {
        if (requireNonNull(response, "null response").getStatus()
            != Status.OK.getStatusCode()) {
            logger.debug("status is not " + Status.OK + "; skipping...");
            return;
        }
        try (InputStream stream = response.readEntity(InputStream.class);
             InputStreamReader reader = new InputStreamReader(stream, charset);
             BufferedReader buffered = new BufferedReader(reader);) {
            buffered.lines().forEach(System.out::println);
        }
    }

    static void body(final Response response) throws IOException {
        if (requireNonNull(response, "null response").getStatus()
            != Status.OK.getStatusCode()) {
            logger.debug("status is not " + Status.OK + "; skipping...");
            return;
        }
        try (Reader reader = response.readEntity(Reader.class);
             BufferedReader buffered = new BufferedReader(reader);) {
            buffered.lines().forEach(System.out::println);
        }
    }

    @Override
    protected void assertSuccesfulAuthentication(final Response response) {
        final StatusType statusInfo = response.getStatusInfo();
        final Family family = statusInfo.getFamily();
        final int statusCode = statusInfo.getStatusCode();
        final String reasonPhrase = statusInfo.getReasonPhrase();
        assertEquals(family, SUCCESSFUL,
                     "status: " + statusCode + " " + reasonPhrase);
    }

    @Override
    protected int statusCode(Response response) {
        return WsRsClient.statusCode(response);
    }

    @Override
    protected String reasonPhrase(Response response) {
        return WsRsClient.reasonPhrase(response);
    }

    @Override
    protected Entity<?> entity() {
        final byte[] bytes
                = new byte[ThreadLocalRandom.current().nextInt(1024)];
        ThreadLocalRandom.current().nextBytes(bytes);
        return Entity.entity(bytes, APPLICATION_OCTET_STREAM);
    }
}
