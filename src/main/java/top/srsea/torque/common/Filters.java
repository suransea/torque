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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A chain of filters.
 *
 * @param <T> class of target object
 * @author sea
 * @see Filter
 */
public class Filters<T> implements Filter<T> {

    /**
     * A list contains all the filters.
     */
    private List<Filter<? super T>> filterList = new ArrayList<>();

    /**
     * A value represents whether the chain has been canceled.
     */
    private boolean canceled = false;

    /**
     * Create a {@code Filters} chain with several filters.
     *
     * @param filters filters array
     * @param <T>     class of target object
     * @return a {@code Filters} chain with several filters
     */
    @SafeVarargs
    public static <T> Filters<T> of(Filter<? super T>... filters) {
        Filters<T> tFilters = new Filters<>();
        Collections.addAll(tFilters.filterList, filters);
        return tFilters;
    }

    /**
     * Adds a filter to the current chain.
     *
     * @param filter target filter
     * @return current chain
     */
    public Filters<T> and(Filter<? super T> filter) {
        filterList.add(filter);
        return this;
    }

    /**
     * Applies the chain to the target object.
     *
     * @param t target object
     */
    @Override
    public void apply(T t) {
        apply(t, null);
    }

    /**
     * Applies the chain to the target object, an exception will be applied for the throwable filter.
     *
     * @param t               target object
     * @param throwableFilter exception will be applied for the throwable filter
     */
    public void apply(T t, Filter<Throwable> throwableFilter) {
        canceled = false;
        for (Filter<? super T> filter : filterList) {
            if (canceled) return;
            if (filter == null) continue;
            try {
                filter.apply(t);
            } catch (Exception e) {
                if (throwableFilter == null) continue;
                try {
                    throwableFilter.apply(e);
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Marks this chain canceled.
     */
    public void cancel() {
        canceled = true;
    }
}
