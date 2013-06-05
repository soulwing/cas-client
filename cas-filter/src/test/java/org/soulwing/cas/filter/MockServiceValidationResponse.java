/*
 * MockServiceValidationResponse.java
 *
 * Created on Jul 7, 2007 
 */
package org.soulwing.cas.filter;

import org.soulwing.cas.client.ServiceValidationResponse;

/**
 * DESCRIBE THE TYPE HERE.
 *
 * @author Carl Harris
 */
public class MockServiceValidationResponse implements
    ServiceValidationResponse {

  private boolean successful;
  private String userName;
  private String proxyGrantingTicketIou;

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ServiceValidationResponse#getProxyGrantingTicketIou()
   */
  public String getProxyGrantingTicketIou() {
    return proxyGrantingTicketIou;
  }
  
  /**
   * Sets the PGTIOU.
   * @param proxyGrantingTicketIou <code>String</code> PGTIOU value
   */
  public void setProxyGrantingTicketIou(String proxyGrantingTicketIou) {
    this.proxyGrantingTicketIou = proxyGrantingTicketIou;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ServiceValidationResponse#getUserName()
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Sets the <code>userName</code> property.
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ValidationResponse#getResultCode()
   */
  public String getResultCode() {
    return Boolean.toString(successful);
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ValidationResponse#getResultMessage()
   */
  public String getResultMessage() {
    return Boolean.toString(successful);
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ValidationResponse#isSuccessful()
   */
  public boolean isSuccessful() {
    return successful;
  }

  /**
   * Sets the <code>successful</code> property.
   */
  public void setSuccessful(boolean successful) {
    this.successful = successful;
  }

}
