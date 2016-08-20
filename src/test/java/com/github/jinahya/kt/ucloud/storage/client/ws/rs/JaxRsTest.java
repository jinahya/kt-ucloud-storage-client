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

import java.lang.reflect.Modifier;
import java.util.stream.Stream;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class JaxRsTest {

    private static final Logger logger = getLogger(JaxRsTest.class);

    @Test
    public void getParameters() {
        Stream.of(MediaType.class.getFields())
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> MediaType.class.isAssignableFrom(f.getType()))
                .map(f -> {
                    try {
                        return (MediaType) f.get(null);
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException(
                                "failed to get static field value", roe);
                    }
                })
                .forEach(t -> {
                    logger.debug("mediaType: {}", t);
                    t.getParameters().forEach((k, v) -> {
                        logger.debug("parameter: {}: {}", k, v);
                    });
                });
    }
}
