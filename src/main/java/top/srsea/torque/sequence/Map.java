package top.srsea.torque.sequence;

import top.srsea.torque.function.Function;

import java.util.Iterator;

public class Map<T, U> extends Sequence<U> {
    private final Sequence<T> sequence;
    private final Function<? super T, ? extends U> transform;

    public Map(Sequence<T> sequence, Function<? super T, ? extends U> transform) {
        this.sequence = sequence;
        this.transform = transform;
    }

    @Override
    public Iterator<U> iterator() {
        return new Iterator<U>() {
            final Iterator<T> iterator = sequence.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public U next() {
                return transform.invoke(iterator.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
