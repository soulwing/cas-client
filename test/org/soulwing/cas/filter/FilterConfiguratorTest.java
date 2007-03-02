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

import org.soulwing.servlet.MockFilterConfig;


public class FilterConfiguratorTest extends TestCase {

  private static final String TEST_PARAM = "testParam";
  private static final String TEST_VALUE = "testValue";
  private static final String DEFAULT_VALUE = "defaultValue";
  
  private MockFilterConfig filterConfig;
  private Configurator configurator;
  
  protected void setUp() throws Exception {
    filterConfig = new MockFilterConfig();
    configurator = new Configurator(filterConfig);
  }

  public void testGetParameterIsSet() throws Exception {
    filterConfig.setInitParameter(TEST_PARAM, TEST_VALUE);
    assertEquals(TEST_VALUE, configurator.getParameter(TEST_PARAM));
  }
  
  public void testGetParameterNotSet() throws Exception {
    assertNull(configurator.getParameter(TEST_PARAM));
  }

  public void testGetParameterIsSetHasDefault() throws Exception {
    filterConfig.setInitParameter(TEST_PARAM, TEST_VALUE);
    assertEquals(TEST_VALUE, configurator.getParameter(TEST_PARAM, 
        DEFAULT_VALUE));
  }

  public void testGetParameterNotSetHasDefault() throws Exception {
    assertEquals(DEFAULT_VALUE, configurator.getParameter(TEST_PARAM,
        DEFAULT_VALUE));
  }

  public void testGetRequiredParameterIsSet() throws Exception {
    filterConfig.setInitParameter(TEST_PARAM, TEST_VALUE);
    assertEquals(TEST_VALUE, configurator.getRequiredParameter(TEST_PARAM));
  }

  public void testGetRequiredParameterNotSet() throws Exception {
    try {
      assertEquals(TEST_VALUE, configurator.getRequiredParameter(TEST_PARAM));
      fail("Expected FilterParameterException");
    }
    catch (FilterParameterException ex) {
      assertTrue(true);
    }
  }

  public void testGetClassFromParameterIsSet() throws Exception {
    filterConfig.setInitParameter(TEST_PARAM, Object.class.getCanonicalName());
    assertEquals(Object.class, 
        configurator.getClassFromParameter(TEST_PARAM));
  }

  public void testGetClassFromParameterNotSet() throws Exception {
    assertNull(configurator.getClassFromParameter(TEST_PARAM));
  }

  public void testGetClassFromParameterIsSetHasDefault() throws Exception {
    filterConfig.setInitParameter(TEST_PARAM, Object.class.getCanonicalName());
    assertEquals(Object.class, 
        configurator.getClassFromParameter(TEST_PARAM, Exception.class));
  }

  public void testGetClassFromParameterNotSetHasDefault() throws Exception {
    assertEquals(Exception.class, 
        configurator.getClassFromParameter(TEST_PARAM, Exception.class));
  }

  public void testGetClassFromRequiredParameterIsSet() throws Exception {
    filterConfig.setInitParameter(TEST_PARAM, Object.class.getCanonicalName());
    assertEquals(Object.class, 
        configurator.getClassFromRequiredParameter(TEST_PARAM));
  }

  public void testGetClassFromRequiredParameterNotSet() throws Exception {
    try {
      assertEquals(Object.class,
          configurator.getClassFromRequiredParameter(TEST_PARAM));
      fail("Expected FilterParameterException");
    }
    catch (FilterParameterException ex) {
      assertTrue(true);
    }
  }

  public void testGetClassFromParameterClassNotFound() throws Exception {
    filterConfig.setInitParameter(TEST_PARAM, TEST_VALUE);
    try {
      configurator.getClassFromParameter(TEST_PARAM);
      fail("Expected FilterParameterException");
    }
    catch (FilterParameterException ex) {
      assertEquals(ClassNotFoundException.class, 
          ex.getRootCause().getClass());
    }
  }
}
