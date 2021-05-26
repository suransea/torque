package top.srsea.torque.sequence;

import java.util.Iterator;

public class Concat<T, U extends T, V extends T> extends Sequence<T> {
    private final Sequence<U> sequence;
    private final Iterable<V> other;

    public Concat(Sequence<U> sequence, Iterable<V> other) {
        this.sequence = sequence;
        this.other = other;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<U> iterator = sequence.iterator();
            Iterator<V> otherIterator;

            private Iterator<V> otherIterator() {
                if (otherIterator == null) {
                    otherIterator = other.iterator();
                }
                return otherIterator;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext() || otherIterator().hasNext();
            }

            @Override
            public T next() {
                return iterator.hasNext() ? iterator.next() : otherIterator().next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
