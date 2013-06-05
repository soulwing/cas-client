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

import junit.framework.TestCase;

import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.http.AuthenticatorConstants;
import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;

public class ProxyCallbackFilterTest extends TestCase {

  private static final String FILTER_PATH = "/proxyCallback.action";
  private static final String PROXY_CALLBACK_URL = "https:" + FILTER_PATH;
  private static final String OTHER_URL = "https:/other.action";
  private static final String TICKET = "ticket";
  private static final String TICKET_IOU = "ticket IOU";
  private static final String OTHER_TICKET_IOU = "other ticket IOU";
  
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockFilterChain filterChain;
  private MockFilterConfig filterConfig;
  private ProxyCallbackFilter filter;
  
  protected void setUp() throws Exception {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filterChain = new MockFilterChain();
    filterConfig = new MockFilterConfig();
    filter = new ProxyCallbackFilter();
  }

  private void initFilter() throws Exception {
    filterConfig.setInitParameter(AuthenticatorConstants.FILTER_PATH, FILTER_PATH);
    filter.init(filterConfig);   
  }
  
  public void testInitNoCallbackUrl() throws Exception {
    try {
      filter.init(filterConfig);
      fail("Expected ServletException");
    }
    catch (ServletException ex) {
      assertTrue(true);
    }
  }

  public void testNormalCallbackRequest() throws Exception {
    initFilter();
    request.setRequestURL(PROXY_CALLBACK_URL);
    request.setParameter(ProtocolConstants.PROXY_TICKET_PARAM, TICKET);
    request.setParameter(ProtocolConstants.PROXY_TICKET_IOU_PARAM, TICKET_IOU);
    filter.doFilter(request, response, filterChain);
    assertTrue(!filterChain.isChainInvoked());
    assertTrue(filter.getTicketMap().containsKey(TICKET_IOU));
  }
  
  public void testCallbackRequestWithNoTicket() throws Exception {
    initFilter();
    request.setRequestURL(PROXY_CALLBACK_URL);
    request.setParameter(ProtocolConstants.PROXY_TICKET_IOU_PARAM, TICKET_IOU);
    filter.doFilter(request, response, filterChain);
    assertTrue(!filterChain.isChainInvoked());
    assertTrue(filter.getTicketMap().keySet().isEmpty());
  }

  public void testCallbackRequestWithNoTicketIou() throws Exception {
    initFilter();
    request.setRequestURL(PROXY_CALLBACK_URL);
    request.setParameter(ProtocolConstants.PROXY_TICKET_PARAM, TICKET);
    filter.doFilter(request, response, filterChain);
    assertTrue(!filterChain.isChainInvoked());
    assertTrue(filter.getTicketMap().keySet().isEmpty());
  }

  @SuppressWarnings("unchecked")
  public void testValidatedRequestMatchingTicket() throws Exception {
    initFilter();
    MockServiceValidationResponse validation = new MockServiceValidationResponse();
    validation.setProxyGrantingTicketIou(TICKET_IOU);
    request.getSession().setAttribute(AuthenticatorConstants.VALIDATION_ATTRIBUTE, 
        validation);
    request.setRequestURL(OTHER_URL);
    filter.getTicketMap().put(TICKET_IOU, TICKET);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNotNull(request.getSession().getAttribute(
        AuthenticatorConstants.PROXY_GRANTING_TICKET_ATTRIBUTE));
  }
  
  @SuppressWarnings("unchecked")
  public void testValidatedRequestDifferentTicket() throws Exception {
    initFilter();
    MockServiceValidationResponse validation = new MockServiceValidationResponse();
    validation.setProxyGrantingTicketIou(OTHER_TICKET_IOU);
    request.getSession().setAttribute(AuthenticatorConstants.VALIDATION_ATTRIBUTE, 
        validation);
    request.setRequestURL(OTHER_URL);
    filter.getTicketMap().put(TICKET_IOU, TICKET);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        AuthenticatorConstants.PROXY_GRANTING_TICKET_ATTRIBUTE));
  }

  public void testRequestWithSessionNoValidation() throws Exception {
    initFilter();
    request.getSession();  // create the session 
    request.setRequestURL(OTHER_URL);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession().getAttribute(
        AuthenticatorConstants.PROXY_GRANTING_TICKET_ATTRIBUTE));
  }

  public void testRequestWithoutSession() throws Exception {
    initFilter();
    request.setRequestURL(OTHER_URL);
    filter.doFilter(request, response, filterChain);
    assertTrue(filterChain.isChainInvoked());
    assertNull(request.getSession(false));
  }

}

