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

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;
import java.util.Objects;

public abstract class Either<L, R> {

    private Either() {
    }

    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    public static <T> T decay(Either<T, T> either) {
        return either.get();
    }

    @Nonnull
    public abstract L left();

    @Nonnull
    public abstract R right();

    public <T> T get() {
        @SuppressWarnings("unchecked")
        T value = isLeft() ? (T) left() : (T) right();
        return value;
    }

    public abstract boolean isLeft();

    public boolean isRight() {
        return !isLeft();
    }

    public L leftOr(L defaultValue) {
        return isLeft() ? left() : defaultValue;
    }

    public R rightOr(R defaultValue) {
        return isLeft() ? defaultValue : right();
    }

    public L leftOr(Function1<? super R, ? extends L> mapper) {
        return isLeft() ? left() : mapper.invoke(right());
    }

    public R rightOr(Function1<? super L, ? extends R> mapper) {
        return isLeft() ? mapper.invoke(left()) : right();
    }

    public L leftOrNull() {
        return isLeft() ? left() : null;
    }

    public R rightOrNull() {
        return isLeft() ? null : right();
    }

    public Option<L> leftOrNone() {
        return Option.from(leftOrNull());
    }

    public Option<R> rightOrNone() {
        return Option.from(rightOrNull());
    }

    public Either<L, R> onLeft(Consumer<L> consumer) {
        if (isLeft()) consumer.accept(left());
        return this;
    }

    public Either<L, R> onRight(Consumer<R> consumer) {
        if (isRight()) consumer.accept(right());
        return this;
    }

    public void match(Consumer<L> left, Consumer<R> right) {
        if (isLeft()) {
            left.accept(left());
        } else {
            right.accept(right());
        }
    }

    public <T> Either<T, R> mapLeft(Function1<? super L, ? extends T> mapper) {
        return isLeft() ? Either.<T, R>left(mapper.invoke(left())) : this.<T>castLeft();
    }

    public <T> Either<L, T> mapRight(Function1<? super R, ? extends T> mapper) {
        return isLeft() ? this.<T>castRight() : Either.<L, T>right(mapper.invoke(right()));
    }

    public <T, U> Either<T, U> map(Function1<? super L, ? extends T> left, Function1<? super R, ? extends U> right) {
        return this.<T>mapLeft(left).mapRight(right);
    }

    public <T> Either<T, R> flatMapLeft(Function1<? super L, ? extends Either<T, R>> mapper) {
        return isLeft() ? mapper.invoke(left()) : this.<T>castLeft();
    }

    public <T> Either<L, T> flatMapRight(Function1<? super R, ? extends Either<L, T>> mapper) {
        return isLeft() ? this.<T>castRight() : mapper.invoke(right());
    }

    public <T, U> Either<T, U> flatMap(Function1<? super L, ? extends Either<T, U>> left,
                                       Function1<? super R, ? extends Either<T, U>> right) {
        return isLeft() ? left.invoke(left()) : right.invoke(right());
    }

    public <T> T foldLeft(T initialValue, Function1<? super L, ? extends T> operation) {
        return isLeft() ? operation.invoke(left()) : initialValue;
    }

    public <T> T foldRight(T initialValue, Function1<? super R, ? extends T> operation) {
        return isLeft() ? initialValue : operation.invoke(right());
    }

    public <T> T fold(Function1<? super L, ? extends T> left, Function1<? super R, ? extends T> right) {
        return isLeft() ? left.invoke(left()) : right.invoke(right());
    }

    public boolean anyLeft(Function1<? super L, Boolean> pred) {
        if (isRight()) {
            return false;
        }
        return pred.invoke(left());
    }

    public boolean anyRight(Function1<? super R, Boolean> pred) {
        if (isLeft()) {
            return false;
        }
        return pred.invoke(right());
    }

    public boolean allLeft(Function1<? super L, Boolean> pred) {
        if (isRight()) {
            return true;
        }
        return pred.invoke(left());
    }

    public boolean allRight(Function1<? super R, Boolean> pred) {
        if (isLeft()) {
            return true;
        }
        return pred.invoke(right());
    }

    public boolean noneLeft(Function1<? super L, Boolean> pred) {
        if (isRight()) {
            return true;
        }
        return !pred.invoke(left());
    }

    public boolean noneRight(Function1<? super R, Boolean> pred) {
        if (isLeft()) {
            return true;
        }
        return !pred.invoke(right());
    }

    public <T> boolean contains(T elem) {
        if (isLeft()) {
            return left().equals(elem);
        }
        return right().equals(elem);
    }

    private <T> Either<T, R> castLeft() {
        @SuppressWarnings("unchecked")
        Either<T, R> either = (Either<T, R>) this;
        return either;
    }

    private <T> Either<L, T> castRight() {
        @SuppressWarnings("unchecked")
        Either<L, T> either = (Either<L, T>) this;
        return either;
    }

    public static final class Left<L, R> extends Either<L, R> {
        private final L value;

        public Left(L value) {
            Objects.requireNonNull(value);
            this.value = value;
        }

        @Nonnull
        @Override
        public L left() {
            return value;
        }

        @Nonnull
        @Override
        public R right() {
            throw new NoSuchElementException("Left.right");
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Left)) {
                return false;
            }
            return value.equals(((Left<?, ?>) obj).value);
        }

        @Override
        public String toString() {
            return "Left(" + value + ")";
        }
    }

    public static final class Right<L, R> extends Either<L, R> {
        private final R value;

        public Right(R value) {
            Objects.requireNonNull(value);
            this.value = value;
        }

        @Nonnull
        @Override
        public L left() {
            throw new NoSuchElementException("Right.left");
        }

        @Nonnull
        @Override
        public R right() {
            return value;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Right)) {
                return false;
            }
            return value.equals(((Right<?, ?>) obj).value);
        }

        @Override
        public String toString() {
            return "Right(" + value + ")";
        }
    }
}
