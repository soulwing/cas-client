/*
 * CasResourcesSupport.java
 *
 * Created on Sep 21, 2007 
 */
package org.soulwing.cas.apps.tomcat;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.ServerFactory;
import org.apache.catalina.core.StandardServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConfiguration;

/**
 * Provides supporting implementation of the CasResources interface
 * for CAS realms.
 *
 * @author Carl Harris
 */
public class CasResourcesSupport implements CasResources {

  private static final Log log = LogFactory.getLog(CasResourcesSupport.class);
  
  private static final String PROTOCOL_CONFIGURATION_RESOURCE =
      "CasProtocolConfiguration";

  private static final String AUTHENTICATION_STRATEGY_RESOURCE = 
      "CasAuthenticationStrategy";

  private static final String TICKET_REGISTRY_RESOURCE = 
      "CasProxyGrantingTicketRegistry";

  private ProtocolConfiguration protocolConfiguration;
  private AuthenticationStrategy authenticationStrategy;
  private ProxyGrantingTicketRegistry ticketRegistry; 

  
  public void init() throws LifecycleException {
    try {
      StandardServer server = (StandardServer) ServerFactory.getServer();
      Context context = server.getGlobalNamingContext();
      protocolConfiguration = (ProtocolConfiguration)
          context.lookup(PROTOCOL_CONFIGURATION_RESOURCE);
      log.debug("obtained protocol configuration resource "
          + protocolConfiguration);
      authenticationStrategy = (AuthenticationStrategy)
          context.lookup(AUTHENTICATION_STRATEGY_RESOURCE);
      log.debug("obtained authentication strategy resource "
            + authenticationStrategy);
      ticketRegistry = (ProxyGrantingTicketRegistry) 
          context.lookup(TICKET_REGISTRY_RESOURCE);
      log.debug("obtained ticket registry resource " 
          + ticketRegistry);
    }
    catch (NamingException ex) {
      throw new LifecycleException(ex);
    }
  }
  
  public void destroy() throws LifecycleException {
    ticketRegistry = null;
    authenticationStrategy = null;
    protocolConfiguration = null;
  }
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.CasResources#getAuthenticationStrategy()
   */
  public AuthenticationStrategy getAuthenticationStrategy() {
    return authenticationStrategy;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.CasResources#getProtocolConfiguration()
   */
  public ProtocolConfiguration getProtocolConfiguration() {
    return protocolConfiguration;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.CasResources#getProxyGrantingTicketRegistry()
   */
  public ProxyGrantingTicketRegistry getProxyGrantingTicketRegistry() {
    return ticketRegistry;
  }

}
