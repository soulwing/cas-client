/*
 * Configurator.java
 *
 * Created on Feb 7, 2007
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
package org.soulwing.cas.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A configurator for a filter.
 *
 * @author Carl Harris
 */
public class Configurator {

  private static final Log log = LogFactory.getLog(Configurator.class);
  private final ParameterProvider parameterProvider;
  
  /**
   * Constructs a Configurator for a Filter.
   * @param filterConfig
   */
  public Configurator(FilterConfig filterConfig) {
    this.parameterProvider = 
        new FilterConfigParameterProvider(filterConfig);
  }

  /**
   * Constructs a Configurator for a ServletContextListener.
   * @param servletContext
   */
  public Configurator(ServletContext servletContext) {
    this.parameterProvider = 
        new ServletContextParameterProvider(servletContext);
  }

  /**
   * Gets the log instance.  Subclasses may override to use a different
   * logger.
   * @return log instance.
   */
  protected Log log() {
    return log;
  }
  
  /**
   * Gets the value of a filter parameter.
   * @param parameterName name of the parameter to get
   * @return <code>String</code> value of the specified parameter 
   *    or <code>null</code> if the specified parameter has not been
   *    set in the backing FilterConfig instance for this configurator
   */
  public String getParameter(String parameterName) {
    String value = parameterProvider.getInitParameter(parameterName);
    if (value != null) {
      log().info(parameterName + " parameter set to '" + value + "'");
    }
    else {
      log().info(parameterName + " parameter not specified");
    }
    return value;
  }
  
  /**
   * Gets the value of a filter parameter or its default value if no
   * value was explicitly specified.
   * @param parameterName name of the parameter
   * @param defaultValue value to return if the parameter has not been set
   * @return <code>String</code> value of the specified parameter
   *    or <code>defaultValue</code> if the specified parameter has not
   *    been set in the backing FilterConfig instance for this configurator
   */
  public String getParameter(String parameterName, String defaultValue) {
    String value = getParameter(parameterName);
    if (value == null) {
      log().info(parameterName + " parameter set to default '" 
          + defaultValue + "'");
      return defaultValue;
    }
    return value;
  }
  
  /**
   * Gets the value of a required filter parameter.
   * @param parameterName name of the parameter
   * @return <code>String</code> value of the specified parameter
   * @throws FilterParameterException if the specified parameter has not been
   *    set in the backing FilterConfig instance for this configurator
   */
  public String getRequiredParameter(String parameterName) 
      throws FilterParameterException {
    String value = parameterProvider.getInitParameter(parameterName);
    if (value != null) {
      log().info(parameterName + " parameter set to '" + value + "'");
      return value;
    }
    else {
      throw new FilterParameterException(parameterName 
          + " parameter is required");
    }
  }

  /**
   * Gets a class from a parameter that specifies a fully-qualified class name.  
   * @param parameterName name of the parameter that specifies a class name.
   * @return <code>Class</code> instance or <code>null</code> if the
   *    specified parameter has not been set in the backing FilterConfig
   *    instance for this configurator.
   * @throws FilterParameterException with root cause ClassNotFoundException 
   *    if the specified class is not found by the class loader.
   */
  public Class getClassFromParameter(String parameterName)
      throws FilterParameterException {
    String className = getParameter(parameterName);
    if (className != null) {
      return getClassByName(className);
    }
    else {
      return null;
    }
  }
  
  /**
   * Gets a class from a parameter that specifies a fully-qualified class name.  
   * @param parameterName name of the parameter that specifies a class name.
   * @return <code>Class</code> instance or <code>defaultClass</code> if the
   *    specified parameter has not been set in the backing FilterConfig
   *    instance for this configurator.
   * @throws FilterParameterException with root cause ClassNotFoundException
   *    if the specified class is not found by the class loader.
   */
  public Class getClassFromParameter(String parameterName, Class defaultClass)
      throws FilterParameterException {
    String className = getParameter(parameterName, 
        defaultClass.getCanonicalName());
    return getClassByName(className);
  }

  /**
   * Gets a class from a parameter that specifies a fully-qualified class name.  
   * @param parameterName name of the parameter that specifies a class name.
   * @return <code>Class</code> instance
   * @throws FilterParameterException if the specified parameter has not been
   *    set or if the specified class is not found by the class loader.
   */
  public Class getClassFromRequiredParameter(String parameterName) 
      throws FilterParameterException {
    String className = getRequiredParameter(parameterName);
    return getClassByName(className);
  }
  
  /**
   * Gets a class from the class loager
   * @param className fully-qualified class name to load.
   * @return <code>Class</code> instance
   * @throws FilterParameterException with root cause ClassNotFoundException 
   *    if the specified class was not found by the class loader.
   */
  private Class getClassByName(String className) 
      throws FilterParameterException {
    try {
      return Class.forName(className);
    }
    catch (ClassNotFoundException ex) {
      throw new FilterParameterException(ex);
    }
  }

  /**
   * A provider of configuration parameters.
   */
  interface ParameterProvider {
    String getInitParameter(String parameterName);
  }

  /**
   * A ParameterProvider that delegates to a FilterConfig.
   */
  static class FilterConfigParameterProvider implements ParameterProvider {
  
    private final FilterConfig config;
    
    public FilterConfigParameterProvider(FilterConfig config) {
      this.config = config;
    }
    
    public String getInitParameter(String parameterName) {
      return config.getInitParameter(parameterName);
    }
    
  }

  /**
   * A ParameterProvider that delegates to a ServletContext.
   */
  static class ServletContextParameterProvider implements ParameterProvider {
    
    private final ServletContext context;
    
    public ServletContextParameterProvider(ServletContext context) {
      this.context = context;
    }
    
    public String getInitParameter(String parameterName) {
      return context.getInitParameter(parameterName);
    }
    
  }

}
