/*
 * ProxyAuthenticatedRedirectServlet.java
 *
 * Created on Dec 26, 2007 
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
package org.soulwing.cas.support;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolConfigurationHolder;
import org.soulwing.cas.client.ProtocolConstants;

/**
 * A servlet that obtains a proxy authentication ticket and uses it as a 
 * query parameter in a redirect to a URL specified as a request parameter.
 *
 * @author Carl Harris
 */
public class ProxyAuthenticatedRedirectServlet extends HttpServlet {

  public static final String URL_PARAM = "url";

  private static final long serialVersionUID = 1837103005783071131L;
  
  private ProtocolConfiguration configuration;
  private ProxyTicketService proxyTicketService;

  /**
   * Gets the <code>configuration</code> property.
   */
  public ProtocolConfiguration getConfiguration() {
    return configuration;
  }

  /**
   * Sets the <code>configuration</code> property.
   */
  public void setConfiguration(ProtocolConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Gets the <code>proxyTicketService</code> property.
   */
  public ProxyTicketService getProxyTicketService() {
    return proxyTicketService;
  }

  /**
   * Sets the <code>proxyTicketService</code> property.
   */
  public void setProxyTicketService(ProxyTicketService proxyTicketService) {
    this.proxyTicketService = proxyTicketService;
  }

  /* (non-Javadoc)
   * @see javax.servlet.GenericServlet#init()
   */
  public void init() throws ServletException {
    try {
      if (getProxyTicketService() == null) {
        if (getConfiguration() == null) {
          setConfiguration(ProtocolConfigurationHolder.getRequiredConfiguration());
        }
        ProxyTicketServiceImpl service = new ProxyTicketServiceImpl();
        service.setConfiguration(getConfiguration());
        setProxyTicketService(service);
      }
    }
    catch (Exception ex) {
      throw new ServletException("initialization error", ex);
    }
  }
  
  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  protected void doGet(HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException {
    String url = request.getParameter(URL_PARAM);
    if (url == null) {
      throw new ServletException(URL_PARAM + " request parameter is required");
    }
    response.sendRedirect(
        constructTargetUrl(url, getProxyTicketService().getTicket(url)));
  }

  /**
   * Constructs the target URL for the redirect, including the CAS proxy
   * authentication ticket as a parameter.
   * @param url base target URL
   * @param ticket proxy authentication ticket
   * @return target URL
   * @throws ServletException if <code>url</code> is malformed
   */
  private String constructTargetUrl(String url, String ticket) 
      throws ServletException {
    try {
      StringBuilder sb = new StringBuilder();
      sb.append(url);
      if (new URL(url).getQuery() == null) {
        sb.append('?');
      }
      else {
        sb.append('&');
      }
      sb.append(ProtocolConstants.TICKET_PARAM);
      sb.append('=');
      sb.append(ticket);
      return sb.toString();
    }
    catch (MalformedURLException ex) {
      throw new ServletException(
          "malformed " + URL_PARAM + " parameter value: " + url, ex);
    }
  }
  
}
