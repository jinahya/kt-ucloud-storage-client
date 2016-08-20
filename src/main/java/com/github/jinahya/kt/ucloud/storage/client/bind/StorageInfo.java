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
package com.github.jinahya.kt.ucloud.storage.client.bind;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@XmlRootElement
public class StorageInfo extends ContainerInfo {

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "containerCount=" + containerCount
               + "}";
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 89 * hash + containerCount;
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (true) {
            return equalsAs(obj) && getClass() == obj.getClass();
        }
        if (!super.equalsAs(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StorageInfo other = (StorageInfo) obj;
        if (containerCount != other.containerCount) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean equalsAs(final Object obj) {
        if (!super.equalsAs(obj)) {
            return false;
        }
        if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        final StorageInfo other = (StorageInfo) obj;
        if (containerCount != other.containerCount) {
            return false;
        }
        return true;
    }

    // ------------------------------------------------------------- objectCount
    @Override
    public StorageInfo objectCount(final int objectCount) {
        return (StorageInfo) super.objectCount(objectCount);
    }

    // --------------------------------------------------------------- bytesUsed
    @Override
    public ContainerInfo bytesUsed(final long bytesUsed) {
        return (StorageInfo) super.bytesUsed(bytesUsed);
    }

    // ---------------------------------------------------------- containerCount
    public int getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(final int containerCount) {
        this.containerCount = containerCount;
    }

    public StorageInfo containerCount(final int containerCount) {
        setContainerCount(containerCount);
        return this;
    }

    // -------------------------------------------------------------------------
    @XmlElement(required = true)
    private int containerCount;
}
