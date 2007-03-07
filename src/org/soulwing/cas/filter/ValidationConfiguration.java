/*
 * ValidationConfiguration.java
 *
 * Created on Feb 7, 2007
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolConfigurationHolder;
import org.soulwing.cas.client.ProtocolSource;
import org.soulwing.cas.client.UrlProtocolSource;


/**
 * A configuration bean for AbstractValidationFilter.  Property values for this
 * bean can be configured using the provided public setters or via a
 * FilterConfig passed to a constructor for this class. 
 *
 * @author Carl Harris
 */
public class ValidationConfiguration {

  private static final Log log = 
      LogFactory.getLog(ValidationConfiguration.class);

  public static final Class SOURCE_CLASS_DEFAULT = UrlProtocolSource.class; 
  public static final boolean REDIRECT_TO_LOGIN_DEFAULT = true;
 
  private String filterPath;
  private boolean redirectToLogin = REDIRECT_TO_LOGIN_DEFAULT;
  private String authFailedUrl;
  private String trustedProxies;
  private ProtocolSource protocolSource;
  private ProtocolConfiguration protocolConfiguration;
  
  /**
   * Initializes this ValidationConfiguration instance.  
   * This method MUST be invoked after all configurable properties are
   * set, and before the configuration is used to perform CAS validation.
   * Typically, this method will be invoked by a dependency injection
   * framework such as Spring.
   * @throws Exception 
   */
  public void init() throws Exception {
    if (getProtocolSource() == null) {
      setProtocolSourceClass(SOURCE_CLASS_DEFAULT);
    }
  }
  
  
  /* 
   * @see org.soulwing.cas.filter.Configurator#log()
   */
  protected Log log() {
    return log;
  }
  
  /**
   * Gets the URL to which a user's browser will be redirected if
   * validation of the user's CAS authentication ticket fails.
   * @return <code>String</code> URL.
   */
  public String getAuthFailedUrl() {
    return authFailedUrl;
  }

  /**
   * Sets the URL to which a user's browser will be redirected if
   * validation of the user's CAS authentication ticket fails.
   * @param authFailedUrl <code>String</code> URL
   */
  public void setAuthFailedUrl(String authFailedUrl) {
    this.authFailedUrl = authFailedUrl;
  }
  
  /**
   * Gets the servlet path that this filter will consider to be a
   * request for authentication ticket validation.
   * @return <code>String</code> servlet path
   */
  public String getFilterPath() {
    return filterPath;
  }  
  
  /**
   * Sets a servlet path that this filter will consider to be a 
   * request for authentication ticket validation.  If this parameter
   * is set, the validation filter will only perform ticket validation --
   * it will not send redirects to the CAS login URL.  This parameter would
   * typically be used only by a web service that is expecting to get
   * proxy authentication tickets from a front-end web application.
   * 
   * @param filterPath <code>String</code> servlet path
   */
  public void setFilterPath(String filterPath) {
    this.filterPath = filterPath;
  }

  /**
   * Gets the CAS protocol configuration object.
   * @return protocol configuration
   */
  public ProtocolConfiguration getProtocolConfiguration() {
    if (protocolConfiguration == null) {
      setProtocolConfiguration(
          ProtocolConfigurationHolder.getRequiredConfiguration());
    }
    return protocolConfiguration;
  }

  /**
   * Sets the CAS protocol configuration object to use in the validation
   * filter that will use this ValidationConfiguration.
   * @param protocolConfiguration CAS protocol configuration
   */
  public void setProtocolConfiguration(
      ProtocolConfiguration protocolConfiguration) {
    if (protocolConfiguration.getServerUrl() == null) {
      throw new IllegalArgumentException(FilterConstants.SERVER_URL
          + " property is required");
    }
    this.protocolConfiguration = protocolConfiguration;
  }

  /**
   * Gets the CAS protocol source object.
   * @return protocol source
   */
  public ProtocolSource getProtocolSource() {
    return protocolSource;
  }

  /**
   * Sets the CAS protocol source object to use in the validation filter
   * that will use this ValidationConfiguration.
   * @param protocolSource protocol source
   */
  public void setProtocolSource(ProtocolSource protocolSource) {
    this.protocolSource = protocolSource;
  }

  /**
   * Sets the class to use as a CAS protocol source in the validation
   * filter that will use this ValidationConfiguration.
   * @param sourceClass class that implements the ProtocolSource interface
   * @throws FilterParameterException
   */
  public void setProtocolSourceClass(Class sourceClass) 
      throws FilterParameterException {
    try {
      setProtocolSource((ProtocolSource) sourceClass.newInstance());
    }
    catch (ClassCastException ex) {
      throw new FilterParameterException(ex);
    }
    catch (IllegalAccessException ex) {
      throw new FilterParameterException(ex);
    }
    catch (InstantiationException ex) {
      throw new FilterParameterException(ex);
    }
  }

  /**
   * Gets the flag that indicates whether the filter should redirect to
   * the CAS login when a request is received that does not have an 
   * authentication ticket and no authentication session has been established.
   * 
   * @return <code>boolean</code> flag state; if
   */
  public boolean isRedirectToLogin() {
    return redirectToLogin;
  }

  /**
   * Sets the flag that indicates whether the filter should redirect to
   * the CAS login when a request is received that does not have an 
   * authentication ticket and no authentication session has been established.
   * 
   * @param redirectToLogin <code>true</code> if you want requests from an
   *    unauthenticated session that do not include a CAS authentication ticket 
   *    to be redirected to the CAS login form; <code>false</code> if you want
   *    such requests to be redirected to the URL specified by 
   *    <code>authFailedUrl</code>.  The default setting for this flag is 
   *    <code>true</code>.  Typically, it would be set to false only for backend
   *    services that are expecting to get an authentication ticket that was
   *    generated via the proxy mechanism by a front-end web application.   
   */
  public void setRedirectToLogin(boolean redirectToLogin) {
    this.redirectToLogin = redirectToLogin;
  }

  /**
   * Gets the list of proxies that should be trusted by this filter.
   */
  public String getTrustedProxies() {
    return trustedProxies;
  }

  /**
   * Sets the list of proxies that should be trusted by this filter.
   * @param trustedProxies
   */
  public void setTrustedProxies(String trustedProxies) {
    this.trustedProxies = trustedProxies;
  }

}
