/*
 * UrlGeneratorFactory.java
 *
 * Created on Feb 11, 2007
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

import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.SimpleUrlGenerator;
import org.soulwing.cas.client.UrlGenerator;


/**
 * A factory for constructing UrlGenerator instances.
 *
 * @author Carl Harris
 */
class UrlGeneratorFactory {

  private static ProtocolConfiguration config;

  /**
   * Gets the ProtocolConfiguration configured for this factory.
   * @return configuration for this factory.
   */
  public static synchronized ProtocolConfiguration getProtocolConfiguration() {
    return config;
  }
  
  /**
   * Sets the ProtocolConfiguration to use in this factory.
   * @param config configuration to use for this factory.
   */
  public static synchronized void setProtocolConfiguration(
      ProtocolConfiguration config) {
    UrlGeneratorFactory.config = config;
  }
  
  /**
   * Gets a UrlGenerator from this factory, configured as this factory
   * is configured. 
   * @param request request for which a UrlGenerator is needed
   * @return <code>UrlGenerator</code> instance
   */
  public static UrlGenerator getUrlGenerator(HttpServletRequest request) {
    return new SimpleUrlGenerator(
        new ContextProtocolConfiguration(request, config));
  }

  /**
   * An extension of ProtocolConfiguration that allows the 
   * value of the <code>serviceUrl</code> property to be derived from
   * an HttpServletRequest.
   *
   * @author Carl Harris
   */
  static class ContextProtocolConfiguration 
      extends ProtocolConfiguration {
    private final HttpServletRequest request;
    
    public ContextProtocolConfiguration(HttpServletRequest request,
        ProtocolConfiguration config) {
      this.request = request;
      setServerUrl(config.getServerUrl());
      setServiceUrl(config.getServiceUrl());
      setProxyCallbackUrl(config.getProxyCallbackUrl());
      setGatewayFlag(config.getGatewayFlag());
      setRenewFlag(config.getRenewFlag());
    }

    public String getServiceUrl() {
      StringBuffer sb = new StringBuffer();
      if (super.getServiceUrl() == null) { 
        sb = request.getRequestURL();
      }
      else {
        sb = new StringBuffer(100);
        sb.append(super.getServiceUrl());
        sb.append(request.getServletPath());
      }
      if (request.getQueryString() != null 
          && request.getQueryString().length() > 0) {
        sb.append('?');
        sb.append(request.getQueryString());
        removeTicketFromQueryString(sb);
      }
      return sb.toString();
    }

    private void removeTicketFromQueryString(StringBuffer sb) {
      int i = sb.indexOf("&" + ProtocolConstants.TICKET_PARAM + "=");
      if (i == -1) {
        i = sb.indexOf("?" + ProtocolConstants.TICKET_PARAM + "=");
      }
      if (i != -1) {
        int j = sb.indexOf("&", i + 1);
        if (j == -1) {
          sb.delete(i, sb.length());
        }
        else {
          sb.delete(i + 1, j + 1);
        }
      }
    }

  }
  
}
