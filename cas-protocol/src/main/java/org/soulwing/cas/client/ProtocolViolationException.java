/*
 * ProtocolViolationException.java
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
 */
package org.soulwing.cas.client;


/**
 * An exception thrown when the CAS server returns results that
 * are not consistent with the CAS protocol.
 *
 * @author Carl Harris
 * 
 */
public class ProtocolViolationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ProtocolViolationException() {
    super();
  }

  public ProtocolViolationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProtocolViolationException(String message) {
    super(message);
  }

  public ProtocolViolationException(Throwable cause) {
    super(cause);
  }

}
