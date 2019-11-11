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
 * Preconditions.
 *
 * @author sea
 */
public class Preconditions {

    /**
     * Ensures the object is not null or throws a NullPointerException.
     *
     * @throws NullPointerException the object is null
     */
    public static void requireNonNull(@Nullable Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Ensures the expression is true or throws a RuntimeException.
     *
     * @param exp expression
     * @throws RuntimeException expression is {@code false}
     */
    public static void require(boolean exp) {
        require(exp, "A Failed requirement.");
    }

    /**
     * Ensures the expression is true or throws a RuntimeException, with the specific message.
     *
     * @param exp expression
     * @param msg message for RuntimeException
     * @throws RuntimeException expression is {@code false}
     */
    public static void require(boolean exp, String msg) {
        require(exp, new RuntimeException(msg));
    }

    /**
     * Ensures the expression is true or throws a specific RuntimeException.
     *
     * @param exp       expression
     * @param elseThrow exception to throw
     * @throws RuntimeException expression is {@code false}
     */
    public static void require(boolean exp, RuntimeException elseThrow) {
        if (exp) return;
        throw elseThrow;
    }

    /**
     * Ensures the expression is true or throws a specific Exception.
     *
     * @param exp       expression
     * @param elseThrow exception to throw
     * @param <T>       type of exception
     */
    public static <T extends Throwable> void require(boolean exp, T elseThrow) throws T {
        if (exp) return;
        throw elseThrow;
    }
}
