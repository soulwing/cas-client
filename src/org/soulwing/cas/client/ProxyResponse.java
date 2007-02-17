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
 * A value class for the response to the CAS <code>/proxy</code> function.
 *
 * @author Carl Harris
 * 
 */
public class ProxyResponse extends Response {

  private String proxyTicket;

  public String getProxyTicket() {
    return proxyTicket;
  }

  public void setProxyTicket(String proxyTicket) {
    this.proxyTicket = proxyTicket;
  }
  
}
