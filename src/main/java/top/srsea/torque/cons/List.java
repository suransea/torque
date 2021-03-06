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

package top.srsea.torque.cons;

import top.srsea.torque.function.Consumer;
import top.srsea.torque.function.Function;
import top.srsea.torque.function.Function2;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class List<T> extends Pair<T, List<T>> implements Iterable<T> {

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

    public static <T> List<T> cons(T car, List<T> cdr) {
        return new List<>(car, cdr);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> nil() {
        return (List<T>) Nil.INSTANCE;
    }

    private static <T> List<T> create(T[] arr, int offset) {
        if (arr.length == offset) return nil();
        return cons(arr[offset], create(arr, offset + 1));
    }

    public static <T> List<T> flatten(List<List<T>> lists) {
        return lists.foldRight(List.<T>nil(), new Function2<List<T>, List<T>, List<T>>() {
            @Override
            public List<T> invoke(List<T> x, List<T> y) {
                return x.concat(y);
            }
        });
    }

    public List<T> concat(List<T> other) {
        if (this == nil()) return other;
        return cons(car, cdr.concat(other));
    }

    public List<T> prepend(T elem) {
        return cons(elem, this);
    }

    public List<T> append(T elem) {
        if (this == nil()) return cons(elem, this);
        return cons(car, cdr.append(elem));
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

    public List<T> nth(int index) {
        if (this == nil() || index < 0) return nil();
        if (index == 0) return this;
        return cdr.nth(--index);
    }

    public List<T> last() {
        if (this == nil() || cdr == nil()) return this;
        return cdr.last();
    }

    public <U> List<U> map(Function<? super T, ? extends U> transform) {
        if (this == nil()) return nil();
        return cons(transform.invoke(car), cdr.map(transform));
    }

    public <U> List<U> flatMap(Function<? super T, ? extends List<U>> transform) {
        if (this == nil()) return nil();
        return flatten(map(transform));
    }

    public List<T> filter(Function<? super T, Boolean> pred) {
        if (this == nil()) return nil();
        if (pred.invoke(car)) return cons(car, cdr.filter(pred));
        return cdr.filter(pred);
    }

    public <R> R foldLeft(R init, Function2<? super R, ? super T, ? extends R> op) {
        if (this == nil()) return init;
        return cdr.foldLeft(op.invoke(init, car), op);
    }

    public <R> R foldRight(R init, Function2<? super T, ? super R, ? extends R> op) {
        if (this == nil()) return init;
        return op.invoke(car, cdr.foldRight(init, op));
    }

    public List<T> reverse() {
        return reverse(List.<T>nil());
    }

    private List<T> reverse(List<T> rest) {
        if (this == nil()) return rest;
        return cdr.reverse(cons(car, rest));
    }

    public Pair<List<T>, List<T>> span(Function<? super T, Boolean> pred) {
        if (this == nil()) return new Pair<>(List.<T>nil(), List.<T>nil());
        if (!pred.invoke(car)) return new Pair<>(List.<T>nil(), this);
        Pair<List<T>, List<T>> spanCdr = cdr.span(pred);
        return new Pair<>(cons(car, spanCdr.car), spanCdr.cdr);
    }

    public List<List<T>> group(final Function2<? super T, ? super T, Boolean> eq) {
        if (this == nil()) return nil();
        Pair<List<T>, List<T>> spanCdr = cdr.span(new Function<T, Boolean>() {
            @Override
            public Boolean invoke(T val) {
                return eq.invoke(car, val);
            }
        });
        return cons(cons(car, spanCdr.car), spanCdr.cdr.group(eq));
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
                if (current == nil()) throw new NoSuchElementException();
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
