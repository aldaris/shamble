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
import javax.xml.bind.UnmarshalException;
import org.shamble.jaxb.saml2.metadata.EntitiesDescriptorType;
import org.shamble.common.utils.IOUtils;
import org.shamble.jaxb.saml2.metadata.EntityDescriptorType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


@Test(threadPoolSize = 2, singleThreaded = false)
public class UnmarshallerTest {

    @DataProvider(name = "success")
    public Object[][] getValidMetadata() {
        return new Object[][]{{"/idp-metadata-success.xml"}, {"/sp-metadata-success.xml"}};
    }

    @DataProvider(name = "failure")
    public Object[][] getInvalidMetadata() {
        return new Object[][]{{"/idp-metadata-failure.xml"}};
    }

    @Test(dataProvider = "success")
    public void metadataSucceeds(String path) throws Exception {
        EntityDescriptorType desc = XMLUtils.parseXML(readFile(path), EntityDescriptorType.class);
        assert desc != null;
    }

    public void compositeMetadataSuccess() throws Exception {
        EntitiesDescriptorType entities = XMLUtils.parseXML(readFile("/shibboleth-metadata.xml"),
                EntitiesDescriptorType.class);
        assert entities != null;
    }

    @Test(dataProvider = "failure", expectedExceptions = UnmarshalException.class)
    public void metadataFails(String path) throws Exception {
        XMLUtils.parseXML(readFile(path), EntityDescriptorType.class);
    }

    private static String readFile(String path) throws IOException {
        return IOUtils.readStream(UnmarshallerTest.class.getResourceAsStream(path));
    }
}
