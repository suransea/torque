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
 * Java bean converter base on {@link Gson}.
 * Converting between java bean object and java {@link Map} object.
 *
 * @param <T> class of bean
 * @author sea
 * @see BeanConverter
 */
public class GsonBeanConverter<T> implements BeanConverter<T> {

    /**
     * Class of bean.
     */
    private Class<T> beanClass;

    /**
     * Gson instance for parse or generate json.
     */
    private Gson gson = new Gson();

    /**
     * Constructs an instance with the specific bean class.
     *
     * @param beanClass class of bean
     */
    public GsonBeanConverter(Class<T> beanClass) {
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
        return gson.fromJson(gson.toJsonTree(map), beanClass);
    }

    /**
     * Returns a java bean list parsed from map list.
     *
     * @param mapList source map list
     * @return a java bean list parsed from map list
     */
    @Override
    public List<T> fromMapList(@Nonnull List<? extends Map<?, ?>> mapList) {
        return gson.fromJson(gson.toJsonTree(mapList), new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * Returns a map contains properties of the specific bean.
     *
     * @param bean source bean
     * @return a map contains properties of the specific bean.
     */
    @Override
    public Map<String, Object> toMap(@Nonnull T bean) {
        return gson.fromJson(gson.toJsonTree(bean), new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * Returns a map list contains every map of bean from the bean list.
     *
     * @param beanList source bean list
     * @return a map list contains every map of bean from the bean list
     */
    @Override
    public List<Map<String, Object>> toMapList(@Nonnull List<T> beanList) {
        return gson.fromJson(gson.toJsonTree(beanList), new TypeToken<List<Map<String, Object>>>() {
        }.getType());
    }
}
