/*
 * AbstractValidationResponseMappingStrategy.java
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
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolViolationException;
import org.soulwing.cas.client.Response;

/**
 * A <code>ProtocolMappingStrategy</code> that forms the basis for
 * mapping responses for the CAS <code>/serviceValidate</code> and
 * <code>/proxyValidate</code> functions.
 *
 * @author Carl Harris
 */
public abstract class AbstractValidationResponseMappingStrategy 
    extends AbstractResponseMappingStrategy {

  /*
   * (non-Javadoc)
   * @see org.soulwing.cas.client.ProtocolMappingStrategy#mapResponse(org.jdom.Element)
   */
  public Response mapResponse(Element element) {
    Element responseElement = JdomUtil.getChild(
        element, ProtocolConstants.AUTHENTICATION_SUCCESS);
    if (responseElement != null) {
      return mapSuccessResponse(responseElement);
    }
    
    responseElement = JdomUtil.getChild(
        element, ProtocolConstants.AUTHENTICATION_FAILURE);
    if (responseElement != null) {
      return mapFailureResponse(responseElement);
    }

    throw new ProtocolViolationException("Invalid CAS validation response");
  }

  /**
   * Gets a <code>Response</code> that corresponds to the content of a 
   * JDOM <code>Element</code> representing a validation success response
   * from the CAS server. 
   * @param element the subject CAS response <code>Element</code>
   * @return an appropriate subclass of <code>Response</code>
   */
  protected abstract Response mapSuccessResponse(Element element);
  
}
