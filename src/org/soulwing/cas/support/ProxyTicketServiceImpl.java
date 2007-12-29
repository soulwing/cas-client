/*
 * ProxyTicketServiceImpl.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.Proxy;
import org.soulwing.cas.client.ProxyFactory;
import org.soulwing.cas.client.ProxyResponse;
import org.soulwing.cas.client.SimpleProxyRequest;
import org.soulwing.cas.client.SimpleUrlGenerator;


/**
 * A service-layer bean that collaborates with ProxyGrantingTicketFilter 
 * (via ProxyGrantingTicketHolder) to provide a simple means of obtaining
 * tickets to access CAS-protected services via proxy.
 *
 * @author Carl Harris
 */
public class ProxyTicketServiceImpl implements ProxyTicketService {
  
  private static final Log log = LogFactory.getLog(ProxyTicketServiceImpl.class);

  private ProtocolConfiguration configuration;
  private Proxy proxy;

  /**
   * Gets the CAS protocol configuration for this ProxyTicketServiceImpl.
   * @return <code>ProtocolConfigurationImpl</code> instance configured for
   *    this ProxyTicketServiceImpl.
   */
  public ProtocolConfiguration getConfiguration() {
    return configuration;
  }
  
  /**
   * Sets the CAS protocol configuration to use with this ProxyTicketServiceImpl.
   * @param configuration <code>ProtocolConfigurationImpl</code> instance
   *    configured for this ProxyTicketServiceImpl.
   */
  public void setConfiguration(ProtocolConfiguration configuration) {
    this.configuration = configuration;
  }
  
  /**
   * Initializes this ProxyTicketServiceImpl instance.  Must be called before
   * the service can be used to obtain proxy tickets.
   */
  public void init() {
    if (configuration == null) {
      throw new IllegalStateException("Must provide configuration");
    }
    proxy = ProxyFactory.getProxy(new SimpleUrlGenerator(configuration));
  }
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.support.ProxyTicketService#getTicket(java.lang.String)
   */
  public String getTicket(String targetService) {
    String pgt = ProxyGrantingTicketHolder.getTicket();
    if (pgt == null) {
      throw new IllegalStateException("proxy granting ticket is not available");
    }
    return getTicket(targetService, pgt);
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.support.ProxyTicketService#getTicket(java.lang.String, java.lang.String)
   */
  public String getTicket(String targetService, String proxyGrantingTicket) {
    log.debug("Requesting ticket for target " + targetService);
    ProxyResponse response = proxy.proxy(
        new SimpleProxyRequest(proxyGrantingTicket, targetService));
    if (!response.isSuccessful()) {
      throw new ProxyTicketException(response);
    }
    log.debug("Obtained ticket " + response.getProxyTicket() 
        + " for target " + targetService);
    return response.getProxyTicket();
  }
  
}
