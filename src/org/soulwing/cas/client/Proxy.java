/*
 * ProxyTicketRequestor.java
 *
 * Created on Feb 9, 2007
 */
package org.soulwing.cas.client;


/**
 * Requests proxy tickets using a proxy granting ticket.
 *
 * @author Carl Harris
 */
public interface Proxy {

  /**
   * Invoke the CAS <code>/proxy</code> function to acquire a proxy ticket.
   * @param request proxy request
   * @return proxy response from the CAS server.
   */
  ProxyResponse proxy(ProxyRequest request);

}
