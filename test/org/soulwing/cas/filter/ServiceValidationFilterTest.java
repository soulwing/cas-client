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
import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;


public class ServiceValidationFilterTest extends TestCase {

  private static final String POST_VALIDATION_PATH_NAME = "destinationPath";
  private static final String POST_VALIDATION_PATH_VALUE = "/destination.action";
  private static final String SERVER_URL = "https://localhost/cas";
  private static final String SERVICE_URL = "https://localhost/apps";
  private static final String URL = "https://localhost/apps/myapp.htm";
  private static final String TICKET = "TEST TICKET";
  private static final String USER = "TEST USER";
  private static final String RESULT_CODE = "TEST CODE";
  private static final String RESULT_MESSAGE = "TEST MESSAGE";
  private static final String AUTH_FAILED_URL = "https:/authFailed.htm";
  private static final String PROXY_CALLBACK_URL = 
      "https://localhost/apps/pgtCallback";
  private static final String SOURCE_CLASS_NAME = 
      "org.soulwing.cas.client.StringProtocolSource";
  
  private ServiceValidationFilter filter;
  private MockFilterConfig config;
  private MockFilterChain filterChain;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private StringProtocolSource source;
  
  protected void setUp() throws Exception {
    ProtocolConfigurationImpl protocolConfig = new ProtocolConfigurationImpl();
    protocolConfig.setServerUrl(SERVER_URL);
    protocolConfig.setServiceUrl(SERVICE_URL);
    protocolConfig.setProxyCallbackUrl(PROXY_CALLBACK_URL);
    ProtocolConfigurationHolder.setConfiguration(protocolConfig);
    config = new MockFilterConfig();
    config.setInitParameter(FilterConstants.SOURCE_CLASS_NAME,
        SOURCE_CLASS_NAME);
    filter = new ServiceValidationFilter();
    filter.init(config);
    source = (StringProtocolSource)
        filter.getConfiguration().getProtocolSource();
    filterChain = new MockFilterChain();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }
  
  protected void tearDown() throws Exception {
    ProtocolConfigurationHolder.setConfiguration(null);
  }

  public void testLoginRedirect() throws Exception {
    request.setRequestURL(URL);
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    assertNotNull(response.getRedirect());
  }

  public void testValidationFailureError() throws Exception {
    request.setRequestURL(URL);
    request.setParameter(ProtocolConstants.TICKET_PARAM, TICKET);
    source.setText(getFailureText());
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
  }

  public void testValidationFailureRedirect() throws Exception {
    config.setInitParameter(FilterConstants.AUTH_FAILED_URL, AUTH_FAILED_URL);
    filter.init(config);
    source = (StringProtocolSource)
        filter.getConfiguration().getProtocolSource();
    request.setRequestURL(URL);
    request.setParameter(ProtocolConstants.TICKET_PARAM, TICKET);
    source.setText(getFailureText());
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

  public void testPostValidationRedirectWhenParameterSpecified() throws Exception {
    filter.getConfiguration().setPostValidationRedirectParameter(
        POST_VALIDATION_PATH_NAME);
    request.setRequestURL(URL);
    request.setParameter(ProtocolConstants.TICKET_PARAM, TICKET);
    request.setParameter(POST_VALIDATION_PATH_NAME, POST_VALIDATION_PATH_VALUE);
    source.setText(getSuccessText());
    filter.doFilter(request, response, filterChain);
    assertFalse(filterChain.isChainInvoked());
    assertTrue(response.getRedirect().endsWith(POST_VALIDATION_PATH_VALUE));
  }

  public void testPostValidationNoRedirectWhenParameterOmitted() throws Exception {
    filter.getConfiguration().setPostValidationRedirectParameter(
        POST_VALIDATION_PATH_NAME);
    request.setRequestURL(URL);
    request.setParameter(ProtocolConstants.TICKET_PARAM, TICKET);
    source.setText(getSuccessText());
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
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

  public void testBypassOnProxyCallback() throws Exception {
    request.setRequestURL(PROXY_CALLBACK_URL);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession(false));
  }
  
  public void testBypassOnBypassAttribute() throws Exception {
    request.setRequestURL(URL);
    request.getSession().setAttribute(FilterConstants.BYPASS_ATTRIBUTE, 
        new Boolean(true));
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
  }
  
}

