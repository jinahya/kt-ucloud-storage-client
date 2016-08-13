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
import java.util.stream.Stream;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeClass;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public abstract class WsRsClientIT extends StorageClientIT<WsRsClient> {

    private static final Logger logger = getLogger(WsRsClientIT.class);

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

//    private static WsRsClient client;
//    @BeforeSuite
//    void doBeforeSuite() {
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
//        client = new WsRsClient(authUrl, authUser, authPass)
//                .authenticateUser(r -> {
//                    status(r, Family.SUCCESSFUL, Status.OK);
//                });
////        client = client().authenticateUser(r -> {
////            status(r, Family.SUCCESSFUL, Status.OK.getStatusCode());
////        });
//    }
//
//    @AfterSuite
//    void doAfterSuite() {
//        client.invalidate();
//        client = null;
//    }
    public WsRsClientIT() {
        super(WsRsClient.class);
    }

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
//    @Deprecated
//    static void status(final StatusType statusInfo, final Family expected) {
//        final Family family = statusInfo.getFamily();
//        final int statusCode = statusInfo.getStatusCode();
//        final String reasonPhrase = statusInfo.getReasonPhrase();
//        logger.debug("-> response.status: {} {}", statusCode, reasonPhrase);
//        assertEquals(family, expected);
//    }
//
//    @Deprecated
//    static void status(final Response response, final Family expected) {
//        status(response.getStatusInfo(), expected);
//    }
//    @Deprecated
//    static void status(final StatusType statusInfo, final Family expectedFamily,
//                       final int... expectedStatuses) {
//        requireNonNull(statusInfo, "null statusInfo");
//        final Family actualFamily = statusInfo.getFamily();
//        final int actualStatus = statusInfo.getStatusCode();
//        final String reasonPhrase = statusInfo.getReasonPhrase();
//        logger.debug("-> response.status: {} {}", actualStatus, reasonPhrase);
//        if (expectedFamily != null) {
//            assertEquals(actualFamily, expectedFamily);
//        }
//        if (expectedStatuses != null && expectedStatuses.length > 0) {
//            assertTrue(
//                    IntStream.of(expectedStatuses)
//                    .filter(v -> v == actualStatus)
//                    .findAny()
//                    .isPresent()
//            );
//        }
//    }
//
//    @Deprecated
//    static void status(final Response response, final Family expectedFamily,
//                       final int... expectedStatuses) {
//        status(response.getStatusInfo(), expectedFamily, expectedStatuses);
//    }
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

    static void status(final Response response, final Family expectedFamily,
                       final Status... expectedStatuses) {
        status(response.getStatusInfo(), expectedFamily, expectedStatuses);
    }

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

//    <T> T apply(final Function<WsRsClient, T> function) {
//        return requireNonNull(function, "null function").apply(client);
//    }
//
//    <U, R> R apply(final BiFunction<WsRsClient, U, R> function,
//                   final Supplier<U> u) {
//        return apply(
//                c -> requireNonNull(function, "null function")
//                .apply(c, requireNonNull(u, "null u").get())
//        );
//    }
//
//    void accept(final Consumer<WsRsClient> consumer) {
//        apply(c -> {
//            requireNonNull(consumer, "null consumer").accept(c);
//            return null;
//        });
//    }
//
//    <U> void accept(final BiConsumer<WsRsClient, U> consumer,
//                    final Supplier<U> u) {
//        accept(c -> requireNonNull(consumer, "null consumer").accept(
//                c, requireNonNull(u, "null u").get())
//        );
//    }
}
