/*
 * CasPrincipal.java
 *
 * Created on Feb 18, 2007 
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

import java.io.Serializable;

import org.soulwing.cas.client.ServiceValidationResponse;

/**
 * A Principal that delegates to a ServiceValidationResponse.
 *
 * @author Carl Harris
 */
class CasPrincipal implements java.security.Principal, Serializable {

  private static final long serialVersionUID = -9217520836500471161L;
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