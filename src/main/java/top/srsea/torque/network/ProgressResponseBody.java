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

package top.srsea.torque.network;

import io.reactivex.Observer;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.*;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private final Observer<Progress> progressObserver;
    private long totalBytesRead;
    private BufferedSource bufferedSource;


    public ProgressResponseBody(ResponseBody responseBody, Observer<Progress> progressObserver) {
        this.responseBody = responseBody;
        this.progressObserver = progressObserver;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Nonnull
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
            public long read(@Nonnull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                progressObserver.onNext(new Progress(totalBytesRead, responseBody.contentLength()));
                return bytesRead;
            }
        };
    }
}
