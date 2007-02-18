/*
 * ValidationConfiguration.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolSource;
import org.soulwing.cas.client.UrlProtocolSource;


/**
 * A configuration bean for AbstractValidationFilter.  Property values for this
 * bean can be configured using the provided public setters or via a
 * FilterConfig passed to a constructor for this class. 
 *
 * @author Carl Harris
 */
public class ValidationConfiguration {

  private static final Log log = 
      LogFactory.getLog(ValidationConfiguration.class);

  public static final boolean GATEWAY_DEFAULT = false;
  public static final boolean RENEW_DEFAULT = false;
  public static final Class SOURCE_CLASS_DEFAULT = UrlProtocolSource.class; 
  
  private String filterPath;
  private String authFailedUrl;
  private String trustedProxies;
  private ProtocolSource protocolSource;
  private ProtocolConfiguration protocolConfiguration;
  
  public ValidationConfiguration() {
  }

  public ValidationConfiguration(FilterConfig filterConfig) 
      throws FilterParameterException {
    FilterConfigurator fc = new FilterConfigurator(filterConfig);
    setFilterPath(fc.getParameter(FilterConstants.FILTER_PATH));
    setAuthFailedUrl(fc.getParameter(FilterConstants.AUTH_FAILED_URL));
    setTrustedProxies(fc.getParameter(FilterConstants.TRUSTED_PROXIES));
    ProtocolConfiguration config = new ProtocolConfiguration();
    config.setServerUrl(
        fc.getRequiredParameter(FilterConstants.SERVER_URL));
    config.setServiceUrl(
        fc.getParameter(FilterConstants.SERVICE_URL));
    config.setGatewayFlag(
        Boolean.valueOf(fc.getParameter(FilterConstants.GATEWAY, 
            Boolean.toString(GATEWAY_DEFAULT))).booleanValue());
    config.setRenewFlag(
        Boolean.valueOf(fc.getParameter(FilterConstants.RENEW, 
            Boolean.toString(RENEW_DEFAULT))).booleanValue()); 
    config.setProxyCallbackUrl(
        fc.getParameter(FilterConstants.PROXY_CALLBACK_URL));
    setProtocolConfiguration(config);
    setProtocolSource(fc.getClassFromParameter(FilterConstants.SOURCE_CLASS_NAME,
        SOURCE_CLASS_DEFAULT));
  }
  
  /* 
   * @see org.soulwing.cas.filter.FilterConfigurator#log()
   */
  protected Log log() {
    return log;
  }
  
  public String getAuthFailedUrl() {
    return authFailedUrl;
  }

  public void setAuthFailedUrl(String authFailedUrl) {
    this.authFailedUrl = authFailedUrl;
  }
  
  public String getFilterPath() {
    return filterPath;
  }  
  
  public void setFilterPath(String filterPath) {
    this.filterPath = filterPath;
  }

  public ProtocolConfiguration getProtocolConfiguration() {
    return protocolConfiguration;
  }

  public void setProtocolConfiguration(
      ProtocolConfiguration protocolConfiguration) {
    this.protocolConfiguration = protocolConfiguration;
    UrlGeneratorFactory.setProtocolConfiguration(protocolConfiguration);
  }

  public ProtocolSource getProtocolSource() {
    return protocolSource;
  }

  public void setProtocolSource(ProtocolSource protocolSource) {
    this.protocolSource = protocolSource;
  }

  public void setProtocolSource(Class sourceClass) 
      throws FilterParameterException {
    try {
      setProtocolSource((ProtocolSource) sourceClass.newInstance());
    }
    catch (ClassCastException ex) {
      throw new FilterParameterException(ex);
    }
    catch (IllegalAccessException ex) {
      throw new FilterParameterException(ex);
    }
    catch (InstantiationException ex) {
      throw new FilterParameterException(ex);
    }
  }

  public String getTrustedProxies() {
    return trustedProxies;
  }

  public void setTrustedProxies(String trustedProxies) {
    this.trustedProxies = trustedProxies;
  }

  public String getProxyCallbackUrl() {
    return protocolConfiguration.getProxyCallbackUrl();
  }
  
  public boolean getGatewayFlag() {
    return protocolConfiguration.getGatewayFlag();
  }
  
  public boolean getRenewFlag() {
    return protocolConfiguration.getRenewFlag();
  }

  public String getServerUrl() {
    return protocolConfiguration.getServerUrl();
  }

  public String getServiceUrl() {
    return protocolConfiguration.getServiceUrl();
  }

}
