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
package com.github.jinahya.kt.ucloud.storage.client.ws.rs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@XmlRootElement
public class AccountInfo extends ContainerInfo {

//    public static AccountInfo newInstance(final MultivaluedMap<String, Object> headers) {
//        final AccountInfo instance = new AccountInfo();
//        instance.containerCount = ofNullable(headers.getFirst(StorageClient.HEADER_X_ACCOUNT_CONTAINER_COUNT))
//                .map(Object::toString)
//                .map(Integer::parseInt)
//                .orElseThrow(() -> {
//                    return new IllegalArgumentException(
//                            "header not found: " + StorageClient.HEADER_X_ACCOUNT_CONTAINER_COUNT);
//                });
//    }
//    public static AccountInfo newInstance(final Response response) {
//    }
    @Override
    public String toString() {
        return super.toString() + "{"
               + "containerCount=" + containerCount
               + "}";
    }

    // ---------------------------------------------------------- containerCount
    public int getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(final int containerCount) {
        this.containerCount = containerCount;
    }

    // -------------------------------------------------------------------------
    @XmlElement(required = true)
    private int containerCount;
}
