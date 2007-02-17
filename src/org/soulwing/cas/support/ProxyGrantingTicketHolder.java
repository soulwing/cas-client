/*
 * ProxyGrantingTicketHolder.java
 *
 * Created on Feb 12, 2007
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


/**
 * A holder for a CAS proxy granting ticket (PGT).  Uses a ThreadLocal to
 * store the PGT that should be used by the thread.
 *
 * @author Carl Harris
 */
public class ProxyGrantingTicketHolder {

  private static final ThreadLocal ticketHolder = new ThreadLocal();
  
  public static synchronized String getTicket() {
    return (String) ticketHolder.get();
  }
  
  public static synchronized void setTicket(String ticket) {
    ticketHolder.set(ticket);
  }
  
  public static synchronized void clearTicket() {
    ticketHolder.remove();
  }

}
