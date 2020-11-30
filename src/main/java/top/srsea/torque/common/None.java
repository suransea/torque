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

package top.srsea.torque.common;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * `None` represents non-existent values
 */
public final class None extends Option<Object> {
    public static final None INSTANCE = new None();

    private None() {
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "None";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof None;
    }

    @Nonnull
    @Override
    public Object get() {
        throw new NoSuchElementException("None.get");
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Nonnull
    @Override
    public Iterator<Object> iterator() {
        return Collections.emptyIterator();
    }
}
