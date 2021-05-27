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
import java.util.NoSuchElementException;

public class Filter<T> extends Sequence<T> {
    private final Sequence<T> sequence;
    private final Function<? super T, Boolean> predicate;

    public Filter(Sequence<T> sequence, Function<? super T, Boolean> predicate) {
        this.sequence = sequence;
        this.predicate = predicate;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<T> iterator = sequence.iterator();
            T next;
            boolean hasNext;
            boolean nextEvaluated = false;

            private void evalNext() {
                if (nextEvaluated) {
                    return;
                }
                if (!iterator.hasNext()) {
                    hasNext = false;
                    return;
                }
                next = iterator.next();
                if (!predicate.invoke(next)) {
                    evalNext();
                    return;
                }
                hasNext = true;
                nextEvaluated = true;
            }

            @Override
            public boolean hasNext() {
                evalNext();
                return hasNext;
            }

            @Override
            public T next() {
                evalNext();
                if (!hasNext) {
                    throw new NoSuchElementException();
                }
                nextEvaluated = false;
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
