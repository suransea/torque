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
import java.util.NoSuchElementException;

public class Flatten<T> extends Sequence<T> {
    private final Iterable<? extends Iterable<? extends T>> iterable;

    public Flatten(Iterable<? extends Iterable<? extends T>> iterable) {
        this.iterable = iterable;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<? extends Iterable<? extends T>> iterator = iterable.iterator();
            Iterator<? extends T> inner;

            @Override
            public boolean hasNext() {
                if (inner != null && inner.hasNext()) {
                    return true;
                }
                if (!iterator.hasNext()) {
                    return false;
                }
                inner = iterator.next().iterator();
                return hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return inner.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
