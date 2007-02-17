/*
 * ProtocolConfiguration.java
 *
 * Created on Feb 11, 2007
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
