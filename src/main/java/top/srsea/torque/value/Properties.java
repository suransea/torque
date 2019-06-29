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

package top.srsea.torque.value;


import top.srsea.torque.function.Provider;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 属性工具
 */
public class Properties {
    /**
     * 创建一个懒加载的属性
     *
     * @param provider value provider
     * @param <T>      type of value
     * @return Lazy instance
     */
    public static <T> Lazy<T> lazy(@Nonnull Provider<T> provider) {
        Objects.requireNonNull(provider, "provider require not null.");
        return new Lazy<>(provider);
    }

    /**
     * 创建一个可观察的属性
     *
     * @param observer 观察者
     * @param <T>      type of value
     * @return Observable instance
     */
    public static <T> Observable<T> observable(Observer<T> observer) {
        return new Observable<>(observer);
    }

    /**
     * 创建一个可观察的属性
     *
     * @param value    initial value (notify observer uniformly)
     * @param observer 观察者
     * @param <T>      type of value
     * @return Observable instance
     */
    public static <T> Observable<T> observable(T value, Observer<T> observer) {
        Observable<T> observable = new Observable<>(observer);
        observable.set(value);
        return observable;
    }
}
