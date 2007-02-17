/*
 * NoTicketException.java
 * 
 * Created on Sep 8, 2006
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
 * 
 */
package org.soulwing.cas.client;


/**
 * An exception thrown when an attempt to validate a request via
 * CAS does not contain a service ticket.
 *
 * @author Carl Harris
 * 
 */
public class NoTicketException extends Exception {

  private static final long serialVersionUID = 1L;

  public NoTicketException() {
    super();
  }

  public NoTicketException(String message) {
    super(message);
  }

  public NoTicketException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoTicketException(Throwable cause) {
    super(cause);
  }

}
