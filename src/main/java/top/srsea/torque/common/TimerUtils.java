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

package top.srsea.torque.common;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时器工具
 */
public class TimerUtils {
    private final Timer timer = new Timer();

    /**
     * run after delay
     *
     * @param delay mill second
     * @param task  what to run
     */
    public static void runDelayed(long delay, final Runnable task) {
        Singleton.INSTANCE.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (task == null) return;
                task.run();
            }
        }, delay);
    }

    /**
     * run at start time
     *
     * @param start time
     * @param task  what to run
     */
    public static void runAt(Date start, final Runnable task) {
        Singleton.INSTANCE.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (task == null) return;
                task.run();
            }
        }, start);
    }

    /**
     * run period after delay
     *
     * @param delay  mill second
     * @param period interval
     * @param task   what to run
     */
    public static void runPeriod(long delay, long period, final Runnable task) {
        Singleton.INSTANCE.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (task == null) return;
                task.run();
            }
        }, delay, period);
    }

    /**
     * run period at start time
     *
     * @param start  time
     * @param period interval
     * @param task   what to run
     */
    public static void runPeriod(Date start, long period, final Runnable task) {
        Singleton.INSTANCE.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (task == null) return;
                task.run();
            }
        }, start, period);
    }

    private static class Singleton {
        private static final TimerUtils INSTANCE = new TimerUtils();
    }
}
