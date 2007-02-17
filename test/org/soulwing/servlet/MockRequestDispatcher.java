/*
 * MockRequestDispatcher.java
 *
 * Created on Feb 8, 2007
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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.soulwing.servlet.http.MockHttpServletRequest;


/**
 * A mock implementation of the Servlet RequestDispatcher interface 
 * for use in unit test mock-ups.
 *
 * @author Carl Harris
 */
public class MockRequestDispatcher implements RequestDispatcher {

  private String resourcePath;
  private boolean forwardInvoked;
  private boolean includeInvoked;
  private ServletRequest request;
  private ServletResponse response;
  
  /* 
   * @see javax.servlet.RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   */
  public void forward(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {
    if (request instanceof MockHttpServletRequest) {
      MockHttpServletRequest mockRequest = (MockHttpServletRequest) request;
      mockRequest.setServletPath(resourcePath);
    }
    this.forwardInvoked = true;
    this.request = request;
    this.response = response;
  }

  /* 
   * @see javax.servlet.RequestDispatcher#include(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   */
  public void include(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {
    this.includeInvoked = true;
    this.request = request;
    this.response = response;
  }
  
  public boolean isForwardInvoked() {
    return forwardInvoked;
  }
  
  public boolean isIncludeInvoked() {
    return includeInvoked;
  }

  public ServletRequest getRequest() {
    return request;
  }
  
  public ServletResponse getResponse() {
    return response;
  }

  public String getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(String resourcePath) {
    this.resourcePath = resourcePath;
  }
}
