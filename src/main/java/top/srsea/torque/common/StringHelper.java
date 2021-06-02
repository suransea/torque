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
import java.util.Arrays;
import java.util.Iterator;

/**
 * String utilities.
 *
 * @author sea
 */
public class StringHelper {

    /**
     * Returns if the str is null or ""
     *
     * @param str source
     * @return true if is empty
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * Returns if the content is blank
     *
     * @param content source
     * @return true if is blank
     */
    public static boolean isBlank(@Nullable String content) {
        return isEmpty(content) || "".equals(content.trim());
    }

    /**
     * Joins a iterable to a string with the delimiter.
     */
    public static String join(CharSequence delimiter, Iterable<?> iterable) {
        if (iterable == null) {
            return null;
        }
        Iterator<?> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder(String.valueOf(iterator.next()));
        while (iterator.hasNext()) {
            builder.append(delimiter);
            builder.append(iterator.next());
        }
        return builder.toString();
    }

    /**
     * Joins elements to a string with the delimiter.
     */
    public static String join(CharSequence delimiter, Object... elements) {
        return join(delimiter, Arrays.asList(elements));
    }
}
