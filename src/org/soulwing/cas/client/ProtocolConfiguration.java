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
public interface ProtocolConfiguration {

  /**
   * Gets the base URL for the CAS server.
   * @return base URL for the CAS server used by this 
   *    ProtocolConfigurationImpl.
   */
  String getServerUrl();

  /**
   * Gets the configured URL for the application. 
   * @return URL for the application
   */
  String getServiceUrl();

  /**
   * Gets the value of the URL that will be used for proxy callback.
   * @return URL for proxy callback.
   */
  String getProxyCallbackUrl();

  /**
   * Gets the CAS gateway flag setting.
   * @return gateway flag setting for this ProtocolConfigurationImpl.
   */
  boolean getGatewayFlag();

  /**
   * Gets the CAS renew flag setting.
   * @return renew flag setting for this ProtocolConfigurationImpl
   */
  boolean getRenewFlag();

}