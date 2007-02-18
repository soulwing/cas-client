/*
 * MockValidationFilter.java
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


/**
 * A mock validation filter implementation for unit testing the 
 * abstract AbstractValidationFilter. 
 *
 * @author Carl Harris
 */
public class MockValidationFilter extends AbstractValidationFilter {

  private final FilterAuthenticator authenticator;

  public MockValidationFilter(FilterAuthenticator authenticator) {
    this.authenticator = authenticator;
  }

  /* 
   * @see org.soulwing.cas.filter.AbstractValidationFilter#getAuthenticator()
   */
  public FilterAuthenticator getAuthenticator() {
    return authenticator;
  }
  
}
