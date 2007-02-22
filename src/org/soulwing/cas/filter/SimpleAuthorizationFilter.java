/*
 * SimpleAuthorizationFilter.java
 *
 * Created on Sep 12, 2006
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
 * An authorization filter for which a list of authorized usernames is 
 * configured as an initialization parameter.
 *
 * @author Carl Harris
 * 
 */
public class SimpleAuthorizationFilter implements Filter {

  private static final Log log = 
      LogFactory.getLog(SimpleAuthorizationFilter.class);
  
  private String authorizedUsers[] = new String[0];
  
  public void init(FilterConfig config) throws ServletException {
    String users = new FilterConfigurator(config).getRequiredParameter(
        FilterConstants.AUTHORIZED_USERS_PARAM);
    setAuthorizedUsers(users.split("\\s*,\\s*"));
    try {
      init();
    }
    catch (Exception ex) {
      throw new ServletException(ex);
    }
  }

  public void init() throws Exception {
    if (getAuthorizedUsers() == null) {
      throw new IllegalStateException("Must set authorizedUsers property");
    }
  }
  
  public String[] getAuthorizedUsers() {
    return authorizedUsers;
  }
  
  public void setAuthorizedUsers(String[] authorizedUsers) {
    this.authorizedUsers = authorizedUsers;
    log.debug("authorized users: " + authorizedUsersToString());
  }

  private String authorizedUsersToString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < authorizedUsers.length; i++) {
      sb.append(authorizedUsers[i]);
      if (i < authorizedUsers.length - 1) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }
  
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {

    doHttpFilter(
        (HttpServletRequest) request,
        (HttpServletResponse) response,
        filterChain);
  }

  public void destroy() {
    // nothing to destroy
  }

  private void doHttpFilter(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    String userName = request.getUserPrincipal().getName();
    if (isAuthorized(userName)) {
      filterChain.doFilter(request, response);
    }
    else {
      log.info(userName + " is not authorized");
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
  }
  
  private boolean isAuthorized(String userName) {
    boolean authorized = false;
    for (int i = 0; !authorized && i < authorizedUsers.length; i++) {
      authorized = userName.equals(authorizedUsers[i]);
    }
    return authorized;
  }
}
