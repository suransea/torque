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

import top.srsea.torque.common.StringUtils;

import java.io.File;

/**
 * 下载参数
 */
public class DownloadParam {
    private File savePath;
    private String filename;
    private String url;

    public DownloadParam(Builder builder) {
        savePath = builder.savePath;
        filename = builder.filename;
        url = builder.url;
    }

    public File getSavePath() {
        return savePath;
    }

    public void setSavePath(File savePath) {
        this.savePath = savePath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

        public DownloadParam build() {
            if (StringUtils.isBlank(url)) throw new IllegalArgumentException("url cannot be blank.");
            if (savePath == null) savePath = new File(System.getenv("HOME"), "Downloads");
            return new DownloadParam(this);
        }
    }
}
