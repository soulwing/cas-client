/*
 * SimpleValidationRequest.java
 *
 * Created on Feb 10, 2007
 */
package org.soulwing.cas.client;


/**
 * A simple immutable implementation of ValidationRequest. 
 *
 * @author Carl Harris
 */
public class SimpleValidationRequest implements ValidationRequest {

  private final String ticket;
  
  public SimpleValidationRequest(String ticket) {
    this.ticket = ticket;
  }

  /* 
   * @see org.soulwing.cas.client.ValidationRequest#getTicket()
   */
  public final String getTicket() {
    return ticket;
  }

}
