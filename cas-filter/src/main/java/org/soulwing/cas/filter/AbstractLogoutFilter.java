/*
 * AbstractLogoutFilter.java
 *
 * Created on June 20, 2008 
 *
 * Copyright (C) 2006, 2007, 2008 Carl E Harris, Jr.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolConfigurationHolder;
import org.soulwing.cas.client.SimpleUrlGenerator;
import org.soulwing.cas.http.AuthenticatorConstants;
import org.soulwing.cas.support.ValidationUtils;

/**
 * <p>
 * A filter that is capable of performing application and CAS-global logout.
 * Subclasses of this filter determine what consitutes a request to log out;
 * this filter manages the filter chain to cause application logout and/or
 * CAS global logout, as desired.
 * </p>
 * <p>
 * This filter supports several configurable properties:
 * </p>
 * <ul>
 * <li><code>applicationLogout</code> &mdash; if <code>true</code> (the default)
 *   the logout request will be forwarded down the filter chain towards the 
 *   application servlet, to allow cleanup of non-CAS session state</li>
 * <li><code>globalLogout</code> &mdash; if <code>true</code> (default is
 *   <code>false</code>) then after the request has been (optionally)
 *   processed by any downstream filters and the application itself, a
 *   redirect to the CAS logout URL will be sent on the response.</li>
 * <li><code>redirectUrl</code> &mdash; if configured, then after the 
 *   request has been (optionally) processed by downstream filters and the
 *   application itself, and after CAS global logout has been performed (if
 *   enabled), then the browser will be directed to this URL.</li>
 * <li><code>bypassRedirectUrl</code> &mdash; if configured, and if a logout
 *   request has been marked for CAS bypass, then the browser will be 
 *   redirected to this URL instead of performing any CAS-related redirects.</li>
 * <li><code>protocolConfiguration</code> &mdash; CAS protocol configuration
 *   bean, required only when <code>globalLogout</code> is enabled.  Will be
 *   obtained from ProtocolConfigurationHolder if this filter is wired into
 *   the application's deployment descriptor (web.xml) as opposed to being
 *   used as a simple bean (e.g. with FilterToBeanProxy)</li>
 *
 * @author Carl Harris
 */
public abstract class AbstractLogoutFilter implements Filter {

  protected static final Log log = LogFactory.getLog(
      AbstractLogoutFilter.class);

  public static final String GLOBAL_LOGOUT_DEFAULT = "false";
  public static final String APPLICATION_LOGOUT_DEFAULT = "true";

  private String redirectUrl;
  private String bypassRedirectUrl;
  private boolean applicationLogout = true;
  private boolean globalLogout = false;
  private ProtocolConfiguration protocolConfiguration;

  public AbstractLogoutFilter() {
    super();
  }

  /**
   * Gets the <code>applicationLogout</code> flag.
   * @return <code>true</code> if application logout is required
   */
  public boolean isApplicationLogout() {
    return applicationLogout;
  }

  /**
   * Sets the <code>applicationLogout</code> flag.
   * @param applicationLogout <code>true</code> if a request matching
   *        <code>logoutPath</code> should be forwarded down the filter chain
   *        towards the application servlet; <code>false</code> to shunt the
   *        logout request at this filter.
   */
  public void setApplicationLogout(boolean applicationLogout) {
    this.applicationLogout = applicationLogout;
  }

  /**
   * Gets the <code>globalLogout</code> flag.
   * @return <code>true</code> if CAS global logout should be invoked
   */
  public boolean isGlobalLogout() {
    return globalLogout;
  }

  /**
   * Sets the <code>globalLogout</code> flag.
   * @param globalLogout <code>true</code> if a request matching
   *        <code>logoutPath</code> should be redirected to the CAS global
   *        logout URL.
   * 
   * Note: the <code>protocolConfiguration</code> property must be set if this
   * flag is set.
   * 
   * Note: if <code>applicationLogout</code> is also set, the redirect to CAS
   * global logout will occur only if no downstream filter or servlet has
   * committed the response.
   */
  public void setGlobalLogout(boolean globalLogout) {
    this.globalLogout = globalLogout;
  }

  /**
   * Gets the URL which will be used to send a redirect after logout is
   * complete.
   * @return <code>String</code> URL.
   */
  public String getRedirectUrl() {
    return redirectUrl;
  }

