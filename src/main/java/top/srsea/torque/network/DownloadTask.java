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

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import top.srsea.torque.common.Conditions;
import top.srsea.torque.common.IOUtils;
import top.srsea.torque.common.StringUtils;

import javax.annotation.Nonnull;
import java.io.*;

/**
 * 下载任务
 */
public class DownloadTask {
    private Subject<Progress> progress;
    private File savePath;
    private String filename;
    private String url;

    private DownloadTask(Builder builder) {
        progress = PublishSubject.create();
        savePath = builder.savePath;
        filename = builder.filename;
        url = builder.url;
    }

    private OkHttpClient newOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Nonnull
                    @Override
                    public Response intercept(@Nonnull Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        return response.newBuilder()
                                .body(new ProgressResponseBody(response.body(), progress))
                                .build();
                    }
                }).build();
    }

    /**
     * 下载进度
     *
     * @return observable progress
     */
    public Observable<Progress> progress() {
        return progress;
    }

    /**
     * 开始下载
     *
     * @return observable file
     */
    public Observable<File> start() {
        return RetrofitProvider.get(newOkHttpClient())
                .create(DownloadService.class)
                .download(url)
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        InputStream stream = responseBody.byteStream();
                        Conditions.require(savePath.exists() || savePath.mkdirs(), new IOException("cannot mkdirs."));
                        if (StringUtils.isBlank(filename)) {
                            filename = url.substring(url.lastIndexOf('/') + 1);
                        }
                        File target = new File(savePath, filename);
                        OutputStream out = new FileOutputStream(target);
                        IOUtils.transfer(stream, out);
                        IOUtils.close(stream, out);
                        return target;
                    }
                });
    }

    public static class Builder {
        File savePath;
        String filename;
        String url;

        public Builder savePath(File savePath) {
            this.savePath = savePath;
            return this;
        }

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public DownloadTask build() {
            if (StringUtils.isBlank(url)) throw new IllegalArgumentException("url cannot be blank.");
            if (savePath == null) savePath = new File(System.getenv("HOME"), "Downloads");
            return new DownloadTask(this);
        }
    }
}
