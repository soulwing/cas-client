/*
 * MockFilterConfig.java
 *
 * Created on Sep 8, 2006
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
package org.soulwing.servlet;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;


public class MockFilterConfig implements FilterConfig {

  private String filterName;
  private Map initParams = new LinkedHashMap();
  private ServletContext servletContext = new MockServletContext();

  public String getFilterName() {
    return this.filterName;
  }

  public void setFilterName(String filterName) {
    this.filterName = filterName;
  }
  
  public ServletContext getServletContext() {
    return this.servletContext;
  }

  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }
  
  @SuppressWarnings("unchecked")
  public void setInitParameter(String paramName, String paramValue) {
    initParams.put(paramName, paramValue);
  }
  
  public String getInitParameter(String paramName) {
    return (String) initParams.get(paramName);
  }

  @SuppressWarnings("unchecked")
  public Enumeration getInitParameterNames() {
    return new Vector(initParams.keySet()).elements();
  }

}
