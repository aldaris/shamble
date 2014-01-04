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
package org.shamble.common.xml;

import java.io.IOException;
import java.util.List;
import javax.xml.bind.UnmarshalException;
import static org.fest.assertions.Assertions.*;
import org.shamble.common.utils.IOUtils;
import org.shamble.jaxb.saml2.metadata.AttributeAuthorityDescriptorType;
import org.shamble.jaxb.saml2.metadata.EntitiesDescriptorType;
import org.shamble.jaxb.saml2.metadata.EntityDescriptorType;
import org.shamble.jaxb.saml2.metadata.IDPSSODescriptorType;
import org.shamble.jaxb.saml2.metadata.RoleDescriptorType;
import org.shamble.jaxb.saml2.metadata.SPSSODescriptorType;
import org.testng.annotations.Test;

@Test(threadPoolSize = 4, singleThreaded = false)
public class XMLParserTest {

    @Test
    public void idpMetadataWorks() throws Exception {
        EntityDescriptorType entity = XMLUtils.parseXML(readFile("/valid-idp-metadata.xml"),
                EntityDescriptorType.class);
        assertThat(entity).isNotNull();
        List<RoleDescriptorType> descriptors = entity.getRoleDescriptorOrIDPSSODescriptorOrSPSSODescriptor();
        assertThat(descriptors).isNotNull().hasSize(1);
        assertThat(descriptors.get(0)).isInstanceOf(IDPSSODescriptorType.class);
    }

    @Test
    public void spMetadataWorks() throws Exception {
        EntityDescriptorType entity = XMLUtils.parseXML(readFile("/valid-sp-metadata.xml"),
                EntityDescriptorType.class);
        assertThat(entity).isNotNull();
        List<RoleDescriptorType> descriptors = entity.getRoleDescriptorOrIDPSSODescriptorOrSPSSODescriptor();
        assertThat(descriptors).isNotNull().hasSize(1);
        assertThat(descriptors.get(0)).isInstanceOf(SPSSODescriptorType.class);
    }

    @Test
    public void shibbolethMetadataWorks() throws Exception {
        EntitiesDescriptorType entities = XMLUtils.parseXML(readFile("/shibboleth-metadata.xml"),
                EntitiesDescriptorType.class);
        assertThat(entities).isNotNull();
        List<Object> entityList = entities.getEntityDescriptorOrEntitiesDescriptor();
        assertThat(entityList).isNotNull().hasSize(2);
        Object obj = entityList.get(0);
        assertThat(obj).isInstanceOf(EntityDescriptorType.class);
        EntityDescriptorType entity = (EntityDescriptorType) obj;
        List<RoleDescriptorType> descriptors = entity.getRoleDescriptorOrIDPSSODescriptorOrSPSSODescriptor();
        assertThat(descriptors).isNotNull().hasSize(2);
        assertThat(descriptors.get(0)).isInstanceOf(IDPSSODescriptorType.class);
        assertThat(descriptors.get(1)).isInstanceOf(AttributeAuthorityDescriptorType.class);

        obj = entityList.get(1);
        assertThat(obj).isInstanceOf(EntityDescriptorType.class);
        entity = (EntityDescriptorType) obj;
        descriptors = entity.getRoleDescriptorOrIDPSSODescriptorOrSPSSODescriptor();
        assertThat(descriptors).isNotNull().hasSize(1);
        assertThat(descriptors.get(0)).isInstanceOf(SPSSODescriptorType.class);
    }

    @Test(expectedExceptions = UnmarshalException.class)
    public void invalidMetadataFails() throws Exception {
        XMLUtils.parseXML(readFile("/invalid-idp-metadata.xml"), EntityDescriptorType.class);
    }

    private static String readFile(String path) throws IOException {
        return IOUtils.readStream(XMLParserTest.class.getResourceAsStream(path));
    }
}
