/*
 * ProxyGrantingTicketFilter.java
 *
 * Created on Feb 12, 2007
 */
package org.soulwing.cas.support;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A filter that looks for the proxy granting ticket in the session
 * of a request and places it in ProxyGrantingTicketHolder.
 *
 * @author Carl Harris
 */
public class ProxyGrantingTicketFilter implements Filter {

  private static final Log log = 
      LogFactory.getLog(ProxyGrantingTicketFilter.class);
  
  /* 
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    // not needed
  }

  /* 
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
    // not needed
  }

  /* 
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    try {
      doHttpFilter(
          (HttpServletRequest) request,
          (HttpServletResponse) response,
          filterChain);
    }
    catch (ClassCastException ex) {
      log.info("Filter supports HTTP only");
      filterChain.doFilter(request, response);
    }
  }

  private void doHttpFilter(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain) 
      throws IOException, ServletException {
    String ticket = ValidationUtils.getProxyGrantingTicket(request);
    if (ticket != null) {
      ProxyGrantingTicketHolder.setTicket(ticket);
    }
    filterChain.doFilter(request, response);
  }

}
