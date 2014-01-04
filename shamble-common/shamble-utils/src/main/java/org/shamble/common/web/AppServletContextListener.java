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
package org.shamble.common.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.shamble.common.config.Configuration;
import org.shamble.common.di.InjectorHolder;
import org.shamble.common.lifecycle.LifecycleManager;

public class AppServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("hello");
        InjectorHolder.getInstance(Configuration.class);
        LifecycleManager.initialize();
        System.out.println("world");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LifecycleManager.shutdown();
    }
}
