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
import okhttp3.*;
import top.srsea.torque.common.Preconditions;
import top.srsea.torque.common.IOUtils;
import top.srsea.torque.common.StringUtils;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URI;
import java.net.URLDecoder;

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
                        if (StringUtils.isBlank(filename)) {
                            filename = obtainFilename(response);
                        }
                        return response.newBuilder()
                                .body(new ProgressResponseBody(response.body(), progress))
                                .build();
                    }
                }).build();
    }

    private String obtainFilename(Response response) {
        String filename = obtainFilename(response.headers());
        if (StringUtils.isBlank(filename)) {
            filename = obtainFilename(response.request().url().uri());
        }
        if (StringUtils.isBlank(filename)) {
            throw new IllegalArgumentException("cannot obtain filename.");
        }
        return filename;
    }

    private String obtainFilename(Headers headers) {
        if (headers == null) {
            return null;
        }
        String contentDisposition = headers.get("Content-Disposition");
        final String mark = "filename=";
        if (contentDisposition == null || !contentDisposition.contains(mark)) {
            return null;
        }
        try {
            String encodedFilename = contentDisposition.substring(contentDisposition.indexOf(mark) + 9);
            String filename = URLDecoder.decode(encodedFilename, "UTF-8").trim();
            if (filename.startsWith("\"") && filename.endsWith("\"")) {
                filename = filename.substring(1, filename.length() - 1);
            }
            return filename.trim();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private String obtainFilename(URI uri) {
        if (uri == null) {
            return null;
        }
        String path = uri.getPath();
        if (path == null) {
            return null;
        }
        String filename = path.substring(path.lastIndexOf('/') + 1);
        try {
            filename = URLDecoder.decode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return filename;
        }
        return filename;
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
                        Preconditions.require(savePath.exists() || savePath.mkdirs(), new IOException("cannot mkdirs."));
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
            if (StringUtils.isBlank(url)) {
                throw new IllegalArgumentException("url cannot be blank.");
            }
            if (savePath == null) {
                savePath = new File(System.getenv("HOME"), "Downloads");
            }
            return new DownloadTask(this);
        }
    }
}
