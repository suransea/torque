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

import javax.annotation.Nullable;

/**
 * 字符串工具
 */
public class Strings {
    private static final char UNDERLINE = '_';

    /**
     * the str is null or ""
     *
     * @param str source
     * @return true if is empty
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * the content is blank
     *
     * @param content source
     * @return true if is blank
     */
    public static boolean isBlank(@Nullable String content) {
        return isEmpty(content) || "".equals(content.trim());
    }

    /**
     * camelCase to snake_case
     *
     * @param content source
     * @return target
     */
    public static String camelToSnake(String content) {
        if (isBlank(content)) {
            return "";
        }
        int length = content.length();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            char c = content.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append(UNDERLINE);
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * snake_case to camelCase
     *
     * @param content source
     * @return target
     */
    public static String snakeToCamel(String content) {
        if (isBlank(content)) {
            return "";
        }
        int length = content.length();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            char c = content.charAt(i);
            if (c == UNDERLINE) {
                if (++i < length) {
                    result.append(Character.toUpperCase(content.charAt(i)));
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
