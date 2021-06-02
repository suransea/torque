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

import java.util.Objects;

public class Pair<A, D> {
    protected final A car;
    protected final D cdr;

    public Pair(A car, D cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public A car() {
        return car;
    }

    public D cdr() {
        return cdr;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        return Objects.equals(((Pair<?, ?>) o).car, car) && Objects.equals(((Pair<?, ?>) o).cdr, cdr);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(car) ^ Objects.hashCode(cdr);
    }

    @Override
    public String toString() {
        return "(" + car + " . " + cdr + ")";
    }
}
