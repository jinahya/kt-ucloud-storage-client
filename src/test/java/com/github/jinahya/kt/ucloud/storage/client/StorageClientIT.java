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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @param <C> client type parameter
 */
public abstract class StorageClientIT<C extends StorageClient> {

    private static final Logger logger = getLogger(StorageClientIT.class);

    public StorageClientIT(final Class<C> clientClass) {
        super();
        this.clientClass = clientClass;
    }

    @BeforeClass
    public void doBeforeClass() throws ReflectiveOperationException {
        logger.debug("=======================================================");
        final String authUrl = System.getProperty("authUrl");
        if (authUrl == null) {
            logger.error("missing property; authUrl; skipping...");
            throw new SkipException("missing property; authUrl");
        }
        final String authAccount = System.getProperty("authAccount");
        if (authAccount != null) {
            logger.info("existing property; authAccount; skipping...");
            throw new SkipException("existing property; authAccount");
        }
        final String authUser = System.getProperty("authUser");
        if (authUser == null) {
            logger.error("missing property; authUser; skipping...");
            throw new SkipException("missing property; authUser");
        }
        final String authKey = System.getProperty("authKey");
        if (authKey == null) {
            logger.error("missing proprety; authKey; skipping...");
            throw new SkipException("missing property; authKey");
        }
        client = clientClass
                .getConstructor(String.class, String.class, String.class)
                .newInstance(authUrl, authUser, authKey);
        logger.debug("client constructed: {}", client);
        client.authenticateUser(false);
        logger.debug("client authenticted");
    }

    @AfterClass
    public void doAfterClass() {
        client.invalidate();
        logger.debug("client invalidated");
        client = null;
        logger.debug("client nullified");
        logger.debug("=======================================================");
    }

    protected <R> R apply(final Function<C, R> function) {
        return function.apply(client);
    }

    protected <U, R> R apply(final BiFunction<C, U, R> function,
                             final Supplier<U> u) {
        return apply(c -> function.apply(c, u.get()));
    }

    protected void accept(final Consumer<C> consumer) {
        apply(c -> {
            consumer.accept(c);
            return null;
        });
    }

    protected <U> void accept(final BiConsumer<C, U> consumer,
                              final Supplier<U> u) {
        accept(c -> consumer.accept(c, u.get()));
    }

    protected final Class<C> clientClass;

    private transient C client;
}
