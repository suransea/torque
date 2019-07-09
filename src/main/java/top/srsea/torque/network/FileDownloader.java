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
 * 文件下载器
 */
public class FileDownloader {

    /**
     * 下载文件
     *
     * @param param 下载参数
     */
    public static Observable<File> download(DownloadParam param) {
        return download(param, null);
    }

    /**
     * 下载文件, 指定保存的文件名
     *
     * @param param    下载参数
     * @param observer 进度回调
     */
    public static Observable<File> download(final DownloadParam param, final ProgressObserver observer) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (observer != null) {
            builder.addInterceptor(new Interceptor() {
                @Nonnull
                @Override
                public Response intercept(@Nonnull Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    return response.newBuilder()
                            .body(new ProgressResponseBody(response.body(), observer))
                            .build();
                }
            });
        }
        final String url = param.getUrl();
        return RetrofitProvider.get(builder.build())
                .create(DownloadService.class)
                .download(url)
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        InputStream stream = responseBody.byteStream();
                        File path = param.getSavePath();
                        Conditions.require(path.exists() || path.mkdirs(), new IOException("cannot mkdirs."));
                        String filename = param.getFilename();
                        if (StringUtils.isBlank(filename)) {
                            filename = url.substring(url.lastIndexOf('/') + 1);
                        }
                        File target = new File(path, filename);
                        OutputStream out = new FileOutputStream(target);
                        IOUtils.transfer(stream, out);
                        IOUtils.close(stream, out);
                        return target;
                    }
                });
    }
}
