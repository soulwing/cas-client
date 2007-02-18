/*
 * ProtocolConfiguration.java
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
 * 
 */
package org.soulwing.cas.client;


/**
 * A configuration holder for the CAS protocol.  Used by a UrlGenerator
 * to produce URLs for CAS protocol interactions, with appropriate query
 * parameters.
 *
 * @author Carl Harris
 */
public class ProtocolConfiguration {

  private String serverUrl;
  private String serviceUrl;
  private String proxyCallbackUrl;
  private boolean gatewayFlag;
  private boolean renewFlag;

  public ProtocolConfiguration() {
  }

  public ProtocolConfiguration(String serverUrl) {
    this(serverUrl, null);
  }
  
  public ProtocolConfiguration(String serverUrl, String serviceUrl) {
    this(serverUrl, serviceUrl, null);
  }
  
  public ProtocolConfiguration(String serverUrl, String serviceUrl,
      String proxyCallbackUrl) {
    this(serverUrl, serviceUrl, proxyCallbackUrl, false, false);
  }

  public ProtocolConfiguration(String serverUrl, String serviceUrl,
      String proxyCallbackUrl, boolean gatewayFlag, boolean renewFlag) {
    setServerUrl(serverUrl);
    setServiceUrl(serviceUrl);
    setProxyCallbackUrl(proxyCallbackUrl);
    setGatewayFlag(gatewayFlag);
    setRenewFlag(renewFlag);
  }

  /**
   * Gets the base URL for the CAS server.
   * @return base URL for the CAS server used by this 
   *    ProtocolConfiguration.
   */
  public String getServerUrl() {
    return serverUrl;
  }
  
  /**
   * Sets the base URL for the CAS server.  The UrlGenerator will append
   * CAS servlet paths such as <code>/login</code> or <code>serviceValidate</code>
   * onto this base URL.  Typically the value of this URL is of the form:
   * 
   * <code>https://</code><em>hostname</em><code>:</code><em>port</em>/cas</code>
   * 
   * @param serverUrl base URL to set
   */
  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }
  
  /**
   * Gets the configured URL for the application. 
   * @return URL for the application
   */
  public String getServiceUrl() {
    return serviceUrl;
  }
  
  /**
   * Sets the URL for the application.  The UrlGenerator will pass this URL 
   * as the value for the <code>service</code> query parameter
   * in CAS protocol URLs.
   * 
   * @param serviceUrl application URL to configure
   */
  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  /**
   * Gets the value of the URL that will be used for proxy callback.
   * @return URL for proxy callback.
   */
  public String getProxyCallbackUrl() {
    return proxyCallbackUrl;
  }
  
  /**
   * Sets the URL for proxy callback. If configured, the UrlGenerator will pass
   * this URL as the value for the <code>pgtUrl</code> query parameter. The
   * CAS server will issue a request for this URL to pass a proxy granting
   * ticket for the authentic user back to the application after a successful
   * CAS validation. If not configured, the UrlGenerator will not include the
   * <code>pgtUrl</code> query parameter in CAS validation requests.
   * 
   * @param proxyCallbackUrl
   */
  public void setProxyCallbackUrl(String proxyCallbackUrl) {
    this.proxyCallbackUrl = proxyCallbackUrl;
  }
  
  /**
   * Gets the CAS gateway flag setting.
   * @return gateway flag setting for this ProtocolConfiguration.
   */
  public boolean getGatewayFlag() {
    return gatewayFlag;
  }
  
  /**
   * Sets the CAS gateway flag.  If this flag is set to <code>true</code>
   * CAS protocol URLs will include <code>gateway=true</code>
   * as a query parameter.
   * @param gatewayFlag <code>boolean</code> new value for the flag.
   */
  public void setGatewayFlag(boolean gatewayFlag) {
    this.gatewayFlag = gatewayFlag;
  }
  
  /**
   * Gets the CAS renew flag setting.
   * @return renew flag setting for this ProtocolConfiguration
   */
  public boolean getRenewFlag() {
    return renewFlag;
  }
  
  /**
   * Sets the CAS renew flag.  If this flag is set to <code>true</code>
   * CAS protocol URLs will include <code>renew=true</code>
   * as a query parameter.
   * @param renewFlag <code>boolean</code> new value for the flag.
   */
  public void setRenewFlag(boolean renewFlag) {
    this.renewFlag = renewFlag;
  }
  
}
