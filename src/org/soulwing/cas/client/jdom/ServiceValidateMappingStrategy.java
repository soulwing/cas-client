/*
 * ServiceValidateMappingStrategy.java
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

import org.jdom.Element;
import org.soulwing.cas.client.AbstractResponse;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolViolationException;
import org.soulwing.cas.client.ValidationResponse;

/**
 * A <code>ProtocolMappingStrategy</code> that maps an instance of
 * <code>ServiceValidateResponse</code>.
 *
 * @author Carl Harris
 */
public class ServiceValidateMappingStrategy 
    extends AbstractValidationResponseMappingStrategy {

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.jdom.AbstractResponseMappingStrategy#mapSuccessResponse(org.jdom.Element)
   */
  public ValidationResponse mapSuccessResponse(Element element) {
    ServiceValidationSuccessResponse response = new ServiceValidationSuccessResponse();
    response.setSuccessful(true);
    response.setUserName(getUserName(
        JdomUtil.getChild(element, mapName(ProtocolConstants.USER))));
    response.setProxyGrantingTicketIou(getProxyGrantingTicketIou(
        JdomUtil.getChild(element, mapName(ProtocolConstants.PROXY_GRANTING_TICKET))));
    log.debug("authentication success");
    return response;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.jdom.AbstractResponseMappingStrategy#newFailureResponse()
   */
  protected AbstractResponse newFailureResponse() {
    return new ServiceValidationFailureResponse();
  }

  /**
   * Gets the username for a CAS response from the given 
   * <code>element</code>.
   * @param element JDOM element which is expected to contain the
   *    username element.
   * @throws ProtocolViolationException if <code>element</code> is
   *    <code>null</code>.
   */
  private String getUserName(Element element) {
    if (element == null) {
      throw new ProtocolViolationException("Expected user element");
    }
    return element.getTextTrim();
  }

  /**
   * Gets the proxy granting ticket IOU for a CAS response from the
   * given <code>element</code>.
   * @param element JDOM element which is expected to contain the 
   *    proxy granting ticket IOU or <code>null</code> if the 
   *    response does not contain an IOU.
   */
  private String getProxyGrantingTicketIou(Element element) {
    return element == null ? null : element.getTextTrim();
  }
  
}
