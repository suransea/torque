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
 * Common IO utilities.
 *
 * @author sea
 */
public class IOHelper {
    private IOHelper() {
    }

    /**
     * Closes all the resources silently.
     *
     * @param resources resources array, item implemented {@link Closeable}
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
     * Writes data from the input stream to the output stream.
     *
     * @param in  source
     * @param out target
     * @throws IOException if an I/O error occurs
     */
    public static void transfer(@Nonnull InputStream in, @Nonnull OutputStream out) throws IOException {
        transfer(in, out, 4096);
    }

    /**
     * Writes data from the input stream to the output stream, using the specified buffer size.
     *
     * @param in      source
     * @param out     target
     * @param bufSize buffer size, must be positive
     * @throws IOException if an I/O error occurs
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
