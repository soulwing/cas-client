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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.soulwing.cas.client.ProtocolConfigurationImpl;
import org.soulwing.cas.client.ProtocolConfigurationHolder;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.StringProtocolSource;
import org.soulwing.cas.http.AuthenticatorConstants;
import org.soulwing.cas.http.UrlGeneratorFactory;
import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;


public class ServiceValidationFilterPathTest extends TestCase {

  private static final String SERVER_URL = "https://localhost/cas";
  private static final String SERVICE_URL = "https://localhost/myapp";
  private static final String FILTER_PATH = "/testFilterPath";
  private static final String TICKET = "TEST TICKET";
  private static final String USER = "TEST USER";
  private static final String RESULT_CODE = "TEST CODE";
  private static final String RESULT_MESSAGE = "TEST MESSAGE";
  private static final String URL = 
      "https://localhost" + FILTER_PATH;
  private static final String SOURCE_CLASS_NAME = 
      "org.soulwing.cas.client.StringProtocolSource";
  private static final String AUTH_FAILED_URL = "mock_redirect_url";

  private ServiceValidationFilter filter;
  private MockFilterConfig config;
  private MockFilterChain filterChain;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private StringProtocolSource source;
  private ProtocolConfigurationImpl protocolConfig;
  
  protected void setUp() throws Exception {
    protocolConfig = new ProtocolConfigurationImpl();
    protocolConfig.setServerUrl(SERVER_URL);
    protocolConfig.setServiceUrl(SERVICE_URL);
    ProtocolConfigurationHolder.setConfiguration(protocolConfig);
    config = new MockFilterConfig();
    config.setInitParameter(AuthenticatorConstants.FILTER_PATH, FILTER_PATH);
    config.setInitParameter(AuthenticatorConstants.SOURCE_CLASS_NAME,
        SOURCE_CLASS_NAME);
    filter = new ServiceValidationFilter();
    filter.init(config);
    source = (StringProtocolSource)
        filter.getConfiguration().getProtocolSource();
    filterChain = new MockFilterChain();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }
  
  public void testRedirectOnFirstValidationFailure() throws Exception {
    request.setRequestURL(URL);
    request.setParameter(ProtocolConstants.TICKET_PARAM, TICKET);
    source.setText(getFailureText());
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    assertNotNull(response.getRedirect());
  }

  public void testFailOnSecondValidationFailure() throws Exception {
    request.setRequestURL(URL);
    request.setParameter(ProtocolConstants.TICKET_PARAM, TICKET);
    source.setText(getFailureText());
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
  }

  public void testNoTicketRedirectToLogin() throws Exception {
    config.setInitParameter(AuthenticatorConstants.AUTH_FAILED_URL, AUTH_FAILED_URL);
    filter.init(config);
    source = (StringProtocolSource)
        filter.getConfiguration().getProtocolSource();
    request.setRequestURL(URL);
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    assertEquals(UrlGeneratorFactory.getUrlGenerator(request, 
        protocolConfig).getLoginUrl(), response.getRedirect());
  }

  public void testNoTicketRedirectToAuthFailed() throws Exception {
    config.setInitParameter(AuthenticatorConstants.AUTH_FAILED_URL, AUTH_FAILED_URL);
    config.setInitParameter(AuthenticatorConstants.REDIRECT_TO_LOGIN, 
        Boolean.toString(false));
    filter.init(config);
    source = (StringProtocolSource)
        filter.getConfiguration().getProtocolSource();
    request.setRequestURL(URL);
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    assertEquals(AUTH_FAILED_URL, response.getRedirect());
  }

  public void testValidationFailureRedirect() throws Exception {
    config.setInitParameter(AuthenticatorConstants.AUTH_FAILED_URL, AUTH_FAILED_URL);
    filter.init(config);
    source = (StringProtocolSource)
        filter.getConfiguration().getProtocolSource();
    request.setRequestURL(URL);
    request.setParameter(ProtocolConstants.TICKET_PARAM, TICKET);
    source.setText(getFailureText());
    filter.doFilter(request, response, filterChain);
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    assertEquals(AUTH_FAILED_URL, response.getRedirect());
  }

  private String getFailureText() {
    StringBuilder sb = new StringBuilder();
    sb.append("<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>");
    sb.append("<cas:authenticationFailure code='");
    sb.append(RESULT_CODE);
    sb.append("'>");
    sb.append(RESULT_MESSAGE);
    sb.append("</cas:authenticationFailure>");
    sb.append("</cas:serviceResponse>");
    return sb.toString();
  }
  
  public void testValidationSuccess() throws Exception {
    request.setRequestURL(URL);
    request.setParameter(ProtocolConstants.TICKET_PARAM, TICKET);
    source.setText(getSuccessText());
    filter.doFilter(request, response, filterChain);
    assertEquals(true, filterChain.isChainInvoked());
    HttpServletRequest chainedRequest = (HttpServletRequest) 
        filterChain.getChainedRequest();
    assertEquals(USER, chainedRequest.getUserPrincipal().getName());
    assertEquals(USER, chainedRequest.getRemoteUser());
    assertEquals(request.getRequestURL().toString(), 
        chainedRequest.getRequestURL().toString());
    assertEquals(request.getQueryString(), chainedRequest.getQueryString());
  }

  private String getSuccessText() {
    StringBuilder sb = new StringBuilder();
    sb.append("<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>");
    sb.append("<cas:authenticationSuccess>");
    sb.append("<cas:user>");
    sb.append(USER);
    sb.append("</cas:user>");
    sb.append("</cas:authenticationSuccess>");
    sb.append("</cas:serviceResponse>");
    return sb.toString();
  }
}

