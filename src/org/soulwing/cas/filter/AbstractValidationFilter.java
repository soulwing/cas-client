/*
 * AbstractValidationFilter.java
 *
 * Created on Sep 8, 2006
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
package org.soulwing.cas.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.ValidatorFactory;
import org.soulwing.cas.support.ValidationUtils;
import org.soulwing.servlet.http.HttpServletRequestWrapper;


/**
 * An abstract base validation filter.  This class must be extended by
 * a subclass that provides an implementation of the <code>getAuthenticator
 * </code> method which returns an instance of FilterAuthenticator that
 * understands how to perform the desired validation of CAS authentication
 * credentials. 
 *
 * Classes that extend this class should be designed to be used as
 * either a Filter that can be configured in <code>web.xml</code> or as
 * a bean that can be used with a filter-to-bean proxy in a dependency 
 * injection framework such as Spring.  
 * 
 * 
 * @author Carl Harris
 */
abstract class AbstractValidationFilter implements Filter {

  private static final Log log = LogFactory.getLog(AbstractValidationFilter.class);
  private ValidationConfiguration config;

  /**
   * Gets the ValidationConfiguration for this AbstractValidationFilter. 
   */
  public ValidationConfiguration getConfiguration() {
    return config;
  }

  /**
   * Sets the ValidationConfiguration for this AbstractValidationFilter.
   */
  public void setConfiguration(ValidationConfiguration config) {
    this.config = config;
  }
  
  /**
   * Gets the FilterAuthenticator instance for this AbstractValidationFilter.
   */
  protected abstract FilterAuthenticator getAuthenticator();

  /**
   * Invoked by filter's container to initialize the filter, when the 
   * filter is configured in the application's deployment descriptor
   * (<code>web.xml</code>).
   * @param filterConfig configuration passed in by the container
   * @throws ServletException if a configuration error is detected
   */
  public final void init(FilterConfig filterConfig) throws ServletException {
    setConfiguration(new ValidationConfiguration(filterConfig));
    try {
      doInit(filterConfig);
      init();
    }
    catch (Exception ex) {
      throw new ServletException(ex);
    }
  }

  /**
   * Subclasses may override this method in order to participate in
   * the filter initialization process when the filter is configured
   * in the application's deployment descriptor (<code>web.xml</code>).
   * The default implementation simply returns.
   * @param filterConfig <code>FilterConfig</code> passed to this
   *    filter's <code>init(FilterConfig)</code> method.
   * @throws Exception
   */
  protected void doInit(FilterConfig filterConfig) throws Exception {
    // default implementation does nothing
  }
  
  /**
   * Initializes this filter. This method should be invoked by framework
   * initialization code after all dependencies have been set when the filter is
   * being used as a bean in a dependency injection framework.
   * @throws Exception
   */
  public final void init() throws Exception {
    if (getConfiguration() == null) {
      throw new IllegalStateException("Must set configuration property");
    }
    doInit();
    if (getAuthenticator() == null) {
      throw new IllegalStateException("Subclass must provide an authenticator");
    }
    ValidatorFactory.setProtocolSource(
        getConfiguration().getProtocolSource());
  }
  
  /**
   * Subclasses may override this method in order to participate in
   * initialization processing.  If this filter is configured in 
   * <code>web.xml</code>, this method is invoked <em>after</em> 
   * <code>doInit(FilterConfig).  The default implementation
   * simply returns.
   * @throws Exception
   */
  protected void doInit() throws Exception {
    // default implementation does nothing
  }
  
  /**
   * Invoked by the servlet container when the application's context is
   * being destroyed.
   */
  public final void destroy() {
    doDestroy();
  }
  
  /**
   * Subclasses may override this method in order to participate in the
   * shutdown processing.  The default implementation simply returns.
   */
  protected void doDestroy() {
    // default implementation does nothing.
  }
  
  /**
   * Invoked by the container when a request is received for one of the 
   * servlets we filter.
   */
  public final void doFilter(ServletRequest request, 
      ServletResponse response, FilterChain filterChain) 
      throws IOException, ServletException {
    try {
      doHttpFilter(
          (HttpServletRequest) request, 
          (HttpServletResponse) response,
          filterChain);
    }
    catch (ClassCastException ex) {
      log.info("Filter supports HTTP only");
      filterChain.doFilter(request, response);
    }
  }

