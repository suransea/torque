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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

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
        return new Downloader(path).download(url, fileName, observer);
    }
}
