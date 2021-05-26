package top.srsea.torque.sequence;

import top.srsea.torque.common.Preconditions;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Take<T> extends Sequence<T> {
    private final Sequence<T> sequence;
    private final int count;

    public Take(Sequence<T> sequence, int count) {
        Preconditions.require(count >= 0, "count < 0");
        this.sequence = sequence;
        this.count = count;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<T> iterator = sequence.iterator();
            int left = count;

            @Override
            public boolean hasNext() {
                return left > 0 && iterator.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                --left;
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
