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
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtils {

    private static final String JAXB_PACKAGES = "org.shamble.jaxb.xmlenc:"
            + "org.shamble.jaxb.xmldsig:"
            + "org.shamble.jaxb.saml2.assertion:"
            + "org.shamble.jaxb.saml2.metadata:"
            + "org.shamble.jaxb.saml2.protocol:";
    private static JAXBContext context;
    private static Schema metadataSchema;

    static {
        try {
            context = JAXBContext.newInstance(JAXB_PACKAGES);
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            metadataSchema = sf.newSchema(XMLUtils.class.getResource("/xsd/saml-schema-metadata-2.0.xsd"));
        } catch (JAXBException jaxbe) {
            jaxbe.printStackTrace();
        } catch (SAXException saxe) {
            saxe.printStackTrace();
        }
    }

    private XMLUtils() {
    }

    public static DocumentBuilder getSafeDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        dbf.setXIncludeAware(false);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new LocalHandler());
        db.setErrorHandler(new LocalHandler());
        return db;
    }

    public static Document getDOM(String content) throws IOException, ParserConfigurationException, SAXException {
        return getSafeDocumentBuilder().parse(new InputSource(new StringReader(content)));
    }

    public static <T> T parseXML(String content, Class<T> clazz) throws JAXBException {
        //TODO use object pool for unmarshallers
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(metadataSchema);
        JAXBElement<T> unmarshalled = unmarshaller.unmarshal(new StreamSource(new StringReader(content)), clazz);
        return unmarshalled.getValue();
    }
}
