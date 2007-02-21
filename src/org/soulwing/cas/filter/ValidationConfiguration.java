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
  private ProtocolConfiguration protocolConfiguration =
      new ProtocolConfiguration();
  
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
    setProtocolSourceClass(fc.getClassFromParameter(FilterConstants.SOURCE_CLASS_NAME,
        SOURCE_CLASS_DEFAULT));
  }
  
  /**
   * Initializes this ValidationConfiguration instance.  
   * This method MUST be invoked after all configurable properties are
   * set, and before the configuration is used to perform CAS validation.
   * Typically, this method will be invoked by a dependency injection
   * framework such as Spring.
   * @throws Exception 
   */
  public void init() throws Exception {
    if (getProtocolConfiguration() == null) {
      throw new FilterParameterException(
          "protocolConfiguration property is required");
    }
    if (getProtocolSource() == null) {
      setProtocolSourceClass(SOURCE_CLASS_DEFAULT);
    }
    UrlGeneratorFactory.setProtocolConfiguration(protocolConfiguration);
  }
  
  
  /* 
   * @see org.soulwing.cas.filter.FilterConfigurator#log()
   */
  protected Log log() {
    return log;
  }
  
  /**
   * Gets the URL to which a user's browser will be redirected if
   * validation of the user's CAS authentication ticket fails.
   * @return <code>String</code> URL.
   */
  public String getAuthFailedUrl() {
    return authFailedUrl;
  }

  /**
   * Sets the URL to which a user's browser will be redirected if
   * validation of the user's CAS authentication ticket fails.
   * @param authFailedUrl <code>String</code> URL
   */
  public void setAuthFailedUrl(String authFailedUrl) {
    this.authFailedUrl = authFailedUrl;
  }
  
  /**
   * Gets the servlet path that this filter will consider to be a
   * request for authentication ticket validation.
   * @return <code>String</code> servlet path
   */
  public String getFilterPath() {
    return filterPath;
  }  
  
  /**
   * Sets a servlet path that this filter will consider to be a 
   * request for authentication ticket validation.  If this parameter
   * is set, the validation filter will only perform ticket validation --
   * it will not send redirects to the CAS login URL.  This parameter would
   * typically be used only by a web service that is expecting to get
   * proxy authentication tickets from a front-end web application.
   * 
   * @param filterPath <code>String</code> servlet path
   */
  public void setFilterPath(String filterPath) {
    this.filterPath = filterPath;
  }

  /**
   * Gets the CAS protocol configuration object.
   * @return protocol configuration
   */
  public ProtocolConfiguration getProtocolConfiguration() {
    return protocolConfiguration;
  }

  /**
   * Sets the CAS protocol configuration object to use in the validation
   * filter that will use this ValidationConfiguration.
   * @param protocolConfiguration CAS protocol configuration
   */
  public void setProtocolConfiguration(
      ProtocolConfiguration protocolConfiguration) {
    if (protocolConfiguration.getServerUrl() == null) {
      throw new IllegalArgumentException(FilterConstants.SERVER_URL
          + " property is required");
    }
    this.protocolConfiguration = protocolConfiguration;
  }

  /**
   * Gets the CAS protocol source object.
   * @return protocol source
   */
  public ProtocolSource getProtocolSource() {
    return protocolSource;
  }

  /**
   * Sets the CAS protocol source object to use in the validation filter
   * that will use this ValidationConfiguration.
   * @param protocolSource protocol source
   */
  public void setProtocolSource(ProtocolSource protocolSource) {
    this.protocolSource = protocolSource;
  }

  /**
   * Sets the class to use as a CAS protocol source in the validation
   * filter that will use this ValidationConfiguration.
   * @param sourceClass class that implements the ProtocolSource interface
   * @throws FilterParameterException
   */
  public void setProtocolSourceClass(Class sourceClass) 
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

  /**
   * Gets the URL that the validation filter will pass to the CAS
   * server as the value for the <code>pgtUrl</code> query parameter. 
   * @return <code>String</code> URL
   */
  public String getProxyCallbackUrl() {
    return getProtocolConfiguration().getProxyCallbackUrl();
  }
  
  /**
   * Sets the URL that the validation filter will pass to the CAS
   * server as the value for the <code>pgtUrl</code> query parameter.
   * If no proxy callback URL is configured, this parameter will not 
   * be included in CAS validation requests.  If this parameter is
   * configured and the ProxyCallbackFilter is being used to process
   * CAS callbacks, the servlet path portion of this URL should correspond
   * to the value configured for ProxyCallbackFilter.  
   * @param proxyCallbackUrl <code>String</code> URL
   */
  public void setProxyCallbackUrl(String proxyCallbackUrl) {
    getProtocolConfiguration().setProxyCallbackUrl(proxyCallbackUrl);    
  }
  
  /**
   * Gets the value of the CAS gateway flag.
   * @return flag value
   */
  public boolean getGatewayFlag() {
    return getProtocolConfiguration().getGatewayFlag();
  }
  
  /**
   * Sets the CAS gateway flag.  If this property is set to <code>true</code>,
   * CAS validation requests will include <code>gateway=true</code> as a
   * query parameter.
   * @param gatewayFlag value to set
   */
  public void setGatewayFlag(boolean gatewayFlag) {
    getProtocolConfiguration().setGatewayFlag(gatewayFlag);
  }
  
  /**
   * Gets the CAS renew flag.
   * @return flag value
   */
  public boolean getRenewFlag() {
    return getProtocolConfiguration().getRenewFlag();
  }

  /**
   * Sets the CAS renew flag.  If this property is set to <code>true</code>,
   * CAS validation requests will include <code>renew=true</code> as a
   * query parameter.
   * @param renewFlag value to set
   */
  public void setRenewFlag(boolean renewFlag) {
    getProtocolConfiguration().setRenewFlag(renewFlag);
  }
  
  /**
   * Gets the configured CAS server URL.
   * @return <code>String</code> URL
   */
  public String getServerUrl() {
    return getProtocolConfiguration().getServerUrl();
  }

  /**
   * Sets the CAS server URL.  This URL should be set to the base URL
   * for the CAS server that provides authentication services that will
   * be used by the validation filter.  Servlet paths for CAS protocol
   * operations (e.g. <code>/serviceValidate</code>) will be appended 
   * to this URL.
   * @param serverUrl <code>String</code> URL
   */
  public void setServerUrl(String serverUrl) {
    getProtocolConfiguration().setServerUrl(serverUrl);
  }
  
  /**
   * Gets the configured CAS service URL.
   * @return <code>String</code> URL
   */
  public String getServiceUrl() {
    return getProtocolConfiguration().getServiceUrl();
  }

  /**
   * Sets the CAS service URL.  Generally, this parameter does not need to be
   * configured, as the validation filter can infer the appropriate service
   * URL for most applications.  If this property is configured, it will
   * be used as the base of the URL that will be passed to the CAS
   * server as the value for the <code>service</code> query parameter.  
   * @param serviceUrl <code>String</code> service URL up to and including
   *    the context path for the application.  It MUST NOT include any 
   *    portion of a servlet path.
   */
  public void setServiceUrl(String serviceUrl) {
    getProtocolConfiguration().setServiceUrl(serviceUrl);
  }

}
