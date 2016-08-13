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

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Test(dependsOnGroups = {"net.account"}, groups = {"net.container"})
public class NetClientContainerIT extends NetClientIT {

    private static final Logger logger = getLogger(NetClientContainerIT.class);

    @Test
    public void peekContainer() {
        logger.debug("---------------------------------- peeking container...");
        final MultivaluedMap<String, Object> haders
                = new MultivaluedHashMap<>();
        accept(c -> {
            haders.putSingle(ACCEPT, TEXT_PLAIN);
            c.peekContainer(
                    containerName,
                    null,
                    null,
                    n -> {
                        status(n, NOT_FOUND, NO_CONTENT);
                    }
            );
        });
    }

    @Test
    public void readContainer() {
        logger.debug("---------------------------------- reading container...");
        final MultivaluedMap<String, Object> haders
                = new MultivaluedHashMap<>();
        accept(c -> {
            haders.putSingle(ACCEPT, TEXT_PLAIN);
            c.readContainer(
                    containerName,
                    null,
                    null,
                    n -> {
                        status(n, NOT_FOUND, OK);
                        headers(n);
                        body(n, UTF_8);
                    }
            );
        });
    }

    private final String containerName = getClass().getName();
}
