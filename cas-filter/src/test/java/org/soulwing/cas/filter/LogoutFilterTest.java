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

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.TestCase;

import org.soulwing.cas.client.ProtocolConfigurationHolder;
import org.soulwing.cas.client.ProtocolConfigurationImpl;
import org.soulwing.cas.client.SimpleUrlGenerator;
import org.soulwing.cas.http.AuthenticatorConstants;
import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.BufferedServletOutputStream;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;

public class LogoutFilterTest extends TestCase {

  private static final String REDIRECT_URL = "http://localhost/afterLogout";
  private static final String BYPASS_REDIRECT_URL = "/afterLogoutBypassed";
  private static final String SERVER_URL = "http://localhost/cas";
  private static final String SERVICE_URL = "https://localhost";
  private static final String LOGOUT_PATH = "/logout.action";
  private static final String LOGOUT_URL = SERVICE_URL + LOGOUT_PATH;
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
    ProtocolConfigurationHolder.setConfiguration(null);
  }

  private void setRequiredConfig() {
    filterConfig.setInitParameter(AuthenticatorConstants.LOGOUT_PATH, LOGOUT_PATH);
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
  
  public void testDefaults() throws Exception {
    setRequiredConfig();
    assertEquals(Boolean.parseBoolean(AbstractLogoutFilter.APPLICATION_LOGOUT_DEFAULT),
        filter.isApplicationLogout());
    assertEquals(Boolean.parseBoolean(AbstractLogoutFilter.GLOBAL_LOGOUT_DEFAULT),
        filter.isGlobalLogout());
    assertNull(filter.getRedirectUrl());
  }
  
  public void testRequestForLogoutPath() throws Exception {
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    request.getSession(true).setAttribute(
        AuthenticatorConstants.VALIDATION_ATTRIBUTE, new Object());
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        AuthenticatorConstants.VALIDATION_ATTRIBUTE));
  }

  public void testRequestForLogoutPathNoApplicationLogout() throws Exception {
    setRequiredConfig();
    filterConfig.setInitParameter(AuthenticatorConstants.APPLICATION_LOGOUT, "false");
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    request.getSession(true).setAttribute(
        AuthenticatorConstants.VALIDATION_ATTRIBUTE, new Object());
    filter.doFilter(request, response, filterChain);
    assertTrue(!filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        AuthenticatorConstants.VALIDATION_ATTRIBUTE));
  }

  public void testRequestForOtherPath() throws Exception {
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(OTHER_URL);
    request.getSession(true).setAttribute(
        AuthenticatorConstants.VALIDATION_ATTRIBUTE, new Object());
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNotNull(request.getSession().getAttribute(
        AuthenticatorConstants.VALIDATION_ATTRIBUTE));
  }

  public void testGlobalLogout() throws Exception {
    filterConfig.setInitParameter(AuthenticatorConstants.GLOBAL_LOGOUT, "true");
    doGlobalLogout();
  }

  public void testGlobalLogoutWithRedirectUrl() throws Exception {
    filterConfig.setInitParameter(AuthenticatorConstants.GLOBAL_LOGOUT, "true");
    filterConfig.setInitParameter(AuthenticatorConstants.REDIRECT_URL, REDIRECT_URL);
    doGlobalLogout();
  }

  private void doGlobalLogout() throws Exception {
    ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
    ProtocolConfigurationHolder.setConfiguration(config);
    config.setServerUrl(SERVER_URL);
    config.setServiceUrl(SERVICE_URL);
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    filter.doFilter(request, response, filterChain);
    assertEquals(
        new SimpleUrlGenerator(config).getLogoutUrl(filter.getRedirectUrl()), 
        response.getRedirect());
  }

  public void testGlobalLogoutBypass() throws Exception {
    ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
    ProtocolConfigurationHolder.setConfiguration(config);
    config.setServerUrl(SERVER_URL);
    setRequiredConfig();
    filterConfig.setInitParameter(AuthenticatorConstants.GLOBAL_LOGOUT, "true");
    filterConfig.setInitParameter(AuthenticatorConstants.BYPASS_REDIRECT_URL, 
        BYPASS_REDIRECT_URL);
    request.getSession().setAttribute(AuthenticatorConstants.BYPASS_ATTRIBUTE, "bypass");
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    filter.doFilter(request, response, filterChain);
    assertEquals(filter.getBypassRedirectUrl(), response.getRedirect());
  }

  public void testRedirectUrl() throws Exception {
    filterConfig.setInitParameter(AuthenticatorConstants.REDIRECT_URL, REDIRECT_URL);
    setRequiredConfig();
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    filter.doFilter(request, response, filterChain);
    assertEquals(filter.getRedirectUrl(), response.getRedirect());
  }

  public void testApplicationLogoutWithNoRedirects() throws Exception {
    filterConfig.setInitParameter(AuthenticatorConstants.APPLICATION_LOGOUT, "true");
    filterConfig.setInitParameter(AuthenticatorConstants.GLOBAL_LOGOUT, "false");
    filterConfig.setInitParameter(AuthenticatorConstants.LOGOUT_PATH, LOGOUT_PATH);
    filter.init(filterConfig);
    request.setRequestURL(LOGOUT_URL);
    filterChain.setServlet(new MockServlet());
    filter.doFilter(request, response, filterChain);
    BufferedServletOutputStream os = (BufferedServletOutputStream)
      response.getOutputStream();
    assertEquals("test", new String(os.toByteArray()));
  }
  
  private static class MockServlet implements Servlet {

    private ServletConfig servletConfig;
    
    public void destroy() {
      // TODO Auto-generated method stub
    }

    public ServletConfig getServletConfig() {
      return servletConfig;
    }

    public String getServletInfo() {
      return "MockServlet";
    }

    public void init(ServletConfig servletConfig) throws ServletException {
      this.servletConfig = servletConfig;
    }

    public void service(ServletRequest request, ServletResponse response)
        throws ServletException, IOException {
      response.getWriter().print("test");
      response.getWriter().flush();
    }
    
  }
}
