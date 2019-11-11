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
 * Properties.
 *
 * @author sea
 * @see Lazy
 * @see Observable
 */
public class Properties {

    /**
     * Creates a lazy loaded property.
     *
     * @param provider value provider
     * @param <T>      type of value
     * @return a lazy loaded property
     */
    public static <T> Lazy<T> lazy(@Nonnull Provider<T> provider) {
        Objects.requireNonNull(provider, "provider require not null.");
        return new Lazy<>(provider);
    }

    /**
     * Creates an observable property.
     *
     * @param observer value observer
     * @param <T>      type of value
     * @return an observable property
     */
    public static <T> Observable<T> observable(Observer<T> observer) {
        return new Observable<>(observer);
    }

    /**
     * Creates an observable property, with the specific initial value.
     *
     * @param value    initial value (notify the observer uniformly)
     * @param observer value observer
     * @param <T>      type of value
     * @return an observable property with the specific initial value.
     */
    public static <T> Observable<T> observable(T value, Observer<T> observer) {
        Observable<T> observable = new Observable<>(observer);
        observable.set(value);
        return observable;
    }
}
