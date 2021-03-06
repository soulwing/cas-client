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

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.valves.ValveBase;
import org.soulwing.cas.client.ProtocolConstants;


/**
 * A Catalina Valve for receiving a CAS proxy callback and storing the
 * received proxy granting ticket (PGT).
 *
 * @author Carl Harris
 */
public class ProxyCallbackValve extends ValveBase {

  private final LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
  
  private String proxyCallbackUri;
  
  public String getProxyCallbackUri() {
    return proxyCallbackUri;
  }

  public void setProxyCallbackUri(String proxyCallbackUri) {
    this.proxyCallbackUri = proxyCallbackUri;
  }

  public void invoke(Request request, Response response)
      throws IOException, ServletException {
    ResourceHelper helper = (ResourceHelper) 
        request.getNote(ResourceValve.RESOURCE_HELPER_ATTR);
    ProxyGrantingTicketRegistry ticketRegistry =
        helper.getTicketRegistry();
    if (request.getRequestURI().equals(proxyCallbackUri)) {
      containerLog.trace("request URI " + request.getRequestURI() + " matches");
      String pgt = request.getParameter(
          ProtocolConstants.PROXY_TICKET_PARAM);
      String pgtIou = request.getParameter(
          ProtocolConstants.PROXY_TICKET_IOU_PARAM);
      if (pgt == null || pgtIou == null) {
        containerLog.trace("parameters incomplete: PGT=" + pgt + " IOU=" + pgtIou);
        return;
      }
      containerLog.debug("callback for IOU " + pgtIou + " with PGT " + pgt);
      ticketRegistry.registerTicket(pgtIou, pgt);
    }
    else {
      containerLog.trace("request URI " + request.getRequestURI() + " does not match");
      getNext().invoke(request, response);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.Lifecycle#startInternal()
   */
  protected void startInternal() throws LifecycleException {
    super.startInternal();
    containerLog.info("listening for URI " + proxyCallbackUri);
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
