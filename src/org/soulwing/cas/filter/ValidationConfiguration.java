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

import javax.servlet.FilterConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolSource;
import org.soulwing.cas.client.UrlProtocolSource;


/**
 * A conffiguration bean for ValidationFilter.  Property values for this
 * bean are obtained from a FilterConfig instance, populated with
 * initialization parameters from the application's deployment descriptor
 * (typically, web.xml).
 *
 * The following initialization parameters are accepted as properties:
 * <table>
 *   <tr><th>Parameter</th><th>Use</th><th>Description</th></tr>
 *   <tr>
 *     <td>serverUrl</td><td>mandatory</td>
 *     <td>Specifies the base URL for the CAS server that will be used 
 *       by this filter.</td>
 *   </tr>
 *   <tr>
 *     <td>filterPath</td><td>mandatory</td>
 *     <td>Specifies the servlet path that the filter will consider to be
 *       a request for validation.  This will be appended onto
 *       the serviceUrl, with the resulting being the value of the
 *       "service" parameter in a CAS serviceValidate or proxyValidate
 *       request.</td>
 *   </tr>
 *   <tr>
 *     <td>serviceUrl</td><td>optional</td>
 *     <td>Specifies the base URL that will be passed to the CAS server as
 *      the argument to the CAS protocol's <code>server</code>
 *      parameter.  Normal this parameter isn't specified, because 
 *      the default is to use the portion of the request URL up to 
 *      (but not including) the servlet path.
 *   </tr>
 *   <tr>
 *     <td>proxyCallbackUrl</td><td>optional</td>
 *     <td>Specifies the URL that will be passed to the CAS server as
 *      the argument to the CAS protocol's <code>pgtUrl</code>
 *      parameter.</td>
 *   </tr>
 *   <tr>
 *     <td>authFailedUrl</td><td>optional</td>
 *     <td>Specifies the URL to which the user will be redirected if
 *       CAS authentication fails.  This URL MUST NOT refer to a resource
 *       that is protected by the ValidationFilter, as a redirection loop
 *       will occur.  If this parameter is not specified, the HTTP result
 *       code FORBIDDEN is returned to the browser.</td>
 *   </tr>
 *   <tr>
 *     <td>gateway</td><td>optional</td>
 *     <td>Set this parameter to "true" in order to utilize the
 *       CAS protocol's gateway functionality.</td>
 *   </tr>
 *   <tr>
 *     <td>renew</td><td>optional</td>
 *     <td>Set this parameter to "true" in order to utilize the
 *       CAS protocol's renew functionality.  This option is intended
 *       primarily for testing.</td>
 *   </tr>
 *   <tr>
 *     <td>sourceClassName</td><td>optional</td>
 *     <td>Set this parameter to the fully-qualified name of an
 *       implementation of ProtocolSource.  By default, UrlValidationSource
 *       is used.</td>  
 *   </td>
 *   <tr>
 *     <td>trustedProxies</td><td>optional</td>
 *     <td>A comma-delimited list of trusted proxy names.  If this parameter
 *       is not specified, all proxies are trusted.</td>
 *   </tr>
 * </table>

 * @author Carl Harris
 */
class ValidationConfiguration extends FilterConfigurator {

  private static final Log log = 
      LogFactory.getLog(ValidationConfiguration.class);

  public static final boolean GATEWAY_DEFAULT = false;
  public static final boolean RENEW_DEFAULT = false;
  public static final Class SOURCE_CLASS_DEFAULT = UrlProtocolSource.class; 
  
  private final String filterPath;
  private final String authFailedUrl;
  private final String trustedProxies;
  private final ProtocolSource protocolSource;
  
  private final ProtocolConfiguration protocolConfig = 
      new ProtocolConfiguration();

  ValidationConfiguration(FilterConfig filterConfig) 
      throws FilterParameterException {

    super(filterConfig);
    configureProtocol();
    filterPath = getParameter(FilterConstants.FILTER_PATH);
    authFailedUrl = getParameter(FilterConstants.AUTH_FAILED_URL);
    trustedProxies = getParameter(FilterConstants.TRUSTED_PROXIES);
    protocolSource = newProtocolSource(
        getClassFromParameter(FilterConstants.SOURCE_CLASS_NAME,
        SOURCE_CLASS_DEFAULT));
  }
  
  /* 
   * @see org.soulwing.cas.filter.FilterConfigurator#log()
   */
  protected Log log() {
    return log;
  }
  
  public String getAuthFailedUrl() {
    return authFailedUrl;
  }

  public String getFilterPath() {
    return filterPath;
  }  
  
  public String getProxyCallbackUrl() {
    return protocolConfig.getProxyCallbackUrl();
  }
  
  public boolean getGatewayFlag() {
    return protocolConfig.getGatewayFlag();
  }
  
  public boolean getRenewFlag() {
    return protocolConfig.getRenewFlag();
  }

  public String getServerUrl() {
    return protocolConfig.getServerUrl();
  }

  public String getServiceUrl() {
    return protocolConfig.getServiceUrl();
  }

  public String getTrustedProxies() {
    return trustedProxies;
  }

  public ProtocolSource getProtocolSource() {
    return protocolSource;
  }

  private void configureProtocol() throws FilterParameterException {
    protocolConfig.setServerUrl(
        getRequiredParameter(FilterConstants.SERVER_URL));
    protocolConfig.setServiceUrl(
        getParameter(FilterConstants.SERVICE_URL));
    protocolConfig.setGatewayFlag(
        Boolean.valueOf(getParameter(FilterConstants.GATEWAY, 
            Boolean.toString(GATEWAY_DEFAULT))).booleanValue());
    protocolConfig.setRenewFlag(
        Boolean.valueOf(getParameter(FilterConstants.RENEW, 
            Boolean.toString(RENEW_DEFAULT))).booleanValue()); 
    protocolConfig.setProxyCallbackUrl(
        getParameter(FilterConstants.PROXY_CALLBACK_URL));
    UrlGeneratorFactory.setProtocolConfiguration(protocolConfig);
  }

  private ProtocolSource newProtocolSource(Class sourceClass) 
      throws FilterParameterException {
    
    try {
      return (ProtocolSource) sourceClass.newInstance();
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
  
  
}
