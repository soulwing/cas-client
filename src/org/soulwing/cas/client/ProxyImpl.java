/*
 * ProxyImpl.java
 *
 * Created on Feb 9, 2007
 */
package org.soulwing.cas.client;

import org.soulwing.cas.client.jdom.ProxyHandler;


/**
 * A proxy issues requests for proxy tickets for a target service.
 *
 * @author Carl Harris
 */
public class ProxyImpl implements Proxy {

  private final ProtocolHandler proxyHandler;
  private UrlGenerator generator;
  private ProtocolSource source = new UrlProtocolSource();

  public ProxyImpl() {
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
