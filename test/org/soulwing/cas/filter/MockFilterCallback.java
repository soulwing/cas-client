/*
 * MockFilterCallback.java
 *
 * Created on Sep 12, 2006
 */
package org.soulwing.cas.filter;

import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ServiceValidationResponse;
import org.soulwing.cas.client.Validator;


/**
 * A mock implementation of FilterAuthenticator.
 *
 * @author Carl Harris
 * 
 */
public class MockFilterCallback implements FilterAuthenticator {

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
