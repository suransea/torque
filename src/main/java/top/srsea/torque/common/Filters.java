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
import java.util.Arrays;
import java.util.List;

/**
 * 过滤器链
 *
 * @param <T>
 */
public class Filters<T> {
    private List<Filter<? super T>> filterList = new ArrayList<>();
    private boolean canceled = false;

    /**
     * 以若干过滤器创建链
     *
     * @param filters 过滤器
     * @param <T>     应用对象类型
     * @return 过滤器链
     */
    @SafeVarargs
    public static <T> Filters<T> of(Filter<? super T>... filters) {
        Filters<T> tFilters = new Filters<>();
        tFilters.filterList.addAll(Arrays.asList(filters));
        return tFilters;
    }

    /**
     * 在当前链上添加一个过滤器
     *
     * @param filter target filter
     * @return 过滤器链
     */
    public Filters<T> and(Filter<? super T> filter) {
        filterList.add(filter);
        return this;
    }

    /**
     * 将链应用给目标对象
     *
     * @param t target object
     */
    public void apply(T t) {
        apply(t, null);
    }

    /**
     * 将链应用给目标对象
     *
     * @param t               target object
     * @param throwableFilter 错误过滤器: exception将通过此对象
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
     * 取消链中后续过滤器的执行
     */
    public void cancel() {
        canceled = true;
    }
}
