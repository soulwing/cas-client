/*
 * ProxyFactory.java
 *
 * Created on Feb 9, 2007
 */
package org.soulwing.cas.client;

import org.soulwing.cas.client.jdom.ProxyHandler;


/**
 * A factory for generating Proxy instances.  The default configuration
 * of this factory uses org.soulwing.cas.jdom.ProxyHandler as the
 * proxy handler.
 *
 * @author Carl Harris
 */
public class ProxyFactory {

  private static ProtocolHandler proxyHandler = new ProxyHandler();

  private static ProtocolSource protocolSource = new UrlProtocolSource();

  public static final Proxy getProxy(UrlGenerator urlGenerator) {
    ProxyImpl proxy = new ProxyImpl();
    proxy.setUrlGenerator(urlGenerator);
    proxy.setProtocolSource(protocolSource);
    return proxy;
  }
  
  /**
   * Gets the handler for the CAS <code>/proxy</code> function.
   */
  public static ProtocolHandler getProxyHandler() {
    return proxyHandler;
  }

  /**
   * Sets the handler for the CAS <code>/proxy</code> function.
   */
  public static synchronized void setProxyHandler(
      ProtocolHandler proxyHandler) {
    ProxyFactory.proxyHandler = proxyHandler;
  }

  /**
   * Gets the ProtocolSource that will be used by Validator instances
   * created by this factory.
   */
  public static ProtocolSource getProtocolSource() {
    return protocolSource;
  }

  /**
   * Sets the ProtocolSource that will be used by Validator instances
   * created by this factory.
   */
  public static synchronized void setProtocolSource(
      ProtocolSource protocolSource) {
    ProxyFactory.protocolSource = protocolSource;
  }

}
