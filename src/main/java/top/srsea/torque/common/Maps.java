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

public class Maps {
    @SafeVarargs
    public static <K> Keys<K> keys(K... keys) {
        return new Keys<>(keys);
    }

    public static class Keys<K> {
        private K[] keys;

        private Keys(K[] keys) {
            this.keys = keys;
        }

        @SafeVarargs
        public final <V> Map<K, V> values(V... values) {
            Conditions.require(keys.length == values.length, "values count not matches.");
            Map<K, V> map = new HashMap<>(keys.length);
            for (int i = 0; i < keys.length; ++i) {
                map.put(keys[i], values[i]);
            }
            return map;
        }
    }
}
