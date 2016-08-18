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
package com.github.jinahya.kt.ucloud.storage.client;

import static com.github.jinahya.kt.ucloud.storage.client.StorageClient.accountMetaHeader;
import static com.github.jinahya.kt.ucloud.storage.client.StorageClient.containerMetaHeader;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.MediaType.WILDCARD;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.Response.Status.Family.familyOf;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @param <ClientType> client type parameter
 * @param <EntityType> request entity type parameter
 * @param <ResponseType> response type parameter
 */
public abstract class StorageClientIT<ClientType extends StorageClient<ClientType, EntityType, ResponseType>, EntityType, ResponseType> {

    private static final Logger logger
            = getLogger(MethodHandles.lookup().lookupClass());

    private static final long MILLIS = TimeUnit.SECONDS.toMillis(4L);

    protected static void sleep() {
        logger.debug("------------------------- sleeping for {} ms...", MILLIS);
        try {
            Thread.sleep(MILLIS);
        } catch (final InterruptedException ie) {
            fail("faield to sleep", ie);
        }
    }

    protected static void assertStatus(final int actual, final Family family,
                                       final Status... expecteds) {
        if (family != null) {
            assertEquals(familyOf(actual), family);
        }
        if (expecteds != null && expecteds.length > 0) {
            boolean matched = false;
            for (final Status expected : expecteds) {
                if (actual == expected.getStatusCode()) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                fail(actual + " \u2288 " + Arrays.toString(expecteds));
            }
        }
    }

    public StorageClientIT(final Class<ClientType> clientClass) {
        super();
        this.clientClass = requireNonNull(clientClass, "null clientClass");
    }

    @BeforeClass
    public void doBeforeClass() throws ReflectiveOperationException {
        logger.debug("=======================================================");
        final String authUrl = System.getProperty("authUrl");
        if (authUrl == null) {
            logger.error("missing property; authUrl; skipping...");
            throw new SkipException("missing property; authUrl");
        }
        logger.debug("authUrl: {}", authUrl);
        final String authUser = System.getProperty("authUser");
        if (authUser == null) {
            logger.error("missing property; authUser; skipping...");
            throw new SkipException("missing property; authUser");
        }
        logger.debug("authUser: {}", authUser);
        final String authKey = System.getProperty("authKey");
        if (authKey == null) {
            logger.error("missing proprety; authKey; skipping...");
            throw new SkipException("missing property; authKey");
        }
        logger.debug("authKey: {}", authKey);
        clientInstance = clientClass
                .getConstructor(String.class, String.class, String.class)
                .newInstance(authUrl, authUser, authKey);
        logger.debug("client instantiated: {}", clientInstance);
        clientInstantiated(clientInstance);
        logger.debug("client.authUser: {}", clientInstance.getAuthUser());
        logger.debug("client.authKey: {}", clientInstance.getAuthKey());
        logger.debug("client.resellerAccount: {}",
                     clientInstance.getAccountName());
        clientInstance.authenticateUser(
                false,
                r -> {
                    assertSuccesfulAuthentication(r);
                }
        );
        logger.debug("client authenticted");
        logger.debug("client.storageUrl: {}", clientInstance.getStorageUrl());
        logger.debug("client.resellerAccountUrl: {}",
                     clientInstance.getAccountUrl());
    }

    protected abstract void assertSuccesfulAuthentication(
            ResponseType response);

    protected void clientInstantiated(final ClientType client) {
    }

    @AfterClass
    public void doAfterClass() {
        clientInstance.invalidate();
        logger.debug("client invalidated");
        clientInstance = null;
        logger.debug("client nullified");
        logger.debug("=======================================================");
    }

    protected void clientNullifying(final ClientType client) {
    }

    protected <R> R apply(final boolean reseller,
                          final Function<ClientType, R> function) {
        if (reseller ^ clientInstance.getAccountName() != null) {
            throw new SkipException("skipping...");
        }
        return function.apply(clientInstance);
    }

    protected <U, R> R apply(final boolean reseller,
                             final BiFunction<ClientType, U, R> function,
                             final Supplier<U> u) {
        return apply(reseller, c -> function.apply(c, u.get()));
    }

    protected void accept(final boolean reseller,
                          final Consumer<ClientType> consumer) {
        apply(reseller, c -> {
          consumer.accept(c);
          return null;
      });
    }

    protected <U> void accept(final boolean reseller,
                              final BiConsumer<ClientType, U> consumer,
                              final Supplier<U> u) {
        accept(reseller, c -> consumer.accept(c, u.get()));
    }

    protected Family family(ResponseType response) {
        return Family.familyOf(statusCode(response));
    }

    protected abstract int statusCode(ResponseType response);

