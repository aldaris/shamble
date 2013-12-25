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
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author aldaris
 */
public class LocalHandler implements EntityResolver, ErrorHandler {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        System.out.println(publicId + " and " + systemId);
        return new InputSource(new StringReader(""));
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        System.out.println("warning" + exception.getMessage());
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        System.out.println("error" + exception.getMessage());
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println("fatalerror" + exception.getMessage());
    }
}
