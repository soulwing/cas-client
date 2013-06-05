/*
 * SimpleProxyRequest.java
 *
 * Created on Feb 10, 2007
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
 * A simple immutable implementation of ProxyRequest.
 *
 * @author Carl Harris
 */
public class SimpleProxyRequest implements ProxyRequest {

  private final String proxyGrantingTicket;
  private final String targetService;
  
  /**
   * @param proxyGrantingTicket proxy granting ticket issued by the CAS
   *    server in the the proxy callback interaction. 
   * @param targetService URL for the service to which the proxy ticket
   *    will be presented for authentication.
   */
  public SimpleProxyRequest(String proxyGrantingTicket, String targetService) {
    this.proxyGrantingTicket = proxyGrantingTicket;
    this.targetService = targetService;
  }

  /* 
   * @see org.soulwing.cas.client.ProxyRequest#getProxyGrantingTicket()
   */
  public final String getProxyGrantingTicket() {
    return proxyGrantingTicket;
  }

  /* 
   * @see org.soulwing.cas.client.ProxyRequest#getTargetService()
   */
  public final String getTargetService() {
    return targetService;
  }

}
