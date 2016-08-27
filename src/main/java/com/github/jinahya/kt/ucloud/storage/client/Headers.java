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

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class Headers {

    public Map<String, List<Object>> toMap() {
        return new HashMap<>(map());
    }

    /**
     * Removes all headers whose field name is equals to given.
     *
     * @param name the field name.
     */
    public void remove(final String name) {
        for (final Iterator<String> i = map().keySet().iterator();
             i.hasNext();) {
            if (i.next().equalsIgnoreCase(name)) {
                i.remove();
            }
        }
    }

    public void put(final String name, final List<Object> values) {
        remove(name);
        map().put(name, new ArrayList<>(values));
    }

    public void add(final String name,
                    final Collection<? extends Object> values) {
        List<Object> existing = map().get(name.toLowerCase());
        if (existing == null) {
            existing = new ArrayList<>();
            put(name, existing);
        }
        existing.addAll(values);
    }

    public void add(final String name, final Object... values) {
        add(name, asList(values));
    }

    // --------------------------------------------------------------------- map
    private Map<String, List<Object>> map() {
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    // -------------------------------------------------------------------------
    private Map<String, List<Object>> map;
}
