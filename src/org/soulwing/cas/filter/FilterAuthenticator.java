/*
 * FilterAuthenticator.java
 *
 * Created on Sep 12, 2006
 */
package org.soulwing.cas.filter;

import javax.servlet.http.HttpServletRequest;

import org.soulwing.cas.client.NoTicketException;
import org.soulwing.cas.client.ServiceValidationResponse;


/**
 * An implementation of this interface sends authentication requests to
 * the CAS server for a particular HTTP request, and returns the 
 * CAS server's response.
 *
 * @author Carl Harris
 * 
 */
public interface FilterAuthenticator {

  /**
   * Performs the action of the filter.
   * @param request the HTTP request that was received
   * @return the response from the CAS server
   * @throws NoTicketException if the request does not contain an
   *    authentication ticket.
   */
  ServiceValidationResponse authenticate(
      HttpServletRequest request) throws NoTicketException;
  
}