  /**
   * Sets the URL which will be used to send a redirect after logout is
   * completed.
   * @param redirectUrl <code>String</code> URL
   * 
   * Note: if <code>applicationLogout</code> is also set, the redirect will
   * occur only if no downstream filter or servlet has committed the response.
   * 
   * Note: if <code>globalLogout</code> is also set, this URL will be passed
   * as a parameter to the CAS logout operation. The CAS server will be
   * responsible for the final redirect to the URL specified here.
   */
  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  /**
   * Gets the URL which will be used to send a redirect in lieu of CAS global
   * logout for a session that has bypassed CAS.
   * @return <code>String</code> URL.
   */
  public String getBypassRedirectUrl() {
    return bypassRedirectUrl;
  }

  /**
   * Sets the URL which will be used to send a redirect in lieu of CAS global
   * logout for a session that has bypassed CAS.
   * @param bypassRedirectUrl <code>String</code> URL
   * 
   * Note: if <code>applicationLogout</code> is also set, the redirect will
   * occur only if no downstream filter or servlet has committed the response.
   */
  public void setBypassRedirectUrl(String bypassRedirectUrl) {
    this.bypassRedirectUrl = bypassRedirectUrl;
  }

  /**
   * Gets the CAS protocol configuration bean.
   */
  public ProtocolConfiguration getProtocolConfiguration() {
    if (protocolConfiguration == null) {
      setProtocolConfiguration(ProtocolConfigurationHolder
          .getRequiredConfiguration());
    }
    return protocolConfiguration;
  }

  /**
   * Sets the CAS protocol configuration bean.
   */
  public void setProtocolConfiguration(
      ProtocolConfiguration protocolConfiguration) {
    this.protocolConfiguration = protocolConfiguration;
  }

  /**
   * <p>
   * Initializes this filter. The default implementation does nothing;
   * subclasses may override without invoking <code>super.init</code>. A
   * subclass should use this method to check that all required properties have
   * been set.
   * </p>
   * <p>
   * When this filter is configured using the servlet deployment descriptor
   * (web.xml), this method is invoked by <code>init(FilterConfig)</code>
   * after propertie values have been extracted from the
   * <code>FilterConfig</code> object.
   * </p>
   * <p>
   * When this filter is being used as a bean in a dependency injection
   * framework (e.g. Spring), this method should be invoked after all
   * dependencies have been set, for example, by configuring the
   * <code>init-method</code> attribute on the corresponding bean declaration.
   * </p>
   * @throws Exception as needed if property values are not as expected
   */
  public void init() throws Exception {
  }

  /**
   * Lifecycle callback for the filter when configured using the servlet
   * deployment descriptor.
   * @param filterConfig a <code>FilterConfig</code> instance
   * @throws ServletException
   */
  public final void init(FilterConfig filterConfig) throws ServletException {
    Configurator fc = new Configurator(filterConfig);
    // extract our properties from the configurator
    setApplicationLogout(Boolean.parseBoolean(fc.getParameter(
        AuthenticatorConstants.APPLICATION_LOGOUT, APPLICATION_LOGOUT_DEFAULT)));
    setGlobalLogout(Boolean.parseBoolean(fc.getParameter(
        AuthenticatorConstants.GLOBAL_LOGOUT, GLOBAL_LOGOUT_DEFAULT)));
    setRedirectUrl(new Configurator(filterConfig)
        .getParameter(AuthenticatorConstants.REDIRECT_URL));
    setBypassRedirectUrl(new Configurator(filterConfig)
        .getParameter(AuthenticatorConstants.BYPASS_REDIRECT_URL));
    // Now give the subclass a chance
    onInit(fc);
    // Validate property settings and translate exceptions
    try {
      init();
    }
    catch (Exception ex) {
      throw new ServletException(ex);
    }
  }

  /**
   * Allows subclasses access to the <code>Configurator</code> for this filter
   * to extract their own parameters (when this filter is configured using the
   * servlet deployment descriptor). The default implementation does nothing;
   * subclasses may override without invoking <code>super.onInit</code>
   * @param filterConfigurator <code>Configurator</code> instance derived from
   *        the container's <code>FilterConfig</code> object.
   * @throws ServletException
   */
  protected void onInit(Configurator filterConfigurator)
      throws ServletException {
  }

  /**
   * Lifecycle callback for the filter when configured using the servlet
   * deployment descriptor. The default implementation does nothing; subclasses
   * may override without invoking <code>super.destroy</code>.
   */
  public void destroy() {
    // not needed
  }

