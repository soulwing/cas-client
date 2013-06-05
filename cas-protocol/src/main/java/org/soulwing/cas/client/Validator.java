/*
 * Validator.java
 *
 * Created on Sep 7, 2006
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
 * Validator is the client interface for CAS authentication
 * ticket validation. Users of the interface will generally use the following 
 * paradigm for CAS authentication.
 * 
 * <ol>
 * <li> Invoke <code>serviceValidate</code> (or <code>proxyValidate</code> 
 *      to attempt to validate the credentials of the user making an HTTP 
 *      request.
 * </li>
 * <li> If the NoTicketException is thrown, redirect the user's browser to
 *      the CAS login URL.
 * <li> If no exception is thrown then use 
 *      <code>ServiceValidationResponse.isSuccessful</code> 
 *      to determine whether to allow the user's request proceed or to 
 *      return an authentication failed response to the user's browser.
 * </li>
 * </ol>
 *
 * @author Carl Harris
 */
public interface Validator {
  
  /**
   * Invokes the CAS <code>/serviceValidate</code> operation to validate
   * a ticket presented
   * @param request request containing the ticket to validate.
   * @return validation response from CAS server.
   * @throws NoTicketException if request does not contain a 
   *    <code>ticket</code> parameter.
   */
  ServiceValidationResponse serviceValidate(ValidationRequest request)
      throws NoTicketException;
  
  /**
   * Invoke the CAS <code>/proxyValidate</code> operation to validate
   * a ticket.
   * @param request request containing the ticket to validate.
   * @return validation response from CAS server.
   * @throws NoTicketException if request does not contain a 
   *    <code>ticket</code> parameter.
   */
  ProxyValidationResponse proxyValidate(ValidationRequest request)
      throws NoTicketException;
  
}
