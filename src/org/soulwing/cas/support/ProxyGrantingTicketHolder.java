/*
 * ProxyGrantingTicketHolder.java
 *
 * Created on Feb 12, 2007
 */
package org.soulwing.cas.support;


/**
 * A holder for a CAS proxy granting ticket (PGT).  Uses a ThreadLocal to
 * store the PGT that should be used by the thread.
 *
 * @author Carl Harris
 */
public class ProxyGrantingTicketHolder {

  private static final ThreadLocal ticketHolder = new ThreadLocal();
  
  public static synchronized String getTicket() {
    return (String) ticketHolder.get();
  }
  
  public static synchronized void setTicket(String ticket) {
    ticketHolder.set(ticket);
  }
  
  public static synchronized void clearTicket() {
    ticketHolder.remove();
  }

}
