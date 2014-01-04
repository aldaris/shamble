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
package org.shamble.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import static org.shamble.common.config.ConfigurationConstants.*;
import org.shamble.common.config.store.ConfigurationBackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The bootstrap configuration contains the important details on where the configuration is currently stored (e.g. in
 * an LDAPv3 compliant directory or in files). The bootstrap configuration is stored in a simple properties file and the
 * location of the bootstrap file is determined in the following order
 * <ul>
 * <li>Using the <code>-Dorg.shamble.bootstrap.file</code> JVM property.</li>
 * <li>Checking the existence of <code>${user.home}/shamble/shamble-bootstrap.properties</code> file.</li>
 * <li>Trying to load <code>shamble-bootstrap.properties</code> file from the classpath.</li>
 * </ul>
 *
 * The bootstrap configuration's main purpose is to define the configuration backend implementation and also to provide
 * the necessary settings for the backend.
 */
public enum BootstrapConfiguration {

    INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(BootstrapConfiguration.class);
    private final Properties bootstrapProperties = new Properties();

    private BootstrapConfiguration() {
        InputStream is = null;
        String bootstrapLocation = System.getProperty(BOOTSTRAP_JVM_PROPERTY);
        if (bootstrapLocation != null) {
            is = getInputStream(bootstrapLocation);
        } else {
            logger.debug("The JVM property \"{}\" was not set", BOOTSTRAP_JVM_PROPERTY);
        }
        if (is == null) {
            bootstrapLocation = System.getProperty(USER_HOME_JVM_PROPERTY);
            bootstrapLocation += File.separator + SHAMBLE + File.separator + DEFAULT_BOOTSTRAP_FILE;
            is = getInputStream(bootstrapLocation);
        }
        if (is == null) {
            is = BootstrapConfiguration.class.getResourceAsStream("/" + DEFAULT_BOOTSTRAP_FILE);
        }
        if (is == null) {
            logger.error("Unable to find bootstrap configuration");
            throw new IllegalStateException("Unable to find bootstrap configuration");
        }
        try {
            bootstrapProperties.load(is);
        } catch (IOException ioe) {
            logger.error("An IO error occurred while loading the bootstrap configuration", ioe);
            throw new IllegalStateException("IO error while loading bootstrap configuration", ioe);
        }
    }

    /**
     * Returns the initialized configuration backend set in the bootstrap configuration. This object instance should be
     * singleton in the injector's context.
     *
     * @return The configuration backend.
     * @throws IllegalStateException if the configured backend cannot be instantiated.
     */
    public ConfigurationBackend getConfigurationBackend() {
        String impl = bootstrapProperties.getProperty(BACKEND_IMPL);
        try {
            return Class.forName(impl).asSubclass(ConfigurationBackend.class).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            logger.error("Unable to instantiate configuration backend", ex);
            throw new IllegalStateException("Unable to initialize configuration backend");
        }
    }

    public Properties getBootstrapProperties() {
        return new Properties(bootstrapProperties);
    }

    /**
     * Returns a FileInputStream for the provided path or null if the path did not exist.
     *
     * @param path Path to the bootstrap config file.
     * @return A {@link FileInputStream} instance for the provided path.
     */
    private InputStream getInputStream(String path) {
        try {
            return new FileInputStream(new File(path));
        } catch (FileNotFoundException fnfe) {
            logger.debug("Unable to find bootstrap configuration under " + path);
        }
        return null;
    }
}
