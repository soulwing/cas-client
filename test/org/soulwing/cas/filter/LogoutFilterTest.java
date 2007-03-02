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

import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.SimpleUrlGenerator;
import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;

import junit.framework.TestCase;

public class LogoutFilterTest extends TestCase {

  private static final String REDIRECT_URL = "http://localhost/afterLogout";
  private static final String SERVER_URL = "http://localhost/cas";
  private static final String LOGOUT_PATH = "/logout.action";
  private static final String LOGOUT_URL = "https://localhost" + LOGOUT_PATH;
  private static final String OTHER_URL = "https://localhost/some/other/path";
  
  private MockFilterConfig filterConfig;
  private MockFilterChain filterChain;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private LogoutFilter filter;
  
  protected void setUp() throws Exception {
    filterConfig = new MockFilterConfig();
    filterChain = new MockFilterChain();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filter = new LogoutFilter();
  }

  protected void tearDown() throws Exception {
    ProtocolConfigurationFilter.setConfiguration(null);
  }

  private void setRequiredConfig() {
    filterConfig.setInitParameter(FilterConstants.LOGOUT_PATH, LOGOUT_PATH);
  }
  
  public void testInitNoLogoutPath() throws Exception {
    try {
      filter.init(filterConfig);
      fail("Expected ServletException");
    }
    catch (ServletException ex) {
      assertTrue(true);
    }
  }
  
  public void testInitGlobalLogoutWithoutProtocolConfiguration() 
    throws Exception {
    setRequiredConfig();
    ProtocolConfigurationFilter.setConfiguration(null);
    filterConfig.setInitParameter(FilterConstants.GLOBAL_LOGOUT, "true");
    try {
      filter.init(filterConfig);
      fail("Expected exception");
    }
    catch (ServletException ex) {
      assertTrue(true);
    }
  }
  
  public void testDefaults() throws Exception {
    setRequiredConfig();
    assertEquals(Boolean.parseBoolean(LogoutFilter.APPLICATION_LOGOUT_DEFAULT),
        filter.isApplicationLogout());
    assertEquals(Boolean.parseBoolean(LogoutFilter.GLOBAL_LOGOUT_DEFAULT),
        filter.isGlobalLogout());
    assertNull(filter.getProtocolConfiguration());
    assertNull(filter.getRedirectUrl());
  }
  
  public void testRequestForLogoutPath() throws Exception {
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    request.getSession(true).setAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE, new Object());
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE));
  }

  public void testRequestForLogoutPathNoApplicationLogout() throws Exception {
    setRequiredConfig();
    filterConfig.setInitParameter(FilterConstants.APPLICATION_LOGOUT, "false");
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    request.getSession(true).setAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE, new Object());
    filter.doFilter(request, response, filterChain);
    assertTrue(!filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE));
  }

  public void testRequestForOtherPath() throws Exception {
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(OTHER_URL);
    request.getSession(true).setAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE, new Object());
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNotNull(request.getSession().getAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE));
  }

  public void testGlobalLogout() throws Exception {
    filterConfig.setInitParameter(FilterConstants.GLOBAL_LOGOUT, "true");
    doGlobalLogout();
  }

  public void testGlobalLogoutWithRedirectUrl() throws Exception {
    filterConfig.setInitParameter(FilterConstants.GLOBAL_LOGOUT, "true");
    filterConfig.setInitParameter(FilterConstants.REDIRECT_URL, REDIRECT_URL);
    doGlobalLogout();
  }

  private void doGlobalLogout() throws Exception {
    ProtocolConfiguration config = new ProtocolConfiguration();
    ProtocolConfigurationFilter.setConfiguration(config);
    config.setServerUrl(SERVER_URL);
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    filter.doFilter(request, response, filterChain);
    assertEquals(
        new SimpleUrlGenerator(config).getLogoutUrl(filter.getRedirectUrl()), 
        response.getRedirect());
  }

  public void testRedirectUrl() throws Exception {
    filterConfig.setInitParameter(FilterConstants.REDIRECT_URL, REDIRECT_URL);
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    filter.doFilter(request, response, filterChain);
    assertEquals(filter.getRedirectUrl(), response.getRedirect());
  }

}
