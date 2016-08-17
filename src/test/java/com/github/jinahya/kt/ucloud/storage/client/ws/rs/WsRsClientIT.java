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
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClient.lines;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ThreadLocalRandom;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.Entity;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import javax.ws.rs.core.Response.StatusType;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class WsRsClientIT
        extends StorageClientIT<WsRsClient, Entity<?>, Response> {

    private static final Logger logger
            = getLogger(MethodHandles.lookup().lookupClass());

    private static final ClientRequestFilter REQUEST_FILTER = requestContext -> {
        logger.debug("request.context.method: {}", requestContext.getMethod());
        logger.debug("request.context.uri: {}", requestContext.getUri());
        requestContext.getHeaders().forEach((k, vs) -> {
            vs.forEach(v -> {
                logger.debug("request.context.header: {}: {}", k, v);
            });
        });
    };

    private static final ClientResponseFilter RESPONSE_FILTER
            = (requestContext, resposneContext) -> {
                final StatusType statusInfo = resposneContext.getStatusInfo();
                logger.debug("response.context.status: {} {}",
                             statusInfo.getStatusCode(),
                             statusInfo.getReasonPhrase());
                resposneContext.getHeaders().forEach((k, vs) -> {
                    vs.forEach(v -> {
                        logger.debug("response.context.header: {}: {}", k, v);
                    });
                });
            };

    public WsRsClientIT() {
        super(WsRsClient.class);
    }

    @Override
    protected void clientInstantiated(final WsRsClient client) {
        super.clientInstantiated(client);
        client.setClientSupplier(() -> {
            final Client c = ClientBuilder.newClient();
            c.register(REQUEST_FILTER);
            c.register(RESPONSE_FILTER);
            return c;
        });

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
    protected void printHeaders(Response response) {
        response.getHeaders().forEach((k, vs) -> {
            vs.forEach(v -> {
                logger.debug("header: {}: {}", k, v);
            });
        });
    }

    @Override
    protected void printBody(Response response) {
        lines(response,
              l -> {
                  logger.debug("line: {}", l);
              }
        );
    }

    @Override
    protected Entity<?> requestEntity() {
        final byte[] bytes
                = new byte[ThreadLocalRandom.current().nextInt(1024)];
        ThreadLocalRandom.current().nextBytes(bytes);
        return Entity.entity(bytes, APPLICATION_OCTET_STREAM);
    }
}
