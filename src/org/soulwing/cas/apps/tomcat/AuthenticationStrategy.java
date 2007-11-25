/*
 * AuthenticationStrategy.java
 *
 * Created on Sep 18, 2007
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
package org.soulwing.cas.apps.tomcat;

import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ServiceValidationResponse;


/**
 * An implementation of this interface sends authentication requests to
 * the CAS server for a particular HTTP request, and returns the 
 * CAS server's response.
 *
 * @author Carl Harris
 * 
 */
public interface AuthenticationStrategy {

  /**
   * Performs the action of the filter.
   * @param request the HTTP request that was received
   * @return the response from the CAS server
   * @throws NoTicketException if the request does not contain an
   *    authentication ticket.
   */
  ServiceValidationResponse authenticate(
      HttpServletRequest request) throws NoTicketException;
  
  void setProtocolConfiguration(ProtocolConfiguration config);
  
}
