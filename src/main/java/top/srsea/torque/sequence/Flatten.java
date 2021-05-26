package top.srsea.torque.sequence;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Flatten<T> extends Sequence<T> {
    private final Iterable<? extends Iterable<? extends T>> iterable;

    public Flatten(Iterable<? extends Iterable<? extends T>> iterable) {
        this.iterable = iterable;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<? extends Iterable<? extends T>> iterator = iterable.iterator();
            Iterator<? extends T> inner;

            @Override
            public boolean hasNext() {
                if (inner != null && inner.hasNext()) {
                    return true;
                }
                if (!iterator.hasNext()) {
                    return false;
                }
                inner = iterator.next().iterator();
                return hasNext();
            }

            @Override
            public T next() {
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
