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
package org.shamble.common.di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

public enum InjectorHolder {

    INSTANCE;
    private final Injector injector;

    private InjectorHolder() {
        injector = Guice.createInjector(new CommonGuiceModule());
    }

    public static <T> T getInstance(Class<T> klazz) {
        return INSTANCE.injector.getInstance(klazz);
    }

    public static <T> T getInstance(Key<T> key) {
        return INSTANCE.injector.getInstance(key);
    }
}
