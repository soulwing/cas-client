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
  
  public void setInitParameter(String paramName, String paramValue) {
    initParams.put(paramName, paramValue);
  }
  
  public String getInitParameter(String paramName) {
    return (String) initParams.get(paramName);
  }

  public Enumeration getInitParameterNames() {
    return new Vector(initParams.keySet()).elements();
  }

}
