/*
 * ProxyRequest.java
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
 * A request to obtain a proxy ticket from a CAS server.
 *
 * @author Carl Harris
 */
public interface ProxyRequest {

  /**
   * Gets the proxy granting ticket for this request.
   * @return <code>String</code> proxy granting ticket obtained through
   *    the CAS proxy callback interaction.
   */
  String getProxyGrantingTicket();
  
  /**
   * Gets the URL for the target service for this request.
   * @return <code>String</code> target service URL to which the requested
   *    proxy ticket will be presented.
   */
  String getTargetService();
  
}
