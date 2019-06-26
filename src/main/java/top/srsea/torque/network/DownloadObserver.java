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

import java.io.File;

public interface DownloadObserver {
    /**
     * 回调进度
     *
     * @param progress [0, 1]
     */
    void onProgress(double progress);

    /**
     * 下载完成
     *
     * @param saved 保存的文件
     */
    void onCompleted(File saved);

    /**
     * 发生错误
     *
     * @param message 错误信息
     */
    void onError(String message);
}
