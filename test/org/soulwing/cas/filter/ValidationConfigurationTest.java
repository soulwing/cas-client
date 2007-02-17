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

import javax.servlet.FilterConfig;

import junit.framework.TestCase;

import org.soulwing.cas.client.StringProtocolSource;
import org.soulwing.servlet.MockFilterConfig;


public class ValidationConfigurationTest extends TestCase {

  private final static String SERVER_URL = "testServerUrl";
  private final static String FILTER_PATH = "testFilterPath";
  private final static String SERVICE_URL = "testServiceUrl";
  private final static String DEFAULT_URL = "testDefaultUrl";
  private final static String PROXY_CALLBACK_URL = "testProxyCallbackUrl";
  private static final String AUTH_FAILED_URL = "testAuthFailedUrl";

  private ValidationConfiguration getConfiguration(FilterConfig config)
      throws Exception {
    return new ValidationConfiguration(config);
  }
  
  private MockFilterConfig getRequiredConfig() {
    MockFilterConfig config = new MockFilterConfig();
    config.setInitParameter(FilterConstants.SERVER_URL, SERVER_URL);
    config.setInitParameter(FilterConstants.SERVICE_URL, SERVICE_URL);
    config.setInitParameter(FilterConstants.FILTER_PATH, FILTER_PATH);
    return config;
  }

  public void testServerUrlNotSet() throws Exception {
    try {
      getConfiguration(new MockFilterConfig());
    }
    catch (FilterParameterException ex) {
      assertTrue(true);
    }
  }
  
  public void testDefaults() throws Exception {
    ValidationConfiguration config = getConfiguration(getRequiredConfig());
    assertEquals(SERVER_URL, config.getServerUrl());
    assertEquals(FILTER_PATH, config.getFilterPath());
    assertNull(config.getDefaultPath());
    assertEquals(SERVICE_URL, config.getServiceUrl());
    assertNull(config.getProxyCallbackUrl());
    assertNull(config.getAuthFailedUrl());
    assertEquals(ValidationConfiguration.GATEWAY_DEFAULT, 
        config.getGatewayFlag());
    assertEquals(ValidationConfiguration.RENEW_DEFAULT, 
        config.getRenewFlag());
    assertEquals(ValidationConfiguration.SOURCE_CLASS_DEFAULT, 
        config.getProtocolSource().getClass());
  }
  
  public void testServiceUrlSet() throws Exception {
    MockFilterConfig filterConfig = getRequiredConfig();
    filterConfig.setInitParameter(FilterConstants.SERVICE_URL, SERVICE_URL);
    ValidationConfiguration config = getConfiguration(filterConfig); 
    assertEquals(SERVICE_URL, 
        config.getServiceUrl());
  }

  public void testDefaultUrlSet() throws Exception {
    MockFilterConfig filterConfig = getRequiredConfig();
    filterConfig.setInitParameter(FilterConstants.DEFAULT_PATH, DEFAULT_URL);
    ValidationConfiguration config = getConfiguration(filterConfig); 
    assertEquals(DEFAULT_URL, config.getDefaultPath());
  }

  public void testProxyCallbackUrlSet() throws Exception {
    MockFilterConfig filterConfig = getRequiredConfig();
    filterConfig.setInitParameter(FilterConstants.PROXY_CALLBACK_URL, 
        PROXY_CALLBACK_URL);
    ValidationConfiguration config = getConfiguration(filterConfig); 
    assertEquals(PROXY_CALLBACK_URL, 
        config.getProxyCallbackUrl());
  }

  public void testAuthFailedUrlSet() throws Exception {
    MockFilterConfig filterConfig = getRequiredConfig();
    filterConfig.setInitParameter(FilterConstants.AUTH_FAILED_URL, 
        AUTH_FAILED_URL);
    ValidationConfiguration config = getConfiguration(filterConfig); 
    assertEquals(AUTH_FAILED_URL, config.getAuthFailedUrl());
  }

  public void testGatewaySet() throws Exception {
    MockFilterConfig filterConfig = getRequiredConfig();
    filterConfig.setInitParameter(FilterConstants.GATEWAY, 
        Boolean.toString(!ValidationConfiguration.GATEWAY_DEFAULT)); 
    ValidationConfiguration config = getConfiguration(filterConfig); 
    assertEquals(!ValidationConfiguration.GATEWAY_DEFAULT,
        config.getGatewayFlag());
  }

  public void testRenewSet() throws Exception {
    MockFilterConfig filterConfig = getRequiredConfig();
    filterConfig.setInitParameter(FilterConstants.RENEW,
        Boolean.toString(!ValidationConfiguration.RENEW_DEFAULT)); 
    ValidationConfiguration config = getConfiguration(filterConfig); 
    assertEquals(!ValidationConfiguration.RENEW_DEFAULT, 
        config.getRenewFlag());
  }

  public void testSourceClassSet() throws Exception {
    MockFilterConfig filterConfig = getRequiredConfig();
    filterConfig.setInitParameter(FilterConstants.SOURCE_CLASS_NAME, 
        StringProtocolSource.class.getCanonicalName());
    ValidationConfiguration config = getConfiguration(filterConfig); 
    assertEquals(StringProtocolSource.class,
        config.getProtocolSource().getClass());
  }
  
  public void testSourceClassNotValidationSource() throws Exception {
    MockFilterConfig filterConfig = getRequiredConfig();
    filterConfig.setInitParameter(FilterConstants.SOURCE_CLASS_NAME, 
        Object.class.getCanonicalName());
    try {
      getConfiguration(filterConfig);
      fail("Expected FilterParameterException");
    }
    catch (FilterParameterException ex) {
      assertTrue(true);
    }
  }
}
