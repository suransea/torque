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

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import top.srsea.torque.common.Conditions;
import top.srsea.torque.common.IOUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;

public class DownloadUtils {

    /**
     * 下载文件
     *
     * @param url      文件地址
     * @param path     保存路径
     * @param fileName 保存文件名
     * @param observer 下载回调
     */
    public static Disposable download(@Nonnull String url, @Nullable final File path,
                                      @Nonnull final String fileName, @Nullable final DownloadObserver observer) {
        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void onProgress(long bytesWritten, long contentLength, boolean done) {
                if (observer == null) return;
                double progress = ((double) bytesWritten) / ((double) contentLength);
                observer.onProgress(progress);
            }
        };
        return RetrofitProvider.get(new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Nonnull
                    @Override
                    public Response intercept(@Nonnull Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        return response.newBuilder()
                                .body(new ProgressResponseBody(response.body(), progressListener))
                                .build();
                    }
                }).build())
                .create(DownloadService.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        InputStream stream = responseBody.byteStream();
                        File savePath = path;
                        if (savePath == null) {
                            savePath = new File(System.getenv("HOME"), "Downloads");
                        }
                        Conditions.require(savePath.exists() || savePath.mkdirs(), new IOException("cannot mkdirs."));
                        File target = new File(savePath, fileName);
                        OutputStream out = new FileOutputStream(target);
                        IOUtils.transfer(stream, out);
                        IOUtils.close(stream, out);
                        if (observer == null) return;
                        observer.onCompleted(target);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        if (observer == null) return;
                        observer.onError(throwable.getMessage());
                    }
                });
    }
}
