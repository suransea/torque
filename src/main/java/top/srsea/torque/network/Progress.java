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

/**
 * A progress status.
 *
 * @author sea
 */
public class Progress {

    /**
     * Current value, usual as bytes.
     */
    private long current;

    /**
     * Total value, usual as bytes.
     */
    private long total;

    /**
     * Constructs an instance with current and total.
     *
     * @param current current value
     * @param total   total value
     */
    public Progress(long current, long total) {
        this.current = current;
        this.total = total;
    }

    /**
     * Gets the current value.
     *
     * @return current value
     */
    public long current() {
        return current;
    }


    /**
     * Sets the current value.
     *
     * @param current current value
     */
    public void setCurrent(long current) {
        this.current = current;
    }

    /**
     * Gets the total value.
     *
     * @return total value
     */
    public long total() {
        return total;
    }

    /**
     * Sets the total value.
     *
     * @param total total value.
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * Returns the ratio between current and total.
     *
     * @return the ratio between current and total
     */
    public double ratio() {
        if (total <= 0) return 1;
        return (double) current / (double) total;
    }
}
