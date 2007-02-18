/*
 * SimpleValidationRequest.java
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
 * A simple immutable implementation of ValidationRequest. 
 *
 * @author Carl Harris
 */
public class SimpleValidationRequest implements ValidationRequest {

  private final String ticket;

  /**
   * @param ticket <code>String</code> ticket to be validated.
   */
  public SimpleValidationRequest(String ticket) {
    this.ticket = ticket;
  }

  /* 
   * @see org.soulwing.cas.client.ValidationRequest#getTicket()
   */
  public final String getTicket() {
    return ticket;
  }

}
