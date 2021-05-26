/*
 * Copyright (C) 2019 sea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.srsea.torque.sequence;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ByteArray extends Sequence<Byte> {
    private final byte[] elems;

    public ByteArray(byte[] elems) {
        this.elems = elems;
    }

    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            private int cursor = 0;

            public boolean hasNext() {
                return cursor != elems.length;
            }

            public Byte next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elems[cursor++];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
