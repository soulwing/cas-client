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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.soulwing.cas.client.StringProtocolSource;
import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;


public class ProxyValidateFilterTest extends TestCase {

  private static final String URL = "https://localhost/myapp/myapp.htm";
  private static final String TICKET = "TEST TICKET";
  private static final String USER = "TEST USER";
  private static final String RESULT_CODE = "TEST CODE";
  private static final String RESULT_MESSAGE = "TEST MESSAGE";
  private static final String PROXY1 = "proxy1";
  private static final String PROXY2 = "proxy2";
  private static final String OTHER_PROXY = "otherProxy";
  private static final String TRUSTED_PROXIES = PROXY1 + ", " + PROXY2;
  
  private ProxyValidateFilter filter;
  private StringProtocolSource source;
  private MockFilterChain filterChain = new MockFilterChain();
  private MockHttpServletRequest request = new MockHttpServletRequest();
  private MockHttpServletResponse response = new MockHttpServletResponse();
  
  
  protected void setUp() throws Exception {
    MockFilterConfig config = new MockFilterConfig();
    config.setInitParameter("serverUrl", "https://localhost/cas");
    config.setInitParameter("serviceUrl", "https://localhost/myapp");
    config.setInitParameter("trustedProxies", TRUSTED_PROXIES);
    config.setInitParameter("sourceClassName",
        "org.soulwing.cas.client.StringProtocolSource");
    this.filter = new ProxyValidateFilter();
    filter.init(config);
    source = (StringProtocolSource)
        filter.getConfiguration().getProtocolSource();
    filterChain = new MockFilterChain();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }
  
  public void testLoginRedirect() throws Exception {
    request.setRequestURL(URL);
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    assertNotNull(response.getRedirect());
  }

  public void testValidationFailure() throws Exception {
    request.setRequestURL(URL);
    request.setParameter("ticket", TICKET);
    source.setText(getFailureText());
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
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
  
  public void testValidationSuccessNoProxies() throws Exception {
    request.setRequestURL(URL);
    request.setParameter("ticket", TICKET);
    source.setText(getSuccessText(null));
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

  public void testValidationSuccessTrustedProxies() throws Exception {
    request.setRequestURL(URL);
    request.setParameter("ticket", TICKET);
    List proxies = Arrays.asList(new String[]{ PROXY1, PROXY2 });
    source.setText(getSuccessText(proxies));
    filter.doFilter(request, response, filterChain);
    assertEquals(true, filterChain.isChainInvoked());
  }

  public void testValidationSuccessUntrustedProxy() throws Exception {
    request.setRequestURL(URL);
    request.setParameter("ticket", TICKET);
    List proxies = Arrays.asList(new String[]{ OTHER_PROXY });
    source.setText(getSuccessText(proxies));
    filter.doFilter(request, response, filterChain);
    assertEquals(false, filterChain.isChainInvoked());
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
  }

  private String getSuccessText(List proxies) {
    StringBuilder sb = new StringBuilder(1000);
    sb.append("<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>");
    sb.append("<cas:authenticationSuccess>");
    sb.append("<cas:user>");
    sb.append(USER);
    sb.append("</cas:user>");
    if (proxies != null) {
      sb.append("<cas:proxies>");
      for (Iterator i = proxies.iterator(); i.hasNext(); ) {
        sb.append("<cas:proxy>");
        sb.append((String) i.next());
        sb.append("</cas:proxy>");
      }
      sb.append("</cas:proxies>");
    }
    sb.append("</cas:authenticationSuccess>");
    sb.append("</cas:serviceResponse>");
    return sb.toString();
  }
}

