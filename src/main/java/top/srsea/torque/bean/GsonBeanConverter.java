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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * 基于Gson的Java Bean转换器
 *
 * @param <T>
 */
public class GsonBeanConverter<T> implements BeanConverter<T> {
    private Class<T> beanClass;
    private Gson gson = new Gson();

    public GsonBeanConverter(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public T fromMap(@Nonnull Map<?, ?> map) {
        return gson.fromJson(gson.toJsonTree(map), beanClass);
    }

    @Override
    public List<T> fromMapList(@Nonnull List<? extends Map<?, ?>> mapList) {
        return gson.fromJson(gson.toJsonTree(mapList), new TypeToken<List<T>>() {
        }.getType());
    }

    @Override
    public Map<String, Object> toMap(@Nonnull T bean) {
        return gson.fromJson(gson.toJsonTree(bean), new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    @Override
    public List<Map<String, Object>> toMapList(@Nonnull List<T> beanList) {
        return gson.fromJson(gson.toJsonTree(beanList), new TypeToken<List<Map<String, Object>>>() {
        }.getType());
    }
}
