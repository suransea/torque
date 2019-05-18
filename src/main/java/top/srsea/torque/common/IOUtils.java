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

import java.io.Closeable;
import java.io.IOException;

/**
 * IO 工具
 */
public class IOUtils {

    /**
     * 尝试关闭所有 resources
     *
     * @param resources IO streams, etc...
     */
    public static void close(Closeable... resources) {
        for (Closeable resource : resources) {
            if (resource == null) continue;
            try {
                resource.close();
            } catch (IOException ignored) {
            }
        }
    }
}
