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

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO 工具
 */
public class IOUtils {

    /**
     * 尝试关闭所有 resources, 不抛出异常
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

    /**
     * 将输入流中数据写入输出流
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IOException IO 错误
     */
    public static void transfer(@Nonnull InputStream in, @Nonnull OutputStream out) throws IOException {
        transfer(in, out, 4096);
    }

    /**
     * 将输入流中数据写入输出流, 使用指定缓冲大小
     *
     * @param in      输入流
     * @param out     输出流
     * @param bufSize 缓冲大小
     * @throws IOException IO 错误
     */
    public static void transfer(@Nonnull InputStream in, @Nonnull OutputStream out, int bufSize) throws IOException {
        Preconditions.require(bufSize > 0, "buffer size must be positive.");
        byte[] buf = new byte[bufSize];
        int read;
        while ((read = in.read(buf)) != -1) {
            out.write(buf, 0, read);
        }
        out.flush();
    }
}
