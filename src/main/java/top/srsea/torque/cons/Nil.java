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

package top.srsea.torque.cons;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;

public final class Nil<T> extends List<T> {
    public static final Nil<Object> INSTANCE = new Nil<>();

    private Nil() {
        super(null, null);
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Nil;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "()";
    }
}
