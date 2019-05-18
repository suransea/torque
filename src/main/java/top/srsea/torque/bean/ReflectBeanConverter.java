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


import com.sun.istack.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于反射的Java Bean转换器
 *
 * @param <T>
 */
public class ReflectBeanConverter<T> implements BeanConverter<T> {
    private Class<T> beanClass;

    public ReflectBeanConverter(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public T fromMap(@NotNull Map<?, ?> map) {
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

    @Override
    public List<T> fromMapList(@NotNull List<? extends Map<?, ?>> mapList) {
        List<T> beanList = new ArrayList<>();
        for (Map<?, ?> map : mapList) {
            T bean = fromMap(map);
            if (bean == null) continue;
            beanList.add(bean);
        }
        return beanList;
    }

    @Override
    public Map<String, Object> toMap(@NotNull T bean) {
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

    @Override
    public List<Map<String, Object>> toMapList(@NotNull List<T> beanList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (T bean : beanList) {
            Map<String, Object> map = toMap(bean);
            if (map == null) continue;
            mapList.add(map);
        }
        return mapList;
    }
}
