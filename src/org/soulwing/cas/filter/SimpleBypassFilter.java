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


/**
 * A filter that detects requests for servlet paths that should bypass CAS
 * authentication. The <code>bypassPaths</code> property is set to a
 * comma-separated string of glob expressions that describe one or more
 * servlet paths that should not be CAS authenticated.
 * When a request for a path that matches one of the configured glob
 * expressions is received by the filter, it adds an attribute to the request
 * that the AbstractValidationFilter can observe.
 * 
 * @author Carl Harris
 */
public class SimpleBypassFilter {

  private static final Log log = LogFactory.getLog(SimpleBypassFilter.class);
  private ServletPathMatcher pathMatcher;

  /**
   * Sets the list of path expressions for which CAS authentication
   * should be bypassed.
   * @param bypassPaths <code>String</code> containing a comma-delimited list 
   *    of servlet path expressions.  Each expression can be a simple string
   *    or a string containing glob wildcards.
   */
  public void setBypassPaths(String bypassPaths) {
    pathMatcher = new ServletPathMatcher(bypassPaths.split("\\s*,\\s*"));
  }

  /**
   * Initializes this filter.  When this filter is being used as a bean in
   * a dependency injection framework (e.g. Spring), this method should be 
   * invoked after all dependencies have been set.
   * @throws Exception
   */
  public void init() throws Exception {
    if (pathMatcher == null) {
      throw new IllegalStateException("must set bypassPaths property");
    }
  }

  /*
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    setBypassPaths(new Configurator(filterConfig)
        .getRequiredParameter(FilterConstants.BYPASS_PATHS));
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
      log.debug("Filter supports HTTP only");
      filterChain.doFilter(request, response);
    }
  }

  protected void doHttpFilter(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain) 
      throws IOException, ServletException { 

    if (pathMatcher.matches(request.getServletPath())) {
      log.debug("servlet path " + request.getServletPath() 
          + " marked for bypass");
      request.setAttribute(FilterConstants.BYPASS_ATTRIBUTE,
          Boolean.valueOf(true));
    }
    filterChain.doFilter(request, response);
  }

}
