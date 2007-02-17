/*
 * ProxyValidateHandler.java
 *
 * Created on Sep 8, 2006
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolViolationException;
import org.soulwing.cas.client.ProxyValidationResponse;
import org.soulwing.cas.client.Response;
import org.soulwing.cas.client.ProtocolHandler;
import org.xml.sax.InputSource;


/**
 * A ProtocolHandler for the CAS <code>/proxyValidate</code> function.
 *
 * @author Carl Harris
 * 
 */
public final class ProxyValidateHandler 
    extends ServiceValidateHandler {

  /**
   * @see ProtocolHandler#processResult(InputSource)
   */
  public Response processResult(InputSource result) {

    Element child = super.getResultElement(result);
    String elementName = child.getQualifiedName();
    if (elementName.equals(ProtocolConstants.AUTHENTICATION_SUCCESS)) {
      return configureSuccessResponse(child);
    }
    else if (elementName.equals(ProtocolConstants.AUTHENTICATION_FAILURE)) {
      return configureFailureResponse(child);
    }
    else {
      throw new ProtocolViolationException("Invalid validation result");
    }
  }

  /**
   * Configures the fields of a success response, using the given CAS
   * response <code>element</code>
   * @param element JDOM element containing an authenticationSuccess
   *    element.
   * @return a ProxyValidationResponse instance
   */
  protected Response configureSuccessResponse(Element element) {
    ProxyValidationResponse response = new ProxyValidationResponse();
    response.setSuccessful(true);
    List children = element.getChildren();
    if (children == null || children.size() < 1 || children.size() > 3) {
      throw new ProtocolViolationException(
          "Invalid number of response elements");
    }
    response.setUserName(super.getUserName((Element) children.get(0)));
    if (children.size() >= 2) {
      Element child = (Element) children.get(1);
      try {
        response.setProxyGrantingTicketIou(super.getProxyGrantingTicketIou(child));
        if (children.size() > 2) {
          child = (Element) children.get(2);
        }
      }
      catch (ProtocolViolationException ex) {
        // it's okay if this child isn't a proxy granting ticket.
      }
      response.setProxies(getProxies(child));
    }
    return response;
  }

  /**
   * Configures the fields of failure response, using the given CAS
   * response <code>element</code>.
   * @param element JDOM element containing an authenticationFailure
   *    element.
   * @return a ProxyValidationResponse instance
   */
  private Response configureFailureResponse(Element element) {
    ProxyValidationResponse response = new ProxyValidationResponse();
    super.configureFailureResponse(response, element);
    return response;
  }
  
  /**
   * Sets the list of proxies for a CAS response from the given
   * <code>element</code>
   * @param element JDOM element which is expected to contain the
   *    proxies element.
   * @throws ProtocolViolationException if <code>element</code> does
   *    not contain the proxies element.
   */
  private List getProxies(Element element) {
    if (!element.getQualifiedName().equals(ProtocolConstants.PROXIES)) {
      throw new ProtocolViolationException("Expected proxies element");
    }
    List proxyElementList = element.getChildren();
    if (proxyElementList.size() == 0) {
      throw new ProtocolViolationException("Expected at least one proxy");
    }
    List proxies = new ArrayList();
    for (Iterator i = proxyElementList.iterator(); i.hasNext(); ) {
      Element proxy = (Element) i.next();
      if (!proxy.getQualifiedName().equals(ProtocolConstants.PROXY)) {
        throw new ProtocolViolationException("Expected proxy element");
      }
      proxies.add(proxy.getTextTrim());
    }
    return proxies;
  }
  

}
