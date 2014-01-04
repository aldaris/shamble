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
package org.shamble.common.xml.security;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.Collections;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import org.w3c.dom.Document;

public class XMLSignatureTools {

    public static void sign(Document doc, String namespaceURI, String idAttribute, String idAttributeValue,
            PrivateKey privateKey) {
        doc.getDocumentElement().setIdAttributeNS(namespaceURI, idAttribute, true);
        XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");
        try {
            Reference ref = factory.newReference("#" + idAttributeValue, factory.newDigestMethod(DigestMethod.SHA1, null),
                    Collections.singletonList(factory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                    null, null);
            SignedInfo si = factory.newSignedInfo(factory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE,
                    (C14NMethodParameterSpec) null), factory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                    Collections.singletonList(ref));
            KeyInfoFactory kif = KeyInfoFactory.getInstance();
            X509Data xd = kif.newX509Data(null);
            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));
            XMLSignature sig = factory.newXMLSignature(si, ki);
            DOMSignContext domsc = new DOMSignContext((PrivateKey) null, doc.getDocumentElement(), doc.getDocumentElement().getFirstChild());
            sig.sign(domsc);

        } catch (GeneralSecurityException gse) {

        } catch (MarshalException me) {

        } catch (XMLSignatureException xse) {

        }
    }
}
