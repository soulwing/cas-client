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

import junit.framework.TestCase;

import org.soulwing.servlet.MockFilterChain;
import org.soulwing.servlet.MockFilterConfig;
import org.soulwing.servlet.http.MockHttpServletRequest;
import org.soulwing.servlet.http.MockHttpServletResponse;


public class ValidationFilterTest extends TestCase {
  
  private static final String SERVER_URL = "https://localhost/cas";
  private static final String FILTER_PATH = "/testFilterPath";
  private static final String SOME_OTHER_PATH = "/some/other/path";
  private static final String CONTEXT_URL = "http://localhost/context";
  
  private MockFilterAuthenticator authenticator;
  private MockValidationFilter filter;
  private MockFilterChain filterChain;
  
  protected void setUp() throws Exception {
    authenticator = new MockFilterAuthenticator();
    filter = new MockValidationFilter(authenticator);
    filterChain = new MockFilterChain();
  }
  
  private MockFilterConfig requiredFilterConfig() {
    MockFilterConfig filterConfig = new MockFilterConfig();
    filterConfig.setInitParameter(FilterConstants.SERVER_URL, SERVER_URL);
    filterConfig.setInitParameter(FilterConstants.SERVICE_URL, CONTEXT_URL);
    filterConfig.setInitParameter(FilterConstants.FILTER_PATH, FILTER_PATH);
    return filterConfig;
  }
  
  private MockHttpServletRequest newServletRequest(String servletPath) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURL(CONTEXT_URL + servletPath);
    return request;
  }
  
  private MockHttpServletResponse newServletResponse() {
    return new MockHttpServletResponse();
  }
  
  public void testRequestNotFilterPath() throws Exception {
    filter.init(requiredFilterConfig());
    MockHttpServletRequest request = newServletRequest(SOME_OTHER_PATH);
    MockHttpServletResponse response = newServletResponse();
    filter.doFilter(request, response, filterChain);
    assertTrue(!filterChain.isChainInvoked());
    assertEquals(UrlGeneratorFactory.getUrlGenerator(request).getLoginUrl(), 
          response.getRedirect());
  }
  
}
