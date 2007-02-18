/*
 * DefaultProxyImpl.java
 *
 * Created on Feb 9, 2007
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

import org.soulwing.cas.client.jdom.ProxyHandler;


/**
 * The default implementation of the Proxy interface.
 *
 * @author Carl Harris
 */
public class DefaultProxyImpl implements Proxy {

  private final ProtocolHandler proxyHandler;
  private UrlGenerator generator;
  private ProtocolSource source = new UrlProtocolSource();

  public DefaultProxyImpl() {
    this.proxyHandler = new ProxyHandler();
  }

  /**
   * Gets the ProtocolSource used by this Proxy.
   * @return configured ProtocolSource.
   */
  public ProtocolSource setProtocolSource() {
    return this.source;
  }
  
  /**
   * Sets the ProtocolSource that will be used by this Proxy.
   * @param source ProtocolSource to configure.
   */
  public void setProtocolSource(ProtocolSource source) {
    this.source = source;
  }
  
  /**
   * Gets the UrlGenerator instance that will be used by this Proxy.
   * @return configured UrlGenerator instance
   */
  public UrlGenerator getUrlGenerator() {
    return this.generator;
  }
  
  /**
   * Sets the UrlGenerator instance that will be used by this Proxy.
   * @param generator UrlGenerator to configure.
   */
  public void setUrlGenerator(UrlGenerator generator) {
    this.generator = generator;
  }

  /* 
   * @see org.soulwing.cas.client.Proxy#proxy(ProxyRequest)
   */
  public ProxyResponse proxy(ProxyRequest request) {
    return (ProxyResponse)
        this.proxyHandler.processResult(
            setProtocolSource().getSource(
                getUrlGenerator().getProxyUrl(
                    request.getProxyGrantingTicket(), 
                    request.getTargetService())));
  }

}
