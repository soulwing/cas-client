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
package org.soulwing.cas.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.NoTicketException;
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
class ProxyValidationAuthenticator implements FilterAuthenticator {

  private List trustedProxies = new ArrayList(0);

  ProxyValidationAuthenticator(String trustedProxies) {
    setTrustedProxies(trustedProxies);
  }
  
  /**
   * Sets the list of proxies this ProxyValidationAuthenticator should
   * trust.
   * @param trustedProxies list of String proxy names
   */
  public void setTrustedProxies(String trustedProxies) {
    if (trustedProxies != null) {
      this.trustedProxies = Arrays.asList(trustedProxies.split("\\s*,\\s*"));
    }
  }
  
  /**
   * @see FilterAuthenticator#authenticate(HttpServletRequest)
   */
  public ServiceValidationResponse authenticate(
      final HttpServletRequest request) 
      throws NoTicketException {
    
    ProxyValidationResponse casResponse = 
        ValidatorFactory.getValidator(
                UrlGeneratorFactory.getUrlGenerator(request))
            .proxyValidate(new ValidationRequest(){
                public String getTicket() {
                  return request.getParameter(ProtocolConstants.TICKET_PARAM);
                }
              });
    
    if (casResponse.isSuccessful() 
        && !allProxiesTrusted(casResponse.getProxies())) {

      casResponse = new UntrustedProxyResponse();
    }
    
    return casResponse;
  }

  /**
   * @return <code>true</code> iff each proxy in <code>proxies</code> 
   * appears in <code>trustedProxies</code>.
   */
  private boolean allProxiesTrusted(List proxies) {
    if (proxies == null) {
      return true;
    }
    for (Iterator i = proxies.iterator(); i.hasNext(); ) {
      if (!trustedProxies.contains(i.next())) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * CAS pseudo-response for an untrusted proxy.
   */
  private class UntrustedProxyResponse extends ProxyValidationResponse {
    UntrustedProxyResponse() {
      super();
      setResultCode("UNTRUSTED_PROXY");
      setResultMessage("Response contained an untrusted proxy");
    }
  }

}
