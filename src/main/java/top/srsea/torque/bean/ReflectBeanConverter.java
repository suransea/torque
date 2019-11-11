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

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java bean converter base on reflect.
 * Converting between java bean object and java {@link Map} object.
 *
 * @param <T> class of bean
 * @author sea
 */
public class ReflectBeanConverter<T> implements BeanConverter<T> {

    /**
     * Class of bean.
     */
    private Class<T> beanClass;

    /**
     * Constructs an instance with the specific bean class.
     *
     * @param beanClass class of bean
     */
    public ReflectBeanConverter(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * Returns a java bean parsed from map.
     *
     * @param map source map
     * @return a java bean parsed from map
     */
    @Override
    public T fromMap(@Nonnull Map<?, ?> map) {
        try {
            T bean = beanClass.newInstance();
            for (Field field : beanClass.getDeclaredFields()) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) continue;
                field.setAccessible(true);
                Object value = map.get(field.getName());
                if (value == null) continue;
                if (field.getType().isAssignableFrom(value.getClass())) {
                    field.set(bean, value);
                }
            }
            return bean;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a java bean list parsed from map list.
     * Loop call {@link BeanConverter#fromMap(Map)} until all the maps has been converted.
     * Each failed conversion will be ignored.
     *
     * @param mapList source map list
     * @return a java bean list parsed from map list
     */
    @Override
    public List<T> fromMapList(@Nonnull List<? extends Map<?, ?>> mapList) {
        List<T> beanList = new ArrayList<>();
        for (Map<?, ?> map : mapList) {
            T bean = fromMap(map);
            if (bean == null) continue;
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * Returns a map contains properties of the specific bean.
     *
     * @param bean source bean
     * @return a map contains properties of the specific bean.
     */
    @Override
    public Map<String, Object> toMap(@Nonnull T bean) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : beanClass.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) continue;
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(bean));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * Returns a map list contains every map of bean from the bean list.
     * Loop call {@link BeanConverter#toMap(Object)} until all the beans has been converted.
     * Each failed conversion will be ignored.
     *
     * @param beanList source bean list
     * @return a map list contains every map of bean from the bean list
     */
    @Override
    public List<Map<String, Object>> toMapList(@Nonnull List<T> beanList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (T bean : beanList) {
            Map<String, Object> map = toMap(bean);
            if (map == null) continue;
            mapList.add(map);
        }
        return mapList;
    }
}
