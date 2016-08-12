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

import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.headers;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import java.io.IOException;
import static java.util.Arrays.asList;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.body;
import static com.github.jinahya.kt.ucloud.storage.client.ws.rs.WsRsClientIT.status;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Test(dependsOnGroups = {"ws.rs.account"}, groups = {"ws.rs.container"})
public class WsRsClientContainerIT extends WsRsClientIT {

    private static final Logger logger
            = getLogger(WsRsClientContainerIT.class);

    @Test
    public void updateContainer() {
        logger.debug("--------------------------------- updating container...");
        accept(c -> c.updateContainer(
                containerName,
                null, // params
                null, // headers
                r -> {
                    status(r, SUCCESSFUL, CREATED, ACCEPTED);
                    headers(r);
                }
        ));
    }

    @Test
    public void peekContainer() {
        logger.debug("---------------------------------- peeking container...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        headers.putSingle(ACCEPT, TEXT_PLAIN);
        accept(c -> c.peekContainer(
                containerName,
                null,
                headers,
                r -> {
                    status(r, null, NOT_FOUND, NO_CONTENT);
                    headers(r);
                }
        ));
    }

    @Test
    public void readContainer() {
        logger.debug("---------------------------------- reading container...");
        final MultivaluedMap<String, Object> headers
                = new MultivaluedHashMap<>();
        asList(TEXT_PLAIN, APPLICATION_XML, APPLICATION_JSON)
                .forEach(mediaType -> {
                    logger.debug("accepting " + mediaType);
                    headers.putSingle(ACCEPT, mediaType);
                    accept(c -> c.readContainer(
                            containerName,
                            null,
                            headers,
                            r -> {
                                status(r, null, NOT_FOUND, OK, NO_CONTENT);
                                try {
                                    body(r);
                                } catch (final IOException ioe) {
                                    fail("failed to read container", ioe);
                                }
                            }
                    ));
                });
    }

    @Test
    public void readContainerObjectNames() {
        logger.debug("--------------------- reading container object names...");
        accept(c -> c.readContainerObjectNames(
                containerName,
                null,
                null,
                r -> r.getStatus() == OK.getStatusCode(),
                l -> {
                    logger.debug("object name: {}", l);
                }
        ));
    }

    @Test(dependsOnMethods = {"readContainerObjectNames"})
    public void deleteContainer() {
        logger.debug("--------------------------------- deleting container...");
        accept(c -> c.deleteContainer(
                containerName,
                null,
                null,
                r -> {
                    status(r, SUCCESSFUL, NO_CONTENT);
                }
        ));
    }

    @Test
    public void all() {
        {
            logger.debug("----------------------------- creating container...");
            accept(c -> c.updateContainer(
                    containerName,
                    null, // params
                    null, // headers
                    r -> {
                        status(r, SUCCESSFUL, CREATED, ACCEPTED);
                        headers(r);
                    }
            ));
        }
        {
            logger.debug("------------------------------ peeking container...");
            final MultivaluedMap<String, Object> headers
                    = new MultivaluedHashMap<>();
            headers.putSingle(ACCEPT, TEXT_PLAIN);
            accept(c -> c.peekContainer(
                    containerName,
                    null,
                    headers,
                    r -> {
                        status(r, SUCCESSFUL, NO_CONTENT);
                        headers(r);
                        assertNotNull(r.getHeaderString(
                                WsRsClient.HEADER_X_CONTAINER_OBJECT_COUNT));
                        assertNotNull(r.getHeaderString(
                                WsRsClient.HEADER_X_CONTAINER_BYTES_USED));
                    }
            ));
        }
        {
            logger.debug("------------------------------ reading container...");
            final MultivaluedMap<String, Object> headers
                    = new MultivaluedHashMap<>();
            asList(TEXT_PLAIN, APPLICATION_XML, APPLICATION_JSON)
                    .forEach(mediaType -> {
                        logger.debug("accepting " + mediaType);
                        headers.putSingle(ACCEPT, mediaType);
                        accept(c -> c.readContainer(
                                containerName,
                                null,
                                headers,
                                r -> {
                                    status(r, SUCCESSFUL, OK, NO_CONTENT);
                                    try {
                                        body(r);
                                    } catch (final IOException ioe) {
                                        fail("failed to read container", ioe);
                                    }
                                }
                        ));
                    });
        }
        {
            logger.debug("----------------- reading container object names...");
            accept(c -> c.readContainerObjectNames(
                    containerName,
                    null,
                    null,
                    r -> r.getStatus() == OK.getStatusCode(),
                    l -> {
                        logger.debug("object name: {}", l);
                    }
            ));
        }
        {
            logger.debug("----------------------------- deleting container...");
            accept(c -> c.deleteContainer(
                    containerName,
                    null,
                    null,
                    r -> {
                        status(r, SUCCESSFUL, NO_CONTENT);
                    }
            ));
        }
    }

    private final String containerName = getClass().getName();
}
