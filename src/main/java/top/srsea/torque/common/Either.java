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
import top.srsea.torque.function.Function;

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

    public static <T> T decay(Either<? extends T, ? extends T> either) {
        return either.get();
    }

    public Option<L> left() {
        return isLeft() ? Option.some(requireLeft()) : Option.<L>none();
    }

    public Option<R> right() {
        return isLeft() ? Option.<R>none() : Option.some(requireRight());
    }

    @SuppressWarnings("unchecked")
    public <T> T get() {
        return isLeft() ? (T) requireLeft() : (T) requireRight();
    }

    public abstract boolean isLeft();

    public boolean isRight() {
        return !isLeft();
    }

    public Either<L, R> onLeft(Consumer<? super L> consumer) {
        if (isLeft()) consumer.accept(requireLeft());
        return this;
    }

    public Either<L, R> onRight(Consumer<? super R> consumer) {
        if (isRight()) consumer.accept(requireRight());
        return this;
    }

    public void match(Consumer<? super L> left, Consumer<? super R> right) {
        if (isLeft()) {
            left.accept(requireLeft());
        } else {
            right.accept(requireRight());
        }
    }

    public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> mapper) {
        return isLeft() ? Either.<T, R>left(mapper.invoke(requireLeft())) : this.<T>castLeft();
    }

    public <T> Either<L, T> mapRight(Function<? super R, ? extends T> mapper) {
        return isLeft() ? this.<T>castRight() : Either.<L, T>right(mapper.invoke(requireRight()));
    }

    public <T, U> Either<T, U> map(Function<? super L, ? extends T> left, Function<? super R, ? extends U> right) {
        return this.<T>mapLeft(left).mapRight(right);
    }

    public <T> Either<T, R> flatMapLeft(Function<? super L, ? extends Either<T, R>> mapper) {
        return isLeft() ? mapper.invoke(requireLeft()) : this.<T>castLeft();
    }

    public <T> Either<L, T> flatMapRight(Function<? super R, ? extends Either<L, T>> mapper) {
        return isLeft() ? this.<T>castRight() : mapper.invoke(requireRight());
    }

    public <T, U> Either<T, U> flatMap(Function<? super L, ? extends Either<T, U>> left,
                                       Function<? super R, ? extends Either<T, U>> right) {
        return isLeft() ? left.invoke(requireLeft()) : right.invoke(requireRight());
    }

    public <T> T fold(Function<? super L, ? extends T> left, Function<? super R, ? extends T> right) {
        return isLeft() ? left.invoke(requireLeft()) : right.invoke(requireRight());
    }

    public <T> boolean contains(T elem) {
        return isLeft() ? requireLeft().equals(elem) : requireRight().equals(elem);
    }

    @SuppressWarnings("unchecked")
    private <T> Either<T, R> castLeft() {
        return (Either<T, R>) this;
    }

    @SuppressWarnings("unchecked")
    private <T> Either<L, T> castRight() {
        return (Either<L, T>) this;
    }

    private L requireLeft() {
        return ((Left<L, R>) this).value;
    }

    private R requireRight() {
        return ((Right<L, R>) this).value;
    }

    public static final class Left<L, R> extends Either<L, R> {
        private final L value;

        public Left(L value) {
            Objects.requireNonNull(value);
            this.value = value;
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
