/**
 * Copyright (c) 2010 Peter Major
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
package org.shamble.common.lifecycle;

import java.util.HashSet;
import java.util.Set;

public final class LifecycleManager {

    private static final Set<LifecycleListener> LISTENERS = new HashSet<>();
    private static volatile boolean shutdownInProgress = false;

    public static void addLifecycleListener(LifecycleListener listener) {
        LISTENERS.add(listener);
    }

    public static void removeLifecycleListener(LifecycleListener listener) {
        LISTENERS.remove(listener);
    }

    public static boolean isShutdownInProgress() {
        return shutdownInProgress;
    }

    public static void initialize() {
        if (isShutdownInProgress()) {
            throw new IllegalStateException("Shutdown is currently in progress, unable to reinitialize system");
        }
        for (LifecycleListener listener : LISTENERS) {
            listener.initialize();
        }
    }

    public static void shutdown() {
        shutdownInProgress = true;
        for (LifecycleListener listener : LISTENERS) {
            listener.shutdown();
        }
        shutdownInProgress = false;
    }
}
