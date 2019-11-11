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

/**
 * A typical ResponseBody with progress.
 *
 * @author sea
 * @see ResponseBody
 */
public class ProgressResponseBody extends ResponseBody {

    /**
     * Inner ResponseBody.
     */
    private final ResponseBody responseBody;

    /**
     * Observer of the progress, to publish progress.
     */
    private final Observer<Progress> progressObserver;

    /**
     * Current bytes read.
     */
    private long totalBytesRead;

    /**
     * ResponseBody source buffer.
     */
    private BufferedSource bufferedSource;


    /**
     * Constructs an instance with the specific response body and progress observer.
     *
     * @param responseBody     the specific response body
     * @param progressObserver the specific progress observer
     */
    public ProgressResponseBody(ResponseBody responseBody, Observer<Progress> progressObserver) {
        this.responseBody = responseBody;
        this.progressObserver = progressObserver;
    }

    /**
     * Returns inner content type of response body.
     *
     * @return content type of response body
     */
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    /**
     * Returns inner content length of response body.
     *
     * @return content length of response body
     */
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    /**
     * Returns a buffered source of response body.
     *
     * @return buffered source
     */
    @Nonnull
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * Wrapped the source with {@link ForwardingSource} to obtain read bytes.
     *
     * @param source source
     * @return wrapped source
     */
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
