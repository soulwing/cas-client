/*
 * Response.java
 *
 * Created on Jul 7, 2007
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
 * A response to a CAS request.
 *
 * @author Carl Harris
 */
public interface Response {

  /**
   * Gets the value of the success flag.
   * @return <code>true</code> if the validation representated by this
   *    response was successful.
   */
  boolean isSuccessful();

  /**
   * Gets the result code for the response to an unsuccessful validation.
   * @return response code if <code>isSuccessful == true</code>, else
   *    <code>null</code>.
   */
  String getResultCode();
  
  /**
   * Gets the result message for the response to an unsuccessful validation.
   * @return response message if <code>isSuccessful == true</code>, else
   *    <code>null</code>.
   */
  String getResultMessage();
  
}
