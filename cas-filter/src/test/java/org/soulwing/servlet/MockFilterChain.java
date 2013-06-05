/*
 * MockFilterChain.java
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
package org.soulwing.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class MockFilterChain implements FilterChain {

  private boolean chainInvoked;
  private ServletRequest request;
  private ServletResponse response;
  private Servlet servlet;
  
  public Servlet getServlet() {
    return servlet;
  }

  public void setServlet(Servlet servlet) {
    this.servlet = servlet;
  }

  public boolean isChainInvoked() {
    return chainInvoked;
  }
  
  public ServletRequest getChainedRequest() {
    return this.request;
  }
  
  public ServletResponse getChainedResponse() {
    return this.response;
  }
  
  public void doFilter(ServletRequest request, ServletResponse response) 
      throws IOException, ServletException {
    this.chainInvoked = true;
    this.request = request;
    this.response = response;
    if (servlet != null) {
      servlet.service(request, response);
    }
  }

}
