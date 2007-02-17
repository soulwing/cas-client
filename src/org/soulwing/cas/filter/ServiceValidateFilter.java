/*
 * ServiceValidateFilter.java
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

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.ProtocolConstants;
import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.ValidationRequest;
import org.soulwing.cas.client.ValidatorFactory;

/**
 * A ValidationFilter that uses CAS's <code>/serviceValidate</code>
 * function to perform the authentication.
 *
 * @author Carl Harris
 * 
 */
public class ServiceValidateFilter extends ValidationFilter {

  private FilterAuthenticator authenticator;
  
  protected FilterAuthenticator getAuthenticator() {
    return authenticator;
  }

  public void init(FilterConfig filterConfig) throws ServletException {
    super.init(filterConfig);
    authenticator = new ServiceValidateAuthenticator();
  }
  
  private class ServiceValidateAuthenticator implements FilterAuthenticator {
    
    public ServiceValidationResponse authenticate(
        final HttpServletRequest request) 
        throws NoTicketException {
      return ValidatorFactory.getValidator(
              UrlGeneratorFactory.getUrlGenerator(request))
          .serviceValidate(new ValidationRequest() {
              public String getTicket() {
                return request.getParameter(ProtocolConstants.TICKET_PARAM);
              }
            });
    }
    
  }

}
