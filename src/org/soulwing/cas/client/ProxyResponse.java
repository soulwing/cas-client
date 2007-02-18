/*
 * ProxyResponse.java
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
package org.soulwing.cas.client;


/**
 * A response received for an invocation of the CAS <code>/proxy</code>
 * operation.
 *
 * @author Carl Harris
 */
public class ProxyResponse extends Response {

  private String proxyTicket;
  
  /**
   * Gets the proxy ticket issued by the CAS server.
   * @return <code>String</code> proxy ticket
   */
  public String getProxyTicket() {
    return proxyTicket;
  }

  /**
   * Sets the proxy ticket that was issued by the CAS server for request
   * corresponding to this ProxyResponse.
   * @param proxyTicket <code>String</code> proxy ticket
   */
  public void setProxyTicket(String proxyTicket) {
    this.proxyTicket = proxyTicket;
  }
  
}
