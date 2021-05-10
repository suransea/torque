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

import top.srsea.torque.function.Function1;
import top.srsea.torque.function.Supplier;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Iterators {

    public static <E> Iterator<E> empty() {
        return Collections.emptyIterator();
    }

    public static <E> Iterator<E> singleton(final E e) {
        return new Iterator<E>() {
            private boolean hasNext = true;

            public boolean hasNext() {
                return hasNext;
            }

            public E next() {
                if (hasNext) {
                    hasNext = false;
                    return e;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @SafeVarargs
    public static <E> Iterator<E> of(final E... elems) {
        return new Iterator<E>() {
            private int cursor = 0;

            public boolean hasNext() {
                return cursor != elems.length;
            }

            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elems[cursor++];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <E> Iterator<E> generate(final E init, final Function1<E, Boolean> cond, final Function1<E, E> iterate) {
        return new Iterator<E>() {
            private E next;
            private boolean hasNext;
            private boolean first = true;
            private boolean nextEvaluated = false;

            private void evalNext() {
                if (nextEvaluated) {
                    return;
                }
                if (first) {
                    next = init;
                    first = false;
                } else {
                    next = iterate.invoke(next);
                }
                hasNext = cond.invoke(next);
                nextEvaluated = true;
            }

            @Override
            public boolean hasNext() {
                evalNext();
                return hasNext;
            }

            @Override
            public E next() {
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

    public static <E> Iterator<E> generate(final E init, final Function1<E, E> iterate) {
        return new Iterator<E>() {
            private E next;
            private boolean first = true;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public E next() {
                if (first) {
                    first = false;
                    next = init;
                    return next;
                }
                next = iterate.invoke(next);
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <E> Iterator<E> generate(final Supplier<E> iterate) {
        return new Iterator<E>() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public E next() {
                return iterate.get();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
