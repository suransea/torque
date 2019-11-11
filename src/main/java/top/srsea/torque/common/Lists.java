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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * List utilities.
 *
 * @author sea
 */
public class Lists {

    /**
     * Creates a {@code ArrayList} contains the specific items, the initial capacity is length of items.
     *
     * @param items the specific items
     * @param <T>   type of item
     * @return {@code ArrayList} contains the specific items
     */
    @SafeVarargs
    public static <T> List<T> of(T... items) {
        List<T> list = new ArrayList<>(items.length);
        Collections.addAll(list, items);
        return list;
    }
}
