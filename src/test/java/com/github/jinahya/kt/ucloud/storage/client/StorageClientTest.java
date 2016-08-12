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

import static java.lang.System.currentTimeMillis;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @param <T>
 */
public abstract class StorageClientTest<T extends StorageClient> {

    public StorageClientTest(final Class<T> clientClass) {
        super();

        this.clientClass = clientClass;
    }

    protected T create() {
        try {
            return clientClass.getConstructor(
                    String.class,
                    String.class,
                    String.class)
                    .newInstance("test", "test", "test");
        } catch (final ReflectiveOperationException roe) {
            fail("failed to create instance", roe);
            throw new RuntimeException("failed to create an instance", roe);
        }
    }

    @Test
    public void assertANewlyCreatedInstanceIsNotValid() {
        assertFalse(create().isValid(currentTimeMillis()));
    }

    protected final Class<T> clientClass;
}
