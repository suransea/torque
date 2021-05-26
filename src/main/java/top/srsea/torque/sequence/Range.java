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

public class Range<T> extends Sequence<T> {
    private final T begin, end;
    private final Function<? super T, ? extends T> successor;

    public Range(T begin, T end, Function<? super T, ? extends T> successor) {
        this.begin = begin;
        this.end = end;
        this.successor = successor;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            T current = begin;

            @Override
            @SuppressWarnings("unchecked")
            public boolean hasNext() {
                if (current instanceof Comparable) {
                    return ((Comparable<T>) current).compareTo(end) < 0;
                }
                return !current.equals(end);
            }

            @Override
            public T next() {
                T result = current;
                current = successor.invoke(current);
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
