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
 * lazy property
 *
 * @param <T> type of value
 */
public class Lazy<T> implements Property<T> {
    private volatile T value;
    private Provider<T> provider;

    public Lazy(@Nonnull Provider<T> provider) {
        this.provider = provider;
    }

    @Override
    @Deprecated
    public void set(T value) {
        this.value = value;
    }

    /**
     * 获取值, 首次调用时从 provider 加载
     *
     * @return value
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
