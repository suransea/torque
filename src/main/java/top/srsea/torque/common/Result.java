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
import top.srsea.torque.function.Function1;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.NoSuchElementException;
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
     * Returns the result's value.
     *
     * @throws E if the result is Failure.
     *           This method throws Throwable by default, can be specific with a generic type param,
     *           such as RuntimeException if you don't want to handle the error.
     */
    @Nonnull
    public <E extends Throwable> T get() throws E {
        if (isSuccess()) {
            return acquireValue();
        }
        @SuppressWarnings("unchecked")
        E error = (E) acquireError();
        throw error;
    }

    /**
     * Returns the result's value, or defaultValue if the result is Failure.
     */
    public T or(T defaultValue) {
        return isSuccess() ? acquireValue() : defaultValue;
    }

    /**
     * Returns the result's value, or the value from the mapper if the result is Failure.
     */
    public T or(Function1<Throwable, ? extends T> mapper) {
        return isSuccess() ? acquireValue() : mapper.invoke(acquireError());
    }

    /**
     * Returns this result, or the given result if is Failure.
     */
    public Result<T> or(Result<T> that) {
        return isSuccess() ? this : that;
    }

    /**
     * Returns the result's value, or null if the result is Failure.
     */
    @Nullable
    public T orNull() {
        return isSuccess() ? acquireValue() : null;
    }

    /**
     * Returns this result, or the result from mapper if the result is Failure.
     */
    public Result<T> orElse(Function1<Throwable, ? extends Result<T>> mapper) {
        return isSuccess() ? this : mapper.invoke(acquireError());
    }

    /**
     * Returns Some(value), or None if the result is Failure.
     */
    public Option<T> orNone() {
        return Option.from(orNull());
    }

    /**
     * Returns the result's error.
     *
     * @throws NoSuchElementException if the result is Success.
     */
    public Throwable error() {
        if (isFailure()) {
            return acquireError();
        }
        throw new NoSuchElementException("A result success has no error.");
    }

    /**
     * Returns the result's error, or null if the result is Success.
     */
    @Nullable
    public Throwable errorOrNull() {
        return isFailure() ? acquireError() : null;
    }

    /**
     * Returns Some(error), or None if the result is Success.
     */
    public Option<Throwable> errorOrNone() {
        return Option.from(errorOrNull());
    }

    /**
     * Returns Success(mapper(value)), or this result if is Failure.
     */
    public <U> Result<U> map(Function1<? super T, ? extends U> mapper) {
        return isSuccess() ? success(mapper.invoke(acquireValue())) : (Failure<U>) this;
    }

    /**
     * Returns Failure(mapper(error)), or this result if is Success.
     */
    public Result<T> mapError(Function1<Throwable, ? extends Throwable> mapper) {
        return isSuccess() ? this : Result.<T>failure(mapper.invoke(acquireError()));
    }

    /**
     * Returns mapper(value), or this result if is Failure.
     */
    public <U> Result<U> flatMap(Function1<? super T, ? extends Result<U>> mapper) {
        return isSuccess() ? mapper.invoke(acquireValue()) : (Failure<U>) this;
    }

    /**
     * Returns mapper(value), or this result if is Success.
     * Same behavior as `orElse` but different semantics.
     *
     * @see Result#orElse(Function1)
     */
    public Result<T> flatMapError(Function1<Throwable, ? extends Result<T>> mapper) {
        return isSuccess() ? this : mapper.invoke(acquireError());
    }

    /**
     * Returns operation(value) or initialValue if empty.
     */
    public <U> U fold(U initialValue, Function1<? super T, ? extends U> operation) {
        return isFailure() ? initialValue : operation.invoke(acquireValue());
    }

    /**
     * Performs the success action on success, the error action on error.
     */
    public void foreach(Consumer<? super T> success, Consumer<Throwable> error) {
        if (isSuccess()) {
            success.accept(acquireValue());
        } else {
            error.accept(acquireError());
        }
    }

    /**
     * Tests whether this result contains a given value as an element.
     */
    public boolean contains(T elem) {
        return isSuccess() && acquireValue().equals(elem);
    }

    /**
     * Returns false if the result is Failure or pred(value) returns false.
     * Otherwise returns true.
     */
    public boolean any(Function1<? super T, Boolean> pred) {
        if (isFailure()) {
            return false;
        }
        return pred.invoke(acquireValue());
    }

    /**
     * Returns false if the result is Success or pred(error) returns false.
     * Otherwise returns true.
     */
    public boolean anyError(Function1<Throwable, Boolean> pred) {
        if (isSuccess()) {
            return false;
        }
        return pred.invoke(acquireError());
    }

    /**
     * Returns true if the result is Failure or pred(value) returns true.
     * Otherwise returns false.
     */
    public boolean all(Function1<? super T, Boolean> pred) {
        if (isFailure()) {
            return true;
        }
        return pred.invoke(acquireValue());
    }

    /**
     * Returns true if the result is Success or pred(value) returns true.
     * Otherwise returns false.
     */
    public boolean allError(Function1<Throwable, Boolean> pred) {
        if (isSuccess()) {
            return true;
        }
        return pred.invoke(acquireError());
    }

    /**
     * Returns true if the result is Failure or pred(value) returns false.
     * Otherwise returns false.
     */
    public boolean none(Function1<? super T, Boolean> pred) {
        if (isFailure()) {
            return true;
        }
        return !pred.invoke(acquireValue());
    }

    /**
     * Returns true if the result is Success or pred(value) returns false.
     * Otherwise returns false.
     */
    public boolean noneError(Function1<Throwable, Boolean> pred) {
        if (isSuccess()) {
            return true;
        }
        return !pred.invoke(acquireError());
    }

    /**
     * Performs the given action for value if is Success, and return this result.
     */
    public Result<T> onSuccess(Consumer<? super T> action) {
        if (isSuccess()) {
            action.accept(acquireValue());
        }
        return this;
    }

    /**
     * Performs the given action for error if is Failure, and return this result.
     */
    public Result<T> onError(Consumer<? super Throwable> action) {
        if (isFailure()) {
            action.accept(acquireError());
        }
        return this;
    }

    private T acquireValue() {
        return ((Success<T>) this).value;
    }

    private Throwable acquireError() {
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
            return "Success(" + value.toString() + ")";
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
            return "Failure(" + error.toString() + ")";
        }
    }
}
