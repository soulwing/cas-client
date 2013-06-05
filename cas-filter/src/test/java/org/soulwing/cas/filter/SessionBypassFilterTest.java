/*
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

import org.soulwing.cas.http.AuthenticatorConstants;
import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;

import junit.framework.TestCase;

public class SessionBypassFilterTest extends TestCase {

  private static final String LOGOUT_PATH = "/logout.action";
  private static final String BYPASS_PATHS = "/other.action,/login.action";
  private static final String BYPASS_URL = "https://localhost/login.action";
  private static final String OTHER_URL = "https://localhost/some/other/path";
  
  private MockFilterConfig filterConfig;
  private MockFilterChain filterChain;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private SessionBypassFilter filter;
  
  protected void setUp() throws Exception {
    filterConfig = new MockFilterConfig();
    filterChain = new MockFilterChain();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filter = new SessionBypassFilter();
  }

  private void setRequiredConfig() {
    filterConfig.setInitParameter("logoutPath", LOGOUT_PATH);
    filterConfig.setInitParameter("bypassPaths", BYPASS_PATHS);
  }
  
  public void testInitNoBypassPath() throws Exception {
    try {
      filter.init(filterConfig);
      fail("Expected ServletException");
    }
    catch (ServletException ex) {
      assertTrue(true);
    }
  }
  
  public void testRequestForBypassPath() throws Exception {
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(BYPASS_URL);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNotNull(request.getSession().getAttribute(
        AuthenticatorConstants.BYPASS_ATTRIBUTE));
  }

  public void testRequestForOtherPath() throws Exception {
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(OTHER_URL);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        AuthenticatorConstants.BYPASS_ATTRIBUTE));
  }

}
