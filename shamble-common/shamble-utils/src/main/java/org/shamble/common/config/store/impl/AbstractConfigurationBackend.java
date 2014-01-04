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
package org.shamble.common.config.store.impl;

import java.util.Properties;
import org.shamble.common.config.BootstrapConfiguration;
import org.shamble.common.config.store.ConfigurationBackend;

public abstract class AbstractConfigurationBackend implements ConfigurationBackend {

    protected final Properties bootstrapProperties;

    public AbstractConfigurationBackend() {
        bootstrapProperties = BootstrapConfiguration.INSTANCE.getBootstrapProperties();
    }
}