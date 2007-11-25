/*
 * ResourceHelper.java
 *
 * Created on Sep 19, 2007
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
package org.soulwing.cas.apps.tomcat;

import org.soulwing.cas.client.ProtocolConfiguration;

/**
 * Default implementation of <code>ResourceHelper</code>.
 * 
 * @author Carl Harris
 */
public class ResourceHelper {

  private AuthenticationStrategy authenticationStrategy;
  private ProtocolConfiguration protocolConfiguration;
  private ProxyGrantingTicketRegistry ticketRegistry;

  /**
   * Gets the <code>authenticationStrategy</code> property.
   */
  public AuthenticationStrategy getAuthenticationStrategy() {
    return authenticationStrategy;
  }

  /**
   * Sets the <code>authenticationStrategy</code> property.
   */
  public void setAuthenticationStrategy(
      AuthenticationStrategy authenticationStrategy) {
    this.authenticationStrategy = authenticationStrategy;
  }

  /**
   * Gets the <code>protocolConfiguration</code> property.
   */
  public ProtocolConfiguration getProtocolConfiguration() {
    return protocolConfiguration;
  }

  /**
   * Sets the <code>protocolConfiguration</code> property.
   */
  public void setProtocolConfiguration(
      ProtocolConfiguration protocolConfiguration) {
    this.protocolConfiguration = protocolConfiguration;
  }

  /**
   * Gets the <code>ticketRegistry</code> property.
   */
  public ProxyGrantingTicketRegistry getTicketRegistry() {
    return ticketRegistry;
  }

  /**
   * Sets the <code>ticketRegistry</code> property.
   */
  public void setTicketRegistry(ProxyGrantingTicketRegistry ticketRegistry) {
    this.ticketRegistry = ticketRegistry;
  }

}
