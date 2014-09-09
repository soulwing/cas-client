/*
 * SimpleUrlGenerator.java
 *
 * Created on Feb 10, 2007
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
package org.soulwing.cas.client;

/**
 * A simple generator for the URL strings used in the CAS protocol.  This
 * URL generator can be provided with a <code>ProtocolConfigurationImpl</code> 
 * that will be used in generating URLs for CAS operations.  Alternatively,
 * the configuration parameters can be passed in as constructor arguments. 
 *
 * @author Carl Harris
 * @see ProtocolConfigurationImpl
 */
public class SimpleUrlGenerator implements UrlGenerator {

  private final ProtocolConfiguration config;
  
  /**
   * @param config <code>ProtocolConfiguration</code> that will provide
   *    the configuration for this generator
   */
  public SimpleUrlGenerator(ProtocolConfiguration config) {
    if (config.getServerUrl() == null || config.getServiceUrl() == null) {
      throw new IllegalArgumentException(
          "configuration must specify serverUrl and serviceUrl");
    }
    this.config = config;
  }
  
  /**
   * Gets the configuration of this SimpleUrlGenerator.
   * @return <code>ProtocolConfiguration</code> containing configuration
   *    properties for this instance.
   */
  public ProtocolConfiguration getConfiguration() {
    return config;
  }
  
  /**
   * Gets the CAS login URL for the configured server URL and service URL.
   * Includes the gateway and/or renew flags if set.
   * @return <code>String</code> CAS login URL.
   */
  public String getLoginUrl() {
    StringBuilder sb = new StringBuilder(100);
    appendLoginUrl(sb);
    appendService(sb);
    appendGateway(sb);
    appendRenew(sb);
    return sb.toString();
  }

  /**
   * Gets the CAS login URL for the configured server URL and service URL.
   * Includes the gateway and/or renew flags if set.
   * @return <code>String</code> CAS login URL.
   */
  public String getLoginUrl(String serviceUri, String serviceQuery) {
    StringBuilder sb = new StringBuilder(100);
    appendLoginUrl(sb);
    appendService(sb, serviceUri, serviceQuery);
    appendGateway(sb);
    appendRenew(sb);
    return sb.toString();
  }
  

  /**
   * Gets the CAS logout URL.  
   * @return <code>String</code> CAS logout URL.
   */
  public String getLogoutUrl() {
    return getLogoutUrl(null);
  }
  
  /**
   * Gets the CAS logout URL with the optional <code>url</code> parameter.
   * @param url a URL for post CAS logout redirection or <code>null</code>  
   * @return <code>String</code> CAS logout URL.  Includes the 
   *    <code>url</code> query parameter if <code>url</code> is
   *    non-null and non-empty.
   */
  public String getLogoutUrl(String url) {
    StringBuilder sb = new StringBuilder(100);
    appendLogoutUrl(sb);
    appendUrl(sb, url);
    return sb.toString();
  }

  /**
   * Gets the CAS proxy URL.
   * @param proxyGrantingTicket proxy granting ticket obtained as a result
   *    of PGT callback after serviceValidate or proxyValidate.
   * @param targetService target service URL
   * @return <code>String</code> CAS proxy URL
   * @throws IllegalArgumentException if either <code>proxyGrantingTicket</code>
   *    or <code>targetService</code> is <code>null</code> or empty.
   */
  public String getProxyUrl(String proxyGrantingTicket, String targetService) {
    requiredParameter("proxyGrantingTicket", proxyGrantingTicket);
    requiredParameter("targetService", targetService);    
    StringBuilder sb = new StringBuilder(100);
    appendProxyUrl(sb);
    appendProxyGrantingTicket(sb, proxyGrantingTicket);
    appendTargetService(sb,targetService);
    return sb.toString();
  }

  /**
   * Gets the CAS proxyValidate URL.
   * @param ticket ticket to validate
   * @return <code>String</code> CAS proxyValidate URL
   * @throws IllegalArgumentException if <code>ticket</code> is
   *    <code>null</code> or empty.
   */
  public String getProxyValidateUrl(String ticket) {
    requiredParameter("ticket", ticket);
    return getValidateUrl(ProtocolConstants.PROXY_VALIDATE_PATH, ticket); 
  }
  
  /**
   * Gets the CAS serviceValidate URL.
   * @param ticket ticket to validate
   * @return <code>String</code> CAS serviceValidate URL
   * @throws IllegalArgumentException if <code>ticket</code> is
   *    <code>null</code> or empty.
   */
  public String getServiceValidateUrl(String ticket) {
    requiredParameter("ticket", ticket);
    return getValidateUrl(ProtocolConstants.SERVICE_VALIDATE_PATH, ticket); 
  }
  
