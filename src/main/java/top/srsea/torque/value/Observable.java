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
 * Observable property.
 *
 * @param <T> type of value
 * @author sea
 */
public class Observable<T> implements Property<T> {

    /**
     * Value of property.
     */
    private T value;

    /**
     * Observer of property.
     */
    private Observer<T> observer;

    /**
     * Constructs an instance with the value observer.
     *
     * @param observer the specific value observer
     */
    public Observable(Observer<T> observer) {
        this.observer = observer;
    }

    /**
     * Sets the property value, and notifies the observer.
     *
     * @param value value to set
     */
    @Override
    public void set(T value) {
        T last = this.value;
        this.value = value;
        if (observer == null) return;
        observer.onChange(this, last, value);
    }

    /**
     * Gets the property value.
     *
     * @return value
     */
    @Override
    public T get() {
        return value;
    }
}