    protected abstract String reasonPhrase(ResponseType response);

    protected abstract void printHeaders(final ResponseType response);

    protected abstract void printBody(final ResponseType response);

    protected void assertStatus(final ResponseType response,
                                final Family family, final Status... statuses) {
        final int statusCode = statusCode(response);
        logger.debug("statusCode: {}", statusCode);
        assertStatus(statusCode, family, statuses);
    }

    protected abstract EntityType requestEntity();

    // ---------------------------------------------------------------- /account
    @Test
    public void testStorage() {
        logger.debug("------------------------------------ testing storage...");
        accept(false,
               c -> {
                   logger.debug("------------------------ peeking account...");
                   final Map<String, List<Object>> headers = new HashMap<>();
                   headers.put(ACCEPT, singletonList(WILDCARD));
                   c.peekStorage(
                           null,
                           headers,
                           r -> {
                               assertStatus(r, SUCCESSFUL, NO_CONTENT);
                           }
                   );
               }
        );
        asList(TEXT_PLAIN, APPLICATION_XML, APPLICATION_JSON).forEach(a -> {
            logger.debug("--------------- reading account in {}...", a);
            accept(false,
                   c -> {
                       final MultivaluedMap<String, Object> headers
                       = new MultivaluedHashMap<>();
                       headers.putSingle(ACCEPT, a);
                       c.readStorage(
                               null,
                               headers,
                               r -> {
                                   assertStatus(r, SUCCESSFUL, OK, NO_CONTENT);
                               }
                       );
                   }
            );
        });
        {
            logger.debug("---------------------------- configuring account...");
            final String[] tokens = randomUUID().toString().split("-");
            accept(false,
                   c -> {
                       logger.debug("--------------------- adding metadata...");
                       final MultivaluedMap<String, Object> headers
                       = new MultivaluedHashMap<>();
                       headers.putSingle(
                               accountMetaHeader(false, tokens), "irrelevant");
                       c.configureStorage(
                               null,
                               headers,
                               r -> {
                                   assertStatus(r, SUCCESSFUL, NO_CONTENT);
                               }
                       );
                   }
            );
            accept(false,
                   c -> {
                       logger.debug("------------------- removing metadata...");
                       final MultivaluedMap<String, Object> headers
                       = new MultivaluedHashMap<>();
                       headers.putSingle(
                               accountMetaHeader(true, tokens), "irrelevant");
                       c.configureStorage(
                               null,
                               headers,
                               r -> {
                                   assertStatus(r, SUCCESSFUL, NO_CONTENT);
                               }
                       );
                   }
            );
        }
    }

    // ------------------------------------------------------ /account/container
    @Test
    public void testContainer() {
        logger.debug("---------------------------------- testing container...");
        final String containerName
                = getClass().getSimpleName() + "-" + randomUUID().toString();
        logger.debug("containerName: {}", containerName);
        accept(false,
               c -> {
                   logger.debug("---------------------- updating container...");
                   c.updateContainer(
                           containerName,
                           null,
                           null,
                           r -> {
                               assertStatus(r, SUCCESSFUL);
                           }
                   );
               }
        );
        {
            logger.debug("-------------------------- configuring container...");
            final String[] tokens = randomUUID().toString().split("-");
            accept(false,
                   c -> {
                       logger.debug("--------------------- adding metadata...");
                       final MultivaluedMap<String, Object> headers
                       = new MultivaluedHashMap<>();
                       headers.putSingle(containerMetaHeader(false, tokens),
                                         "irrelevant");
                       c.configureStorage(
                               null,
                               headers,
                               r -> {
                                   assertStatus(r, SUCCESSFUL, NO_CONTENT);
                               }
                       );
                   }
            );
            accept(false,
                   c -> {
                       logger.debug("------------------- removing metadata...");
                       final MultivaluedMap<String, Object> headers
                       = new MultivaluedHashMap<>();
                       headers.putSingle(
                               containerMetaHeader(true, tokens), "irrelevant");
                       c.configureStorage(
                               null,
                               headers,
                               r -> {
                                   assertStatus(r, SUCCESSFUL, NO_CONTENT);
                               }
                       );
                   }
            );
        }
        accept(false,
               c -> {
                   logger.debug("---------------------- deleting container...");
                   c.deleteContainer(
                           containerName,
                           null,
                           null,
                           r -> {
                               assertStatus(r, SUCCESSFUL);
                           }
                   );
               }
        );
    }

