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

import top.srsea.torque.common.Preconditions;

import java.util.Iterator;

public class Drop<T> extends Sequence<T> {
    private final Sequence<T> sequence;
    private final int count;

    public Drop(Sequence<T> sequence, int count) {
        Preconditions.require(count >= 0, "count < 0");
        this.sequence = sequence;
        this.count = count;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<T> iterator = sequence.iterator();
            int left = count;

            private void drop() {
                while (left > 0 && iterator.hasNext()) {
                    iterator.next();
                    --left;
                }
            }

            @Override
            public boolean hasNext() {
                drop();
                return iterator.hasNext();
            }

            @Override
            public T next() {
                drop();
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
