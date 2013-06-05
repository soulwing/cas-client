/*
 * FilterToBeanProxy.java
 *
 * Created on Feb 21, 2007
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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * A Filter that acts as a proxy for a Spring bean.  The idea for this
 * filter is due to the authors of the Acegi Security framework.
 *
 * @author Carl Harris
 */
public class FilterToBeanProxy implements Filter {

  private static final String BEAN_NOT_FOUND = 
      " not found in application context";

  private static final String MUST_IMPLEMENT_FILTER = 
      " must implement Filter";
  
  private static final String INIT_PARAMETER_REQUIRED = 
      " init parameter is required";
  
  private static final String TARGET_BEAN = "targetBean";
  
  private Filter target;
  
  /* 
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    target = getTargetBean(
        WebApplicationContextUtils.getRequiredWebApplicationContext(
            filterConfig.getServletContext()), 
        getTargetBeanName(filterConfig));
  }

  /* 
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
    // not needed
  }

  /* 
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    target.doFilter(request, response, filterChain);
  }

  private Filter getTargetBean(ApplicationContext appContext, 
      String beanName) throws ServletException {
    if (appContext.containsBean(beanName)) {
      Object bean = appContext.getBean(beanName);
      if (bean instanceof Filter) {
        return (Filter) bean;
      }
      else {
        throw new ServletException(bean.getClass().getCanonicalName() 
            + MUST_IMPLEMENT_FILTER);
      }
    }
    else {
      throw new ServletException(beanName + BEAN_NOT_FOUND);
    }
  }
  
  private String getTargetBeanName(FilterConfig filterConfig) 
      throws ServletException {
    String beanName = filterConfig.getInitParameter(TARGET_BEAN);
    if (beanName == null) {
      throw new ServletException(
          TARGET_BEAN + INIT_PARAMETER_REQUIRED);
    }
    return beanName;
  }
  

}