    // ----------------------------------------------- /account/container/object
    @Test
    public void testObject() {
        final long sleep = SECONDS.toMillis(2L);
        logger.debug("------------------------------------- testing object...");
        final String containerName
                = getClass().getSimpleName() + "-" + randomUUID().toString();
        logger.debug("containerName: {}", containerName);
        final String objectName
                = getClass().getSimpleName() + "-" + randomUUID().toString();
        logger.debug("objectName: {}", objectName);
        accept(false,
               c -> {
                   logger.debug("---------------------- updating container...");
                   c.updateContainer(
                           containerName,
                           null,
                           null,
                           r -> {
                               assertStatus(r, SUCCESSFUL);
                           }
                   );
               }
        );
        accept(false,
               c -> {
                   logger.debug("------------------------- updating object...");
                   c.updateObject(containerName,
                                  objectName,
                                  null,
                                  null,
                                  () -> requestEntity(),
                                  r -> {
                                      assertStatus(r, SUCCESSFUL);
                                  }
                   );
               }
        );
        accept(false,
               c -> {
                   logger.debug("-------------------------- reading object...");
                   c.readObject(
                           containerName,
                           objectName,
                           null,
                           null,
                           r -> {
                               assertStatus(r, SUCCESSFUL, OK);
                           }
                   );
               }
        );
        accept(false,
               c -> {
                   logger.debug("------------------------- deleting object...");
                   c.deleteObject(
                           containerName,
                           objectName,
                           null,
                           null,
                           r -> {
                               assertStatus(r, SUCCESSFUL);
                           }
                   );
               }
        );
        logger.debug("sleeping for " + sleep + "ms...");
        try {
            Thread.sleep(2000L);
        } catch (final InterruptedException ie) {
            fail("failed to sleep", ie);
        }
        accept(false,
               c -> {
                   logger.debug("----------------------- peeking container...");
                   final Map<String, List<Object>> headers = new HashMap<>();
                   headers.put(ACCEPT, singletonList(WILDCARD));
                   c.peekContainer(
                           containerName,
                           null,
                           headers,
                           r -> {
                               printHeaders(r);
                               assertStatus(r, SUCCESSFUL, NO_CONTENT);
                           }
                   );
               }
        );
        accept(false,
               c -> {
                   logger.debug("---------------------- deleting container...");
                   c.deleteContainer(
                           containerName,
                           null,
                           null,
                           r -> {
                               assertStatus(r, SUCCESSFUL);
                           }
                   );
               }
        );
    }

    // ------------------------------------------------------- /reseller/account
    @Test
    public void testResellerAccount() {
        logger.debug("--------------------------- testing reseller account...");
        accept(true,
               c -> {
                   logger.debug("---------------- reading reseller account...");
                   c.readAccount(
                           null,
                           null,
                           r -> {
                               printBody(r);
                               assertStatus(r, SUCCESSFUL);
                           }
                   );
               }
        );
    }

    // -------------------------------------------------- /reseller/account/user
    @Test
    public void testResellerUser() {
        logger.debug("------------------------------ testing reseller user...");
        final String userName = UUID.randomUUID().toString();

        accept(true,
               c -> {
                   logger.debug("----------------- updating reseller user...1");
                   final String userKey = UUID.randomUUID().toString();
                   final boolean userAdmin
                   = ThreadLocalRandom.current().nextBoolean();
                   c.updateUser(
                           userName,
                           userKey,
                           userAdmin,
                           null,
                           null,
                           r -> {
                               assertStatus(r, SUCCESSFUL);
                           }
                   );
               }
        );
        sleep();
        accept(true,
               c -> {
                   logger.debug("------------------ reading reseller user...1");
                   c.readUser(
                           userName,
                           null,
                           null,
                           r -> {
                               printBody(r);
                               assertStatus(r, SUCCESSFUL);
                           }
                   );
               }
        );
        accept(true,
               c -> {
                   logger.debug("----------------- updating reseller user...2");
                   String userKey = UUID.randomUUID().toString();
                   final boolean userAdmin
                   = ThreadLocalRandom.current().nextBoolean();
                   c.updateUser(
                           userName,
                           userKey,
                           null,
                           null,
                           null,
                           r -> {
                               assertStatus(r, SUCCESSFUL);
                               printBody(r);
                           }
                   );
               }
        );
        sleep();
        accept(true,
               c -> {
                   logger.debug("------------------ reading reseller user...2");
                   c.readUser(
                           userName,
                           null,
                           null,
                           r -> {
                               printBody(r);
                               assertStatus(r, SUCCESSFUL);
                           }
                   );
               }
        );
        accept(true,
               c -> {
                   logger.debug("------------------ deleting reseller user...");
                   c.deleteUser(
                           userName,
                           null,
                           null,
                           r -> {
                               assertStatus(r, SUCCESSFUL);
                           }
                   );
               }
        );
    }

    protected final Class<ClientType> clientClass;

    private transient ClientType clientInstance;
}
