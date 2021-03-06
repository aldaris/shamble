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

import com.google.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import org.shamble.common.config.store.ConfigurationBackend;
import org.shamble.common.xml.XMLUtils;
import org.shamble.jaxb.saml2.metadata.EntityDescriptorType;

@Singleton
public class Configuration {

    private final ConfigurationBackend backend;

    @Inject
    public Configuration(ConfigurationBackend backend) {
        this.backend = backend;
        backend.initialize();
    }

    public EntityDescriptorType getEntityDescriptor(String entityId) {
        String metadata = backend.getMetadata(entityId);
        try {
            EntityDescriptorType entity = XMLUtils.parseXML(metadata, EntityDescriptorType.class);
        } catch (JAXBException ex) {
        }
        return null;
    }
}
