/*
 * ProxyValidationSuccessResponse.java
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

import java.util.List;

import org.soulwing.cas.client.ProxyValidationResponse;
import org.soulwing.cas.client.ServiceValidationResponse;

/**
 * An implementation of <code>ProxyValidationResponse</code> as a POJO that
 * is composed with an instance of <code>ServiceValidationResponse</code>
 * to which all of the methods of that interface are delegated.
 *
 * @author Carl Harris
 */
public class ProxyValidationSuccessResponse implements ProxyValidationResponse {

  private List proxies;
  private final ServiceValidationResponse response;
  
  /**
   * Constructs a new instance that will delegate all of the methods
   * of <code>ServiceValidationResponse</code>.
   * @param response <code>ServiceValidationResponse</code> to which
   *    the methods of that interface will be delegated. 
   */
  public ProxyValidationSuccessResponse(ServiceValidationResponse response) {
    this.response = response;
  }
  
  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ProxyValidationResponse#getProxies()
   */
  public List getProxies() {
    return proxies;
  }

  /**
   * Sets the <code>proxies</code> property.
   * @param proxies <code>List</code> of proxies.
   */
  public void setProxies(List proxies) {
    this.proxies = proxies;
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ServiceValidationResponse#getProxyGrantingTicketIou()
   */
  public String getProxyGrantingTicketIou() {
    return response.getProxyGrantingTicketIou();
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ServiceValidationResponse#getUserName()
   */
  public String getUserName() {
    return response.getUserName();
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ValidationResponse#getResultCode()
   */
  public String getResultCode() {
    return response.getResultCode();
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ValidationResponse#getResultMessage()
   */
  public String getResultMessage() {
    return response.getResultMessage();
  }

  /* (non-Javadoc)
   * @see org.soulwing.cas.client.ValidationResponse#isSuccessful()
   */
  public boolean isSuccessful() {
    return response.isSuccessful();
  }

}
