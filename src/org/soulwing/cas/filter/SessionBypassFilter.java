/*
 * SessionBypassFilter.java
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.support.ValidationUtils;


/**
 * A filter that detects requests for servlet paths that should cause
 * CAS authentication to be bypassed for an entire session.  
 * The <code>bypassPaths</code> property is set to a comma-separated 
 * string of glob expressions that describe one or more servlet paths that 
 * should cause the session to be marked for CAS bypass.  When a request
 * for a path that matches one of the configured glob expressions is
 * received by the filter, it adds an attribute to the session that the
 * AbstractValidationFilter can observe.
 *
 * @author Carl Harris
 */
public class SessionBypassFilter extends LogoutFilter {

  private static final Log log = LogFactory.getLog(SessionBypassFilter.class);
  private ServletPathMatcher pathMatcher;

  /**
   * Sets the list of path expressions for which CAS authentication
   * should be bypassed.
   * @param bypassPaths <code>String</code> containing a comma-delimited list 
   *    of servlet path expressions.  Each expression can be a simple string
   *    or a string containing glob wildcards.
   */
  public void setBypassPaths(String bypassPaths) {
    if (bypassPaths == null) return;
    pathMatcher = new ServletPathMatcher(bypassPaths.split("\\s*,\\s*"));
  }

  public void init() throws Exception {
    super.init();
    if (pathMatcher == null) {
      throw new IllegalArgumentException("must configure " 
          + FilterConstants.BYPASS_PATHS);
    }
  }

  /*
   * @see org.soulwing.cas.filter.AbstractLogoutFilter#onInit(org.soulwing.cas.filter.Configurator)
   */
  public void onInit(Configurator configurator) throws ServletException {
    super.onInit(configurator);
    setBypassPaths(configurator.getParameter(FilterConstants.BYPASS_PATHS));
  }
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.filter.LogoutFilter#isLogoutRequest(javax.servlet.http.HttpServletRequest)
   */
  protected boolean isLogoutRequest(HttpServletRequest request) {
    boolean bypassedPath = pathMatcher.matches(request.getServletPath());
    if (bypassedPath) {
      log.debug("servlet path " + request.getServletPath() 
          + " marked for bypass");
      ValidationUtils.removeSessionState(request);
      request.getSession(true).setAttribute(FilterConstants.BYPASS_ATTRIBUTE,
          Boolean.valueOf(true));
    }
    return super.isLogoutRequest(request);
  }

}
