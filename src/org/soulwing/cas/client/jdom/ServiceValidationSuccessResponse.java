/*
 * ServiceValidationSuccessResponse.java
 *
 * Created on Jul 7, 2007
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
package org.soulwing.cas.client.jdom;

import org.soulwing.cas.client.AbstractResponse;
import org.soulwing.cas.client.ServiceValidationResponse;

/**
 * An implementation of <code>ServiceValidationResponse</code> as a POJO
 * with configurable properties that correspond to the elements of the
 * CAS authenticationSuccess response.
 *
 * @author Carl Harris
 */
public class ServiceValidationSuccessResponse extends AbstractResponse
    implements ServiceValidationResponse {

  private String userName;
  private String proxyGrantingTicketIou;
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ServiceValidationResponse#getProxyGrantingTicketIou()
   */
  public String getProxyGrantingTicketIou() {
    return proxyGrantingTicketIou;
  }

  /**
   * Sets the <code>proxyGrantingTicketIou</code> property.
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
   * @param userName <code>String</code> user name
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

}
