package top.srsea.torque.sequence;

import top.srsea.torque.function.Function;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Filter<T> extends Sequence<T> {
    private final Sequence<T> sequence;
    private final Function<? super T, Boolean> predicate;

    public Filter(Sequence<T> sequence, Function<? super T, Boolean> predicate) {
        this.sequence = sequence;
        this.predicate = predicate;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<T> iterator = sequence.iterator();
            T next;
            boolean hasNext;
            boolean nextEvaluated = false;

            private void evalNext() {
                if (nextEvaluated) {
                    return;
                }
                if (!iterator.hasNext()) {
                    hasNext = false;
                    return;
                }
                next = iterator.next();
                if (!predicate.invoke(next)) {
                    evalNext();
                    return;
                }
                hasNext = true;
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
}
