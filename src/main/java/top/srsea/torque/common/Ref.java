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

public final class Ref<T> implements Iterable<T> {
    private T value;

    public Ref() {
        this.value = null;
    }

    public Ref(T value) {
        this.value = value;
    }

    public static <T> Ref<T> of(T value) {
        return new Ref<>(value);
    }

    public static <T> Ref<T> ofNull() {
        return new Ref<>();
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return Iterators.singleton(value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Ref(" + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Ref)) {
            return false;
        }
        return Objects.equals(value, ((Ref<?>) obj).value);
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return false;
    }

    public int size() {
        return 1;
    }

    public boolean contains(T elem) {
        return Objects.equals(value, elem);
    }

    public void foreach(Consumer<? super T> action) {
        action.accept(value);
    }

    public Ref<T> onEach(Consumer<? super T> action) {
        foreach(action);
        return this;
    }

    public <U> Ref<U> map(Function1<? super T, ? extends U> mapper) {
        return new Ref<>(mapper.invoke(value));
    }

    public <U> Ref<U> flatMap(Function1<? super T, ? extends Ref<U>> mapper) {
        return mapper.invoke(value);
    }

    public <U, V> Ref<V> zip(Ref<U> that, Function2<? super T, ? super U, V> zipper) {
        return new Ref<>(zipper.invoke(value, that.value));
    }

    public <U> U fold(Function1<? super T, ? extends U> operation) {
        return operation.invoke(value);
    }
}
