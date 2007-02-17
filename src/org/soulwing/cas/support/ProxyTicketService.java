/*
 * ProxyTicketService.java
 *
 * Created on Feb 12, 2007
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
package org.soulwing.cas.support;

import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.Proxy;
import org.soulwing.cas.client.ProxyFactory;
import org.soulwing.cas.client.ProxyResponse;
import org.soulwing.cas.client.SimpleProxyRequest;
import org.soulwing.cas.client.SimpleUrlGenerator;


/**
 * A service-layer bean that collaborates with ProxyGrantingTicketFilter 
 * (via ProxyGrantingTicketHolder) providing a simple means of obtaining
 * tickets to access CAS-protected services via proxy.
 *
 * @author Carl Harris
 */
public class ProxyTicketService {

  private ProtocolConfiguration configuration;
  private Proxy proxy;

  public ProtocolConfiguration getConfiguration() {
    return configuration;
  }
  
  public void setConfiguration(ProtocolConfiguration configuration) {
    this.configuration = configuration;
  }
  
  public void init() {
    if (configuration == null) {
      throw new IllegalStateException("Must provide configuration");
    }
    proxy = ProxyFactory.getProxy(new SimpleUrlGenerator(configuration));
  }
  
  public String getTicket(String targetService) {
    ProxyResponse response = proxy.proxy(
        new SimpleProxyRequest(getProxyGrantingTicket(), targetService));
    if (!response.isSuccessful()) {
      throw new ProxyTicketException(response);
    }
    return response.getProxyTicket();
  }
  
  private String getProxyGrantingTicket() {
    String proxyGrantingTicket = ProxyGrantingTicketHolder.getTicket();
    if (proxyGrantingTicket == null) {
      throw new IllegalStateException("Proxy granting ticket not available");
    }
    return proxyGrantingTicket;
  }
}
