/*
 * ValidationUtils.java
 *
 * Created on Feb 6, 2007 
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
package org.soulwing.cas.support;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProxyValidationResponse;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.http.AuthenticatorConstants;

/**
 * Utility methods for extracting the CAS validation artifacts from
 * an HttpServletRequest.
 *
 * @author Carl Harris
 */
public class ValidationUtils {

  private static final Log log = LogFactory.getLog(ValidationUtils.class);

  /**
   * Gets the ServiceValidationResponse from an HttpServletRequest.
   * @param request HttpServletRequest from which to obtain the validation
   *    response
   * @return ServiceValidationResponse if it exists, else <code>null</code>
   */
  public static ServiceValidationResponse getServiceValidationResponse(
      HttpServletRequest request) {
    
    HttpSession session = request.getSession(false);
    if (session != null) {
      Object o = session.getAttribute(AuthenticatorConstants.VALIDATION_ATTRIBUTE);
      if (o instanceof ServiceValidationResponse) {
        return (ServiceValidationResponse) o;
      }
    }
    return null;
  }
  
  /**
   * Gets the ProxyValidationResponse from an HttpServletRequest.
   * @param request HttpServletRequest from which to obtain the validation
   *    response
   * @return ProxyValidationResponse if it exists, else <code>null</code>
   */
  public static ProxyValidationResponse getProxyValidationResponse(
      HttpServletRequest request) {
    return (ProxyValidationResponse) getServiceValidationResponse(request); 
  }
  
  /**
   * Gets the proxy granting ticket from an HttpServletRequest.  In order
   * to obtain a proxy granting ticket, the ProxyCallbackFilter must be
   * configured and placed in the filter chain <strong>after</strong> the
   * ServiceValidate or ProxyValidate filter.  Additionally, the validation
   * filter and ProxyCallbackFilter must be configured to use the same
   * value for proxyCallbackUrl.
   * 
   * @param request HttpServletRequest from which to obtain the proxy
   *    granting ticket
   * @return proxy granting ticket string from CAS.
   */
  public static String getProxyGrantingTicket(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      Object o = session.getAttribute(
          AuthenticatorConstants.PROXY_GRANTING_TICKET_ATTRIBUTE);
      if (o instanceof String) {
        return (String) o;
      }
    }
    return null;
  }
  
  /**
   * Determines whether a request has bypassed CAS validation.  
   * @param request request to inspect
   * @return <code>true</code> if <code>request</code> will not be subject to
   *    CAS authentication ticket validation either because the request or
   *    the session is marked for bypass.
   */
  public static boolean isValidationBypassed(HttpServletRequest request) {
    return request.getAttribute(AuthenticatorConstants.BYPASS_ATTRIBUTE) != null
        || isValidationBypassedForSession(request);
  }

  /**
   * Determines whether the session for a request is set to bypass
   * CAS validation.
   * @param request request to inspect
   * @return <code>true</code> if <code>request</code> will not be subject to
   *    CAS authentication ticket validation because the session is marked
   *    for bypass.
   */
  public static boolean isValidationBypassedForSession(
      HttpServletRequest request) {
    return request.getSession(false) != null 
        && isValidationBypassedForSession(request.getSession()); 
  }

  /**
   * Determines whether a session is set to be bypass CAS validation.
   * @param session session to inspect
   * @return <code>true</code> if requests received for <code>session</code> 
   *    will not be subject to CAS authentication ticket validation.
   */
  public static boolean isValidationBypassedForSession(
      HttpSession session) {
    return session.getAttribute(AuthenticatorConstants.BYPASS_ATTRIBUTE) != null;
  }

  /**
   * Removes all CAS-related session state for a given request
   * @param request the subject <code>HttpServletRequest</code>
   */
  public static void removeSessionState(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    ServiceValidationResponse validation =
        ValidationUtils.getServiceValidationResponse(request);
    Enumeration names = session.getAttributeNames();
    while (names.hasMoreElements()) {
      String name = (String) names.nextElement();
      if (name.startsWith(AuthenticatorConstants.ATTRIBUTE_PREFIX)) {
        log.debug("Removing attribute " + name + " from session");
        session.removeAttribute(name);
      }
    }
    if (validation != null) {
      log.info("User " + validation.getUserName() + " has logged out");
    }
  }


}
