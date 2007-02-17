/*
 * MockRequestDispatcher.java
 *
 * Created on Feb 8, 2007
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
