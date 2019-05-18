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
 *
 */

package top.srsea.torque.network;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.*;

import java.io.IOException;

public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private final ProgressListener progressListener;
    @Nullable
    private BufferedSource bufferedSource;
    private long totalBytesRead;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
        totalBytesRead = 0L;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    public long totalBytesRead() {
        return totalBytesRead;
    }

    @NotNull
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            @Override
            public long read(@NotNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                progressListener.onProgress(
                        totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }
}