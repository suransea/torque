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

import top.srsea.torque.function.Consumer;
import top.srsea.torque.function.Function1;
import top.srsea.torque.function.Function2;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Objects;

public final class Val<T> implements Iterable<T> {
    @Nonnull
    private final T value;

    public Val(@Nonnull T value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static <T> Val<T> of(T value) {
        return new Val<>(value);
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return Iterators.singleton(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "Val(" + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Val)) {
            return false;
        }
        return value.equals(((Val<?>) obj).value);
    }

    @Nonnull
    public T get() {
        return value;
    }

    public boolean contains(T elem) {
        return value.equals(elem);
    }

    public Val<T> onEach(Consumer<? super T> action) {
        foreach(action);
        return this;
    }

    public void foreach(Consumer<? super T> action) {
        action.accept(value);
    }

    public <U> Val<U> map(Function1<? super T, ? extends U> mapper) {
        return new Val<>(mapper.invoke(value));
    }

    public <U> Val<U> flatMap(Function1<? super T, ? extends Val<U>> mapper) {
        return mapper.invoke(value);
    }

    public <U, V> Val<V> zip(Val<U> that, Function2<? super T, ? super U, V> zipper) {
        return new Val<>(zipper.invoke(value, that.value));
    }

    public <U> U fold(Function1<? super T, ? extends U> operation) {
        return operation.invoke(value);
    }
}
