/*
 * LogoutFilter.java
 *
 * Created on Feb 9, 2007 
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

import org.soulwing.cas.http.AuthenticatorConstants;


/**
 * A subclass of <code>AbstractLogoutFilter</code> that monitors incoming
 * requests for those that match a servlet path that matches its
 * <code>logoutPath</code> property.  Such requests are considered to be
 * logout requests.
 *    
 * @author Carl Harris
 */
public class LogoutFilter extends AbstractLogoutFilter {

  String logoutPath;
  /**
   * Gets the servlet path that should be interpreted by this filter
   * as a logout request.
   * @return <code>String</code> servlet path this filter should interpret
   *    as a logout request.
   */
  public String getLogoutPath() {
    return logoutPath;
  }

  /**
   * Sets the servlet path that should be interpreted by this filter as
   * a logout request.
   * @param logoutPath <code>String</code> servlet path that this filter
   *    should interpret as a logout request.
   */
  public void setLogoutPath(String logoutPath) {
    this.logoutPath = logoutPath;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.filter.AbstractLogoutFilter#init()
   */
  public void init() throws Exception {
    super.init();
    if (logoutPath == null || logoutPath.length() == 0) {
      throw new IllegalArgumentException("logoutPath must be configured");
    }
    if (!logoutPath.startsWith("/")) {
      throw new IllegalArgumentException(
          "logoutPath must be a servlet path starting with a forward slash (/)");
    }
  }
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.filter.AbstractLogoutFilter#onInit(org.soulwing.cas.filter.Configurator)
   */
  protected void onInit(Configurator configurator)
      throws ServletException {
    super.onInit(configurator);
    setLogoutPath(configurator.getParameter(AuthenticatorConstants.LOGOUT_PATH));
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.filter.AbstractLogoutFilter#isLogoutRequest(javax.servlet.http.HttpServletRequest)
   */
  protected boolean isLogoutRequest(HttpServletRequest request) {
    return request.getServletPath().equals(logoutPath);
  }

}
