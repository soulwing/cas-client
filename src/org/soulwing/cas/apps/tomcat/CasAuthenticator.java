/*
 * CasAuthenticator.java
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

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Realm;
import org.apache.catalina.Session;
import org.apache.catalina.authenticator.AuthenticatorBase;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.deploy.LoginConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.filter.FilterConstants;

/**
 * A Catalina Authenticator for CAS authentication.
 *
 * @author Carl Harris
 */
public class CasAuthenticator extends AuthenticatorBase {

  private static final Log log = LogFactory.getLog(CasAuthenticator.class);
  
  private CasRealm casRealm;
  private AuthenticationStrategy authenticationStrategy;
  private ProtocolConfiguration protocolConfiguration;
  
  /* (non-Javadoc)
   * @see org.apache.catalina.authenticator.AuthenticatorBase#authenticate(org.apache.catalina.connector.Request, org.apache.catalina.connector.Response, org.apache.catalina.deploy.LoginConfig)
   */
  protected boolean authenticate(Request request, Response response, 
      LoginConfig loginConfig)
      throws IOException {
    
    if (isSessionAuthenticated(request)) {
      ServiceValidationResponse validationResponse = (ServiceValidationResponse)
          request.getSessionInternal()
              .getNote(FilterConstants.VALIDATION_ATTRIBUTE);
      request.setUserPrincipal(casRealm.getPrincipal(
          validationResponse.getUserName()));
      log.debug("principal " + request.getUserPrincipal().getName()
          + " authenticated via session state");
      return true;
    }
    else {
      try {
        log.debug("validating request for " + request.getRequestURI());
        ServiceValidationResponse validationResponse =
            authenticationStrategy.authenticate(request);
        if (validationResponse.isSuccessful()) {
          request.setUserPrincipal(casRealm.getPrincipal(
              validationResponse.getUserName()));
          request.getSessionInternal(true).setNote(
              FilterConstants.VALIDATION_ATTRIBUTE, validationResponse);
          log.debug("principal " + request.getUserPrincipal().getName()
              + " authenticated via CAS");
          return true;
        }
        else {
          return false;
        }
      }
      catch (NoTicketException ex) {
        response.sendRedirect(UrlGeneratorFactory.getUrlGenerator(request, 
            protocolConfiguration).getLoginUrl());
        return false;
      }
    }
  }

  private boolean isSessionAuthenticated(Request request) {
    Session session = request.getSessionInternal(false);
    return session != null 
      && session.getNote(FilterConstants.VALIDATION_ATTRIBUTE) != null;
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.authenticator.AuthenticatorBase#start()
   */
  public void start() throws LifecycleException {
    super.start();
    Realm realm = getContainer().getRealm();
    if (!(realm instanceof CasRealm)) {
      log.error("a CasRealm is required");
      throw new LifecycleException(this.getClass().getSimpleName()
          + " requires a CasRealm");
    }
    casRealm = (CasRealm) realm;
    authenticationStrategy = casRealm.getAuthenticationStrategy();
    protocolConfiguration = casRealm.getProtocolConfiguration();
  }
  
}
