/*
 * MockFilterAuthenticator.java
 *
 * Created on Feb 7, 2007
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
      ServiceValidationResponse response = new ServiceValidationResponse();
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