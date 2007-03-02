/*
 * SimpleBypassFilter.java
 *
 * Created on Feb 13, 2007
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
package org.soulwing.cas.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Matcher;


/**
 * A filter that detects requests for servlet paths that should bypass
 * CAS authentication.  The <code>bypassPath</code> property is set to 
 * a comma-separated string of glob expressions that describe one or
 * more servlet paths that should be marked for CAS bypass.  When a request
 * for a path that matches one of the configured glob expressions is
 * received by the filter, it adds an attribute to the session that the
 * AbstractValidationFilter will observe as a request to bypass validation of
 * CAS credentials.
 *
 * @author Carl Harris
 */
public class SimpleBypassFilter extends LogoutFilter {

  private static final String BYPASS_PATHS = "bypassPaths";
  
  private static final Log log = LogFactory.getLog(LogoutFilter.class);
  private Pattern[] bypassPaths;

  /**
   * Sets the list of path expressions for which CAS authentication
   * should be bypassed.
   * @param bypassPaths <code>String</code> containing a comma-delimited list 
   *    of servlet path expressions.  Each expression can be a simple string
   *    or a string containing glob wildcards.
   */
  public void setBypassPaths(String bypassPaths) {
    setBypassPaths(bypassPaths.split("\\s*,\\s*"));
  }

  /**
   * Initializes this filter.  When this filter is being used as a bean in
   * a dependency injection framework (e.g. Spring), this method should be 
   * invoked after all dependencies have been set.
   * @throws Exception
   */
  public void init() throws Exception {
    if (bypassPaths == null) {
      throw new IllegalStateException("must set bypassPaths property");
    }
    super.init();
  }

  /*
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    setBypassPaths(new Configurator(filterConfig)
        .getRequiredParameter(BYPASS_PATHS));
    super.init(filterConfig);
  }

  /* 
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
    super.destroy();
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
      log.debug("Filter supports HTTP only");
      filterChain.doFilter(request, response);
    }
  }

  protected void doHttpFilter(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain) 
      throws IOException, ServletException { 

    if (isBypassPath(request.getServletPath())) {
      log.debug("servlet path " + request.getServletPath() 
          + " marked for bypass");
      removeSessionState(request);
      request.getSession(true).setAttribute(FilterConstants.BYPASS_ATTRIBUTE,
          Boolean.valueOf(true));
    }
    super.doHttpFilter(request, response, filterChain);
  }

  private void setBypassPaths(String[] paths) {
    if (paths.length > 0) {
      GlobCompiler compiler = new GlobCompiler();
      bypassPaths = new Pattern[paths.length];
      for (int i = 0; i < paths.length; i++) {
        try {
          bypassPaths[i] = compiler.compile(paths[i]);
        }
        catch (MalformedPatternException ex) {
          throw new IllegalArgumentException("compile error for pattern "
              + paths[i] + ": ", ex);
        }
      }
    }
  }
  
  private boolean isBypassPath(String servletPath) {
    boolean match = false;
    Perl5Matcher matcher = new Perl5Matcher();
    for (int i = 0; !match && i < bypassPaths.length; i++) {
      match = matcher.matches(servletPath, bypassPaths[i]);
    }
    return match;
  }
}
