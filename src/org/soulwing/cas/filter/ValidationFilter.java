/*
 * ValidationFilter.java
 *
 * Created on Sep 8, 2006
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
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.ValidatorFactory;
import org.soulwing.servlet.http.HttpServletRequestWrapper;


/**
 * A CAS validation filter. 
 * 
 * @author Carl Harris
 */
public abstract class ValidationFilter implements Filter {

  private static final Log log = LogFactory.getLog(ValidationFilter.class);
  private ValidationConfiguration config;

  /**
   * Gets the ValidationConfiguration for this ValidationFilter. 
   */
  protected ValidationConfiguration getConfiguration() {
    return config;
  }

  /**
   * Sets the ValidationConfiguration for this ValidationFilter.
   */
  private void setConfiguration(ValidationConfiguration config) {
    this.config = config;
  }
  
  /**
   * Gets the FilterAuthenticator instance for this ValidationFilter.
   */
  protected abstract FilterAuthenticator getAuthenticator();

  /**
   * Invoked by filter's container to initialize the filter.  May be 
   * overriden by subclasses to perform subclass-specific configuration.
   * If so, the subclass <em>must</em> first invoke <code>super.init</code>
   * to start the initialization.
   * 
   * @param filterConfig configuration passed in by the container
   * @throws ServletException if a configuration error is detected
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    setConfiguration(new ValidationConfiguration(filterConfig));
    ValidatorFactory.setProtocolSource(
        getConfiguration().getProtocolSource());
  }

  /**
   * Invoked by the container when the application context is
   * begin destroyed.  Subclasses may override this method, but must
   * invoke <code>super.destroy</code> to complete the shutdown process.
   */
  public void destroy() {
    // Nothing to destroy
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

    if (isRequestForProxyCallbackUrl(request)) {
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
      saveRequest(request);
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

  private boolean isRequestForFilterPath(HttpServletRequest request) {
    return request.getServletPath().equals(getConfiguration().getFilterPath());
  }
  
  private boolean isAuthenticationInProgress(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return false;
    }
    else {
      return session.getAttribute(FilterConstants.SAVED_REQUEST_ATTRIBUTE) 
          != null;
    }
  }
  
  private void validate(HttpServletRequest request, 
      HttpServletResponse response, FilterChain filterChain) 
      throws NoTicketException, IOException, ServletException {
    ValidationResponse validationResponse = new ValidationResponse(
        getAuthenticator().authenticate(request)); 
    if (validationResponse.success()) {
      if (isRequestSaved(request)) {
        wrapAndPassToFilterChain(getSavedRequest(request), response, 
            filterChain, validationResponse);
      }
      else if (config.getDefaultPath() != null) {
        setSessionValidation(request, validationResponse.getResponse());
        forwardToDefault(request, response);
      }
      else {
        setSessionValidation(request, validationResponse.getResponse());
        passToFilterChain(wrapRequest(request, 
            validationResponse.getUserName()), 
            response, filterChain);
      }
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
    setSessionValidation(wrappedRequest, validationResponse.getResponse());
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
  
  private void forwardToDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    log.debug("Forwarding to default " + config.getDefaultPath());
    request.getRequestDispatcher(config.getDefaultPath())
        .forward(request, response);
  }

  private void saveRequest(HttpServletRequest request) {
    log.debug("Saved request in session");
    request.getSession()
        .setAttribute(FilterConstants.SAVED_REQUEST_ATTRIBUTE, request);
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

  private HttpServletRequest getSavedRequest(HttpServletRequest request) {
    HttpServletRequest savedRequest = (HttpServletRequest)
        request.getSession().getAttribute(
            FilterConstants.SAVED_REQUEST_ATTRIBUTE);
    request.getSession().removeAttribute(
        FilterConstants.SAVED_REQUEST_ATTRIBUTE);
    return savedRequest;
  }

  boolean isRequestSaved(HttpServletRequest request) {
    return request.getSession().getAttribute(
        FilterConstants.SAVED_REQUEST_ATTRIBUTE) != null;
  }
  
  
  /**
   * @return if the session attached to <code>request</code> contains
   *    a validation request, returns the cached validation request
   *    else returns <code>null</code>.
   * TODO: should we check a signature on the stored attribute to make
   * certain we created it.
   */
  ServiceValidationResponse getSessionValidation(
      HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    ServiceValidationResponse response = null;
    if (session != null) {
      response = (ServiceValidationResponse)
        session.getAttribute(FilterConstants.VALIDATION_ATTRIBUTE);
    }
    log.debug("Validation object " 
        + (response != null ? "exists" : "does not exist") + " in session");
    return response;
  }

  /**
   * Stores the validation response in the session attached to 
   * <code>request</code>.
   * TODO: should we sign the request?
   */
  void setSessionValidation(HttpServletRequest request,
      ServiceValidationResponse response) {
    request.getSession(true).setAttribute(
        FilterConstants.VALIDATION_ATTRIBUTE, response);
  }

}
