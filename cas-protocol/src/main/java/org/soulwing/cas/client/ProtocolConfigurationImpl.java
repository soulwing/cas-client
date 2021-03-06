/*
 * ProtocolConfigurationImpl.java
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
public class ProtocolConfigurationImpl implements ProtocolConfiguration {

  private String serverUrl;
  private String serviceUrl;
  private String proxyCallbackUrl;
  private boolean gatewayFlag;
  private boolean renewFlag;

  public ProtocolConfigurationImpl() {
  }

  public ProtocolConfigurationImpl(String serverUrl) {
    this(serverUrl, null);
  }
  
  public ProtocolConfigurationImpl(String serverUrl, String serviceUrl) {
    this(serverUrl, serviceUrl, null);
  }
  
  public ProtocolConfigurationImpl(String serverUrl, String serviceUrl,
      String proxyCallbackUrl) {
    this(serverUrl, serviceUrl, proxyCallbackUrl, false, false);
  }

  public ProtocolConfigurationImpl(String serverUrl, String serviceUrl,
      String proxyCallbackUrl, boolean gatewayFlag, boolean renewFlag) {
    setServerUrl(serverUrl);
    setServiceUrl(serviceUrl);
    setProxyCallbackUrl(proxyCallbackUrl);
    setGatewayFlag(gatewayFlag);
    setRenewFlag(renewFlag);
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ProtocolConfiguration#getServerUrl()
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
    this.serverUrl = stripTrailingSlash(serverUrl);
  }
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ProtocolConfiguration#getServiceUrl()
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
    this.serviceUrl = stripTrailingSlash(serviceUrl);
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ProtocolConfiguration#getProxyCallbackUrl()
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
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ProtocolConfiguration#getGatewayFlag()
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
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ProtocolConfiguration#getRenewFlag()
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

  /**
   * Strips a trailing slash from a URL.
   * @param url the subject URL
   * @return <code>url</code> with a trailing slash removed
   */
  private String stripTrailingSlash(String url) {
    url = url.trim();
    if (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1);
    }
    return url;
  }

}
