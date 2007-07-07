/*
 * AbstractResponseMappingStrategy.java
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
import org.soulwing.cas.client.ProtocolMappingStrategy;

/**
 * A <code>ProtocolMappingStrategy</code> that maps an instance of
 * <code>AbstractResponse</code>.
 *
 * @author Carl Harris
 */
public abstract class AbstractResponseMappingStrategy implements
    ProtocolMappingStrategy {

  protected AbstractResponse mapFailureResponse(Element element) {
    AbstractResponse response = newFailureResponse();
    response.setSuccessful(false);
    response.setResultCode(element.getAttributeValue(ProtocolConstants.CODE));
    response.setResultMessage(element.getTextTrim());
    return response;
  }

  /**
   * Constructs a new subclass of <code>AbstractResponse</code> of the 
   * subclass appropriate for the CAS request that failed; e.g. a concrete
   * subclass of <code>AbstractResponse</code> taht implements 
   * <code>ServiceValidationResponse</code> would be returned for a failure
   * of a CAS <code>/serviceValidate</code> request. 
   * @return <code>AbstractResponse</code> instance.
   */
  protected abstract AbstractResponse newFailureResponse();

}
