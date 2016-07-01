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

import java.security.SecureRandom;
import java.util.Random;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import org.slf4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.Test;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class RsStorageClientIT {

    private static final Logger logger = getLogger(RsStorageClientIT.class);

    private static RsStorageClient client() {
        final String authUrl = System.getProperty("authUrl");
        if (authUrl == null) {
            throw new SkipException("missing property; authUrl");
        }
        final String authUser = System.getProperty("authUser");
        if (authUser == null) {
            throw new SkipException("missing property; authUser");
        }
        final String authPass = System.getProperty("authPass");
        if (authPass == null) {
            throw new SkipException("missing property; authPass");
        }
        return new RsStorageClient(authUrl, authUser, authPass);
    }

    @Test(enabled = true)
    public void authenticateUser() {
        client().authenticateUser(response -> {
            logger.debug("xStorageUrl: {}",
                         response.getHeaderString("X-Storage-Url"));
            logger.debug("xAuthToken: {}",
                         response.getHeaderString("X-Auth-Token"));
            logger.debug("xAuthTokenExpires: {}",
                         response.getHeaderString("X-Auth-Token-Expires"));
            return null;
        });
    }

    @Test(enabled = true)
    public void test1() {
        final RsStorageClient client = client();
        client.authenticateUser(response -> null);
        final String containerName = client.getClass().getPackage().getName();
        final String objectName = client.getClass().getName().replaceAll("\\.", "/");
        client.updateContainer(
                containerName,
                response -> {
                    final StatusType status = response.getStatusInfo();
                    final int statusCode = status.getStatusCode();
                    final String reasonPhrase = status.getReasonPhrase();
                    return null;
                });
        final Random random = new SecureRandom();
        final byte[] bytes = new byte[random.nextInt(1024)];
        random.nextBytes(bytes);
        final Entity entity = Entity.entity(
                bytes, MediaType.APPLICATION_OCTET_STREAM);
        client.updateObject(
                containerName, objectName, null, entity,
                response -> {
                    final StatusType status = response.getStatusInfo();
                    final int statusCode = status.getStatusCode();
                    final String reasonPhrase = status.getReasonPhrase();
                    assertEquals(statusCode, Status.CREATED.getStatusCode());
                    return null;
                });
        client.readObject(containerName, objectName, null, response -> {
                      final StatusType status = response.getStatusInfo();
                      final int statusCode = status.getStatusCode();
                      final String reasonPhrase = status.getReasonPhrase();
                      assertEquals(statusCode, Status.OK.getStatusCode());
                      final byte[] b = response.readEntity(byte[].class);
                      assertEquals(b, bytes);
                      return null;
                  });
        client.deleteObject(
                containerName, objectName, null, response -> {
                    final StatusType status = response.getStatusInfo();
                    final int statusCode = status.getStatusCode();
                    final String reasonPhrase = status.getReasonPhrase();
                    assertEquals(statusCode, Status.NO_CONTENT.getStatusCode());
                    return null;
                });
        client.deleteContainer(
                containerName,
                response -> {
                    final StatusType status = response.getStatusInfo();
                    final int statusCode = status.getStatusCode();
                    final String reasonPhrase = status.getReasonPhrase();
                    assertEquals(statusCode, Status.NO_CONTENT.getStatusCode());
                    return null;
                });
    }
}
