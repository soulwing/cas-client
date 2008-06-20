/*
 * LogoutValve.java
 *
 * Created on Dec 6, 2007
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
package org.soulwing.cas.apps.tomcat;



import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A <code>Valve</code> that listens for CAS logout requests that match a
 * configured URI.  If a matching request includes <code>global=true</code>
 * as a query parameter, the request is redirected to the configured CAS
 * global logout URL.
 *
 * @author Carl Harris
 */
public class LogoutValve extends LogoutValveBase {

  public static final String GLOBAL_LOGOUT_PARAM = "global";
  
  private static final Log log = LogFactory.getLog(LogoutValve.class);
  
  private String logoutUri;
  private String redirectUrl;
  
  public String getLogoutUri() {
    return logoutUri;
  }

  public void setLogoutUri(String logoutUri) {
    this.logoutUri = logoutUri;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.LogoutValveBase#getLogoutStatus(org.apache.catalina.connector.Request)
   */
  public LogoutStatus getLogoutStatus(Request request) {
    if (!request.getRequestURI().equals(logoutUri)) {
      return LogoutStatus.NOT_LOGOUT;
    }
    
    LogoutStatus status = new LogoutStatus();
    status.setLogout(true);
    status.setGlobal(request.getParameter(GLOBAL_LOGOUT_PARAM) != null);
    status.setRedirectUrl(getRedirectUrl());
    return status;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.LogoutValveBase#start()
   */
  public void start() throws LifecycleException {
    super.start();
    if (logoutUri == null || logoutUri.length() == 0) {
      throw new LifecycleException(new IllegalArgumentException(
          "must configure logoutUri"));
    }
    if (!logoutUri.startsWith("/")) {
      throw new LifecycleException(new IllegalArgumentException(
          "logoutUri must be a valid URI starting with a forward slash (/)"));
    }
    log.info("listening for " + logoutUri);
  }

}
