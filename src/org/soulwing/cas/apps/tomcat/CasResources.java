/*
 * CasResources.java
 *
 * Created on Sep 21, 2007 
 */
package org.soulwing.cas.apps.tomcat;

import org.soulwing.cas.client.ProtocolConfiguration;

/**
 * An accessor interface for resources needed in CAS authentication
 * realms.
 *
 * @author Carl Harris
 */
public interface CasResources {

  ProtocolConfiguration getProtocolConfiguration();
  
  AuthenticationStrategy getAuthenticationStrategy();
  
  ProxyGrantingTicketRegistry getProxyGrantingTicketRegistry();
  
}
