/*
 * ProxySuccessResponse.java
 *
 * Created on Sep 7, 2006
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
import org.soulwing.cas.client.ProxyResponse;


/**
 * A response received for an invocation of the CAS <code>/proxy</code>
 * operation.
 *
 * @author Carl Harris
 */
public class ProxySuccessResponse extends AbstractResponse 
    implements ProxyResponse {

  private String proxyTicket;
  
  /*
   * (non-Javadoc)
   * @see org.soulwing.cas.client.ProxyResponse#getProxyTicket()
   */
  public String getProxyTicket() {
    return proxyTicket;
  }

  /**
   * Sets the proxy ticket that was issued by the CAS server for request
   * corresponding to this ProxySuccessResponse.
   * @param proxyTicket <code>String</code> proxy ticket
   */
  public void setProxyTicket(String proxyTicket) {
    this.proxyTicket = proxyTicket;
  }
  
}
