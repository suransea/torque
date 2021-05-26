package top.srsea.torque.sequence;

import top.srsea.torque.function.Function2;

import java.util.Iterator;

public class Zip<T, U, R> extends Sequence<R> {
    private final Sequence<T> sequence;
    private final Iterable<U> other;
    private final Function2<? super T, ? super U, ? extends R> zipper;

    public Zip(Sequence<T> sequence, Iterable<U> other, Function2<? super T, ? super U, ? extends R> zipper) {
        this.sequence = sequence;
        this.other = other;
        this.zipper = zipper;
    }

    @Override
    public Iterator<R> iterator() {
        return new Iterator<R>() {
            final Iterator<T> iterator = sequence.iterator();
            final Iterator<U> otherIterator = other.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext() && otherIterator.hasNext();
            }

            @Override
            public R next() {
                return zipper.invoke(iterator.next(), otherIterator.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
