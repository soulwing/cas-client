/*
 * ServiceValidationResponse.java
 *
 * Created on Sep 7, 2006
 */
package org.soulwing.cas.client;


/**
 * A value class that represents the response to the CAS
 * <code>/serviceValidate</code> function.
 *
 * @author Carl Harris
 * 
 */
public class ServiceValidationResponse extends Response {

  private String userName;
  private String proxyGrantingTicketIou;

  /**
   * Gets the username from the CAS response.
   * @return String username
   */
  public String getUserName() {
    return this.userName;
  }
  
  /**
   * Sets the username for a CAS response.
   * @param userName username string.
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  /**
   * Gets the proxy granting ticket IOU from a CAS response.
   * @return proxy granting ticket IOU string.
   */
  public String getProxyGrantingTicketIou() {
    return proxyGrantingTicketIou;
  }

  /**
   * Sets the proxy granting ticket for a CAS response.
   * @param proxyGrantingTicketIou proxy granting ticket IOU string.
   */
  public void setProxyGrantingTicketIou(String proxyGrantingTicketIou) {
    this.proxyGrantingTicketIou = proxyGrantingTicketIou;
  }
  
}
