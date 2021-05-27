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

import java.util.Iterator;

public class Concat<T, U extends T, V extends T> extends Sequence<T> {
    private final Sequence<U> sequence;
    private final Iterable<V> other;

    public Concat(Sequence<U> sequence, Iterable<V> other) {
        this.sequence = sequence;
        this.other = other;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<U> iterator = sequence.iterator();
            Iterator<V> otherIterator;

            private Iterator<V> otherIterator() {
                if (otherIterator == null) {
                    otherIterator = other.iterator();
                }
                return otherIterator;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext() || otherIterator().hasNext();
            }

            @Override
            public T next() {
                return iterator.hasNext() ? iterator.next() : otherIterator().next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
