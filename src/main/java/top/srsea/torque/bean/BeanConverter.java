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
 * Java Bean转换器
 *
 * @param <T>
 */
public interface BeanConverter<T> {
    /**
     * 将 map 解析为 Java bean
     *
     * @param map Source map
     * @return Bean
     */
    T fromMap(@Nonnull Map<?, ?> map);

    /**
     * 将 map list 解析为 Java bean list
     *
     * @param mapList Source map list
     * @return Bean list
     */
    List<T> fromMapList(@Nonnull List<? extends Map<?, ?>> mapList);

    /**
     * 将 bean 转换为 map
     *
     * @param bean Source bean
     * @return map
     */
    Map<String, Object> toMap(@Nonnull T bean);

    /**
     * 将 bean list 转换为 map list
     *
     * @param beanList Source bean list
     * @return map list
     */
    List<Map<String, Object>> toMapList(@Nonnull List<T> beanList);
}
