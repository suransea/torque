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
import top.srsea.torque.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A optional value.
 */
public abstract class Option<T> implements Iterable<T> {

    private Option() {
    }

    /**
     * Returns Some(value), or None if the value is null.
     */
    public static <T> Option<T> from(@Nullable T value) {
        if (value == null) {
            return none();
        }
        return some(value);
    }

    /**
     * Returns Some(value).
     */
    public static <T> Option<T> some(@Nonnull T value) {
        return new Some<>(value);
    }

    /**
     * Returns the None.
     */
    public static <T> Option<T> none() {
        @SuppressWarnings("unchecked")
        Option<T> none = (Option<T>) None.INSTANCE;
        return none;
    }

    /**
     * Returns the nested option value if it is not empty.
     * Otherwise returns None.
     */
    public static <T> Option<T> flatten(Option<Option<T>> option) {
        return option.isEmpty() ? Option.<T>none() : option.get();
    }

    /**
     * Returns this option's value.
     *
     * @throws java.util.NoSuchElementException if empty
     */
    @Nonnull
    public abstract T get();

    /**
     * Returns this option's value or the default value if empty.
     */
    public T or(T defaultValue) {
        return isEmpty() ? defaultValue : get();
    }

    /**
     * Returns this option's value or the value from supplier if empty.
     */
    public T or(Supplier<? extends T> supplier) {
        return isEmpty() ? supplier.get() : get();
    }

    /**
     * Returns this option or the given option if empty.
     */
    public Option<T> or(Option<T> that) {
        return isEmpty() ? that : this;
    }

    /**
     * Returns this option's value or null if empty.
     */
    @Nullable
    public T orNull() {
        return isEmpty() ? null : get();
    }

    /**
     * Returns this option or the option from supplier if empty.
     */
    public Option<T> orElse(Supplier<? extends Option<T>> supplier) {
        return isEmpty() ? supplier.get() : this;
    }

    /**
     * Returns Some(mapper(value)) or the None if empty.
     */
    public <U> Option<U> map(Function1<? super T, ? extends U> mapper) {
        return isEmpty() ? Option.<U>none() : some(mapper.invoke(get()));
    }

    /**
     * Returns mapper(value) or the None if empty.
     */
    public <U> Option<U> flatMap(Function1<? super T, ? extends Option<U>> mapper) {
        return isEmpty() ? Option.<U>none() : mapper.invoke(get());
    }

    /**
     * Returns this option if it is not empty and pred(value) returns ture.
     * Otherwise returns the None.
     */
    public Option<T> filter(Function1<? super T, Boolean> pred) {
        return isEmpty() || pred.invoke(get()) ? this : Option.<T>none();
    }

    /**
     * Returns this option if it is not empty and pred(value) returns false.
     * Otherwise returns the None.
     */
    public Option<T> filterNot(Function1<? super T, Boolean> pred) {
        return isEmpty() || !pred.invoke(get()) ? this : Option.<T>none();
    }

    /**
     * Returns false if this is empty or pred(value) returns false.
     * Otherwise returns true.
     */
    public boolean any(Function1<? super T, Boolean> pred) {
        if (isEmpty()) {
            return false;
        }
        return pred.invoke(get());
    }

    /**
     * Returns true if this is empty or pred(value) returns true.
     * Otherwise returns false.
     */
    public boolean all(Function1<? super T, Boolean> pred) {
        if (isEmpty()) {
            return true;
        }
        return pred.invoke(get());
    }

    /**
     * Returns true if this is empty or pred(value) returns false.
     * Otherwise returns false.
     */
    public boolean none(Function1<? super T, Boolean> pred) {
        if (isEmpty()) {
            return true;
        }
        return !pred.invoke(get());
    }

    /**
     * Returns Some(zipper(this.value, that.value)) or the None if this or that empty.
     */
    public <U, V> Option<V> zip(Option<U> that, Function2<? super T, ? super U, V> zipper) {
        if (isEmpty() || that.isEmpty()) {
            return none();
        }
        return some(zipper.invoke(get(), that.get()));
    }

    /**
     * Returns operation(initialValue, value) or initialValue if empty.
     */
    public <U> U fold(U initialValue, Function1<? super T, ? extends U> operation) {
        return isEmpty() ? initialValue : operation.invoke(get());
    }

    /**
     * Performs the given action for value if not empty.
     */
    public void foreach(Consumer<? super T> action) {
        if (isNotEmpty()) {
            action.accept(get());
        }
    }

    /**
     * Performs the given action for value if not empty, and return this option.
     */
    public Option<T> onEach(Consumer<? super T> action) {
        foreach(action);
        return this;
    }

    /**
     * Tests whether this option contains a given value as an element.
     */
    public boolean contains(T elem) {
        return isNotEmpty() && get().equals(elem);
    }

    /**
     * Returns 1 for a Some, 0 for the None.
     */
    public int size() {
        return isEmpty() ? 0 : 1;
    }

    /**
     * Returns if this option is None.
     */
    public abstract boolean isEmpty();

    /**
     * Returns if this option is Some.
     */
    public boolean isNotEmpty() {
        return !isEmpty();
    }


    /**
     * `Some` represents existing values of type
     *
     * @param <T> value type
     */
    public static final class Some<T> extends Option<T> {
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
            return Iterators.singleton(value);
        }
    }


    /**
     * `None` represents non-existent values
     */
    public static final class None extends Option<Object> {
        public static final None INSTANCE = new None();

        private None() {
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "None";
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof None;
        }

        @Nonnull
        @Override
        public Object get() {
            throw new NoSuchElementException("None.get");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Nonnull
        @Override
        public Iterator<Object> iterator() {
            return Collections.emptyIterator();
        }
    }
}
