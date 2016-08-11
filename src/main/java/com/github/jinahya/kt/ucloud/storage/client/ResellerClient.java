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
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public abstract class ResellerClient extends StorageClient {

    // -------------------------------------------------------------------------
    public ResellerClient(final String authUrl, final String authUser,
                          final String authKey, final String authAccount) {
        super(authUrl, authUser, authKey);
        this.authAccount = requireNonNull(authAccount, "authAccount");
    }

    // -------------------------------------------------------------------------
    protected final String authAccount;
}
