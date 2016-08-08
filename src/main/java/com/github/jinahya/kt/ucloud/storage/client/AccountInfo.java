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
package com.github.jinahya.kt.ucloud.storage.client;

import java.net.URLConnection;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@XmlRootElement
public class AccountInfo extends ContainerInfo {

    public static AccountInfo newInstance(final Response response) {
        final AccountInfo instance = newInstance(new AccountInfo(), response);
        instance.containerCount = Integer.parseInt(response.getHeaderString(
                StorageClient.HEADER_X_ACCOUNT_CONTAINER_COUNT));
        return instance;
    }

    public static AccountInfo newInstance(final URLConnection connection) {
        final AccountInfo instance = newInstance(new AccountInfo(), connection);
        instance.containerCount = Integer.parseInt(connection.getHeaderField(
                StorageClient.HEADER_X_ACCOUNT_CONTAINER_COUNT));
        return instance;
    }

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "containerCount=" + containerCount
               + "}";
    }

    // ------------------------------------------------------------- objectCount
    @Override
    public AccountInfo objectCount(final int objectCount) {
        return (AccountInfo) super.objectCount(objectCount);
    }

    // --------------------------------------------------------------- bytesUsed
    @Override
    public ContainerInfo bytesUsed(final long bytesUsed) {
        return (AccountInfo) super.bytesUsed(bytesUsed);
    }

    // ---------------------------------------------------------- containerCount
    public int getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(final int containerCount) {
        this.containerCount = containerCount;
    }

    public AccountInfo containerCount(final int containerCount) {
        setContainerCount(containerCount);
        return this;
    }

    // -------------------------------------------------------------------------
    @XmlElement(required = true)
    private int containerCount;
}
