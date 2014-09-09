/*
 * UrlGenerator.java
 *
 * Created on Sep 12, 2006
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
 * A generator for CAS service request URLs.  
 *
 * @author Carl Harris
 */
public interface UrlGenerator {
  
  /**
   * Gets the CAS login URL for the configured server URL and service URL.
   * Includes the gateway and/or renew flags if set.
   * @return <code>String</code> CAS login URL.
   */
  String getLoginUrl();
  
  /**
   * Gets the CAS login URL for the configured server URL and service URL,
   * with the given service URI and query appended to the service URL.
   * @param serviceUri service URI 
   * @param serviceQuery service query string
   * @return login URL
   */
  String getLoginUrl(String serviceUri, String serviceQuery);

  /**
   * Gets the CAS logout URL.  
   * @return <code>String</code> CAS logout URL.
   */
  String getLogoutUrl();

  /**
   * Gets the CAS logout URL with the optional <code>url</code> parameter.
   * @param url a URL for post CAS logout redirection or <code>null</code>  
   * @return <code>String</code> CAS logout URL.  Includes the 
   *    <code>url</code> query parameter if <code>url</code> is
   *    non-empty.
   */
  String getLogoutUrl(String url);

  /**
   * Gets the CAS proxy URL.
   * @param proxyGrantingTicket proxy granting ticket obtained as a result
   *    of PGT callback after serviceValidate or proxyValidate.
   * @param targetService target service URL
   * @return <code>String</code> CAS proxy URL
   * @throws IllegalArgumentException if either <code>proxyGrantingTicket</code>
   *    or <code>targetService</code> is <code>null</code> or empty.
   */
  String getProxyUrl(String proxyGrantingTicket, String targetService);

  /**
   * Gets the CAS proxyValidate URL.
   * @param ticket ticket to validate
   * @return <code>String</code> CAS proxyValidate URL
   * @throws IllegalArgumentException if <code>ticket</code> is
   *    <code>null</code> or empty.
   */
  String getProxyValidateUrl(String ticket);

  /**
   * Gets the CAS serviceValidate URL.
   * @param ticket ticket to validate
   * @return <code>String</code> CAS serviceValidate URL
   * @throws IllegalArgumentException if <code>ticket</code> is
   *    <code>null</code> or empty.
   */
  String getServiceValidateUrl(String ticket);

}