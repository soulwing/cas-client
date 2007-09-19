package org.soulwing.cas.apps.tomcat;

import java.lang.reflect.Constructor;
import java.security.Principal;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.realm.RealmBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConfiguration;

/**
 * A Catalina Realm that provides support for CAS authentication.
 *
 * @author Carl Harris
 */
public class CasRealm extends RealmBase {
  
  private static final Log log = LogFactory.getLog(CasRealm.class);

  private Class strategy;
  
  private AuthenticationStrategy authenticationStrategy;
  
  private ProtocolConfiguration protocolConfiguration = 
      new ProtocolConfiguration();

  private String trustedProxies;
  
  public AuthenticationStrategy getAuthenticationStrategy() {
    return authenticationStrategy;
  }
  
  public ProtocolConfiguration getProtocolConfiguration() {
    return protocolConfiguration;
  }
  
  public Class getStrategy() {
    return strategy.getClass();
  }
  
  public void setStrategy(Class strategy) {
    if (AuthenticationStrategy.class.isAssignableFrom(strategy)) {
      throw new IllegalArgumentException("a subclass of "
          + AuthenticationStrategy.class.getCanonicalName()
          + " is required");
    }
    this.strategy = strategy;
  }
  
  public String getServerUrl() {
    return protocolConfiguration.getServerUrl();
  }

  public void setServerUrl(String serverUrl) {
    protocolConfiguration.setServerUrl(serverUrl);
  }

  public String getServiceUrl() {
    return protocolConfiguration.getServiceUrl();
  }

  public void setServiceUrl(String serviceUrl) {
    protocolConfiguration.setServiceUrl(serviceUrl);
  }

  public String getProxyCallbackUrl() {
    return protocolConfiguration.getProxyCallbackUrl();
  }

  public void setProxyCallbackUrl(String proxyCallbackUrl) {
    protocolConfiguration.setProxyCallbackUrl(proxyCallbackUrl);
  }

  public boolean getRenew() {
    return protocolConfiguration.getRenewFlag();
  }

  public void setRenew(boolean renew) {
    protocolConfiguration.setRenewFlag(renew);
  }

  public boolean getGateway() {
    return protocolConfiguration.getGatewayFlag();
  }

  public void setGateway(boolean gateway) {
    protocolConfiguration.setGatewayFlag(gateway);
  }

  public String getTrustedProxies() {
    return trustedProxies;
  }

  public void setTrustedProxies(String trustedProxies) {
    this.trustedProxies = trustedProxies;
  }

  protected String getName() {
    return CasRealm.class.getSimpleName();
  }

  protected Principal getPrincipal(String username) {
    throw new UnsupportedOperationException();
  }

  protected String getPassword(String username) {
    throw new UnsupportedOperationException();
  }

  public void start() throws LifecycleException {
    super.start();
    log.debug("serverUrl=" + protocolConfiguration.getServerUrl() 
        + " serviceUrl=" + protocolConfiguration.getServiceUrl()
        + " proxyCallbackUrl=" + protocolConfiguration.getProxyCallbackUrl()
        + " renew=" + protocolConfiguration.getRenewFlag()
        + " gateway=" + protocolConfiguration.getGatewayFlag());
    authenticationStrategy = newAuthenticationStrategy(strategy);
  }

  private AuthenticationStrategy newAuthenticationStrategy(Class strategy) 
      throws LifecycleException {
    try {
      Constructor constructor = strategy.getConstructor(
          new Class[] { ProtocolConfiguration.class });
      return (AuthenticationStrategy)
          constructor.newInstance(
              new Object[] { protocolConfiguration });
    }
    catch (Exception ex) {
      throw new LifecycleException(
          "while constructing CAS " 
          + AuthenticationStrategy.class.getSimpleName()
          + " instance", ex);  
    }
  } 

  public void stop() throws LifecycleException {
    log.info("CasRealm shut down");
    super.stop();
  }

}
