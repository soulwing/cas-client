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
import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.authenticator.AuthenticatorBase;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.deploy.LoginConfig;
import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.filter.FilterConstants;
import org.soulwing.cas.filter.UrlGeneratorFactory;


/**
 * A Catalina Authenticator for CAS authentication.
 *
 * @author Carl Harris
 */
public class CasAuthenticator extends AuthenticatorBase {

  private RealmWrapper realm;
 
  /* (non-Javadoc)
   * @see org.apache.catalina.authenticator.AuthenticatorBase#authenticate(org.apache.catalina.connector.Request, org.apache.catalina.connector.Response, org.apache.catalina.deploy.LoginConfig)
   */
  protected boolean authenticate(Request request, Response response, 
      LoginConfig loginConfig)
      throws IOException {
    
    ServiceValidationResponse validationResponse = 
        getSessionValidationResponse(request);
    if (validationResponse != null) {
      return isKnownUser(request, response, validationResponse);
    }
    else {
      return isAuthentic(request, response);
    }
  }

  private boolean isAuthentic(Request request, Response response)
      throws IOException {
    ResourceHelper helper = getResourceHelper(request);
    try {
      ServiceValidationResponse validationResponse = 
          helper.getAuthenticator().authenticate(request);
      if (validationResponse.isSuccessful()) {
        boolean knownUser = isKnownUser(request, response, validationResponse);
        if (knownUser) {
          addSessionToTicketRegistry(request, validationResponse, helper);
        }
        return knownUser;
      }
      else {
        if (previouslyRedirectedToLogin(request, helper)) {
          response.sendError(Response.SC_UNAUTHORIZED);
        }
        else {
          redirectToLogin(request, response, helper);
        }
        return false;
      }
    }
    catch (NoTicketException ex) {
      redirectToLogin(request, response, helper);
      return false;
    }
  }

  private void redirectToLogin(Request request, Response response,
      ResourceHelper helper) throws IOException {
    String loginUrl = getLoginUrl(request, helper);
    request.getSessionInternal().setNote(FilterConstants.LOGIN_ATTRIBUTE, 
        loginUrl);
    response.sendRedirect(loginUrl);
  }

  private boolean previouslyRedirectedToLogin(Request request, 
      ResourceHelper helper) {
    String loginUrl = (String)
        request.getSessionInternal().getNote(FilterConstants.LOGIN_ATTRIBUTE);
    return loginUrl != null && loginUrl.equals(getLoginUrl(request, helper));
  }

  private String getLoginUrl(Request request, ResourceHelper helper) {
    return UrlGeneratorFactory.getUrlGenerator(request, 
            helper.getProtocolConfiguration()).getLoginUrl();
  }

  private boolean isKnownUser(Request request, Response response,
      ServiceValidationResponse validationResponse) throws IOException {
    String userName = validationResponse.getUserName();
    /*
     *  SCC-47: thanks to Ralf Lorenz
     *  We now use AuthenticatorBase.register to reduce the number of 
     *  realm (database lookups) after a user has been authenticated
     */
    Principal principal = request.getUserPrincipal();
    if (principal == null) {
      // principal not already known; look it up via the configured realm
      principal = realm.getPrincipal(userName);
      if (principal != null) {
        // register it so that Tomcat can reuse it without another realm lookup
        register(request, response, principal, "CAS", userName, null);
      }
    }
    if (principal == null) {
      containerLog.warn("unknown CAS user " + userName + " for " 
          + request.getRequestURI());
      response.sendError(Response.SC_UNAUTHORIZED);
      return false;
    }
    request.setUserPrincipal(principal);
    containerLog.debug("setting " + FilterConstants.VALIDATION_ATTRIBUTE + " to " +
        validationResponse);
    request.getSession(true).setAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE, validationResponse);
    return true;
  }

  private ServiceValidationResponse getSessionValidationResponse(
      Request request) {
    HttpSession session = request.getSession(false);
    if (session == null) return null;
    ServiceValidationResponse validationResponse = (ServiceValidationResponse)
        session.getAttribute(FilterConstants.VALIDATION_ATTRIBUTE);
    if (validationResponse == null) return null;
    containerLog.debug("principal " + validationResponse.getUserName()
        + " authenticated via session state");
    return validationResponse;
  }

  private ResourceHelper getResourceHelper(Request request) {
    ResourceHelper helper = (ResourceHelper) 
        request.getNote(ResourceValve.RESOURCE_HELPER_ATTR);
    if (helper == null) {
      throw new IllegalStateException(
          "No ResourceHelper in private session state -- "
           + " is a ResourceValve configured in this container?");
    }
    return helper;
  }

  private void addSessionToTicketRegistry(Request request,
      ServiceValidationResponse validationResponse, ResourceHelper helper) {
    ProxyGrantingTicketRegistry ticketRegistry =
        helper.getTicketRegistry();
    if (ticketRegistry != null) {
      ticketRegistry.registerSession(
          validationResponse.getProxyGrantingTicketIou(),
          request.getSession());
    }
  }

  /*
   * (non-Javadoc)
   * @see org.apache.catalina.authenticator.AuthenticatorBase#startInternal()
   */
  public void startInternal() throws LifecycleException {
    super.startInternal();
    try {
      realm = new RealmWrapper(getContainer().getRealm());
    }
    catch (IllegalArgumentException ex){
      containerLog.error("CAS authenticator requires a Realm that extends from "
         + "Catalina's RealmBase");
      throw new LifecycleException(ex);
    }
  }
  
}
