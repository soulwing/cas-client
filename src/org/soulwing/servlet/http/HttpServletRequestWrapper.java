/*
 * HttpServletRequestWrapper.java
 *
 * Created on Sep 12, 2006
 */
package org.soulwing.servlet.http;

import java.security.Principal;


/**
 * A HttpServletRequest decorator that includes a CAS Principal. 
 *
 * @author Carl Harris
 * 
 */
public class HttpServletRequestWrapper 
    extends javax.servlet.http.HttpServletRequestWrapper {

  private Principal principal;
  
  /**
   * Decorates <code>request</code> to include the CAS Principal.
   * @param request HTTP request to decorate
   * @param principal CAS Principal that will be included in the wrapped
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
