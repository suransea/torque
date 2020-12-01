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

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * `Some` represents existing values of type
 *
 * @param <T> value type
 */
public final class Some<T> extends Option<T> {
    @Nonnull
    private final T value;

    /**
     * Constructs an Some with the value.
     */
    public Some(@Nonnull T value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "Some(" + value.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Some)) {
            return false;
        }
        return value.equals(((Some<?>) obj).value);
    }

    @Nonnull
    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public T next() {
                if (hasNext) {
                    hasNext = false;
                    return value;
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
