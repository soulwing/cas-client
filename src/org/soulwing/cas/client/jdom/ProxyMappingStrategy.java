/*
 * ProxyMappingStrategy.java
 *
 * Created on Jul 7, 2007 
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

import org.jdom.Element;
import org.soulwing.cas.client.AbstractResponse;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolViolationException;
import org.soulwing.cas.client.ValidationResponse;

/**
 * A <code>ProtocolMappingStrategy</code> that maps to an instance
 * of <code>ProxySuccessResponse</code>.
 *
 * @author Carl Harris
 */
public class ProxyMappingStrategy extends AbstractResponseMappingStrategy {

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.jdom.AbstractResponseMappingStrategy#mapSuccessResponse(org.jdom.Element)
   */
  public ValidationResponse mapResponse(Element element) {
    Element responseElement = JdomUtil.getChild(
        element, ProtocolConstants.PROXY_SUCCESS);
    if (responseElement != null) {
      log.debug("proxy success");
      return mapSuccessResponse(responseElement);
    }
    
    responseElement = JdomUtil.getChild(
        element, ProtocolConstants.PROXY_FAILURE);
    if (responseElement != null) {
      log.debug("proxy failure");
      return mapFailureResponse(responseElement);
    }

    throw new ProtocolViolationException("Invalid CAS proxy response");
  }

  /**
   * Gets a <code>ValidationResponse</code> that corresponds to the content of a 
   * JDOM <code>Element</code> representing a proxy success response
   * from the CAS server. 
   * @param element the subject CAS response <code>Element</code>
   * @return an appropriate subclass of <code>ValidationResponse</code>
   */
  private ValidationResponse mapSuccessResponse(Element element) {
    ProxySuccessResponse response = new ProxySuccessResponse();
    response.setSuccessful(true);
    response.setProxyTicket(getProxyTicket(
        JdomUtil.getChild(element, ProtocolConstants.PROXY_TICKET)));
    return response;
  }
  
  /**
   * Gets the proxy ticket for a CAS proxy response from the given 
   * <code>element</code>.
   * @param element JDOM element which is expected to contain the
   *    proxy ticket element.
   * @throws ProtocolViolationException if <code>element</code> is
   *    not the proxy ticket element.
   */
  private String getProxyTicket(Element element) {
    if (element == null) {
      throw new ProtocolViolationException("Expected proxy ticket");
    }
    return element.getTextTrim();
  }

  /*
   * (non-Javadoc)
   * @see org.soulwing.cas.client.jdom.AbstractResponseMappingStrategy#newFailureResponse()
   */
  protected AbstractResponse newFailureResponse() {
    return new ProxyFailureResponse();
  }

}
