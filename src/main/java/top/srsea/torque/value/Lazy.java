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

/**
 * Lazy property.
 *
 * @param <T> type of value
 * @author sea
 * @see Property
 */
public class Lazy<T> implements Property<T> {

    /**
     * Value of property.
     */
    private volatile T value;

    /**
     * Provider of property value.
     */
    private Provider<T> provider;

    /**
     * Constructs an instance with the value provider.
     *
     * @param provider the specific value provider
     */
    public Lazy(@Nonnull Provider<T> provider) {
        this.provider = provider;
    }

    /**
     * Sets the property value.
     *
     * @param value value to set
     * @deprecated don't change actual value of lazy property, unless you have to do this
     */
    @Override
    @Deprecated
    public void set(T value) {
        this.value = value;
    }

    /**
     * Gets the property value, load it from {@code provider} if called firstly.
     *
     * @return property value
     */
    @Override
    public T get() {
        T tmp = value;
        if (tmp == null) {
            synchronized (this) {
                tmp = value;
                if (tmp == null) {
                    value = provider.get();
                    provider = null;
                }
            }
        }
        return value;
    }
}
