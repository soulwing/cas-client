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

import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolConfigurationImpl;
import org.soulwing.cas.client.SimpleUrlGenerator;
import org.soulwing.cas.http.UrlGeneratorFactory;
import org.soulwing.servlet.http.MockHttpServletRequest;

import junit.framework.TestCase;


public class ContextProtocolConfigurationTest extends TestCase {

  private static final String SERVER_URL = "https://localhost/cas";
  private static final String CONTEXT_PATH = "/appContext";
  private static final String SERVLET_PATH = "/app.action";
  private static final String TICKET = "testTicket";
  private static final String SERVICE_URL = "https://service/";
  private static final String REQUEST_URL = "https://localhost"
      + CONTEXT_PATH + SERVLET_PATH;
  
  private MockHttpServletRequest request;
  private ProtocolConfigurationImpl config;
  private SimpleUrlGenerator generator;
  
  protected void setUp() throws Exception {
    request = new MockHttpServletRequest(CONTEXT_PATH, REQUEST_URL);
    config = new ProtocolConfigurationImpl();
    config.setServerUrl(SERVER_URL);
    config.setServiceUrl(SERVICE_URL);
    generator = new SimpleUrlGenerator(
        new UrlGeneratorFactory.ContextProtocolConfiguration(request, config));
  }
  
  /*
   * Deriving the service URL from the request URL is no longer supported
   */
//  public void testUseRequestUrl() throws Exception {
//    URL url = getServiceUrl(new URL(generator.getServiceValidateUrl(TICKET))
//        .getQuery());
//    assertEquals(REQUEST_URL, url.toString());
//  }
  
  public void testServiceUrl() throws Exception {
    config.setServiceUrl(SERVICE_URL);
    URL url = getServiceUrl(new URL(generator.getServiceValidateUrl(TICKET))
        .getQuery());
    assertEquals(SERVICE_URL.substring(0, SERVICE_URL.length() - 1) 
        + CONTEXT_PATH + SERVLET_PATH, url.toString());
  }

  public void testStripTicketParam() throws Exception {
    request.setQueryString("ticket=anotherTicket");
    URL url = getServiceUrl(new URL(generator.getServiceValidateUrl(TICKET))
        .getQuery());
    Map params = getQueryParameterMap(url.getQuery());
    assertTrue(!params.containsKey(ProtocolConstants.TICKET_PARAM));
  }
  
  public void testStripLeadingTicketParam() throws Exception {
    request.setQueryString("ticket=anotherTicket&param=value");
    URL url = getServiceUrl(new URL(generator.getServiceValidateUrl(TICKET))
        .getQuery());
    Map params = getQueryParameterMap(url.getQuery());
    assertTrue(!params.containsKey(ProtocolConstants.TICKET_PARAM));
    assertTrue(params.containsKey("param"));
  }
  
  public void testStripTrailingTicketParam() throws Exception {
    request.setQueryString("param=value&ticket=anotherTicket");
    URL url = getServiceUrl(new URL(generator.getServiceValidateUrl(TICKET))
        .getQuery());
    Map params = getQueryParameterMap(url.getQuery());
    assertTrue(!params.containsKey(ProtocolConstants.TICKET_PARAM));
    assertTrue(params.containsKey("param"));
  }
  
  public void testStripEmbeddedTicketParam() throws Exception {
    request.setQueryString("param1=value&ticket=anotherTicket&param2=value");
    URL url = getServiceUrl(new URL(generator.getServiceValidateUrl(TICKET))
        .getQuery());
    Map params = getQueryParameterMap(url.getQuery());
    assertTrue(!params.containsKey(ProtocolConstants.TICKET_PARAM));
    assertTrue(params.containsKey("param1"));
    assertTrue(params.containsKey("param2"));
  }
  
  private URL getServiceUrl(String queryString) throws Exception {
    return new URL(URLDecoder.decode(
        (String) getQueryParameterMap(queryString)
            .get(ProtocolConstants.SERVICE_PARAM), "UTF-8"));
  }
  
  @SuppressWarnings("unchecked")
  public Map getQueryParameterMap(String queryString) {
    if (queryString == null) {
      return new HashMap();
    }
    Map map = new HashMap();
    String[] params = queryString.split("&");
    for (int i = 0; i < params.length; i++) {
      int j = params[i].indexOf('=');
      String name = params[i].substring(0, j);
      String value = params[i].substring(j + 1);
      map.put(name, value);
    }
    return map;
  }
}
