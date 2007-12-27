/*
 * ProxyTicketService.java
 *
 * Created on Dec 27, 2007
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
package org.soulwing.cas.support;

public interface ProxyTicketService {

  /**
   * Gets a proxy authentication ticket for a target service.
   * @param targetService URL of the service to which the authentication
   *    ticket will be presented 
   * @return <code>String</code> CAS proxy authentication ticket
   * @throws ProxyTicketException if any exception occurs in obtaining
   *    the ticket.
   */
  String getTicket(String targetService);

}