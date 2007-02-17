/*
 * ProxyResponse.java
 *
 * Created on Sep 7, 2006
 */
package org.soulwing.cas.client;


/**
 * A value class for the response to the CAS <code>/proxy</code> function.
 *
 * @author Carl Harris
 * 
 */
public class ProxyResponse extends Response {

  private String proxyTicket;

  public String getProxyTicket() {
    return proxyTicket;
  }

  public void setProxyTicket(String proxyTicket) {
    this.proxyTicket = proxyTicket;
  }
  
}
