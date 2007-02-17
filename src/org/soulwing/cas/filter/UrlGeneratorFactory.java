/*
 * ContextUrlGeneratorFactory.java
 *
 * Created on Feb 11, 2007
 */
package org.soulwing.cas.filter;

import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.SimpleUrlGenerator;
import org.soulwing.cas.client.UrlGenerator;


/**
 * A factory for constructing ContextUrlGenerator instances.
 *
 * @author Carl Harris
 */
public class UrlGeneratorFactory {

  private static ProtocolConfiguration config;

  public static synchronized ProtocolConfiguration getProtocolConfiguration() {
    return config;
  }
  
  public static synchronized void setProtocolConfiguration(
      ProtocolConfiguration config) {
    UrlGeneratorFactory.config = config;
  }
  
  public static UrlGenerator getUrlGenerator(HttpServletRequest request) {
    return new SimpleUrlGenerator(
        new ContextProtocolConfiguration(request, config));
  }
  
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
