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

import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@XmlRootElement
public class ContainerInfo {

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "objectCount=" + objectCount
               + ", bytesUsed=" + bytesUsed
               + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectCount, bytesUsed);
    }

    @Override
    public boolean equals(final Object obj) {
        if (true) {
            return equalsAs(obj) && getClass() == obj.getClass();
        }
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContainerInfo other = (ContainerInfo) obj;
        if (objectCount != other.objectCount) {
            return false;
        }
        if (bytesUsed != other.bytesUsed) {
            return false;
        }
        return true;
    }

    protected boolean equalsAs(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(getClass().isAssignableFrom(obj.getClass()))) {
            return false;
        }
        final ContainerInfo other = (ContainerInfo) obj;
        if (objectCount != other.objectCount) {
            return false;
        }
        if (bytesUsed != other.bytesUsed) {
            return false;
        }
        return true;
    }

    // ------------------------------------------------------------- objectCount
    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(final int objectCount) {
        this.objectCount = objectCount;
    }

    public ContainerInfo objectCount(final int objectCount) {
        setObjectCount(objectCount);
        return this;
    }

    // --------------------------------------------------------------- bytesUsed
    public long getBytesUsed() {
        return bytesUsed;
    }

    public void setBytesUsed(final long bytesUsed) {
        this.bytesUsed = bytesUsed;
    }

    public ContainerInfo bytesUsed(final long bytesUsed) {
        setBytesUsed(bytesUsed);
        return this;
    }

    // -------------------------------------------------------------------------
    @XmlElement(required = true)
    private int objectCount;

    @XmlElement(required = true)
    private long bytesUsed;
}
