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
 * A configuration holder for the CAS protocol.
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

  public boolean getGatewayFlag() {
    return gatewayFlag;
  }
  
  public void setGatewayFlag(boolean gatewayFlag) {
    this.gatewayFlag = gatewayFlag;
  }
  
  public String getProxyCallbackUrl() {
    return proxyCallbackUrl;
  }
  
  public void setProxyCallbackUrl(String proxyCallbackUrl) {
    this.proxyCallbackUrl = proxyCallbackUrl;
  }
  
  public boolean getRenewFlag() {
    return renewFlag;
  }
  
  public void setRenewFlag(boolean renewFlag) {
    this.renewFlag = renewFlag;
  }
  
  public String getServerUrl() {
    return serverUrl;
  }
  
  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }
  
  public String getServiceUrl() {
    return serviceUrl;
  }
  
  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

}
