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

package top.srsea.torque.bean;

/**
 * Available built-in converters
 *
 * @author sea
 */
public class BeanConverters {

    /**
     * Creates a new {@code BeanConverter} instance implement with Gson {@link com.google.gson.Gson}.
     *
     * @param beanClass class of bean
     * @see GsonBeanConverter
     */
    public static <T> BeanConverter<T> gson(Class<T> beanClass) {
        return new GsonBeanConverter<>(beanClass);
    }

    /**
     * Creates a new {@code BeanConverter} instance implement with reflect.
     *
     * <p>Notice: Cannot parse beans of map properties, for nesting map please use {@link BeanConverters#gson(Class)}
     *
     * @param beanClass class of bean
     * @see ReflectBeanConverter
     */
    public static <T> BeanConverter<T> reflect(Class<T> beanClass) {
        return new ReflectBeanConverter<>(beanClass);
    }
}
