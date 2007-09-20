/*
 * CasUserDatabaseRealm.java
 *
 * Created on Sep 19, 2007
 *
 * Copyright (C) 2006, 2007 Carl E Harris, Jr.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 */
package org.soulwing.cas.apps.tomcat;

import java.lang.reflect.Constructor;
import java.security.Principal;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.ServerFactory;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConfiguration;

/**
 * A Catalina Realm that provides support for CAS authentication.
 *
 * @author Carl Harris
 */
public class CasUserDatabaseRealm 
    extends UserDatabaseRealm 
    implements CasRealm {
  
  private static final Log log = LogFactory.getLog(CasUserDatabaseRealm.class);

  private String strategy = ServiceValidationStrategy.class.getCanonicalName();
  
  private String ticketRegistryResourceName = "ProxyGrantingTicketRegistry";
  
  private AuthenticationStrategy authenticationStrategy;
  
  private ProxyGrantingTicketRegistry ticketRegistry;
  
  private ProtocolConfiguration protocolConfiguration = 
      new ProtocolConfiguration();

  private String trustedProxies;
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.CasRealm#getAuthenticationStrategy()
   */
  public AuthenticationStrategy getAuthenticationStrategy() {
    return authenticationStrategy;
  }
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.CasRealm#getProtocolConfiguration()
   */
  public ProtocolConfiguration getProtocolConfiguration() {
    return protocolConfiguration;
  }
  
  /*
   * (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.CasRealm#getProxyGrantingTicketRegistry()
   */
  public ProxyGrantingTicketRegistry getProxyGrantingTicketRegistry() {
    return ticketRegistry;
  }
  
  public String getStrategy() {
    return strategy;
  }
  
  public void setStrategy(String strategy) {
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

  public void setProxyCallbackUrl(String proxyCallbackUri) {
    protocolConfiguration.setProxyCallbackUrl(proxyCallbackUri);
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

  public String getTicketRegistryResourceName() {
    return ticketRegistryResourceName;
  }

  public void setTicketRegistryResourceName(String ticketRegistryResourceName) {
    this.ticketRegistryResourceName = ticketRegistryResourceName;
  }

  public Principal getPrincipal(String username) {
    return super.getPrincipal(username);
  }
  
  protected String getPassword(String username) {
    throw new UnsupportedOperationException();
  }

  public void start() throws LifecycleException {
    super.start();
    log.debug("strategy=" + strategy
        + " serverUrl=" + protocolConfiguration.getServerUrl() 
        + " serviceUrl=" + protocolConfiguration.getServiceUrl()
        + " proxyCallbackUrl=" + protocolConfiguration.getProxyCallbackUrl()
        + " renew=" + protocolConfiguration.getRenewFlag()
        + " gateway=" + protocolConfiguration.getGatewayFlag()
        + " trustedProxies=" + trustedProxies
        + " ticketRegistryResourceName=" + ticketRegistryResourceName);
    authenticationStrategy = newAuthenticationStrategy(strategy);
    if (authenticationStrategy instanceof ProxyValidationStrategy) {
      ((ProxyValidationStrategy) authenticationStrategy)
          .setTrustedProxies(trustedProxies);
    }
    if (protocolConfiguration.getProxyCallbackUrl() != null) {
      ticketRegistry = getTicketRegistryResource(ticketRegistryResourceName);
    }
  }

  private AuthenticationStrategy newAuthenticationStrategy(String strategy) 
      throws LifecycleException {
    try {
      Class strategyClass = Class.forName(strategy);
      if (!(AuthenticationStrategy.class.isAssignableFrom(strategyClass))) {
        throw new IllegalArgumentException("a subclass of "
            + AuthenticationStrategy.class.getCanonicalName()
            + " is required");
      }
      Constructor constructor = strategyClass.getConstructor(
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

  private ProxyGrantingTicketRegistry getTicketRegistryResource(
      String ticketRegistryResourceName) throws LifecycleException {
    try {
      StandardServer server = (StandardServer) ServerFactory.getServer();
      Context context = server.getGlobalNamingContext();
      ProxyGrantingTicketRegistry ticketRegistry = (ProxyGrantingTicketRegistry) 
          context.lookup(ticketRegistryResourceName);
      log.debug("obtained ticket registry resource " 
          + ticketRegistryResourceName);
      return ticketRegistry;
    }
    catch (NamingException ex) {
      throw new LifecycleException(
          "error obtaining ticket registry resource", ex);
    }
  }

}
