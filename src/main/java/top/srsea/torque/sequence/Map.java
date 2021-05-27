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

package top.srsea.torque.sequence;

import top.srsea.torque.function.Function;

import java.util.Iterator;

public class Map<T, U> extends Sequence<U> {
    private final Sequence<T> sequence;
    private final Function<? super T, ? extends U> transform;

    public Map(Sequence<T> sequence, Function<? super T, ? extends U> transform) {
        this.sequence = sequence;
        this.transform = transform;
    }

    @Override
    public Iterator<U> iterator() {
        return new Iterator<U>() {
            final Iterator<T> iterator = sequence.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public U next() {
                return transform.invoke(iterator.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
