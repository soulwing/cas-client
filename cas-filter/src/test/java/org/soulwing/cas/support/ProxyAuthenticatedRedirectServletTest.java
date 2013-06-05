/*
 * ProxyAuthenticatedRedirectServlet.java
 *
 * Created on Dec 26, 2007 
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
package org.soulwing.cas.support;

import java.net.MalformedURLException;

import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolConfigurationHolder;
import org.soulwing.cas.client.ProtocolConfigurationImpl;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;

/**
 * Unit tests for <code>ProxyAuthenticatedRedirectServlet</code>.
 *
 * @author Carl Harris
 */
public class ProxyAuthenticatedRedirectServletTest extends TestCase {

  private static final String TEST_URL = "http://localhost/testServlet";
  private static final String TEST_QUERY_STRING = "param1=value";
  private static final String TEST_TICKET = "testTicket";

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockProxyTicketService proxyTicketService;
  private ProxyAuthenticatedRedirectServlet servlet;

  protected void setUp() throws Exception {
    super.setUp();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    proxyTicketService = new MockProxyTicketService();
    servlet = new ProxyAuthenticatedRedirectServlet();
    servlet.setProxyTicketService(proxyTicketService);
  }
  
  public void testInitWithTicketServiceSet() throws Exception {
    servlet.init();
    assertSame(proxyTicketService, servlet.getProxyTicketService());
    assertNull(servlet.getConfiguration());
  }

  public void testInitWithConfigurationSet() throws Exception {
    ProtocolConfiguration configuration = new ProtocolConfigurationImpl();
    servlet.setConfiguration(configuration);
    servlet.setProxyTicketService(null);
    servlet.init();
    assertSame(configuration, servlet.getConfiguration());
    assertNotNull(servlet.getProxyTicketService());
  }

  public void testInitWithConfigurationHolderSet() throws Exception {
    ProtocolConfigurationHolder.setConfiguration(new ProtocolConfigurationImpl());
    servlet.setConfiguration(null);
    servlet.setProxyTicketService(null);
    servlet.init();
    assertSame(ProtocolConfigurationHolder.getConfiguration(),
        servlet.getConfiguration());
    assertNotNull(servlet.getProxyTicketService());
  }

  public void testInitWithNothingSet() throws Exception {
    ProtocolConfigurationHolder.setConfiguration(null);
    servlet.setConfiguration(null);
    servlet.setProxyTicketService(null);
    try {
      servlet.init();
      fail("expected exception");
    }
    catch (ServletException ex) {
      assertEquals(IllegalStateException.class, ex.getRootCause().getClass());
    }
  }
  
  public void testDoGetWithNoUrlParameter() throws Exception {
    try {
      servlet.doGet(request, response);
      fail("expected exception");
    }
    catch (ServletException ex) {
      assertTrue(true);
    }
  }
  
  public void testDoGetWhenUrlParameterHasNoQueryString() throws Exception {
    request.setParameter(ProxyAuthenticatedRedirectServlet.URL_PARAM, 
        TEST_URL);
    servlet.doGet(request, response);
    assertEquals(TEST_URL + "?" + ProtocolConstants.TICKET_PARAM + "=" + TEST_TICKET, 
        response.getRedirect());
  }

  public void testDoGetWhenUrlParameterHasQueryString() throws Exception {
    request.setParameter(ProxyAuthenticatedRedirectServlet.URL_PARAM, 
        TEST_URL + "?" + TEST_QUERY_STRING);
    servlet.doGet(request, response);
    assertEquals(TEST_URL + "?" + TEST_QUERY_STRING
        + "&" + ProtocolConstants.TICKET_PARAM + "=" + TEST_TICKET, 
        response.getRedirect());
  }

  public void testDoGetWhenUrlParameterIsMalformed() throws Exception {
    try {
      request.setParameter(ProxyAuthenticatedRedirectServlet.URL_PARAM, "*");
      servlet.doGet(request, response);
    }
    catch (ServletException ex) {
      assertEquals(MalformedURLException.class, ex.getRootCause().getClass());
    }
  }

  private static class MockProxyTicketService implements ProxyTicketService {

    private String ticket = TEST_TICKET;
    
    public String getTicket(String targetService) {
      return ticket;
    }

    public String getTicket(String targetService, String proxyGrantingTicket) {
      return ticket;
    }
    
  }
  
}
