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
 * 条件判断
 */
public class Preconditions {

    /**
     * 非空检查
     */
    public static void requireNonNull(@Nullable Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    /**
     * 要求表达式为真, 否则抛出运行时异常
     *
     * @param exp bool 表达式
     */
    public static void require(boolean exp) {
        require(exp, "Failed requirement.");
    }

    /**
     * 要求表达式为真, 否则抛出运行时异常
     *
     * @param exp bool 表达式
     * @param msg 异常 message
     */
    public static void require(boolean exp, String msg) {
        require(exp, new RuntimeException(msg));
    }

    /**
     * 要求表达式为真, 否则抛出指定运行时异常
     *
     * @param exp       bool 表达式
     * @param elseThrow which exception to throw
     */
    public static void require(boolean exp, RuntimeException elseThrow) {
        if (exp) return;
        throw elseThrow;
    }

    /**
     * 要求表达式为真, 否则抛出指定异常
     *
     * @param exp       bool 表达式
     * @param elseThrow which exception to throw
     */
    public static <T extends Throwable> void require(boolean exp, T elseThrow) throws T {
        if (exp) return;
        throw elseThrow;
    }
}
