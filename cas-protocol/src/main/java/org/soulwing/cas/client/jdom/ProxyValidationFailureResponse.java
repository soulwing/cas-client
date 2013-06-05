/*
 * ProxyValidationFailureResponse.java
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

import java.util.List;

import org.soulwing.cas.client.ProxyValidationResponse;

/**
 * A <code>ProxyValidationResponse</code> that represents a failed
 * <code>/proxyValidate</code> authentication.
 *
 * @author Carl Harris
 */
public class ProxyValidationFailureResponse 
    extends ServiceValidationFailureResponse
    implements ProxyValidationResponse {

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ProxyValidationResponse#getProxies()
   */
  public List getProxies() {
    throw new IllegalStateException("Cannot get proxies from a failed validation");
  }

}
