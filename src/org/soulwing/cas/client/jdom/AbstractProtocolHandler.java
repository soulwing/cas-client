/*
 * AbstractProtocolHandler.java
 *
 * Created on Sep 14, 2006
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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolViolationException;
import org.soulwing.cas.client.Response;
import org.soulwing.cas.client.ServiceAccessException;
import org.soulwing.cas.client.ProtocolHandler;
import org.xml.sax.InputSource;


/**
 * An abstract base implementation of ProtocolHandler that uses JDOM.
 *
 * @author Carl Harris
 * 
 */
public abstract class AbstractProtocolHandler implements ProtocolHandler {

  private static final Log log = 
      LogFactory.getLog(AbstractProtocolHandler.class);
  
  /** 
   * @see org.soulwing.cas.client.ProtocolHandler#processResult(
   *    org.xml.sax.InputSource)
   */
  public abstract Response processResult(InputSource result);

  /**
   * Gets the validation result element (child of the cas:serviceResponse
   * element) from <code>result</code>
   * @param result the response from the CAS server
   * @return the child of the cas:serviceResponse element.
   * @throws ServiceAccessException if an I/O error occurs while 
   *    accessing <code>result</code>
   * @throws ProtocolViolationException if the root element of 
   *    <code>result</code> is not a CAS service response or does not 
   *    contain exactly one child element, or if a JDOMException occurs.
   */
  protected Element getResultElement(InputSource result) { 
 
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
    
    log.debug(new XMLOutputter(Format.getCompactFormat())
        .outputString(document));

    Element rootElement = document.getRootElement();
    if (!rootElement.getQualifiedName()
        .equals(ProtocolConstants.SERVICE_RESPONSE)) {
      log.error("Invalid root element: " + rootElement.getQualifiedName());
      throw new ProtocolViolationException("Invalid root element");
    }
    
    List children = rootElement.getChildren();
    if (children == null || children.size() != 1) {
      throw new ProtocolViolationException("Expected validation response");
    }
    
    return (Element) children.get(0);
  }

  /**
   * Configures a CAS failure response from the given <code>element</code>.
   * @param response the response to configure
   * @param element JDOM element that is assumed to contain a JDOM
   *    validation failure response.
   */
  protected void configureFailureResponse(Response response, 
      Element element) {
    response.setSuccessful(false);
    response.setResultCode(element.getAttributeValue(ProtocolConstants.CODE));
    response.setResultMessage(element.getTextTrim());
  }
  
}
