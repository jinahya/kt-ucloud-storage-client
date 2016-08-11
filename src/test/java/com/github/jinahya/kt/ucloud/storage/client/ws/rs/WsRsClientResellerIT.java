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

import static com.github.jinahya.kt.ucloud.storage.client.StorageClient.HEADER_X_CONTAINER_WRITE;
import com.github.jinahya.kt.ucloud.storage.client.StorageClientResellerIT;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientITs.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientITs.status;
import java.util.UUID;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import javax.ws.rs.core.Response.StatusType;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class WsRsClientResellerIT
        extends StorageClientResellerIT<WsRsClient> {

    private static final Logger logger = getLogger(WsRsClientResellerIT.class);

    public WsRsClientResellerIT() {
        super(WsRsClient.class);
    }

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

    @Test
    public void readAccount() {
        logger.debug("------------------------ testing readResellerAccount...");
        accept(c -> {
            logger.debug("c.storageUrl: {}", c.getStorageUrl());
            logger.debug("c.authToken: {}", c.getAuthToken());
            logger.debug("c.authTokenExpires: {}", c.getAuthTokenExpires());
            c.readResellerAccount(
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                        body(r);
                    }
            );
        });
    }

    @Test
    public void user() {
        logger.debug("----------------------------------- testing user...");
        final String userName = UUID.randomUUID().toString();
        final String userKey = UUID.randomUUID().toString();
        accept(c -> {
            logger.debug("--------------------------------- updating user...1");
            c.updateResellerUser(
                    userName,
                    userKey,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("--------------------------------- updating user...2");
            c.updateResellerUser(
                    userName,
                    userKey,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("----------------------------------- reading user...");
            c.readResellerUser(
                    userName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("---------------------------------- deleting user...");
            c.deleteResellerUser(
                    userName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
    }

    @Test
    public void objectWithUser() {
        logger.debug("--------------------------- testing object with user...");
        final String userName = UUID.randomUUID().toString();
        final String userKey = UUID.randomUUID().toString();
        accept(c -> {
            logger.debug("---------------------------------- updating user...");
            c.updateResellerUser(
                    userName,
                    userKey,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("-------------------------------- updating container");
            c.updateContainer(
                    containerName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("------------------------ updating container; acl...");
            final MultivaluedMap<String, Object> headers
                    = new MultivaluedHashMap<>();
            headers.putSingle(HEADER_X_CONTAINER_WRITE,
                              c.getAuthAccount() + ":" + userName);
            c.configureContainer(
                    containerName,
                    null,
                    headers,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("-------------------------------- updating object...");
            c.updateObject(
                    containerName,
                    objectName,
                    null,
                    null,
                    Entity.text(""),
                    r -> {
                        status(r, SUCCESSFUL);
                    });
        });
        accept(c -> {
            logger.debug("-------------------------------- deleting object...");
            c.deleteObject(
                    containerName,
                    objectName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("----------------------------- deleting container...");
            c.deleteContainer(
                    containerName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL);
                    }
            );
        });
        accept(c -> {
            logger.debug("---------------------------------- deleting user...");
            c.deleteResellerUser(
                    userName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL, NO_CONTENT);
                    }
            );
        });
    }

    private final String containerName = getClass().getName();

    private final String objectName = getClass().getName();
}
