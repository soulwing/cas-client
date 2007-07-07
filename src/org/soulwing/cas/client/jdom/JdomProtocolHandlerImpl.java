/*
 * JdomProtocolHandlerImpl.java
 *
 * Created on Jul 7, 2007 
 *
 * Copyright (C) 2006, 2007 Carl E Harris, Jr.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 */
package org.soulwing.cas.client.jdom;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolHandler;
import org.soulwing.cas.client.ProtocolMappingStrategy;
import org.soulwing.cas.client.ProtocolViolationException;
import org.soulwing.cas.client.Response;
import org.soulwing.cas.client.ServiceAccessException;
import org.xml.sax.InputSource;

/**
 * A <code>ProtocolHandler</code> that uses JDOM to process a CAS protocol
 * response.
 *
 * @author Carl Harris
 */
public class JdomProtocolHandlerImpl implements ProtocolHandler {
  
  private final Log log = LogFactory.getLog(this.getClass().getCanonicalName());

  /*
   * (non-Javadoc)
   * @see org.soulwing.cas.client.ProtocolHandler#processResult(org.xml.sax.InputSource, org.soulwing.cas.client.ProtocolMappingStrategy)
   */
  public Response processResult(InputSource result, 
      ProtocolMappingStrategy strategy) {
    return strategy.mapResponse(getResponseElement(result));
  }

  /**
   * Gets the response element from an <code>InputSource</code>.
   * @param result the response from the CAS server
   * @returnroot element of the response as a JDOM <code>Element</code>
   * @throws ServiceAccessException if an I/O error occurs while 
   *    accessing <code>result</code>
   * @throws ProtocolViolationException if the root element of 
   *    <code>result</code> is not a CAS service response, 
   *    or if a JDOMException occurs.
   */
  private Element getResponseElement(InputSource result) { 
 
    Document document = getJdomDocument(result);
    
    log.debug(new XMLOutputter(Format.getCompactFormat())
        .outputString(document));

    Element rootElement = document.getRootElement();
    if (!rootElement.getName().equals(ProtocolConstants.SERVICE_RESPONSE)
        || !rootElement.getNamespace().equals(JdomUtil.CAS_NAMESPACE)) {
      log.error("Invalid root element: " + rootElement.getQualifiedName());
      throw new ProtocolViolationException("Invalid root element");
    }
    
    return rootElement;
  }

  private Document getJdomDocument(InputSource result) {
    SAXBuilder builder = new SAXBuilder();
//    builder.setFeature("http://xml.org/sax/features/namespaces", false);

    Document document = null;
    try {
      document = builder.build(result);
    }
    catch (JDOMException ex) {
      throw new ProtocolViolationException(ex);
    }
    catch (IOException ex) {
      throw new ServiceAccessException(ex);
    }
    return document;
  }


}
