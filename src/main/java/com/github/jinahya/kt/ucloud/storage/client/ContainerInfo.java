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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@XmlRootElement
public class ContainerInfo {

    protected static <T extends ContainerInfo> T newInstance(
            final T instance, final Response response) {
        final StatusType statusInfo
                = requireNonNull(response, "null response").getStatusInfo();
        if (!Family.SUCCESSFUL.equals(statusInfo.getFamily())) {
            throw new IllegalArgumentException(
                    "response.statusInfo.family != " + Family.SUCCESSFUL);
        }
        final MultivaluedMap<String, Object> headers = response.getHeaders();
        instance.setObjectCount(ofNullable(headers.getFirst(
                StorageClient.HEADER_X_ACCOUNT_OBJECT_COUNT))
                .map(Object::toString)
                .map(Integer::parseInt)
                .orElseThrow(() -> {
                    return new IllegalArgumentException(
                            "header not found: "
                            + StorageClient.HEADER_X_ACCOUNT_OBJECT_COUNT);
                }));
        instance.setBytesUsed(ofNullable(headers.getFirst(
                StorageClient.HEADER_X_ACCOUNT_BYTES_USED))
                .map(Object::toString)
                .map(Long::parseLong)
                .orElseThrow(() -> {
                    return new IllegalArgumentException(
                            "header not found: "
                            + StorageClient.HEADER_X_ACCOUNT_BYTES_USED);
                }));
        return instance;
    }

    public static ContainerInfo newInstance(final Response response) {
        return newInstance(new ContainerInfo(), response);
    }

    @Override
    public String toString() {
        return super.toString() + "{"
               + "objectCount=" + objectCount
               + ", bytesUsed=" + bytesUsed
               + "}";
    }

    // ------------------------------------------------------------- objectCount
    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(final int objectCount) {
        this.objectCount = objectCount;
    }

    // --------------------------------------------------------------- bytesUsed
    public long getBytesUsed() {
        return bytesUsed;
    }

    public void setBytesUsed(final long bytesUsed) {
        this.bytesUsed = bytesUsed;
    }

    // -------------------------------------------------------------------------
    @XmlElement(required = true)
    private int objectCount;

    @XmlElement(required = true)
    private long bytesUsed;
}
