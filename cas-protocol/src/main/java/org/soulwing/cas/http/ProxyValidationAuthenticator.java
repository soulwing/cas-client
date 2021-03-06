/*
 * ProxyValidationAuthenticator.java
 *
 * Created on Sep 12, 2006
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
package org.soulwing.cas.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soulwing.cas.client.AbstractResponse;
import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ProxyValidationResponse;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.ValidationRequest;
import org.soulwing.cas.client.ValidatorFactory;


/**
 * FilterAuthenticator implementation for the CAS <code>/proxyValidate</code>
 * operation.
 *
 * @author Carl Harris
 */
public class ProxyValidationAuthenticator implements Authenticator {

  private final Log log = LogFactory.getLog(ProxyValidationAuthenticator.class); 
  private ProtocolConfiguration protocolConfiguration;
  private List trustedProxies = new ArrayList(0);

  public ProxyValidationAuthenticator() {
  }
  
  public ProxyValidationAuthenticator(ProtocolConfiguration protocolConfiguration,
      String trustedProxies) {
    setProtocolConfiguration(protocolConfiguration);
    setTrustedProxies(trustedProxies);
  }
  
  /*
   * (non-Javadoc)
   * @see org.soulwing.cas.filter.FilterAuthenticator#setProtocolConfiguration(org.soulwing.cas.client.ProtocolConfiguration)
   */
  public final void setProtocolConfiguration(
      ProtocolConfiguration protocolConfiguration) {
    this.protocolConfiguration = protocolConfiguration;
  }

  /**
   * Sets the list of proxies this ProxyValidationAuthenticator should
   * trust.
   * @param trustedProxies list of String proxy names
   */
  public final void setTrustedProxies(String trustedProxies) {
    if (trustedProxies != null) {
      this.trustedProxies = Arrays.asList(trustedProxies.split("\\s*,\\s*"));
    }
    if (this.trustedProxies.isEmpty()) {
      log.warn("no trusted proxies configured; all proxies will be trusted");
    }
    for (Iterator i = this.trustedProxies.iterator(); i.hasNext(); ) {
      log.info("trusted proxy " + i.next());
    }
  }
  
  /*
   * (non-Javadoc)
   * @see org.soulwing.cas.filter.FilterAuthenticator#authenticate(javax.servlet.http.HttpServletRequest)
   */
  public ServiceValidationResponse authenticate(
      final HttpServletRequest request) 
      throws NoTicketException {
    if (protocolConfiguration == null) {
      throw new IllegalStateException("must configure protocolConfiguration");
    }
    ProxyValidationResponse casResponse = 
        ValidatorFactory.getValidator(
                UrlGeneratorFactory.getUrlGenerator(request, 
                    protocolConfiguration))
            .proxyValidate(new ValidationRequest(){
                public String getTicket() {
                  return request.getParameter(ProtocolConstants.TICKET_PARAM);
                }
              });
    if (casResponse.isSuccessful() 
        && !allProxiesTrusted(casResponse.getProxies())) {
      casResponse = new UntrustedProxyResponse(casResponse);
    }
    
    return casResponse;
  }

  /**
   * @return <code>true</code> iff each proxy in <code>proxies</code> 
   * appears in <code>trustedProxies</code>.
   */
  private boolean allProxiesTrusted(List proxies) {
    if (proxies == null || trustedProxies == null || trustedProxies.size() == 0) {
      return true;
    }
    for (Iterator i = proxies.iterator(); i.hasNext(); ) {
      String proxy = (String) i.next();
      if (!trustedProxies.contains(proxy)) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * CAS pseudo-response for an untrusted proxy.
   */
  private class UntrustedProxyResponse extends AbstractResponse 
      implements ProxyValidationResponse {
    
    private final ProxyValidationResponse response;
    
    UntrustedProxyResponse(ProxyValidationResponse response) {
      super();
      setSuccessful(false);
      setResultCode("UNTRUSTED_PROXY");
      setResultMessage("CAS response contained an untrusted proxy");
      this.response = response;
    }

    public List getProxies() {
      return response.getProxies();
    }

    public String getProxyGrantingTicketIou() {
      return response.getProxyGrantingTicketIou();
    }

    public String getUserName() {
      return response.getUserName();
    }
  }

}
