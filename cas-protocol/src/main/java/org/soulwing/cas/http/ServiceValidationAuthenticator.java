/*
 * ServiceValidationAuthenticator.java
 *
 * Created on Feb 18, 2007 
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

import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.ValidationRequest;
import org.soulwing.cas.client.ValidatorFactory;

/**
 * FilterAuthenticator implementation for the CAS <code>/serviceValidate</code>
 * operation.
 *
 * @author Carl Harris
 */
public class ServiceValidationAuthenticator implements Authenticator {
  
  private ProtocolConfiguration protocolConfiguration;
  
  public ServiceValidationAuthenticator() {
  }
  
  public ServiceValidationAuthenticator(ProtocolConfiguration protocolConfiguration) {
    setProtocolConfiguration(protocolConfiguration);
  }
  
  /*
   * (non-Javadoc)
   * @see org.soulwing.cas.filter.FilterAuthenticator#setProtocolConfiguration(org.soulwing.cas.client.ProtocolConfiguration)
   */
  public final void setProtocolConfiguration(
      ProtocolConfiguration protocolConfiguration) {
    this.protocolConfiguration = protocolConfiguration;
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
    return ValidatorFactory.getValidator(
            UrlGeneratorFactory.getUrlGenerator(request, protocolConfiguration))
        .serviceValidate(new ValidationRequest() {
            public String getTicket() {
              return request.getParameter(ProtocolConstants.TICKET_PARAM);
            }
          });
  }
  
}