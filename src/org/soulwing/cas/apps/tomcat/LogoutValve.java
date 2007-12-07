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

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Session;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.valves.ValveBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.SimpleUrlGenerator;
import org.soulwing.cas.filter.FilterConstants;

/**
 * A <code>Valve</code> that listens for CAS logout requests that match a
 * configured URI.  If a matching request includes <code>global=true</code>
 * as a query parameter, the request is redirected to the configured CAS
 * global logout.
 *
 * @author Carl Harris
 */
public class LogoutValve extends ValveBase implements Lifecycle {

  public static final String GLOBAL_LOGOUT_PARAM = "global";

  private static final Log log = LogFactory.getLog(LogoutValve.class);

  private final LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
  
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

  public void invoke(Request request, Response response) throws IOException,
      ServletException {
    String requestUri = request.getRequestURI();
    if (requestUri.equals(logoutUri)) {
      log.trace("request URI " + requestUri + " matches");
      Session session = request.getSessionInternal();
      if (session != null) {
        session.removeNote(FilterConstants.VALIDATION_ATTRIBUTE);
      }
      if (isGlobalLogout(request.getParameter(GLOBAL_LOGOUT_PARAM))) {
        ResourceHelper helper = (ResourceHelper) 
            request.getNote(ResourceValve.RESOURCE_HELPER_ATTR);
        SimpleUrlGenerator urlGenerator = 
            new SimpleUrlGenerator(helper.getProtocolConfiguration());
        response.sendRedirect(urlGenerator.getLogoutUrl());
      }
      else if (redirectUrl != null) {
        response.sendRedirect(redirectUrl);
      }
      else {
        log.warn("no post-logout redirect configured");
      }
    }
    else {
      log.trace("request URI " + requestUri + " does not match");
      getNext().invoke(request, response);
    }
      
  }

  private boolean isGlobalLogout(String globalLogout) {
    if (globalLogout == null) return false;
    try {
      return Boolean.parseBoolean(globalLogout);
    }
    catch (IllegalArgumentException ex) {
      return false;
    }
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#start()
   */
  public synchronized void start() throws LifecycleException {
    log.debug("listening for URI " + logoutUri);
  }
  
  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#stop()
   */
  public synchronized void stop() throws LifecycleException {
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#addLifecycleListener(org.apache.catalina.LifecycleListener)
   */
  public void addLifecycleListener(LifecycleListener lifecycleListener) {
    lifecycleSupport.addLifecycleListener(lifecycleListener);
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#findLifecycleListeners()
   */
  public LifecycleListener[] findLifecycleListeners() {
    return lifecycleSupport.findLifecycleListeners();
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#removeLifecycleListener(org.apache.catalina.LifecycleListener)
   */
  public void removeLifecycleListener(LifecycleListener lifecycleListener) {
    lifecycleSupport.removeLifecycleListener(lifecycleListener);
  }


}
