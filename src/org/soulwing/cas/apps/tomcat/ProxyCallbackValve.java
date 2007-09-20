/*
 * ProxyCallbackValve.java
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConstants;


/**
 * A Catalina Valve for receiving a CAS proxy callback and storing the
 * received proxy granting ticket (PGT).
 *
 * @author Carl Harris
 */
public class ProxyCallbackValve extends ValveBase implements Lifecycle {

  private static final Log log = LogFactory.getLog(ProxyCallbackValve.class);
  
  private final LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
  
  private String ticketRegistryResourceName = "ProxyGrantingTicketRegistry";
  private String proxyCallbackUri;
  private ProxyGrantingTicketRegistry ticketRegistry;
  
  public String getTicketRegistryResourceName() {
    return ticketRegistryResourceName;
  }

  public void setTicketRegistryResourceName(String resourceName) {
    this.ticketRegistryResourceName = resourceName;
  }

  public String getProxyCallbackUri() {
    return proxyCallbackUri;
  }

  public void setProxyCallbackUri(String proxyCallbackUri) {
    this.proxyCallbackUri = proxyCallbackUri;
  }

  public void invoke(Request request, Response response)
      throws IOException, ServletException {
    if (request.getRequestURI().equals(proxyCallbackUri)) {
      log.debug("request URI " + request.getRequestURI() + " matches");
      String pgt = request.getParameter(
          ProtocolConstants.PROXY_TICKET_PARAM);
      String pgtIou = request.getParameter(
          ProtocolConstants.PROXY_TICKET_IOU_PARAM);
      if (pgt == null || pgtIou == null) {
        log.debug("parameters incomplete: PGT=" + pgt + " IOU=" + pgtIou);
        return;
      }
      log.debug("callback for IOU " + pgtIou + " with PGT " + pgt);
      ticketRegistry.registerTicket(pgtIou, pgt);
    }
    else {
      log.debug("request URI " + request.getRequestURI() + " does not match");
      getNext().invoke(request, response);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#start()
   */
  public synchronized void start() throws LifecycleException {
    try {
      StandardServer server = (StandardServer) ServerFactory.getServer();
      Context context = server.getGlobalNamingContext();
      ticketRegistry = (ProxyGrantingTicketRegistry) 
          context.lookup(ticketRegistryResourceName);
      log.debug("obtained ticket registry resource " 
          + ticketRegistryResourceName);
      log.debug("listening for URI " + proxyCallbackUri);
    }
    catch (NamingException ex) {
      throw new LifecycleException(ex);
    }
  }
  
  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#stop()
   */
  public synchronized void stop() throws LifecycleException {
    ticketRegistry = null;
  }
  
  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#addLifecycleListener(org.apache.catalina.LifecycleListener)
   */
  public void addLifecycleListener(LifecycleListener lifecycleListener) {
    lifecycleSupport.addLifecycleListener(lifecycleListener);
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#findLifecycleListeners()
   */
  public LifecycleListener[] findLifecycleListeners() {
    return lifecycleSupport.findLifecycleListeners();
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#removeLifecycleListener(org.apache.catalina.LifecycleListener)
   */
  public void removeLifecycleListener(LifecycleListener lifecycleListener) {
    lifecycleSupport.removeLifecycleListener(lifecycleListener);
  }

}
