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
 * Map 工具
 */
public class Maps {
    public static <K, V> MapWrapper<K, V, HashMap<K, V>> hashMap() {
        return new MapWrapper<>(new HashMap<K, V>());
    }

    public static <K extends Comparable, V> MapWrapper<K, V, TreeMap<K, V>> treeMap() {
        return new MapWrapper<>(new TreeMap<K, V>());
    }

    public static <K, V, M extends Map<K, V>> MapWrapper<K, V, M> of(M map) {
        return new MapWrapper<>(map);
    }

    public static class MapWrapper<K, V, M extends Map<K, V>> {
        private M map;

        public MapWrapper(M map) {
            this.map = map;
        }

        public MapWrapper<K, V, M> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public M get() {
            return map;
        }
    }
}
