package top.srsea.torque.sequence;

import top.srsea.torque.common.Preconditions;

import java.util.Iterator;

public class Drop<T> extends Sequence<T> {
    private final Sequence<T> sequence;
    private final int count;

    public Drop(Sequence<T> sequence, int count) {
        Preconditions.require(count >= 0, "count < 0");
        this.sequence = sequence;
        this.count = count;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<T> iterator = sequence.iterator();
            int left = count;

            private void drop() {
                while (left > 0 && iterator.hasNext()) {
                    iterator.next();
                    --left;
                }
            }

            @Override
            public boolean hasNext() {
                drop();
                return iterator.hasNext();
            }

            @Override
            public T next() {
                drop();
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
