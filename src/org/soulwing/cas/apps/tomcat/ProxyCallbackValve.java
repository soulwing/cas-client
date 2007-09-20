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
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

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
import org.soulwing.cas.filter.FilterConstants;


/**
 * A Catalina Valve for receiving a CAS proxy callback and storing the
 * received proxy granting ticket (PGT).
 *
 * @author Carl Harris
 */
public class ProxyCallbackValve extends ValveBase 
    implements Lifecycle, ProxyGrantingTicketRegistry {

  private static final Log log = LogFactory.getLog(ProxyCallbackValve.class);
  
  private final LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
  
  private String resourceName;
  private String proxyCallbackUri;
  
  private final Map iouToSessionMap = new HashMap();
  private final Map iouToPgtMap = new HashMap();
  
  public String getResourceName() {
    return resourceName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public String getProxyCallbackUri() {
    return proxyCallbackUri;
  }

  public void setProxyCallbackUri(String proxyCallbackUri) {
    this.proxyCallbackUri = proxyCallbackUri;
  }

  /* (non-Javadoc)
   * @see org.apache.catalina.valves.ValveBase#invoke(org.apache.catalina.connector.Request, org.apache.catalina.connector.Response)
   */
  public synchronized void registerSession(String pgtIou, HttpSession session) {
    String pgt = getIouToPgtMapping(pgtIou);
    if (pgt != null) {
      addPgtToSession(pgt, session);
    }
    else {
      putIouToSessionMapping(pgtIou, session);
    }
  }

  public void invoke(Request request, Response response)
      throws IOException, ServletException {
    if (request.getRequestURI().equals(proxyCallbackUri)) {
      String pgt = request.getParameter(
          ProtocolConstants.PROXY_TICKET_PARAM);
      String pgtIou = request.getParameter(
          ProtocolConstants.PROXY_TICKET_IOU_PARAM);
      if (pgt == null || pgtIou == null) {
        log.warn("Proxy callback parameters incomplete");
        return;
      }
      log.debug("Callback for IOU " + pgtIou + " with PGT " + pgt);

      HttpSession session = getIouToSessionMapping(pgtIou);
      if (session != null) {
        addPgtToSession(pgt, session);
      }
      else {
        putIouToPgtMapping(pgtIou, pgt);
      }
    }
    else {
      
    }
  }

  private synchronized void addPgtToSession(String pgt, HttpSession session) {
    session.setAttribute(FilterConstants.PROXY_GRANTING_TICKET_ATTRIBUTE, pgt);
    log.debug("PGT " + pgt + " added to session " + session.getId());
  }
  
  private synchronized String getIouToPgtMapping(String pgtIou) {
    if (iouToPgtMap.containsKey(pgtIou)) {
      String pgt = (String) iouToPgtMap.get(pgtIou);
      iouToPgtMap.remove(pgtIou);
      return pgt;
    }
    else {
      return null;
    }
  }

  private synchronized void putIouToPgtMapping(String pgtIou, String pgt) {
    iouToPgtMap.put(pgtIou, pgt);
    log.debug("IOU " + pgtIou + " mapped to PGT " + pgt);
  }
  
  private synchronized HttpSession getIouToSessionMapping(String pgtIou) {
    if (iouToSessionMap.containsKey(pgtIou)) {
      HttpSession session = (HttpSession) iouToSessionMap.get(pgtIou);
      iouToSessionMap.remove(pgtIou);
      return session;
    }
    else {
      return null;
    }
  }
  
  private synchronized void putIouToSessionMapping(String pgtIou,
      HttpSession session) {
    iouToSessionMap.put(pgtIou, session);
    log.debug("IOU " + pgtIou + " mapped to session " + session.getId());
  }
  
  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#start()
   */
  public void start() throws LifecycleException {
    try {
      StandardServer server = (StandardServer) ServerFactory.getServer();
      Context context = server.getGlobalNamingContext();
      context.bind(resourceName, this);
      
    }
    catch (NamingException ex) {
      throw new LifecycleException(ex);
    }
  }
  
  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#stop()
   */
  public void stop() throws LifecycleException {
    try {
      StandardServer server = (StandardServer) ServerFactory.getServer();
      Context context = server.getGlobalNamingContext();
      context.unbind(resourceName);
    }
    catch (NamingException ex) {
      throw new LifecycleException(ex);
    } 
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
