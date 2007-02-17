/*
 * ProxyCallbackFilter.java
 *
 * Created on Feb 6, 2007 
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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.support.ValidationUtils;

/**
 * A filter for processing CAS proxy callback requests.
 *
 * @author Carl Harris
 */
public class ProxyCallbackFilter implements Filter {

  private static final Log log = LogFactory.getLog(ProxyCallbackFilter.class);
  private String filterPath;
  private Map ticketMap = new HashMap();
  
  /**
   * Gets the map of proxy granting tickets.  Exposed to package for use
   * by unit tests.
   */
  Map getTicketMap() {
    return ticketMap;
  }
  
  /*
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig config) throws ServletException {
    filterPath = config.getInitParameter(
        FilterConstants.FILTER_PATH);
    if (filterPath == null) {
      throw new ServletException(FilterConstants.FILTER_PATH
          + " parameter is required");
    }
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
    try {
      doHttpFilter((HttpServletRequest) request,
          (HttpServletResponse) response,
          filterChain);
    }
    catch (ClassCastException ex) {
      log.info("Filter supports HTTP only");
      filterChain.doFilter(request, response);
    }
  }

  private void doHttpFilter(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain) 
      throws IOException, ServletException {
    
    if (hasProxyCallbackUrl(request)) {
      processCallback(request);
    }
    else {
      if (hasValidation(request)) {
        addTicketToSession(request);
      }
      filterChain.doFilter(request, response);
    }
  }

  private boolean hasProxyCallbackUrl(HttpServletRequest request) {
    boolean matches = request.getServletPath().equals(filterPath);
    log.debug("request for " + request.getServletPath()
        + (matches ? " matches " : " does not match ") + filterPath);
    return matches;
  }

  private void processCallback(HttpServletRequest request) {
    String pgtId = request.getParameter(
        ProtocolConstants.PROXY_TICKET_PARAM);
    String pgtIou = request.getParameter(
        ProtocolConstants.PROXY_TICKET_IOU_PARAM);
    if (pgtId == null || pgtIou == null) {
      log.warn("Proxy callback parameters incomplete");
      return;
    }
    log.debug("Callback for IOU " + pgtIou + " with PGT " + pgtId);
    ticketMap.put(pgtIou, pgtId);
  }

  private boolean hasValidation(HttpServletRequest request) {
    return ValidationUtils.getServiceValidationResponse(request) != null;
  }
  
  private void addTicketToSession(HttpServletRequest request) {
    ServiceValidationResponse validation = 
        ValidationUtils.getServiceValidationResponse(request);
    String proxyGrantingTicket = (String) 
        ticketMap.get(validation.getProxyGrantingTicketIou());
    if (proxyGrantingTicket != null) {
      ticketMap.remove(validation.getProxyGrantingTicketIou());
      log.debug("PGT " + proxyGrantingTicket + " added to session");
      request.getSession().setAttribute(
          FilterConstants.PROXY_GRANTING_TICKET_ATTRIBUTE, 
          proxyGrantingTicket);
    } 
  }
  
}
