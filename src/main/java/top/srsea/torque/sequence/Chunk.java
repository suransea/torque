package top.srsea.torque.sequence;

import top.srsea.torque.common.ObjectHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Chunk<T> extends Sequence<List<T>> {
    private final Sequence<T> sequence;
    private final int size;

    public Chunk(Sequence<T> sequence, int size) {
        ObjectHelper.verifyPositive(size, "size");
        this.sequence = sequence;
        this.size = size;
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new Iterator<List<T>>() {
            final Iterator<T> iterator = sequence.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public List<T> next() {
                List<T> block = new ArrayList<>(size);
                for (int i = size; i > 0 && iterator.hasNext(); --i) {
                    block.add(iterator.next());
                }
                return block;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