  /**
   * Performs the actual work of the filter.  This method expects 
   * HttpServletRequest and HttpServletResponse types, which are more
   * convenient to work with.
   */
  private void doHttpFilter(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (isRequestForProxyCallbackUrl(request) || isRequestForBypass(request)) {
      bypassValidation(request, response, filterChain);
    }
    else if (getSessionValidation(request) != null) {
      wrapAndPassToFilterChain(request, response, filterChain,
          new ValidationResponse(getSessionValidation(request)));
    }
    else if (isRequestForFilterPath(request)) {
      try {
        validate(request, response, filterChain);
      }
      catch (NoTicketException ex) {
        redirectToAuthFailed(response);
      }
    }
    else if (isAuthenticationInProgress(request)) {
      try {
        validate(request, response, filterChain);
      }
      catch (NoTicketException ex) {
        redirectToLogin(request, response);
      }
    }
    else {
      redirectToLogin(request, response);
    }
  }

  private void bypassValidation(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    log.info("Bypassing CAS validation for " + request.getServletPath());
    filterChain.doFilter(request, response);
  }

  private boolean isRequestForProxyCallbackUrl(HttpServletRequest request) {
    return request.getRequestURL().toString().equals(
        getConfiguration().getProxyCallbackUrl());
  }

  private boolean isRequestForBypass(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    return session != null 
        && session.getAttribute(FilterConstants.BYPASS_ATTRIBUTE) != null;
  }
  
  private boolean isRequestForFilterPath(HttpServletRequest request) {
    return request.getServletPath().equals(getConfiguration().getFilterPath());
  }
  
  private boolean isAuthenticationInProgress(HttpServletRequest request) {
    return request.getParameter(ProtocolConstants.TICKET_PARAM) != null;
  }
  
  private void validate(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain) 
      throws NoTicketException, IOException, ServletException {
    ValidationResponse validationResponse = new ValidationResponse(
        getAuthenticator().authenticate(request)); 
    if (validationResponse.success()) {
      setSessionValidation(request, validationResponse.getResponse());
      wrapAndPassToFilterChain(request, response, filterChain, 
          validationResponse);
    }
    else {
      redirectToAuthFailed(response);
    }
  }

  private void wrapAndPassToFilterChain(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain, 
      ValidationResponse validationResponse) 
      throws IOException, ServletException {
    HttpServletRequest wrappedRequest = 
        wrapRequest(request, validationResponse.getUserName()); 
    passToFilterChain(wrappedRequest, response, filterChain);
  }

  private void passToFilterChain(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain) 
      throws IOException, ServletException {
    log.debug("User " + request.getUserPrincipal().getName()
        + " is authentic");
    filterChain.doFilter(request, response);
  }

  private class ValidationResponse {
    private ServiceValidationResponse response;

    public ValidationResponse(ServiceValidationResponse response) {
      this.response = response;
    }
    
    public boolean success() {
      return response == null ? false : response.isSuccessful();
    }
    
    public String getUserName() {
      return response.getUserName();
    }
    
    public ServiceValidationResponse getResponse() {
      return response;
    }
  }
  
  private void redirectToLogin(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    log.debug("Redirecting request to CAS login");
    response.sendRedirect(UrlGeneratorFactory.getUrlGenerator(request)
        .getLoginUrl());
  }
  
  private void redirectToAuthFailed(HttpServletResponse response) 
      throws IOException {
    if (getConfiguration().getAuthFailedUrl() != null) {
      log.debug("Redirecting request to auth failed URL");
      response.sendRedirect(getConfiguration().getAuthFailedUrl());
    }
    else {
      log.debug("Sending error to browser");
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
  }
  
  private HttpServletRequest wrapRequest(HttpServletRequest request,
      String userName) {
    return new HttpServletRequestWrapper(request, new Principal(userName));
  }
  
  private class Principal implements java.security.Principal {
    private String name;
   
    public Principal(String userName) {
      setName(userName);
    }
    
    public Principal(ServiceValidationResponse response) {
      this(response.getUserName());
    }
    
    public String getName() {
      return this.name;
    }

    private void setName(String name) {
      this.name = name;
    }
  }

  // This method has package-level access to facilitate unit testing.
  ServiceValidationResponse getSessionValidation(
      HttpServletRequest request) {
    ServiceValidationResponse response = null;
      ValidationUtils.getServiceValidationResponse(request);
    log.debug("Validation object " 
        + (response != null ? "exists" : "does not exist") + " in session");
    return response;
  }

  // This method has package-level access to facilitate unit testing.
  void setSessionValidation(HttpServletRequest request,
      ServiceValidationResponse response) {
    request.getSession(true).setAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE, response);
  }

}
