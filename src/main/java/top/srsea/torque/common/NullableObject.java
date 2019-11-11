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

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A value wrapper for class or its null.
 * Useful for a null value, but require a non-null value.
 *
 * @param <T> Type of wrapped value
 */
public class NullableObject<T> {
    private T object;

    /**
     * Constructs an instance wrapper the value.
     *
     * @param object the value to be wrapper
     */
    public NullableObject(T object) {
        this.object = object;
    }

    /**
     * Returns a {@code NullableObject} wrapping an object.
     *
     * @param object value to wrap
     * @param <T>    type of value
     * @return a {@code NullableObject} with the specific object
     */
    public static <T> NullableObject<T> of(T object) {
        return new NullableObject<>(object);
    }

    /**
     * Returns a {@code NullableObject} wrapping a null.
     *
     * @param objClass class of this null value
     * @param <T>      type of value
     * @return a {@code NullableObject} wrapping a null
     */
    public static <T> NullableObject<T> nullOfClass(Class<T> objClass) {
        return new NullableObject<>(null);
    }

    /**
     * Returns a {@code NullableObject} wrapping a null, the class will be inferred.
     * If you want to set the class manually, use {@link NullableObject#nullOfClass(Class)}.
     *
     * @param <T> type of value
     * @return a {@code NullableObject} wrapping a null
     */
    public static <T> NullableObject<T> nullObject() {
        return new NullableObject<>(null);
    }

    /**
     * Returns the inner value, include a null.
     *
     * @return inner value
     */
    @Nullable
    public T get() {
        return object;
    }

    /**
     * Returns if the inner value is null.
     *
     * @return {@code true} inner value is null or not
     */
    public boolean isNull() {
        return object == null;
    }

    /**
     * Returns if the inner value is not null.
     *
     * @return {@code true} if the inner value is not null, otherwise {@code false}
     */
    public boolean isNotNull() {
        return object != null;
    }

    /**
     * Hashcode just for the inner value, 0 if it is null.
     *
     * @return hashcode of inner value
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(object);
    }

    /**
     * Just inner value equals to another inner value of {@code NullableObject}, or both of them are null.
     *
     * @param obj an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NullableObject)) {
            return false;
        }
        return Objects.equals(object, ((NullableObject) obj).object);
    }
}
