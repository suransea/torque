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

package top.srsea.torque.list;

import top.srsea.torque.common.Preconditions;
import top.srsea.torque.function.Consumer;
import top.srsea.torque.function.Function;
import top.srsea.torque.function.Function2;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class List<T> extends Cons<T, List<T>> implements Iterable<T> {

    public List(T car, List<T> cdr) {
        super(car, cdr);
    }

    public List(T car) {
        super(car, List.<T>nil());
    }

    @SafeVarargs
    public static <T> List<T> of(T... elems) {
        return create(elems, 0);
    }

    @SafeVarargs
    public static <T> List<T> list(T... elems) {
        return create(elems, 0);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> nil() {
        return (List<T>) Nil.INSTANCE;
    }

    public static <T> List<T> flatten(List<List<T>> lists) {
        return lists.foldRight(List.<T>nil(), new Function2<List<T>, List<T>, List<T>>() {
            @Override
            public List<T> invoke(List<T> x, List<T> y) {
                return y.append(x);
            }
        });
    }

    private static <T> List<T> create(T[] arr, int offset) {
        if (arr.length == offset) return nil();
        return new List<>(arr[offset], create(arr, offset + 1));
    }

    public List<T> append(List<T> other) {
        if (this == nil()) return other;
        return new List<>(car, cdr.append(other));
    }

    public int length() {
        if (this == nil()) return 0;
        return 1 + cdr.length();
    }

    public T first() {
        return car;
    }

    public List<T> rest() {
        return cdr;
    }

    public T nth(int index) {
        Preconditions.require(index >= 0, "index < 0");
        if (this == nil()) throw new IndexOutOfBoundsException();
        if (index == 0) return car;
        return cdr.nth(--index);
    }

    public T last() {
        if (this == nil()) throw new NoSuchElementException();
        if (cdr == nil()) return car;
        return cdr.last();
    }

    public <U> List<U> map(Function<? super T, ? extends U> transform) {
        if (this == nil()) return nil();
        return new List<>(transform.invoke(car), cdr.map(transform));
    }

    public <U> List<U> flatMap(Function<? super T, ? extends List<U>> transform) {
        if (this == nil()) return nil();
        return flatten(map(transform));
    }

    public List<T> filter(Function<? super T, Boolean> pred) {
        if (this == nil()) return nil();
        if (pred.invoke(car)) return new List<>(car, cdr.filter(pred));
        return cdr.filter(pred);
    }

    public <R> R foldLeft(R init, Function2<? super R, ? super T, ? extends R> op) {
        if (this == nil()) return init;
        return cdr.foldLeft(op.invoke(init, car), op);
    }

    public <R> R foldRight(R init, Function2<? super R, ? super T, ? extends R> op) {
        if (this == nil()) return init;
        return op.invoke(cdr.foldRight(init, op), car);
    }

    public List<T> reverse() {
        return reverse(List.<T>nil());
    }

    private List<T> reverse(List<T> rest) {
        if (this == nil()) return rest;
        return cdr.reverse(new List<>(car, rest));
    }

    public Cons<List<T>, List<T>> span(Function<? super T, Boolean> pred) {
        if (this == nil()) return new Cons<>(List.<T>nil(), List.<T>nil());
        if (!pred.invoke(car)) return new Cons<>(List.<T>nil(), this);
        Cons<List<T>, List<T>> spanCdr = cdr.span(pred);
        return new Cons<>(new List<>(car, spanCdr.car), spanCdr.cdr);
    }

    public List<List<T>> group(final Function2<? super T, ? super T, Boolean> eq) {
        if (this == nil()) return nil();
        Cons<List<T>, List<T>> spanCdr = cdr.span(new Function<T, Boolean>() {
            @Override
            public Boolean invoke(T val) {
                return eq.invoke(car, val);
            }
        });
        return new List<>(new List<>(car, spanCdr.car), spanCdr.cdr.group(eq));
    }

    public List<List<T>> group() {
        return group(new Function2<T, T, Boolean>() {
            @Override
            public Boolean invoke(T x, T y) {
                return Objects.equals(x, y);
            }
        });
    }

    public void foreach(Consumer<? super T> action) {
        if (this == nil()) return;
        action.accept(car);
        cdr.foreach(action);
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            List<T> current = List.this;

            @Override
            public boolean hasNext() {
                return current != nil();
            }

            @Override
            public T next() {
                T next = current.car;
                current = current.cdr;
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        List<T> current = this;
        StringBuilder builder = new StringBuilder("(");
        while (current != nil()) {
            builder.append(current.car);
            current = current.cdr;
            if (current != nil()) {
                builder.append(',').append(' ');
            }
        }
        builder.append(')');
        return builder.toString();
    }
}
