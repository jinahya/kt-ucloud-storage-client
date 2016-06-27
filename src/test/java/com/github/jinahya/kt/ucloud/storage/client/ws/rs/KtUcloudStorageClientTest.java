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

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import org.slf4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.Test;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class KtUcloudStorageClientTest {

    private static final Logger logger
            = getLogger(KtUcloudStorageClientTest.class);

    private static final String TEST_CONTAINER_NAME
            = "kt-ucloud-storage-client-test-container";

    private static Response _authenticateUser() {
        final String xAuthUrl = System.getProperty("xAuthUrl");
        if (xAuthUrl == null) {
            throw new SkipException("missing property; xAuthUrl");
        }
        final String xAuthUser = System.getProperty("xAuthUser");
        if (xAuthUser == null) {
            throw new SkipException("missing property; xAuthUser");
        }
        final String xAuthPass = System.getProperty("xAuthPass");
        if (xAuthPass == null) {
            throw new SkipException("missing property; xAuthPass");
        }
        final Response response = new KtUcloudStorageClient()
                .authenticateUser(xAuthUrl, xAuthUser, xAuthPass, false);
        final StatusType statusType = response.getStatusInfo();
        final int statusCode = statusType.getStatusCode();
        logger.debug("statusCode: {}", statusCode);
        final String reasonPhrase = statusType.getReasonPhrase();
        logger.debug("reasonPhrase: {}", reasonPhrase);
        return response;
    }

    @Test(enabled = true)
    public void authenticateUser() {
        final Response response = _authenticateUser();
        logger.debug("xStorageUrl: {}",
                     response.getHeaderString("X-Storage-Url"));
        logger.debug("xAuthToken: {}",
                     response.getHeaderString("X-Auth-Token"));
        logger.debug("xAuthTokenExpires: {}",
                     response.getHeaderString("X-Auth-Token-Expires"));
    }

    @Test(enabled = false)
    public void updateContainer() {
        final String xStorageUrl;
        final String xAuthToken;
        {
            final Response response = _authenticateUser();
            assertEquals(response.getStatus(), 200);
            xStorageUrl = response.getHeaderString("X-Storage-Url");
            xAuthToken = response.getHeaderString("X-Auth-Token");
        }
        final Response response = new KtUcloudStorageClient().updateContainer(
                xStorageUrl, xAuthToken, TEST_CONTAINER_NAME);
        logger.debug("reasonPhrase: {}", response.getStatusInfo().getReasonPhrase());
        assertTrue(response.getStatus() == 200 || response.getStatus() == 202);
    }
}
