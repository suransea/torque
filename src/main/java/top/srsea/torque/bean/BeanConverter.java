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
import java.util.List;
import java.util.Map;

/**
 * Java bean converter.
 * Converting between java bean object and java {@link Map} object.
 *
 * @param <T> class of bean
 * @author sea
 * @see GsonBeanConverter
 * @see ReflectBeanConverter
 */
public interface BeanConverter<T> {

    /**
     * Returns a java bean parsed from map.
     *
     * @param map source map
     * @return a java bean parsed from map
     */
    T fromMap(@Nonnull Map<?, ?> map);

    /**
     * Returns a java bean list parsed from map list.
     *
     * @param mapList source map list
     * @return a java bean list parsed from map list
     */
    List<T> fromMapList(@Nonnull List<? extends Map<?, ?>> mapList);

    /**
     * Returns a map contains properties of the specific bean.
     *
     * @param bean source bean
     * @return a map contains properties of the specific bean.
     */
    Map<String, Object> toMap(@Nonnull T bean);

    /**
     * Returns a map list contains every map of bean from the bean list.
     *
     * @param beanList source bean list
     * @return a map list contains every map of bean from the bean list
     */
    List<Map<String, Object>> toMapList(@Nonnull List<T> beanList);
}