  /**
   * Performs the action of the filter.
   * @param request the subject <code>ServletRequest</code>
   * @param response the subject <code>ServletResponse</code>
   * @param filterChain the tail of the <code>FilterChain</code>
   * @throws IOException
   * @throws ServletException
   */
  public final void doFilter(ServletRequest request,
      ServletResponse response, FilterChain filterChain) throws IOException,
      ServletException {
    try {
      doHttpFilter((HttpServletRequest) request,
          (HttpServletResponse) response, filterChain);
    }
    catch (ClassCastException ex) {
      log.info("Filter supports HTTP only");
      filterChain.doFilter(request, response);
    }
  }

  /**
   * Performs the action of the filter for an HTTP request
   * @param request the subject <code>HttpServletRequest</code>
   * @param response the subject <code>HttpServletResponse</code>
   * @param filterChain the tail of the filter chain
   * @throws IOException
   * @throws ServletException
   */
  private final void doHttpFilter(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    // short-circuit if this is not a logout request
    if (!isLogoutRequest(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    log.debug("logout requested by URI: " + request.getRequestURI());
    BufferedHttpServletResponse bufferedResponse = null;
    
    if (isApplicationLogout()) {
      bufferedResponse = new BufferedHttpServletResponse(response);
      request.setAttribute(AuthenticatorConstants.LOGOUT_ATTRIBUTE,
          new Boolean(true));
      filterChain.doFilter(request, bufferedResponse);
    }
    boolean sessionBypassed =
        ValidationUtils.isValidationBypassedForSession(request);
    ValidationUtils.removeSessionState(request);
    onLogout(request);
    if (isGlobalLogout()) {
      if (!sessionBypassed) {
        doGlobalLogout(response);
      }
      else if (getBypassRedirectUrl() != null) {
        doLogoutRedirect(response, getBypassRedirectUrl());
      }
    }
    else if (getRedirectUrl() != null) {
      doLogoutRedirect(response, getRedirectUrl());
    }
    else {
      // send any output buffered from the application logout
      if (bufferedResponse != null) {
        bufferedResponse.flushAll();
      }
    }
  }

  /**
   * Determine whether a request represents the logout action.
   * @param request the subject <code>HttpServletRequest</code>
   * @return <code>true</code> if <code>request</code> should be considered
   *    a request to log out.
   */
  protected abstract boolean isLogoutRequest(HttpServletRequest request);

  /**
   * Perform any subclass-specific logout actions.  The default implementation
   * does nothing.
   * @param request the subject <code>HttpServletRequest</code>.
   */
  protected void onLogout(HttpServletRequest request) {
  }
  
  private void doGlobalLogout(HttpServletResponse response)
      throws IOException {
    if (!response.isCommitted()) {
      SimpleUrlGenerator urlGenerator =
          new SimpleUrlGenerator(getProtocolConfiguration());
      response.sendRedirect(urlGenerator.getLogoutUrl(getRedirectUrl()));
    }
    else {
      // this really shouldn't happen
      log.error("skipping CAS global logout because response is committed");
    }
  }

  private void doLogoutRedirect(HttpServletResponse response, String url)
      throws IOException {
    if (!response.isCommitted()) {
      response.sendRedirect(url);
    }
    else {
      // this really shouldn't happen
      log.error("skipping logout redirect because response is committed");
    }
  }

  private static class BufferedHttpServletResponse 
      extends HttpServletResponseWrapper {

    private BufferedServletOutputStream bufferedOutputStream;
    private PrintWriter writer;

    public BufferedHttpServletResponse(HttpServletResponse response) {
      super(response);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getOutputStream()
     */
    public ServletOutputStream getOutputStream() throws IOException {
      if (bufferedOutputStream == null) {
        bufferedOutputStream = new BufferedServletOutputStream();
      }
      return bufferedOutputStream;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getWriter()
     */
    public PrintWriter getWriter() throws IOException {
      if (writer == null) {
        writer = new PrintWriter(getOutputStream());
      }
      return writer;
    }

    public void flushAll() throws IOException {
      ServletOutputStream os = getResponse().getOutputStream();
      os.write(((BufferedServletOutputStream) getOutputStream()).getOutput());
      os.flush();
    }

  }

  private static class BufferedServletOutputStream extends ServletOutputStream {

    private final ByteArrayOutputStream os = new ByteArrayOutputStream(8192);

    public void write(int b) throws IOException {
      os.write(b);
    }

    public byte[] getOutput() {
      return os.toByteArray();
    }

    /*
     * (non-Javadoc)
     * @see java.io.OutputStream#close()
     */
    public void close() throws IOException {
      os.close();
      super.close();
    }

    /*
     * (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    public void flush() throws IOException {
      // ignore it
    }

  }

}