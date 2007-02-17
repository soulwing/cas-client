/*
 * LogoutFilter.java
 *
 * Created on Feb 9, 2007 
 */
package org.soulwing.cas.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.support.ValidationUtils;

/**
 * A filter for removing CAS-related session state.  This filter expects
 * a single configuration parameter <code>logoutPath</code> that should
 * be configured as a servlet path that indicates a logout request.  If, 
 * after passing control down the filter chain, 
 * <code>request.getServletPath</code> equals <code>logoutPath</code>, 
 * then this filter will remove all CAS-related state from the session.
 * 
 * @author Carl Harris
 */
public class LogoutFilter implements Filter {

  private static final Log log = LogFactory.getLog(LogoutFilter.class);
  private String logoutPath;
  
  /*
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    this.logoutPath = new FilterConfigurator(filterConfig)
        .getRequiredParameter(FilterConstants.LOGOUT_PATH);
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

  public void doHttpFilter(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain) 
      throws IOException, ServletException { 

    filterChain.doFilter(request, response);
    if (request.getServletPath().equals(logoutPath)) {
      removeSessionState(request);
    }
  }
  
  private void removeSessionState(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    ServiceValidationResponse validation =
        ValidationUtils.getServiceValidationResponse(request);
    Enumeration names = session.getAttributeNames();
    while (names.hasMoreElements()) {
      String name = (String) names.nextElement();
      if (name.startsWith(FilterConstants.ATTRIBUTE_PREFIX)) {
        log.debug("Removing attribute " + name + " from session");
        session.removeAttribute(name);
      }
    }
    if (validation != null) {
      log.info("User " + validation.getUserName() + " has logged out");
    }
  }
  
}
