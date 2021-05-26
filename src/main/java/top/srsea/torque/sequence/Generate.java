package top.srsea.torque.sequence;

import top.srsea.torque.function.Function;
import top.srsea.torque.function.Supplier;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Generate<T> extends Sequence<T> {
    private final Supplier<? extends T> init;
    private final Function<? super T, Boolean> cond;
    private final Function<? super T, ? extends T> iterate;

    public Generate(Supplier<? extends T> init, Function<? super T, Boolean> cond, Function<? super T, ? extends T> iterate) {
        this.init = init;
        this.cond = cond;
        this.iterate = iterate;
    }

    public Generate(Supplier<? extends T> init, Function<? super T, ? extends T> iterate) {
        this(init, null, iterate);
    }

    public Generate(Supplier<? extends T> generator) {
        this(generator, null, null);
    }

    @Override
    public Iterator<T> iterator() {
        if (cond == null) {
            if (iterate == null) {
                return noIterate();
            }
            return noCondition();
        }
        return new Iterator<T>() {
            private T next;
            private boolean hasNext;
            private boolean first = true;
            private boolean nextEvaluated = false;

            private void evalNext() {
                if (nextEvaluated) {
                    return;
                }
                if (first) {
                    next = init.get();
                    first = false;
                } else {
                    next = iterate.invoke(next);
                }
                hasNext = cond.invoke(next);
                nextEvaluated = true;
            }

            @Override
            public boolean hasNext() {
                evalNext();
                return hasNext;
            }

            @Override
            public T next() {
                evalNext();
                if (!hasNext) {
                    throw new NoSuchElementException();
                }
                nextEvaluated = false;
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private Iterator<T> noCondition() {
        return new Iterator<T>() {
            private T next;
            private boolean first = true;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                if (first) {
                    first = false;
                    next = init.get();
                    return next;
                }
                next = iterate.invoke(next);
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private Iterator<T> noIterate() {
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return init.get();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
