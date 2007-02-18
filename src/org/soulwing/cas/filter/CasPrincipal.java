/*
 * CasPrincipal.java
 *
 * Created on Feb 18, 2007 
 */
package org.soulwing.cas.filter;

import org.soulwing.cas.client.ServiceValidationResponse;

class CasPrincipal implements java.security.Principal {
  private String name;

  public CasPrincipal(String userName) {
    setName(userName);
  }
  
  public CasPrincipal(ServiceValidationResponse response) {
    this(response.getUserName());
  }
  
  public String getName() {
    return this.name;
  }

  private void setName(String name) {
    this.name = name;
  }
}