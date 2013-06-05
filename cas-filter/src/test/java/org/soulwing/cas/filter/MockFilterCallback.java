/*
 * MockFilterCallback.java
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

import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ProtocolConfiguration;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.Validator;
import org.soulwing.cas.http.Authenticator;


/**
 * A mock implementation of FilterAuthenticator.
 *
 * @author Carl Harris
 * 
 */
public class MockFilterCallback implements Authenticator {

  private boolean filterFlag;
  private ServiceValidationResponse response;
  
  public ServiceValidationResponse getResponse() {
    return this.response;
  }
  
  public void setResponse(ServiceValidationResponse response) {
    this.response = response;
  } 
  
  public boolean getFilterFlag() {
    return this.filterFlag;
  }
  
  public void setFilterFlag(boolean filterFlag) {
    this.filterFlag = filterFlag;
  }
  
  public void resetFilterFlag() {
    setFilterFlag(false);
  }
  
  public void setProtocolConfiguration(
      ProtocolConfiguration protocolConfiguration) {
  }

  public ServiceValidationResponse authenticate(HttpServletRequest request) 
      throws NoTicketException {
    setFilterFlag(true);
    if (getResponse() == null) {
      throw new NoTicketException();
    }
    else {
      return getResponse();
    }
  }

  public void setValidator(Validator validator) {
    // no-op
  }

}
