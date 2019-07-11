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

public class Progress {
    private long current;
    private long total;

    public Progress(long current, long total) {
        this.current = current;
        this.total = total;
    }

    public long current() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long total() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public double ratio() {
        if (total <= 0) return 1;
        return (double) current / (double) total;
    }
}
