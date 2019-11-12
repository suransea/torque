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
import top.srsea.torque.common.IOHelper;
import top.srsea.torque.common.Preconditions;
import top.srsea.torque.common.StringHelper;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URI;
import java.net.URLDecoder;

/**
 * A download task, with observable progress.
 *
 * @author sea
 */
public class DownloadTask {

    /**
     * A progress subject to publish and subscribe.
     */
    private Subject<Progress> progress;

    /**
     * Parent path to save file, default is {@code ${HOME}/Downloads}
     */
    private File savePath;

    /**
     * Filename of download file, if empty, it will be obtained from response.
     */
    private String filename;

    /**
     * Remote file URL, required.
     */
    private String url;

    /**
     * Constructs an instance with builder.
     *
     * @param builder the specific builder.
     */
    private DownloadTask(Builder builder) {
        progress = PublishSubject.create();
        savePath = builder.savePath;
        filename = builder.filename;
        url = builder.url;
    }

    /**
     * Creates a new {@code OkHttpClient} instance with progress observer.
     *
     * @return new {@code OkHttpClient} instance with progress observer
     */
    private OkHttpClient newOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Nonnull
                    @Override
                    public Response intercept(@Nonnull Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        if (StringHelper.isBlank(filename)) {
                            filename = obtainFilename(response);
                        }
                        return response.newBuilder()
                                .body(new ProgressResponseBody(response.body(), progress))
                                .build();
                    }
                }).build();
    }

    /**
     * Obtains a filename from the response.
     *
     * <p>Obtains a filename from headers preferentially, if failed, obtain a filename from url path.
     *
     * @param response response to obtain a filename.
     * @return filename
     * @throws IllegalArgumentException cannot obtain a filename from the response
     */
    private String obtainFilename(Response response) {
        String filename = obtainFilename(response.headers());
        if (StringHelper.isBlank(filename)) {
            filename = obtainFilename(response.request().url().uri());
        }
        if (StringHelper.isBlank(filename)) {
            throw new IllegalArgumentException("cannot obtain a filename.");
        }
        return filename;
    }

    /**
     * Obtains a filename from the headers.
     *
     * <p>Obtains a filename from headers, if failed, returns null.
     *
     * @param headers headers to obtain a filename.
     * @return filename
     */
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

    /**
     * Obtains a filename from the uri.
     *
     * <p>Obtains a filename from the uri, if failed, returns null.
     *
     * @param uri uri to obtain a filename.
     * @return filename
     */
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
     * Observable progress of this download task.
     *
     * @return observable progress
     */
    public Observable<Progress> progress() {
        return progress;
    }

    /**
     * Starts this download task.
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
                        IOHelper.transfer(stream, out);
                        IOHelper.close(stream, out);
                        return target;
                    }
                });
    }

    /**
     * Builder of {@link DownloadTask}
     *
     * @see DownloadTask
     */
    public static class Builder {

        /**
         * Parent path to save file.
         */
        File savePath;

        /**
         * Filename of download file.
         */
        String filename;

        /**
         * Remote file URL.
         */
        String url;

        /**
         * Sets the save path.
         *
         * @param savePath the specific save path
         * @return current builder
         */
        public Builder savePath(File savePath) {
            this.savePath = savePath;
            return this;
        }

        /**
         * Sets the filename.
         *
         * @param filename the specific filename
         * @return current builder
         */
        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        /**
         * Sets the url.
         *
         * @param url the specific url
         * @return current builder
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * Builds a {@code DownloadTask} with this builder.
         *
         * @return {@code DownloadTask} instance
         */
        public DownloadTask build() {
            if (StringHelper.isBlank(url)) {
                throw new IllegalArgumentException("url cannot be blank.");
            }
            if (savePath == null) {
                savePath = new File(System.getenv("HOME"), "Downloads");
            }
            return new DownloadTask(this);
        }
    }
}