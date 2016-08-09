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

import com.github.jinahya.kt.ucloud.storage.client.StorageClient;
import java.io.IOException;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.WILDCARD;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Test(groups = {"net.user"})
public class NetClientUserIT extends NetClientIT {

    private static final Logger logger = getLogger(NetClientUserIT.class);

    @Test
    public void checkTokens() {
        logger.debug("------------------------------------ checking tokens...");
        final String oldToken = apply(c -> {
            return c.getAuthToken();
        });
        logger.debug("oldToken: {}", oldToken);
        final String newToken = apply(c -> {
            try {
                return c.authenticateUser(n -> {
                    return n.getHeaderField(StorageClient.HEADER_X_AUTH_TOKEN);
                });
            } catch (final IOException ioe) {
                fail("failed to authenticate user", ioe);
                throw new RuntimeException(ioe);
            }
        });
        logger.debug("newToken: {}", newToken);
        logger.debug("--------------------- peeking account with old token...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        headers.putSingle(ACCEPT, WILDCARD);
        headers.putSingle(StorageClient.HEADER_X_AUTH_TOKEN, oldToken);
        accept(c -> {
            try {
                c.peekAccount(
                        null,
                        headers,
                        n -> {
                            // @todo check this out
//                            status(n, NO_CONTENT);
                        }
                );
            } catch (final IOException ioe) {
                fail("failed to peek account", ioe);
            }
        });
    }
}
