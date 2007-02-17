/*
 * MockValidationFilter.java
 *
 * Created on Feb 7, 2007
 */
package org.soulwing.cas.filter;




/**
 * A mock ValidationFilter implementation for unit testing the 
 * abstract ValidationFilter. 
 *
 * @author Carl Harris
 */
public class MockValidationFilter extends ValidationFilter {

  private final FilterAuthenticator authenticator;

  public MockValidationFilter(FilterAuthenticator authenticator) {
    this.authenticator = authenticator;
  }

  /* 
   * @see org.soulwing.cas.filter.ValidationFilter#getAuthenticator()
   */
  public FilterAuthenticator getAuthenticator() {
    return authenticator;
  }
  
}
