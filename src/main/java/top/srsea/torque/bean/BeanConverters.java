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
 * Java Bean转化器工厂
 */
public class BeanConverters {

    /**
     * 获取基于Gson的BeanConverter
     * {@link GsonBeanConverter}
     */
    public static <T> BeanConverter<T> gson(Class<T> beanClass) {
        return new GsonBeanConverter<>(beanClass);
    }

    /**
     * 获取基于反射的BeanConverter
     * {@link ReflectBeanConverter}
     * 缺陷: 无法解析map属性为bean, 如有嵌套Bean请使用{@link BeanConverters#gson(Class)}
     */
    public static <T> BeanConverter<T> reflect(Class<T> beanClass) {
        return new ReflectBeanConverter<>(beanClass);
    }
}
