/*
 * ServiceValidationFailureResponse.java
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

import org.soulwing.cas.client.AbstractResponse;
import org.soulwing.cas.client.ServiceValidationResponse;

/**
 * A <code>ServiceValidationResponse</code> that represents a failed
 * <code>/serviceValidate</code> authentication.
 *
 * @author Carl Harris
 */
public class ServiceValidationFailureResponse extends AbstractResponse
    implements ServiceValidationResponse {

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ServiceValidationResponse#getProxyGrantingTicketIou()
   */
  public String getProxyGrantingTicketIou() {
    throw new IllegalStateException("Cannot get PGTIOU for a failed validation");
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ServiceValidationResponse#getUserName()
   */
  public String getUserName() {
    throw new IllegalStateException("Cannot get user for a failed validation");
  }

}