  private void requiredParameter(String name, String value) {  
    if (value == null || value.length() == 0) {
      throw new IllegalArgumentException(name + " is required");
    }
  }
  
  private String getValidateUrl(String validatePath, String ticket) {
    StringBuilder sb = new StringBuilder(100);
    appendValidateUrl(sb, validatePath);
    appendService(sb);
    appendTicket(sb, ticket);
    appendProxyCallbackUrl(sb, config.getProxyCallbackUrl());
    appendGateway(sb);
    appendRenew(sb);
    return sb.toString();
  }

  private void appendLoginUrl(StringBuilder url) {
    url.append(config.getServerUrl());
    url.append(ProtocolConstants.LOGIN_PATH);
  }

  private void appendLogoutUrl(StringBuilder url) {
    url.append(config.getServerUrl());
    url.append(ProtocolConstants.LOGOUT_PATH);
  }

  private void appendProxyUrl(StringBuilder url) {
    url.append(config.getServerUrl());
    url.append(ProtocolConstants.PROXY_PATH);
  }
  
  private void appendValidateUrl(StringBuilder sb, String validatePath) {
    sb.append(config.getServerUrl());
    sb.append(validatePath);
  }

  private void appendUrl(StringBuilder sb, String url) {
    if (url != null && url.length() > 0) {
      sb.append('?');
      appendParameter(sb, ProtocolConstants.URL_PARAM, 
          URLEncoder.encode(url));
    }
  }
  
  private void appendProxyGrantingTicket(StringBuilder sb,
      String proxyGrantingTicket) {
    sb.append('?');
    appendParameter(sb, ProtocolConstants.PGT_PARAM, 
	URLEncoder.encode(proxyGrantingTicket));
  }

  private void appendTargetService(StringBuilder sb, String targetService) {
    sb.append('&');
    appendParameter(sb, ProtocolConstants.TARGET_SERVICE_PARAM, 
        URLEncoder.encode(targetService));
  }

  private void appendService(StringBuilder sb) {
    appendService(sb, null, null);
  }
  
  private void appendService(StringBuilder sb, String serviceUri, 
      String serviceQuery) {
    sb.append('?');
    appendParameter(sb, ProtocolConstants.SERVICE_PARAM, 
        URLEncoder.encode(createServiceUrl(serviceUri, serviceQuery)));
  }

  private String createServiceUrl(String serviceUri, String serviceQuery) {
    StringBuilder sb = new StringBuilder();
    sb.append(config.getServiceUrl());
    if (serviceUri != null && !serviceUri.isEmpty()) {
      sb.append(serviceUri);      
    }
    if (serviceQuery != null && !serviceQuery.isEmpty()) {
      sb.append("?").append(serviceQuery);
    }
    return sb.toString();
  }
  private void appendTicket(StringBuilder sb, String ticket) {
    sb.append('&');
    appendParameter(sb, ProtocolConstants.TICKET_PARAM, 
        URLEncoder.encode(ticket));
  }

  private void appendProxyCallbackUrl(StringBuilder sb, 
      String proxyCallbackUrl) {
    if (proxyCallbackUrl != null  && proxyCallbackUrl.length() > 0) {
      sb.append('&');
      appendParameter(sb, ProtocolConstants.PGT_URL_PARAM, 
          URLEncoder.encode(proxyCallbackUrl));
    }
  }

  private void appendRenew(StringBuilder sb) {
    if (config.getRenewFlag() == true) {
      sb.append('&');
      appendParameter(sb, ProtocolConstants.RENEW_PARAM, 
          URLEncoder.encode(Boolean.toString(true)));
    }
  }

  private void appendGateway(StringBuilder sb) {
    if (config.getGatewayFlag() == true) {
      sb.append('&');
      appendParameter(sb, ProtocolConstants.GATEWAY_PARAM, 
          Boolean.toString(true));
    }
  }

  private void appendParameter(StringBuilder sb, String name, String value) {
    sb.append(name);
    sb.append('=');
    sb.append(value);
  }
  
  /**
   * A URL encoder that delegates to java.net.URLEncoder but specifies
   * an encoding character set.  This avoids deprecation warnings from using 
   * java.net.URLEncoder.encode(String).
   */
  static class URLEncoder {
    public static String encode(String url) {
      try {
        return java.net.URLEncoder.encode(url, "UTF-8");
      }
      catch (java.io.UnsupportedEncodingException ex) {
        throw new UnsupportedOperationException(ex);
      }
    }
  }

}
