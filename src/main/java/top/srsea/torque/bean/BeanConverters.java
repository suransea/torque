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

import java.lang.reflect.Constructor;

/**
 * Java Bean转化器工厂
 */
public class BeanConverters {
    public static <T> BeanConverter<T> of(Class<T> beanClass) {
        return new ReflectBeanConverter<>(beanClass);
    }

    public static <T> BeanConverter<T> of(Class<T> beanClass, Class<? extends BeanConverter<T>> converterClass) {
        try {
            Constructor<? extends BeanConverter<T>> constructor = converterClass.getConstructor(Class.class);
            return constructor.newInstance(beanClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
