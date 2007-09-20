/*
 * ProxyGrantingTicketRegistryImpl.java
 *
 * Created on Sep 20, 2007
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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.filter.FilterConstants;

/**
 * Default implementation of ProxyGrantingTicketRegistry.
 *
 * @author Carl Harris
 */
public class ProxyGrantingTicketRegistryImpl 
    implements ProxyGrantingTicketRegistry {

  private static final Log log = LogFactory.getLog(ProxyGrantingTicketRegistry.class);

  private final Map iouToSessionMap = new HashMap();
  private final Map iouToPgtMap = new HashMap();
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.ProxyGrantingTicketRegistry#registerSession(java.lang.String, javax.servlet.http.HttpSession)
   */
  public void registerSession(String pgtIou, HttpSession session) {
    String pgt = getIouToPgtMapping(pgtIou);
    if (pgt != null) {
      addPgtToSession(pgt, session);
    }
    else {
      putIouToSessionMapping(pgtIou, session);
    }
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.tomcat.ProxyGrantingTicketRegistry#registerTicket(java.lang.String, java.lang.String)
   */
  public void registerTicket(String pgtIou, String pgt) {
    HttpSession session = getIouToSessionMapping(pgtIou);
    if (session != null) {
      addPgtToSession(pgt, session);
    }
    else {
      putIouToPgtMapping(pgtIou, pgt);
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
  
}
