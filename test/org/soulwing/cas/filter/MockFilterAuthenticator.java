/*
 * MockFilterAuthenticator.java
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

import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ServiceValidationResponse;

/**
 * A mock FilterAuthenticator for use in unit testing.
 *
 * @author Carl Harris
 */
public class MockFilterAuthenticator implements FilterAuthenticator {

  private String userName = "mockUserName";
  private boolean successFlag;
  private boolean noTicketFlag;
  
  public ServiceValidationResponse authenticate(HttpServletRequest request) 
      throws NoTicketException {
    if (noTicketFlag) {
      throw new NoTicketException();
    }
    else {
      MockServiceValidationResponse response = new MockServiceValidationResponse();
      response.setSuccessful(successFlag);
      response.setUserName(userName);
      return response;
    }
  }
  
  public boolean isNoTicketFlag() {
    return noTicketFlag;
  }
  
  public void setNoTicketFlag(boolean noTicketFlag) {
    this.noTicketFlag = noTicketFlag;
  }

  public boolean isSuccessFlag() {
    return successFlag;
  }
  
  public void setSuccessFlag(boolean successFlag) {
    this.successFlag = successFlag;
  }
  
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
  
}