/*
 * JiraCasAuthenticator.java
 * 
 * Created on Feb 12, 2007
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

package org.soulwing.cas.apps.atlassian;


import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.support.ValidationUtils;

import com.atlassian.seraph.auth.AuthenticatorException;
import com.atlassian.seraph.auth.DefaultAuthenticator;
import com.atlassian.seraph.config.SecurityConfig;


/**
 * Subclass of ConfluenceAuthenticator that provides CAS authentication for
 * Confluence.
 *
 * @author ingomar.otter
 * @author jayshao
 * @author Carl Harris
 * 
 */
public class JiraCasAuthenticator extends DefaultAuthenticator {

  private static final long serialVersionUID = 1L;
  private static final Log log = 
      LogFactory.getLog(JiraCasAuthenticator.class);

  public void init(Map params, SecurityConfig config) {
    super.init(params, config);
    
    log.debug("initialized with loginURL=" + config.getLoginURL()
        + " and logoutURL=" + config.getLogoutURL());
    log.debug("param keys=" + params.keySet());
  }

  public Principal getUser(HttpServletRequest request, 
      HttpServletResponse response) {

    ServiceValidationResponse validation =
        ValidationUtils.getServiceValidationResponse(request);
    if (validation == null) {
      log.debug("no CAS validation");
      return super.getUser(request, response);
    }
    log.debug("CAS validation for user " + validation.getUserName());
    Principal user = super.getUser(validation.getUserName());
    request.getSession().setAttribute(LOGGED_IN_KEY, user);
    request.getSession().setAttribute(LOGGED_OUT_KEY, null);
    return user;
  }

  public boolean login(HttpServletRequest request,
      HttpServletResponse response, String username, String password) 
      throws AuthenticatorException {
    return login(request, response, username, password, false);
  }

  public boolean login(HttpServletRequest request,
      HttpServletResponse response, String username, 
      String password, boolean cookie) 
      throws AuthenticatorException {
    log.debug("login invoked");
    return super.login(request, response, username, password, cookie);
  }

  public boolean logout(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticatorException {
    log.debug("logout invoked");
    request.getSession().setAttribute(LOGGED_IN_KEY, null);
    request.getSession().setAttribute(LOGGED_OUT_KEY, new Boolean(true));
    return true;
  }
 
}
