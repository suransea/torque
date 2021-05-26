package top.srsea.torque.sequence;

import top.srsea.torque.function.Function;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FlatMap<T, U> extends Sequence<U> {
    private final Sequence<T> sequence;
    private final Function<? super T, ? extends Iterable<U>> transform;

    public FlatMap(Sequence<T> sequence, Function<? super T, ? extends Iterable<U>> transform) {
        this.sequence = sequence;
        this.transform = transform;
    }

    @Override
    public Iterator<U> iterator() {
        return new Iterator<U>() {
            final Iterator<T> iterator = sequence.iterator();
            Iterator<U> inner;

            @Override
            public boolean hasNext() {
                if (inner != null && inner.hasNext()) {
                    return true;
                }
                if (!iterator.hasNext()) {
                    return false;
                }
                inner = transform.invoke(iterator.next()).iterator();
                return hasNext();
            }

            @Override
            public U next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return inner.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
