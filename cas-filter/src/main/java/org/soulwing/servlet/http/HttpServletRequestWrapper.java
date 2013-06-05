/*
 * HttpServletRequestWrapper.java
 *
 * Created on Sep 12, 2006
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
package org.soulwing.servlet.http;

import java.security.Principal;


/**
 * A HttpServletRequest decorator that includes a CAS CasPrincipal. 
 *
 * @author Carl Harris
 * 
 */
public class HttpServletRequestWrapper 
    extends javax.servlet.http.HttpServletRequestWrapper {

  private Principal principal;
  
  /**
   * Decorates <code>request</code> to include the CAS CasPrincipal.
   * @param request HTTP request to decorate
   * @param principal CAS CasPrincipal that will be included in the wrapped
   *  request.
   */
  public HttpServletRequestWrapper(javax.servlet.http.HttpServletRequest request, 
      Principal principal) {
    super(request);
    this.principal = principal;
  }
  
  public java.security.Principal getUserPrincipal() {
    return this.principal;
  }
  
  public String getRemoteUser() {
    return this.principal.getName();
  }

}
