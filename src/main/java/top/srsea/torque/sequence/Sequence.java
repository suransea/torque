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

package top.srsea.torque.sequence;

import top.srsea.torque.common.Option;
import top.srsea.torque.common.Preconditions;
import top.srsea.torque.function.Consumer;
import top.srsea.torque.function.Function;
import top.srsea.torque.function.Function2;
import top.srsea.torque.function.Supplier;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class Sequence<T> implements Iterable<T> {

    @SafeVarargs
    public static <T> Sequence<T> of(T... elems) {
        return elems.length == 0 ? Sequence.<T>empty() : new Array<>(elems);
    }

    public static <T> Sequence<T> single(T elem) {
        return new Single<>(elem);
    }

    public static <T> Sequence<T> maybe(@Nullable T elem) {
        return new Maybe<>(elem);
    }

    @SuppressWarnings("unchecked")
    public static <T> Sequence<T> empty() {
        return (Sequence<T>) Empty.INSTANCE;
    }

    public static <T> Sequence<T> from(Iterable<T> iterable) {
        return new FromIterable<>(iterable);
    }

    public static <T> Sequence<T> from(T[] array) {
        return of(array);
    }

    public static Sequence<Integer> from(int[] array) {
        return new IntArray(array);
    }

    public static Sequence<Short> from(short[] array) {
        return new ShortArray(array);
    }

    public static Sequence<Float> from(float[] array) {
        return new FloatArray(array);
    }

    public static Sequence<Double> from(double[] array) {
        return new DoubleArray(array);
    }

    public static Sequence<Long> from(long[] array) {
        return new LongArray(array);
    }

    public static Sequence<Character> from(char[] array) {
        return new CharArray(array);
    }

    public static Sequence<Boolean> from(boolean[] array) {
        return new BooleanArray(array);
    }

    public static Sequence<Byte> from(byte[] array) {
        return new ByteArray(array);
    }

    public static Sequence<Character> from(CharSequence s) {
        return new FromCharSequence(s);
    }

    public static <T> Sequence<T> range(T begin, T end, Function<? super T, ? extends T> successor) {
        return new Range<>(begin, end, successor);
    }

    public static Sequence<Integer> range(int begin, int end, final int step) {
        return new Range<>(begin, end, new Function<Integer, Integer>() {
            @Override
            public Integer invoke(Integer val) {
                return val + step;
            }
        });
    }

    public static Sequence<Integer> range(int begin, int end) {
        return range(begin, end, 1);
    }

    public static Sequence<Long> range(long begin, long end, final long step) {
        return new Range<>(begin, end, new Function<Long, Long>() {
            @Override
            public Long invoke(Long val) {
                return val + step;
            }
        });
    }

    public static Sequence<Long> range(long begin, long end) {
        return range(begin, end, 1);
    }

    public static <T> Sequence<T> generate(Supplier<? extends T> init, Function<? super T, Boolean> cond, Function<? super T, ? extends T> iterate) {
        return new Generate<>(init, cond, iterate);
    }

    public static <T> Sequence<T> generate(Supplier<? extends T> init, Function<? super T, ? extends T> iterate) {
        return new Generate<>(init, iterate);
    }

    public static <T> Sequence<T> generate(Supplier<? extends T> generator) {
        return new Generate<>(generator);
    }

    public static <T> Sequence<T> flatten(Iterable<? extends Iterable<? extends T>> iterable) {
        return new Flatten<>(iterable);
    }

    public int count() {
        int count = 0;
        for (T ignored : this) {
            ++count;
        }
        return count;
    }

    public int count(Function<? super T, Boolean> pred) {
        int count = 0;
        for (T it : this) {
            if (pred.invoke(it)) {
                ++count;
            }
        }
        return count;
    }

    public <R> R fold(R init, Function2<? super R, ? super T, ? extends R> op) {
        for (T it : this) {
            init = op.invoke(init, it);
        }
        return init;
    }

    public T reduce(Function2<? super T, ? super T, ? extends T> op) {
        Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) {
            throw new UnsupportedOperationException();
        }
        T init = iterator.next();
        while (iterator.hasNext()) {
            init = op.invoke(init, iterator.next());
        }
        return init;
    }

    public Option<T> first() {
        for (T it : this) {
            return Option.some(it);
        }
        return Option.none();
    }

    public Option<T> last() {
        Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) {
            return Option.none();
        }
        T last = iterator.next();
        while (iterator.hasNext()) {
            last = iterator.next();
        }
        return Option.some(last);
    }

    public Option<T> nth(int index) {
        Preconditions.require(index >= 0, "index < 0");
        Iterator<T> iterator = iterator();
        for (int i = index; i > 0; --i, iterator.next()) {
            if (!iterator.hasNext()) {
                return Option.none();
            }
        }
        return iterator.hasNext() ? Option.some(iterator.next()) : Option.<T>none();
    }

    public <U> Sequence<U> map(Function<? super T, ? extends U> transform) {
        return new Map<>(this, transform);
    }

    public <U> Sequence<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> transform) {
        return new FlatMap<>(this, transform);
    }

    public Sequence<T> filter(Function<? super T, Boolean> pred) {
        return new Filter<>(this, pred);
    }

    public Sequence<T> take(int n) {
        if (n == 0) {
            return empty();
        }
        return new Take<>(this, n);
    }

    public Sequence<T> drop(int n) {
        if (n == 0) {
            return this;
        }
        return new Drop<>(this, n);
    }

    public <U, R> Sequence<R> zip(Iterable<U> other, Function2<? super T, ? super U, ? extends R> zipper) {
        return new Zip<>(this, other, zipper);
    }

    public <U extends T> Sequence<T> concat(Iterable<U> other) {
        return new Concat<>(this, other);
    }

    public Sequence<List<T>> chunk(int n) {
        return new Chunk<>(this, n);
    }

    public Sequence<T> onEach(final Consumer<? super T> action) {
        return new Map<>(this, new Function<T, T>() {
            @Override
            public T invoke(T val) {
                action.accept(val);
                return val;
            }
        });
    }

    public void foreach(Consumer<? super T> action) {
        for (T it : this) {
            action.accept(it);
        }
    }

    public <C extends Collection<T>> C to(C collection) {
        for (T it : this) {
            collection.add(it);
        }
        return collection;
    }

    @Override
    public String toString() {
        Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) return "()";
        StringBuilder builder = new StringBuilder("(");
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(',').append(' ');
            }
        }
        builder.append(')');
        return builder.toString();
    }
}
