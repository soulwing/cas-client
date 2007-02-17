/*
 * SimpleProxyRequest.java
 *
 * Created on Feb 10, 2007
 */
package org.soulwing.cas.client;


/**
 * A simple immutable implementation of ProxyRequest.
 *
 * @author Carl Harris
 */
public class SimpleProxyRequest implements ProxyRequest {

  private final String proxyGrantingTicket;
  private final String targetService;
  
  public SimpleProxyRequest(String proxyGrantingTicket, String targetService) {
    this.proxyGrantingTicket = proxyGrantingTicket;
    this.targetService = targetService;
  }

  /* 
   * @see org.soulwing.cas.client.ProxyRequest#getProxyGrantingTicket()
   */
  public final String getProxyGrantingTicket() {
    return proxyGrantingTicket;
  }

  /* 
   * @see org.soulwing.cas.client.ProxyRequest#getTargetService()
   */
  public final String getTargetService() {
    return targetService;
  }

}
