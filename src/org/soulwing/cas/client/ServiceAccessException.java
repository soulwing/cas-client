/*
 * ServiceAccessException.java
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
 * An exception thrown when an I/O or other network-related exception occurs 
 * in connecting to the CAS server, making a request, and getting the
 * response. 
 *
 * @author Carl Harris
 * 
 */
public class ServiceAccessException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ServiceAccessException() {
    super();
  }

  public ServiceAccessException(String message) {
    super(message);
  }

  public ServiceAccessException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceAccessException(Throwable cause) {
    super(cause);
  }

}
