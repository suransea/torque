/*
 * Copyright (C) 2020 sea
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
import top.srsea.torque.function.Function;
import top.srsea.torque.function.Supplier;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Result represents a value or an error.
 */
public abstract class Result<T> {

    private Result() {
    }

    /**
     * Returns Success(value) from the callable,
     * or Failure(error) if an error happened.
     */
    public static <T> Result<T> from(@Nonnull Callable<T> callable) {
        try {
            return success(callable.call());
        } catch (Throwable e) {
            return failure(e);
        }
    }

    /**
     * Returns Success(value).
     */
    public static <T> Result<T> success(@Nonnull T value) {
        return new Success<>(value);
    }

    /**
     * Returns Failure(error).
     */
    public static <T> Result<T> failure(@Nonnull Throwable error) {
        return new Failure<>(error);
    }

    /**
     * Returns if the result is Success.
     */
    public abstract boolean isSuccess();

    /**
     * Returns if the result is Failure.
     */
    public boolean isFailure() {
        return !isSuccess();
    }

    /**
     * Returns the result's value option.
     */
    public Option<T> value() {
        return isSuccess() ? Option.some(requireValue()) : Option.<T>none();
    }

    /**
     * Returns the result's error option.
     */
    public Option<Throwable> error() {
        return isSuccess() ? Option.<Throwable>none() : Option.some(requireError());
    }

    /**
     * Returns the result's value.
     *
     * @throws E if the result is Failure.
     *           This method throws Throwable by default, can be specific with a generic type param.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public <E extends Throwable> T get() throws E {
        if (isSuccess()) {
            return requireValue();
        }
        throw (E) requireError();
    }

    /**
     * Returns the result's value, or the value from the mapper if the result is Failure.
     */
    public T or(Function<Throwable, ? extends T> mapper) {
        return isSuccess() ? requireValue() : mapper.invoke(requireError());
    }

    /**
     * Returns this result, or the given result if is Failure.
     */
    public Result<T> or(Result<T> that) {
        return isSuccess() ? this : that;
    }

    /**
     * Returns this result, or the result from supplier if the result is Failure.
     */
    public Result<T> orElse(Supplier<? extends Result<T>> supplier) {
        return isSuccess() ? this : supplier.get();
    }

    /**
     * Returns Success(mapper(value)), or this result if is Failure.
     */
    public <U> Result<U> map(Function<? super T, ? extends U> mapper) {
        return isSuccess() ? success(mapper.invoke(requireValue())) : (Failure<U>) this;
    }

    /**
     * Returns Failure(mapper(error)), or this result if is Success.
     */
    public Result<T> mapError(Function<Throwable, ? extends Throwable> mapper) {
        return isSuccess() ? this : Result.<T>failure(mapper.invoke(requireError()));
    }

    /**
     * Returns mapper(value), or this result if is Failure.
     */
    public <U> Result<U> flatMap(Function<? super T, ? extends Result<U>> mapper) {
        return isSuccess() ? mapper.invoke(requireValue()) : (Failure<U>) this;
    }

    /**
     * Returns mapper(value), or this result if is Success.
     */
    public Result<T> flatMapError(Function<Throwable, ? extends Result<T>> mapper) {
        return isSuccess() ? this : mapper.invoke(requireError());
    }

    /**
     * Returns value(value) or error(error) if is Failure.
     */
    public <U> U fold(Function<? super T, ? extends U> value, Function<Throwable, ? extends U> error) {
        return isSuccess() ? value.invoke(requireValue()) : error.invoke(requireError());
    }

    /**
     * Performs the given action for value if is Success, and return this result.
     */
    public Result<T> onSuccess(Consumer<? super T> action) {
        if (isSuccess()) {
            action.accept(requireValue());
        }
        return this;
    }

    /**
     * Performs the given action for error if is Failure, and return this result.
     */
    public Result<T> onError(Consumer<? super Throwable> action) {
        if (isFailure()) {
            action.accept(requireError());
        }
        return this;
    }

    /**
     * Performs the success action on success, the error action on error.
     */
    public void match(Consumer<? super T> success, Consumer<Throwable> error) {
        if (isSuccess()) {
            success.accept(requireValue());
        } else {
            error.accept(requireError());
        }
    }

    /**
     * Tests whether this result contains a given value as an element.
     */
    public boolean contains(T elem) {
        return isSuccess() && requireValue().equals(elem);
    }

    private T requireValue() {
        return ((Success<T>) this).value;
    }

    private Throwable requireError() {
        return ((Failure<T>) this).error;
    }

    /**
     * A success result.
     */
    public static final class Success<T> extends Result<T> {
        private final T value;

        public Success(T value) {
            Objects.requireNonNull(value);
            this.value = value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "Success(" + value + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Success)) {
                return false;
            }
            return value.equals(((Success<?>) obj).value);
        }
    }

    /**
     * A failure result.
     */
    public static final class Failure<T> extends Result<T> {
        private final Throwable error;

        public Failure(Throwable error) {
            Objects.requireNonNull(error);
            this.error = error;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public String toString() {
            return "Failure(" + error + ")";
        }
    }
}
