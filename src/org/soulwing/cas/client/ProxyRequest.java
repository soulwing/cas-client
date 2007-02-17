/*
 * ProxyRequest.java
 *
 * Created on Feb 10, 2007
 */
package org.soulwing.cas.client;


/**
 * A request for a proxy ticket.
 *
 * @author Carl Harris
 */
public interface ProxyRequest {

  String getProxyGrantingTicket();
  
  String getTargetService();
  
}
