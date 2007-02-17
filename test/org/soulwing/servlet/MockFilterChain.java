package org.soulwing.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class MockFilterChain implements FilterChain {

  private boolean chainInvoked;
  private ServletRequest request;
  private ServletResponse response;
  
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
  }

}
