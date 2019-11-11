/*
 * Copyright (C) 2019 sea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.srsea.torque.common;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Map utilities.
 *
 * @author sea
 */
public class Maps {

    /**
     * Creates a wrapper of {@code HashMap} to put values.
     *
     * @param <K> type of key
     * @param <V> type of value
     * @return a wrapper of {@code HashMap} to put values
     */
    public static <K, V> MapWrapper<K, V, HashMap<K, V>> hashMap() {
        return new MapWrapper<>(new HashMap<K, V>());
    }

    /**
     * Creates a wrapper of {@code TreeMap} to put values.
     *
     * @param <K> type of key
     * @param <V> type of value
     * @return a wrapper of {@code TreeMap} to put values
     */
    public static <K extends Comparable, V> MapWrapper<K, V, TreeMap<K, V>> treeMap() {
        return new MapWrapper<>(new TreeMap<K, V>());
    }

    /**
     * Creates a wrapper of the specific map to put values.
     *
     * @param <K> type of key
     * @param <V> type of value
     * @return a wrapper of the specific map to put values
     */
    public static <K, V, M extends Map<K, V>> MapWrapper<K, V, M> of(M map) {
        return new MapWrapper<>(map);
    }

    /**
     * A wrapper of map for chained calling {@link Map#put(Object, Object)}.
     *
     * @param <K> type of key
     * @param <V> type of value
     * @param <M> type of map
     */
    public static class MapWrapper<K, V, M extends Map<K, V>> {

        /**
         * The map to put values.
         */
        private M map;

        /**
         * Constructs an instance with the specific map.
         *
         * @param map the specific map
         */
        public MapWrapper(M map) {
            this.map = map;
        }

        /**
         * Puts the value into inner map.
         *
         * @param key   key with which the specified value is to be associated
         * @param value value to be associated with the specified key
         * @return current map wrapper
         */
        public MapWrapper<K, V, M> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        /**
         * Gets the inner map.
         *
         * @return inner map
         */
        public M get() {
            return map;
        }
    }
}
