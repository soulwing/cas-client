/*
 * ResourceValve.java
 *
 * Created on Nov 25, 2007
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

import java.io.IOException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.ServerFactory;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.valves.ValveBase;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.filter.FilterAuthenticator;
import org.soulwing.cas.filter.ServiceValidationAuthenticator;

/**
 * A <code>Valve</code> that attaches some CAS resource references to the
 * request's session as internal notes.
 * 
 * @author Carl Harris
 */
public class ResourceValve extends ValveBase implements Lifecycle {

  public static final String RESOURCE_HELPER_ATTR = 
      "org.soulwing.cas.apps.tomcat.resourceHelper";

  private final LifecycleSupport lifecycleSupport = new LifecycleSupport(this);

  private String configResourceName = "CasProtocolConfiguration";

  private String authenticatorClassName = 
      ServiceValidationAuthenticator.class.getCanonicalName();

  private ResourceHelper helper = new ResourceHelper();

  public String getConfig() {
    return configResourceName;
  }

  public void setConfig(String configResourceName) {
    this.configResourceName = configResourceName;
  }

  public String getAuthenticatorClass() {
    return authenticatorClassName;
  }

  public void setAuthenticatorClass(String authenticatorClassName) {
    this.authenticatorClassName = authenticatorClassName;
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.valves.ValveBase#invoke(org.apache.catalina.connector.Request,
   *      org.apache.catalina.connector.Response)
   */
  public void invoke(Request request, Response response) throws IOException,
      ServletException {
    request.setNote(RESOURCE_HELPER_ATTR, helper);
    getNext().invoke(request, response);
  }

  public void addLifecycleListener(LifecycleListener listener) {
    lifecycleSupport.addLifecycleListener(listener);
  }

  public LifecycleListener[] findLifecycleListeners() {
    return lifecycleSupport.findLifecycleListeners();
  }

  public void removeLifecycleListener(LifecycleListener listener) {
    lifecycleSupport.removeLifecycleListener(listener);
  }

  public void start() throws LifecycleException {
    try {
      helper.setProtocolConfiguration((ProtocolConfiguration)
          getResource(getConfig()));
      FilterAuthenticator authenticator = newAuthenticator();
      authenticator.setProtocolConfiguration(helper.getProtocolConfiguration());
      helper.setAuthenticator(authenticator);
      helper.setTicketRegistry(new ProxyGrantingTicketRegistryImpl());
    }
    catch (Exception ex) {
      throw new LifecycleException(ex);
    }
  }

  public void stop() throws LifecycleException {
    // TODO Auto-generated method stub

  }

  private Object getResource(String resourceName)
      throws NamingException {
    try {
      StandardServer server = (StandardServer) ServerFactory.getServer();
      Context context = server.getGlobalNamingContext();
      return context.lookup(resourceName);
    }
    catch (NamingException ex) {
      NamingException nex = new NamingException(
          "error obtaining resource named " + resourceName);
      nex.setRootCause(ex);
      throw nex;
    }
  }

  private FilterAuthenticator newAuthenticator() throws Exception {
    Class authenticatorClass = Class.forName(getAuthenticatorClass());
    FilterAuthenticator authenticator = (FilterAuthenticator)
        authenticatorClass.newInstance();
    return authenticator;
  }
  
}
