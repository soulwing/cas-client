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
 * A simple generator for the URL strings used in the CAS protocol.
 *
 * @author Carl Harris
 */
public class SimpleUrlGenerator implements UrlGenerator {

  private final ProtocolConfiguration config;
  
  
  public SimpleUrlGenerator(ProtocolConfiguration config) {
    this.config = config;
  }
  
  public SimpleUrlGenerator(String serverUrl, String serviceUrl) {
    this(serverUrl, serviceUrl, null, false, false);
  }
  
  public SimpleUrlGenerator(String serverUrl, String serviceUrl,
      String proxyCallbackUrl) {
    this(serverUrl, serviceUrl, proxyCallbackUrl, false, false);
  }
  
  public SimpleUrlGenerator(String serverUrl, String serviceUrl,
      String proxyCallbackUrl, boolean gatewayFlag, boolean renewFlag) {
    config = new ProtocolConfiguration(serverUrl, serviceUrl,
        proxyCallbackUrl, gatewayFlag, renewFlag);
  }

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
    appendLoginUri(sb);
    appendService(sb);
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
   *    non-empty.
   */
  public String getLogoutUrl(String url) {
    StringBuilder sb = new StringBuilder(100);
    appendLogoutUri(sb);
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
    appendProxyUri(sb);
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
    return getValidateUrl(ProtocolConstants.PROXY_VALIDATE_URI, ticket); 
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
    return getValidateUrl(ProtocolConstants.SERVICE_VALIDATE_URI, ticket); 
  }
  
  private void requiredParameter(String name, String value) {  
    if (value == null || value.length() == 0) {
      throw new IllegalArgumentException(name + " is required");
    }
  }
  
  private String getValidateUrl(String validate, String ticket) {
    StringBuilder sb = new StringBuilder(100);
    appendValidateUri(sb, validate);
    appendService(sb);
    appendTicket(sb, ticket);
    appendProxyCallbackUrl(sb, config.getProxyCallbackUrl());
    appendGateway(sb);
    appendRenew(sb);
    return sb.toString();
  }

  private void appendLoginUri(StringBuilder sb) {
    sb.append(config.getServerUrl());
    sb.append('/');
    sb.append(ProtocolConstants.LOGIN_URI);
  }

  private void appendLogoutUri(StringBuilder sb) {
    sb.append(config.getServerUrl());
    sb.append('/');
    sb.append(ProtocolConstants.LOGOUT_URI);
  }

  private void appendProxyUri(StringBuilder sb) {
    sb.append(config.getServerUrl());
    sb.append('/');
    sb.append(ProtocolConstants.PROXY_URI);
  }
  
  private void appendValidateUri(StringBuilder sb, String validate) {
    sb.append(config.getServerUrl());
    sb.append('/');
    sb.append(validate);
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
    appendParameter(sb, ProtocolConstants.PGT_PARAM, proxyGrantingTicket);
  }

  private void appendTargetService(StringBuilder sb, String targetService) {
    sb.append('&');
    appendParameter(sb, ProtocolConstants.TARGET_SERVICE_PARAM, 
        URLEncoder.encode(targetService));
  }
  
  private void appendService(StringBuilder sb) {
    sb.append('?');
    appendParameter(sb, ProtocolConstants.SERVICE_PARAM, 
        URLEncoder.encode(config.getServiceUrl()));
  }

  private void appendTicket(StringBuilder sb, String ticket) {
    sb.append('&');
    appendParameter(sb, ProtocolConstants.TICKET_PARAM, ticket);
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
          Boolean.toString(true));
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
