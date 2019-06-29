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

/**
 * Observable Property
 *
 * @param <T> type of value
 */
public class Observable<T> implements Property<T> {
    private T value;
    private Observer<T> observer;

    public Observable(Observer<T> observer) {
        this.observer = observer;
    }

    @Override
    public void set(T value) {
        T last = this.value;
        this.value = value;
        if (observer == null) return;
        observer.onChange(this, last, value);
    }

    @Override
    public T get() {
        return value;
    }
}
