/*
 * CasUserDatabaseRealm.java
 *
 * Created on Sep 21, 2007 
 */
package org.soulwing.cas.apps.tomcat;

import java.security.Principal;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.soulwing.cas.client.ProtocolConfiguration;

/**
 * A Catalina UserDatabaseRealm that allows a Principal (with associated
 * roles) to be obtained without performing any authentication of credentials.
 * 
 * @author Carl Harris
 */
public class CasUserDatabaseRealm extends UserDatabaseRealm 
    implements CasRealm {

  private CasResourcesSupport resources = new CasResourcesSupport(); 
  
  public Principal getPrincipal(String username) {
    return super.getPrincipal(username);
  }

  public void start() throws LifecycleException {
    super.start();
    resources.init();
  }
  
  public void stop() throws LifecycleException {
    resources.destroy();
    super.stop();
  }

  public AuthenticationStrategy getAuthenticationStrategy() {
    return resources.getAuthenticationStrategy();
  }

  public ProtocolConfiguration getProtocolConfiguration() {
    return resources.getProtocolConfiguration();
  }

  public ProxyGrantingTicketRegistry getProxyGrantingTicketRegistry() {
    return resources.getProxyGrantingTicketRegistry();
  }
  
}
